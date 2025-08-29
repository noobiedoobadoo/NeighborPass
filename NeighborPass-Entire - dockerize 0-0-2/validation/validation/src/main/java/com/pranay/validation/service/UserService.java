package com.pranay.validation.service;

import com.pranay.validation.entity.User;
import com.pranay.validation.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public boolean addUser(User user) {
//        if (userRepository.findByHouseNumber(user.getHouseNumber()).isPresent() ||
//            userRepository.findById(user.getUsername()).isPresent()
//        ) {
//            return false;
//        }
        userRepository.save(user);
        //produce event send user
        return true;
    }

    public boolean removeUser(String houseNumber) {
        if (!checkHouseNumberExists(houseNumber)) {
            return false;
        }
        User user = userRepository.findByHouseNumber(houseNumber).get();
        userRepository.delete(user);
        return true;
    }

    public boolean checkHouseNumberExists(String houseNumber) {
        return userRepository.findByHouseNumber(houseNumber).isPresent();
    }

    public boolean findByUsername(String username) {
        return userRepository.findById(username).isPresent();
    }

    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
    }
}
