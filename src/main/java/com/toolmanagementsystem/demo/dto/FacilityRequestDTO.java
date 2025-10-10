package com.toolmanagementsystem.demo.dto;

import com.toolmanagementsystem.demo.enums.SiteLocation;
import com.toolmanagementsystem.demo.enums.SiteType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FacilityRequestDTO {

    @NotBlank(message = "Facility code cannot be blank")
    @Size(max = 20, message = "Facility code cannot exceed 20 characters")
    private String facilityCode;

    @NotBlank(message = "Facility name cannot be blank")
    @Size(max = 100, message = "Facility name cannot exceed 100 characters")
    private String facilityName;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Site type must be provided")
    private SiteLocation siteLocation;



    @NotNull(message = "Site type must be provided")
    @Enumerated(EnumType.STRING)
    private SiteType siteType;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotNull(message = "Facility systems list cannot be null")
    @Size(min = 1, message = "At least one facility system must be provided")
    private List<String> facilitySystems;

    @NotNull(message = "isActive flag must be provided")
    private Boolean isActive;
}
