package com.toolmanagementsystem.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.toolmanagementsystem.demo.enums.SiteLocation;
import com.toolmanagementsystem.demo.enums.SiteType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "facilities")
@Getter
@Setter
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

    @Enumerated(EnumType.STRING)
    @Column(name = "site_location", nullable = false)
    private SiteLocation siteLocation;
// City/State


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SiteType siteType; // e.g., "FAB", "Assembly", "Test"

    @Column(length = 1000)
    private String description;

    // Optional notes
    @ElementCollection
    @CollectionTable(name = "facility_systems", joinColumns = @JoinColumn(name = "facility_id"))
    @Column(name = "system_name")
    private List<String> facilitySystems = new ArrayList<>();

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean isActive; // Facility operational or not

    @OneToMany(mappedBy = "facility")
    @JsonIgnore
    private List<Tool> tools = new ArrayList<>();



}
