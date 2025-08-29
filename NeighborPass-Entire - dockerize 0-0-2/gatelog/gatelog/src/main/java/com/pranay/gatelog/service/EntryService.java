package com.pranay.gatelog.service;


import com.pranay.gatelog.dto.EntryDto;
import com.pranay.gatelog.dto.VisitorDto;
import com.pranay.gatelog.entity.Entry;
import com.pranay.gatelog.entity.UserInfo;
import com.pranay.gatelog.entity.Visitor;
import com.pranay.gatelog.exception.NotFoundException;
import com.pranay.gatelog.repository.EntryRepository;
import com.pranay.gatelog.repository.VisitorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EntryService {

    private EntryRepository entryRepository;

    private UserInfoService userInfoService;

    private VisitorService visitorService;

    private VisitorRepository visitorRepository;


    //async
    @Transactional
    public EntryDto createEntry(VisitorDto visitorDto, String HouseNumber) {
        UserInfo userInfo = userInfoService.findByHouseNumber(HouseNumber);
        // Because we have already validated our visitor
        Optional<Visitor> byContact = visitorRepository.findByContact(visitorDto.getContact());

        Visitor visitor = byContact.orElse(null);
        if (byContact.isEmpty()) {
            visitor = visitorRepository.save(Visitor.builder()
                            .name(visitorDto.getName())
                            .contact(visitorDto.getContact())
                    .build());
        }


        //handle this case otherwise can't do async
        if (userInfo == null) {
            // given houseNumber doesn't exist
            return null;
        }

        Entry entry = Entry.builder()
                .visitor(visitor)
                .userInfo(userInfo)
                .build();
        entryRepository.save(entry);
        return null;
        //return toDto(entryRepository.save(entry));

    }


    //async
    public EntryDto approveOrDenyEntry(Long id, Boolean allow) {
        Optional<Entry> entryOptional = entryRepository.findById(id);
        Entry entry = entryOptional.orElseThrow(() -> new NotFoundException("entry not found with Id : "+ id));

        if (Objects.nonNull(entry.getEnterTime()) || entry.getDenied()) {
            throw new RuntimeException("Already Approved OR Denied");
        }

        if (!allow) {
            entry.setDenied(true);
        } else {
            entry.setEnterTime(LocalDateTime.now());
        }

        //notification send to admin
        return toDto(entryRepository.save(entry));
    }

    //async
    public List<EntryDto> logExit(String contact) {

        Optional<Visitor> byContact = visitorRepository.findByContact(contact);

        Visitor visitor = byContact.orElse(null);
        List<Entry> entryList = entryRepository
                .findByExitTimeIsNullAndEnterTimeIsNotNullAndDeniedFalseAndVisitor(visitor);

        entryList.forEach(entry -> entry.setExitTime(LocalDateTime.now()));


        //notification send to user
        return entryRepository.saveAll(entryList).stream()
                .map(this::toDto)
                .toList();


        //return toDto(entryRepository.save(entry));
    }

    public List<EntryDto> findAllEntry() {
        return entryRepository.findAll().stream().map(this::toDto).toList();
    }

    public List findByContact(String contact) {
        return entryRepository.findByVisitor(
                visitorRepository.findByContact(contact).orElse(null)
        ).stream().map(this::toDto).toList();
    }

    public List findByHouseNumber(String houseNumber) {
        return entryRepository.findByUserInfo(
                userInfoService.findByHouseNumber(houseNumber)
        ).stream().map(this::toDto).toList();
    }

    public List findAllWaiting(String houseNumber) {
        return entryRepository.findByEnterTimeIsNull().stream()
                .filter(x -> x.getUserInfo().getHouseNumber().equals(houseNumber))
                .map(this::toDto).toList();
    }
    public List findAllWaiting() {
        return entryRepository.findByEnterTimeIsNull().stream()
                .map(this::toDto).toList();
    }

    public List findAllCurrent(String houseNumber) {
        return entryRepository.findByEnterTimeIsNotNullAndExitTimeIsNull().stream()
                .filter(x -> x.getUserInfo().getHouseNumber().equals(houseNumber))
                .map(this::toDto).toList();
    }
    public List findAllCurrent() {
        return entryRepository.findByEnterTimeIsNotNullAndExitTimeIsNull().stream()
                .map(this::toDto).toList();
    }

    public List findAllLeft() {
        return entryRepository.findByExitTimeIsNotNull().stream()
                .map(this::toDto).toList();
    }

    public List findAllLeft(String houseNumber) {
        return entryRepository.findByExitTimeIsNotNull().stream()
                .filter(x -> x.getUserInfo().getHouseNumber().equals(houseNumber))
                .map(this::toDto).toList();
    }

    private EntryDto toDto(Entry entry) {
        EntryDto entryDto = EntryDto.builder()
                .id(entry.getId())
                .houseNumber(Objects.nonNull(entry.getUserInfo())? entry.getUserInfo().getHouseNumber() : null)
                .denied(entry.getDenied())
                .arrivalTime(entry.getArrivalTime())
                .enterTime(entry.getEnterTime())
                .exitTime(entry.getExitTime())
                .visitorDto(visitorService.toDto(entry.getVisitor()))
                .build();
        return entryDto;
    }

    public List<EntryDto> getEntriesByUserInfo(UserInfo userInfo) {
        return entryRepository.findByUserInfo(userInfo).stream()
                .map(this::toDto).toList();
    }

    public Map getEntryMapByUserInfo(UserInfo userInfo,
                                     Boolean isAdmin,
                                     String contact,
                                     String houseNumber,
                                     LocalDateTime lowerBound,
                                     LocalDateTime upperBound
    ) {

        List<EntryDto> entryDtoList = null;

        if (isAdmin) {
            entryDtoList = entryRepository.findAll().stream()
                    .map(this::toDto)
                    .toList();
        } else {
            entryDtoList = getEntriesByUserInfo(userInfo);
        }

        List<EntryDto> currentList = entryDtoList.stream()
                .filter(entry -> Objects.isNull(entry.getEnterTime()) && !entry.getDenied())
                .toList();

        List<EntryDto> enteredList = entryDtoList.stream()
                .filter(entry -> Objects.nonNull(entry.getEnterTime())
                        && !entry.getDenied()
                        && Objects.isNull(entry.getExitTime()))
                .toList();

        List<EntryDto> pastList = entryDtoList.stream()
                .filter(entry -> Objects.nonNull(entry.getEnterTime())
                        && !entry.getDenied()
                        && Objects.nonNull(entry.getExitTime()))
                .toList();

        List<EntryDto> deniedList = entryDtoList.stream()
                .filter(EntryDto::getDenied)
                .toList();

        Map<String, List<EntryDto>> entryDtoMap = new HashMap<>();
        entryDtoMap.put("waiting", currentList);
        entryDtoMap.put("entered", enteredList);
        entryDtoMap.put("past", pastList);
        entryDtoMap.put("denied", deniedList);


        Map<String, List<EntryDto>> filteredMap = entryDtoMap.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .filter(dto ->
                                        Objects.isNull(contact)
                                        || dto.getVisitorDto().getContact().equals(contact))  // Your condition here
                                .filter(dto ->
                                        Objects.isNull(houseNumber)
                                        || dto.getHouseNumber().equals(houseNumber)
                                )
                                .filter(dto ->
                                        Objects.isNull(lowerBound)
                                        || dto.getArrivalTime().isAfter(lowerBound)
                                )
                                .filter(dto ->
                                        Objects.isNull(upperBound)
                                                || dto.getArrivalTime().isBefore(upperBound)
                                )
                                .collect(Collectors.toList()),
                        (oldVal, newVal) -> oldVal, HashMap::new));
        return filteredMap;




        //return entryDtoMap;
    }
}
