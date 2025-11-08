package com.toolmanagementsystem.demo.dto;

import com.toolmanagementsystem.demo.enums.ToolStatus;
import com.toolmanagementsystem.demo.enums.ToolType;
import lombok.Data;

@Data
public class ToolPatchDTO {

    private String toolName;       // optional
    private ToolType toolType;     // optional
    private String manufacturer;   // optional
    private String modelNumber;    // optional
    private String serialNumber;   // optional
        // optional
    private Integer quantity;      // optional
    private String remarks;        // optional
    private Long facilityId;       // optional, can move to another facility
}
