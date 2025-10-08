package com.toolmanagementsystem.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ToolRequestDTO {

    @NotBlank(message = "Tool name is required")
    public String toolName;

    @NotBlank(message = "Tool type is required")
    public String toolType;

    @NotBlank(message = "Manufacturer is required")
    public String manufacturer;

    public String modelNumber;

    public String serialNumber;

    @NotBlank(message = "Status is required")
    public String status;

    @NotNull(message = "Quantity is required")
    public Integer quantity;

    public String remarks;

    @NotNull(message = "Facility ID is required")
    public Long facilityId;
}
