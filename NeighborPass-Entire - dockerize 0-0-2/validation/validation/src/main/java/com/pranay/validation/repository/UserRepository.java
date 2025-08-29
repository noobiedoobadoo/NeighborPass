package com.pranay.validation.repository;

import com.pranay.validation.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends CrudRepository<User, String> {
    Optional<User> findByHouseNumber(String houseNumber);
}
