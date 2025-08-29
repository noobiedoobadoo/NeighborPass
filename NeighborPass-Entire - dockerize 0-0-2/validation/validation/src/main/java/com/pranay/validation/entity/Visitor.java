package com.pranay.validation.entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@RedisHash("Visitor")
public class Visitor implements Serializable {
    @Id
    private String contact;

    private String name;

    @Builder.Default
    private Boolean inside = false;

}
