package com.pranay.gatelog.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EntryDto {
    private Long id;

    private LocalDateTime arrivalTime;

    private LocalDateTime enterTime;

    private LocalDateTime exitTime;

    private Boolean denied;

    private VisitorDto visitorDto;

    private String houseNumber;
}
