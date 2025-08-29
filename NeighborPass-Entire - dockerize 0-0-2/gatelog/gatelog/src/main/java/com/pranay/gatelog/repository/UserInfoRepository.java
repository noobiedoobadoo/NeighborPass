package com.pranay.gatelog.repository;

import com.pranay.gatelog.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserInfoRepository extends JpaRepository<UserInfo, UUID> {

    Optional<UserInfo> findByHouseNumber(String HouseNumber);
    Optional<UserInfo> findByUsername(String username);
}
