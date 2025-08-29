package com.pranay.gatelog.controller;


import com.pranay.gatelog.dto.ApprovalDto;
import com.pranay.gatelog.dto.EntryDto;
import com.pranay.gatelog.dto.VisitorDto;
import com.pranay.gatelog.entity.UserInfo;
import com.pranay.gatelog.service.EntryService;
import com.pranay.gatelog.service.UserInfoService;
import com.pranay.gatelog.service.ValidationServiceClient;
import com.pranay.gatelog.service.VisitorService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@AllArgsConstructor
//@RequestMapping("")
public class EntryController {

    private final VisitorService visitorService;
    private final UserInfoService userInfoService;
    private final EntryService entryService;

    private final ValidationServiceClient validationServiceClient;


//    @GetMapping("/entries")
//    public ResponseEntity<?> findByContact(@RequestParam(required = false) String status, Principal principal) {
//        UserInfo userInfo = userInfoService.findByUsername(principal.getName());
//        //List<EntryDto> entryDtoList = entryService.getEntriesByUserInfo(userInfo);
//
//        Map entryMapByUserInfo = entryService.getEntryMapByUserInfo(userInfo);
//
//        return new ResponseEntity<>( entryMapByUserInfo,HttpStatus.FOUND);
//    }

    @GetMapping("/entries")
    public ResponseEntity<?> findByContact(@RequestHeader("X-User-Name") String username,
                                           @RequestHeader("X-Authority") String role,
                                           @RequestParam(value = "Contact", required = false) String contact,
                                           @RequestParam(value = "House", required = false) String houseNumber,


                                           @RequestParam(value = "Start-Time", required = false)
                                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                               LocalDateTime lowerBound,
                                           @RequestParam(value = "End-Time", required = false)
                                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                               LocalDateTime upperBound
    ) {
        UserInfo userInfo = userInfoService.findByUsername(username);
        //List<EntryDto> entryDtoList = entryService.getEntriesByUserInfo(userInfo);

        Boolean isAdmin = role.equals("ROLE_ADMIN");
        Map entryMapByUserInfo = entryService.getEntryMapByUserInfo(userInfo, isAdmin, contact, houseNumber, lowerBound, upperBound);

        return new ResponseEntity<>( entryMapByUserInfo,HttpStatus.FOUND);
    }



    //private record ApprovalDto(Long id, Boolean allow){}

    @PutMapping("/entries/{id}")
    public ResponseEntity<?> approveOrDeny(@RequestBody Boolean allow, @PathVariable Long id, @RequestHeader("X-User-Name") String username) {

        userInfoService.findByUsername(username);
        EntryDto entryDto = entryService.approveOrDenyEntry(id, allow);

        //async produce event v
        String contact = entryDto.getVisitorDto().getContact();
        validationServiceClient.permission(new ApprovalDto(allow,contact));
        return new ResponseEntity<>(entryDto, HttpStatus.OK);
    }

    /**
     * first we send without the name
     * if we can find a visitor solely based on contact then we proceed
     * otherwise we ask for post request again
     *
     *
     * */
    private record EntryRequestDto(String name, String contact, String houseNumber){}
    private record MessageDto(String message){}

    @PostMapping("/admin")// ONLY FOR TESTING!
    public ResponseEntity<?> createEntry(@RequestBody EntryRequestDto entryRequestDto) {

        VisitorDto visitorDtoRequest = VisitorDto.builder()
                .name(entryRequestDto.name())
                .contact(entryRequestDto.contact())
                .build();

        VisitorDto visitorDto = visitorService.findByContact(entryRequestDto.contact());
        if (Objects.isNull(entryRequestDto.name) && Objects.isNull(visitorDto)) {
            //1st request, visitor was not found
            return new ResponseEntity<>(new MessageDto("contact :  '"+ entryRequestDto.contact()
                    +"'  not found please provide details"),HttpStatus.NOT_FOUND);
        } else {
            //re- request sent, visitor was not found, so we also send other details
            // OR
            //visitor was found using contact num
            EntryDto entryDto = entryService.createEntry(visitorDtoRequest, entryRequestDto.houseNumber());
            // if houseNumber is NOT valid (i.e. null) then
            if (Objects.isNull(entryDto)) {
                return new ResponseEntity<>(new MessageDto("houseNumber: '"+ entryRequestDto.houseNumber()
                        +"' does NOT exist"), HttpStatus.BAD_REQUEST);
            }


            // SEND NOTIFICATION FOR APPROVAL
            return new ResponseEntity<>(entryDto, HttpStatus.CREATED);
        }

    }


    @PatchMapping("/admin")// ONLY FOR TESTING
    public ResponseEntity<?> logExit(@RequestBody VisitorDto visitorDto) {
        //async produce event v
        List<EntryDto> entryDtoList = entryService.logExit(visitorDto.getContact());
        return new ResponseEntity<>(entryDtoList, HttpStatus.OK);
    }

}
