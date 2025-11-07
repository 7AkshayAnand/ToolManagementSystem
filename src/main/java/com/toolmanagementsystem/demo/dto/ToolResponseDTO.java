package com.toolmanagementsystem.demo.dto;

import com.toolmanagementsystem.demo.enums.SiteLocation;
import com.toolmanagementsystem.demo.enums.ToolStatus;
import com.toolmanagementsystem.demo.enums.ToolType;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ToolResponseDTO {

    public Long id;
    public String toolName;
    public ToolType toolType;
    public String manufacturer;
    public String modelNumber;
    public String serialNumber;
    public ToolStatus status;
    public Integer quantity;
    public String remarks;
    public Long facilityId;
    public SiteLocation location;

    // Optional audit fields
    public LocalDateTime createdDate;
    public LocalDateTime updatedDate;
    public String createdBy;
    public String updatedBy;
}
