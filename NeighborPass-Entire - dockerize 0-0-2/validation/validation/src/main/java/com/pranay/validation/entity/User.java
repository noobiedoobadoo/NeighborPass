package com.pranay.validation.entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@RedisHash("User")
public class User implements Serializable {
    @Id
    private String username;

    @Indexed
    private String houseNumber;
    //private String password;
    // Other fields, getters, setters
}
