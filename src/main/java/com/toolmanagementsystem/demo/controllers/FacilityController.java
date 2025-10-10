package com.toolmanagementsystem.demo.controllers;

import com.toolmanagementsystem.demo.dto.FacilityRequestDTO;
import com.toolmanagementsystem.demo.dto.FacilityResponseDTO;
import com.toolmanagementsystem.demo.services.FacilityService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/facility")
@AllArgsConstructor
@Slf4j
public class FacilityController {

    private final FacilityService facilityService;


    @PostMapping("/singleEntry")
    public ResponseEntity<FacilityResponseDTO> createFacility(@Valid @RequestBody FacilityRequestDTO facilityRequestDTO) {
        // Call the service layer to save the facility


        FacilityResponseDTO responseDTO = facilityService.createFacility(facilityRequestDTO);
        log.info("facility created with code "+responseDTO.getFacilityCode());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(responseDTO);
    }




    @PostMapping("/bulkEntry")
    public ResponseEntity<List<FacilityResponseDTO>> createFacilityBulk(
            @Valid @RequestBody List<FacilityRequestDTO> facilityRequestDTOList) {

        List<FacilityResponseDTO> responseDTOs = facilityService.createFacilityBulk(facilityRequestDTOList);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTOs);
    }
}
