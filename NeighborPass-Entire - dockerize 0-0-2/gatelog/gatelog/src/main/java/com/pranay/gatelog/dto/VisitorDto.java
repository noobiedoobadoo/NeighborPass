package com.pranay.gatelog.dto;


import com.pranay.gatelog.entity.Visitor;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VisitorDto {

    private String name;

    private String contact;
}
