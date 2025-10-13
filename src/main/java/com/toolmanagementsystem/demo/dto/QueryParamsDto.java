package com.toolmanagementsystem.demo.dto;


import com.toolmanagementsystem.demo.enums.SiteLocation;
import com.toolmanagementsystem.demo.enums.SiteType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class QueryParamsDto {



    private String siteLocation;
    private String siteType;
    private String facilityCode;
    private Boolean isActive;




}
