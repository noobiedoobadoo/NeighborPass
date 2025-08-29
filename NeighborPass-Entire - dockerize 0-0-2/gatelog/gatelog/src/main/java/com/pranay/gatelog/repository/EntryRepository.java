package com.pranay.gatelog.repository;

import com.pranay.gatelog.entity.Entry;
import com.pranay.gatelog.entity.UserInfo;
import com.pranay.gatelog.entity.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EntryRepository extends JpaRepository<Entry, Long> {
    List<Entry> findByDenied(Boolean denied);
    List<Entry> findByVisitor(Visitor visitor);
    List<Entry> findByUserInfo(UserInfo userInfo);
    // 1. Entries where enterTime is null WAITING
    List<Entry> findByEnterTimeIsNull();

    // 2. Entries where enterTime is non-null but exitTime is null ENTERED
    List<Entry> findByEnterTimeIsNotNullAndExitTimeIsNull();

    // 3. Entries where both enterTime and exitTime are non-null PAST
    List<Entry> findByExitTimeIsNotNull();

    //Waiting and Entered
    List<Entry> findByExitTimeIsNullAndDeniedFalse();

    //Waiting and Entered  Visitor
    List<Entry> findByExitTimeIsNullAndDeniedFalseAndVisitor(Visitor visitor);

    //Entered  Visitor
    List<Entry> findByExitTimeIsNullAndEnterTimeIsNotNullAndDeniedFalseAndVisitor(Visitor visitor);

}
