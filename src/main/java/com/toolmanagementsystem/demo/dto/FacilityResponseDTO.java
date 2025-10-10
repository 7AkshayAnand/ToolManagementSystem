package com.toolmanagementsystem.demo.dto;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FacilityResponseDTO {

    private Long id;                 // Facility ID
    private String facilityCode;     // Unique code, e.g., "FAB10A"
    private String facilityName;
    private String location;         // City/State
    private String country;
    private String siteType;         // e.g., "FAB", "Assembly", "Test"
    private String description;
    private List<String> facilitySystems; // Optional notes
    private Boolean isActive;        // Facility operational or not
}
