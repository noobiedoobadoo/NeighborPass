package com.pranay.gatelog.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "user")
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "username")
    private String username;

//    @Column(name = "password")
//    private String password;


    @Column(name = "user_home", unique = true)
    private String houseNumber;

//    @Column(name = "user_role")
//    @Builder.Default
//    private String role = "ROLE_USER";
}
