package com.toolmanagementsystem.demo.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ToolResponseDTO {

    public Long id;
    public String toolName;
    public String toolType;
    public String manufacturer;
    public String modelNumber;
    public String serialNumber;
    public String status;
    public Integer quantity;
    public String remarks;
    public Long facilityId;

    // Optional audit fields
    public LocalDateTime createdDate;
    public LocalDateTime updatedDate;
    public String createdBy;
    public String updatedBy;
}
