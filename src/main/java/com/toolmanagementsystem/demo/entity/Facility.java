package com.toolmanagementsystem.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "facilities")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Facility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String facilityCode; // Unique code for facility, e.g., "FAB10A"

    @Column(nullable = false)
    private String facilityName;

    @Column(nullable = false)
    private String location; // City/State

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String siteType; // e.g., "FAB", "Assembly", "Test"

    @Column(length = 1000)
    private String description;

    // Optional notes
    private List<String> facilitySystems = new ArrayList<>();

    @Column
    private Boolean isActive = true; // Facility operational or not

    @OneToMany(mappedBy = "facility", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tool> tools = new ArrayList<>();



}
