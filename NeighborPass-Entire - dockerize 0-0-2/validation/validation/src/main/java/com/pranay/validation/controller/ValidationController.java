package com.pranay.validation.controller;

import com.pranay.validation.dto.CustomMessage;
import com.pranay.validation.dto.EntryRequestDto;
import com.pranay.validation.dto.ExitDto;
import com.pranay.validation.dto.UserDto;
import com.pranay.validation.entity.User;
import com.pranay.validation.entity.Visitor;
import com.pranay.validation.producer.EntryProducer;
import com.pranay.validation.producer.ExitProducer;
import com.pranay.validation.producer.RabbitMQProducer;
import com.pranay.validation.producer.UserProducer;
import com.pranay.validation.service.UserService;
import com.pranay.validation.service.VisitorService;
import lombok.AllArgsConstructor;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@AllArgsConstructor
public class ValidationController {

    private final UserService userService;
    private final VisitorService visitorService;

    private final RabbitMQProducer rabbitMQProducer;
    private final EntryProducer entryProducer;
    private final UserProducer userProducer;
    private final ExitProducer exitProducer;

    //private record EntryRequestDto(String name, String contact, String houseNumber){}
    private record Message(String message){}



    @PostMapping("/admin")
    public ResponseEntity<?> createEntry(@RequestBody EntryRequestDto entryRequestDto) {
        String name = entryRequestDto.getName();
        String contact = entryRequestDto.getContact();
        String houseNumber = entryRequestDto.getHouseNumber();

        if (visitorService.checkVisitorExists(contact)) {
            name = visitorService.findByContact(contact).getName();
        } else {//new visitor - no name
            if (Objects.isNull(name)) {
                //System.out.println(visitorService.findAll());
                return new ResponseEntity<>(new Message("enter visitor's name"), HttpStatus.NOT_FOUND);
            } else {//new Visitor - but name is given - save
                Visitor visitor = Visitor.builder()
                        .name(name)
                        .contact(contact)
                        .build();
                visitorService.addVisitor(visitor);
            }
        }


        //validate houseNumber
        if (!userService.checkHouseNumberExists(houseNumber)) {

            //System.out.println(userService.findAll());
            return new ResponseEntity<>(new Message("House No.: "+ houseNumber + " not found"), HttpStatus.NOT_FOUND);
        }

        if (visitorService.findByContact(contact).getInside()) {
            return new ResponseEntity<>(new Message("Visitor "+name+" hasn't logged out yet"),HttpStatus.CONFLICT);
        }

        //produce event send (name, contact, houseNumber)
        entryProducer.sendEntry(new EntryRequestDto(name, contact, houseNumber));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public record LogExitDto(String contact){}

    @PatchMapping("/admin")
    public ResponseEntity<?> logExit(@RequestBody LogExitDto logExitDto) {
        String contact = logExitDto.contact();

        //validate
        if (!visitorService.checkVisitorExists(contact)) {
            return new ResponseEntity<>(new Message("Contact Not found"), HttpStatus.NOT_FOUND);
        }

        Boolean isCompliant = visitorService.exitVisitorByContact(contact);
        //produce event, send contact
        exitProducer.sendExit(new ExitDto(contact));
        if (isCompliant) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>( new Message("entry is not logged in for the visitor") , HttpStatus.OK);
        }
    }

    public record DeleteUserDto(String houseNumber) {}

    @DeleteMapping("/admin")
    public ResponseEntity<?> deleteUser(@RequestBody DeleteUserDto deleteUserDto) {
        boolean removed = userService.removeUser(deleteUserDto.houseNumber());
        if (removed) {
            userProducer.removeUser(UserDto.builder()
                            .houseNumber(deleteUserDto.houseNumber())
                    .build());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(new Message("houseNumber : "+deleteUserDto.houseNumber()+" not found") , HttpStatus.NOT_FOUND);
    }

    public record RegisterDTO(String username, String houseNumber){}

    @PostMapping("/auth/register") //ONLY FOR TESTING
    public ResponseEntity<?> registerUser(@RequestBody RegisterDTO registerDTO) {
        String username = registerDTO.username();
        String houseNumber = registerDTO.houseNumber();

        //validate
        if (userService.checkHouseNumberExists(houseNumber) ||
                userService.findByUsername(username)
        ) {
            return new ResponseEntity<>(new Message("Username OR houseNumber already exists"), HttpStatus.BAD_REQUEST);
        }


        //save to db
        User user = User.builder()
                .username(username)
                .houseNumber(houseNumber)
                .build();
        userService.addUser(user);

        //maybe encrypt password
        //produce event send registerDTO
        userProducer.sendUser(UserDto.builder()
                        .username(username)
                        .houseNumber(houseNumber)
                .build());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public record AdminRegisterDTO(String username, String password, String adminSecret){}

    public record AdminRegisterValidationDto(String username){}

    @PostMapping("/auth/admin/register") //ONLY FOR TESTING
    public ResponseEntity<?> registerAdmin(@RequestBody AdminRegisterValidationDto adminRegisterValidationDto) {
        String username = adminRegisterValidationDto.username();

        //validate
        if (
                userService.findByUsername(username)
        ) {
            return new ResponseEntity<>(new Message("Username already exists"), HttpStatus.BAD_REQUEST);
        }


        //save to db
        User user = User.builder()
                .username(username)
                .build();
        userService.addUser(user);

        //maybe encrypt password
        //produce event send registerDTO

        //userProducer.sendUser(new UserDto(username,null, registerDTO.password()));
        userProducer.sendUser(UserDto.builder()
                    .username(username)
                .build());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public record ApprovalDto(Boolean approve, String contact){}

    @PutMapping("/permission")
    public void permission(@RequestBody ApprovalDto approvalDto) {
        String contact = approvalDto.contact();
        visitorService.approve(contact, approvalDto.approve());
        System.out.println("APPROVAL : "+approvalDto);
    }

    ///ONLY FOR TESTING CONNECTION
    @PostMapping("validation/send")
    public ResponseEntity<?> sendCustomMessage(@RequestBody CustomMessage customMessage) {
        rabbitMQProducer.sendMessage(customMessage);
        return new ResponseEntity<>(customMessage, HttpStatus.OK);
    }
}
