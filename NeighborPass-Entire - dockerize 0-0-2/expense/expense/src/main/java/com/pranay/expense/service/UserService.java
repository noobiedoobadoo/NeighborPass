package com.pranay.expense.service;


import com.pranay.expense.entity.User;
import com.pranay.expense.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        Optional<User> byUsername = userRepository.findByUsername(username);
        return byUsername.orElse(null);
    }

    public User findByContact(String contact) {
        Optional<User> byUserContact = userRepository.findByUserContact(contact);
        return byUserContact.orElse(null);
    }

    public User findByHouseNumber(String houseNumber) {
        Optional<User> byHouseNumber = userRepository.findByHouseNumber(houseNumber);
        return byHouseNumber.orElse(null);
    }

    public void deleteUser(String houseNumber) {
        User byHouseNumber = this.findByHouseNumber(houseNumber);
        userRepository.delete(byHouseNumber);
    }
}
