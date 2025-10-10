package com.toolmanagementsystem.demo.dto;

import com.toolmanagementsystem.demo.enums.ToolStatus;
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
    public ToolStatus status;
    public Integer quantity;
    public String remarks;
    public Long facilityId;
    public String location;

    // Optional audit fields
    public LocalDateTime createdDate;
    public LocalDateTime updatedDate;
    public String createdBy;
    public String updatedBy;
}
