package com.pranay.gatelog.repository;

import com.pranay.gatelog.entity.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VisitorRepository extends JpaRepository<Visitor, Long> {
    Optional<Visitor> findByContact(String contact);
}
