package com.pranay.gatelog.service;


import com.pranay.gatelog.entity.UserInfo;
import com.pranay.gatelog.exception.NotFoundException;
import com.pranay.gatelog.repository.UserInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserInfoService {

    private final UserInfoRepository userInfoRepository;
    //private final PasswordEncoder passwordEncoder;

    public void createUserInfo(UserInfo userInfo) {
        //encrypt
        //userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        //save
        userInfoRepository.save(userInfo);
    }

    public List<UserInfo> findAllUserInfo() {
        return userInfoRepository.findAll();
    }

    public UserInfo findByUsername(String username) {
        return userInfoRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("no user found : "+ username));
    }

    public UserInfo findByHouseNumber(String houseNumber) {
        Optional<UserInfo> byHouseNumber = userInfoRepository.findByHouseNumber(houseNumber);
        return byHouseNumber.orElse(null);
    }

    public void updateUserInfoHouseNumber(UserInfo newInfo, String prevHouseNumber) {
        UserInfo userInfo = findByHouseNumber(prevHouseNumber);
        userInfo.setHouseNumber(newInfo.getHouseNumber());
        userInfo.setUsername(newInfo.getUsername());

        userInfoRepository.save(userInfo);
    }

    public Boolean deleteUserInfo(String houseNumber) {
        Optional<UserInfo> userInfoOptional = userInfoRepository.findByHouseNumber(houseNumber);
        if (userInfoOptional.isEmpty()) {
            return false;
        }
        userInfoRepository.delete(userInfoOptional.get());
        return true;
    }
}
