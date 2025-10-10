package com.toolmanagementsystem.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tools")
public class Tool extends AuditableEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String toolName; // e.g., "ETCHER-5000A"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private String toolType; // e.g., "Etcher"

    @Column(nullable = false)
    private String manufacturer;

    @Column
    private String modelNumber;

    @Column
    private String serialNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private String status; // e.g., "NEW", "PENDING", "ACCEPTED"

    @Column(nullable = false)
    private Integer quantity ; // Number of tools of this type at the facility

    @Column(length = 1000)
    private String remarks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id", nullable = false)
    private Facility facility;

}
