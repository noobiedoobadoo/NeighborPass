package com.pranay.gatelog.entity;


import jakarta.persistence.*;
import lombok.*;


import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "visitor")
public class Visitor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "visitor_id")
    private Long id;

    @Column(name = "visitor_name")
    private String name;

    @Column(name = "visitor_contact", unique = true)
    private String contact;



}
