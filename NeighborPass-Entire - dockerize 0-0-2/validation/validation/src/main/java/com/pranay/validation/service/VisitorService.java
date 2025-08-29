package com.pranay.validation.service;


import com.pranay.validation.entity.Visitor;
import com.pranay.validation.repository.VisitorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class VisitorService {

    private final VisitorRepository visitorRepository;

    public boolean addVisitor(Visitor visitor) {
        if (checkVisitorExists(visitor.getContact())) {
            return false;
        }
        visitorRepository.save(visitor);
        return true;
    }

    public boolean checkVisitorExists(String contact) {
        return visitorRepository.findById(contact).isPresent();
    }

    public Visitor findByContact(String contact) {
        if (checkVisitorExists(contact)) {
            return visitorRepository.findById(contact).get();
        }
        return null;
    }

    public List<Visitor> findAll() {
        return (List<Visitor>) visitorRepository.findAll();
    }

    public void approve(String contact, Boolean approve) {
        Visitor visitor = this.findByContact(contact);
        visitor.setInside(approve);
        visitorRepository.save(visitor);
    }

    public Boolean exitVisitorByContact(String contact) {
        Visitor visitor = this.findByContact(contact);
        Boolean isCompliant = visitor.getInside();
        visitor.setInside(false);
        visitorRepository.save(visitor);
        return isCompliant;
    }

}
