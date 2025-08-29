package com.pranay.validation.repository;

import com.pranay.validation.entity.Visitor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface VisitorRepository extends CrudRepository<Visitor, String> {
}
