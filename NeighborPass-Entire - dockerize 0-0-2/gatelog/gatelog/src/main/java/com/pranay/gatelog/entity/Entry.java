package com.pranay.gatelog.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "entry")
public class Entry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "entry_id")
    private Long id;

    @Column(name = "arrival_time")
    @Builder.Default
    private LocalDateTime arrivalTime = LocalDateTime.now();

    @Column(name = "entry_time")
    private LocalDateTime enterTime;

    @Column(name = "exit_time")
    private LocalDateTime exitTime;

    @Column(name = "denied")
    @Builder.Default
    private Boolean denied = false;

    @ManyToOne(optional = true , cascade = CascadeType.ALL)
    @JoinColumn(name = "visitor_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Visitor visitor;

    @ManyToOne(optional = true , cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private UserInfo userInfo;
}
