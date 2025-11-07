package com.toolmanagementsystem.demo.controllers;

import com.toolmanagementsystem.demo.dto.ToolRequestDTO;
import com.toolmanagementsystem.demo.dto.ToolResponseDTO;
import com.toolmanagementsystem.demo.services.ToolService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tool")
@AllArgsConstructor
@Slf4j
public class ToolController {

  private final  ToolService toolService;
    @PostMapping("/enter")
    public ResponseEntity<ToolResponseDTO> enterTool(@RequestBody @Valid ToolRequestDTO toolRequestDTO){
        log.trace("manufacturaro is "+toolRequestDTO.getManufacturer());
        ToolResponseDTO toolResponseDTO=toolService.enterTool(toolRequestDTO);

        return new ResponseEntity<>(toolResponseDTO, HttpStatus.OK);

    }
}


/*
a)post create tool
b)get by id
c)GET /api/tools
Query Params (recommended):

Param	     Description
page	     Pagination
size	     Pagination
toolType	 Filter by type
status	     Filter by status
facilityId	 Filter by facility
manufacturer	Filter
search	Search by name/code


GET /api/tools?page=0&size=20&status=ACCEPTED&facilityId=38

d)update PUt by id
e)patch partial update

g)delete by id
h)move tool to another facility
i)get tool by facility




 */