package com.pranay.gatelog.service;


import com.pranay.gatelog.dto.VisitorDto;
import com.pranay.gatelog.entity.UserInfo;
import com.pranay.gatelog.entity.Visitor;
import com.pranay.gatelog.repository.VisitorRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class VisitorService {

    private VisitorRepository visitorRepository;

    public VisitorDto createVisitor(String name, String contact) {


        Visitor visitor = Visitor.builder()
                .name(name)
                .contact(contact)
                .build();

        Visitor saved = visitorRepository.save(visitor);
        return toDto(saved);
    }

     VisitorDto toDto(Visitor visitor) {
        VisitorDto visitorDto = VisitorDto.builder()
                .name(visitor.getName())
                .contact(visitor.getContact())
                .build();
        return visitorDto;
    }

    public List<VisitorDto> findAllVisitor() {
        List<Visitor> visitorList = visitorRepository.findAll();
        List<VisitorDto> visitorDtoList = visitorList.stream().map(this::toDto).toList();
        return visitorDtoList;
    }

    public VisitorDto findByContact(String contact) {
        Optional<Visitor> byContact = visitorRepository.findByContact(contact);
        if (byContact.isEmpty()) {
            return null;
        } else {
            return toDto(byContact.get());
        }
    }
}
