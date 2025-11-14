package com.toolmanagementsystem.demo.dto;

import com.toolmanagementsystem.demo.enums.ToolStatus;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ToolQueryParamsDTO {

    private Long id;
    private String toolName;
    private String toolType;
    private List<ToolStatus> statuses;;
    private Long facilityId;


    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdFrom;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdTo;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime updatedFrom;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime updatedTo;

    private Integer page = 0;
    private Integer size = 10;
    private String sortBy = "id";
    private String sortDir = "asc";
}

//work on locaiton part
//work on multivalued search part