package com.toolmanagementsystem.demo.dto;

import com.toolmanagementsystem.demo.enums.ToolStatus;
import com.toolmanagementsystem.demo.enums.ToolType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ToolRequestDTO {

    @NotBlank(message = "Tool name is required")
    public String toolName;

    @Enumerated(EnumType.STRING)
    public ToolType toolType;

    @NotBlank(message = "Manufacturer is required")
    public String manufacturer;

    public String modelNumber;

    public String serialNumber;

    @Enumerated(EnumType.STRING)

    public ToolStatus status;

    @NotNull(message = "Quantity is required")
    public Integer quantity;

    public String remarks;

    @NotNull(message = "Facility ID is required")
    public Long facilityId;
}
