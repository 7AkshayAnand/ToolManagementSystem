package com.toolmanagementsystem.demo.dto;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FacilityRequestDTO {

    private String facilityCode;
    private String facilityName;
    private String location;
    private String country;
    private String siteType;
    private String description;
    private List<String> facilitySystems;
    private Boolean isActive; // Optional, can default to true
}
