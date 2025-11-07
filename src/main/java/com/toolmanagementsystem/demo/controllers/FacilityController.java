package com.toolmanagementsystem.demo.controllers;

import com.toolmanagementsystem.demo.dto.FacilityRequestDTO;
import com.toolmanagementsystem.demo.dto.FacilityResponseDTO;
import com.toolmanagementsystem.demo.dto.QueryParamsDto;
import com.toolmanagementsystem.demo.entity.Facility;
import com.toolmanagementsystem.demo.enums.SiteLocation;
import com.toolmanagementsystem.demo.enums.SiteType;
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





    @GetMapping("/getById/{id}")
    public ResponseEntity<FacilityResponseDTO> getById(@PathVariable Long id){
      FacilityResponseDTO facilityResponseDTO=facilityService.getById(id);

      return new ResponseEntity<>(facilityResponseDTO,HttpStatus.OK);
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
      String response=  facilityService.deleteById(id);
        return new ResponseEntity<>(response,HttpStatus.NO_CONTENT);  // HTTP 204
    }

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


    @GetMapping("/getAllFacitliy")
    public ResponseEntity<List<FacilityResponseDTO>> getAllFacility(){
        List<FacilityResponseDTO> responseDTOs = facilityService.getAllFacilityDetail();
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTOs);
    }

    @GetMapping("/getFacilityByPage/{pageNumber}")
    public ResponseEntity<List<FacilityResponseDTO>> getFacilityByPage(@PathVariable int pageNumber,@RequestParam(defaultValue = "5") int pageSize) {

        List<FacilityResponseDTO> facilities = facilityService.getFacilityByPage(pageNumber, pageSize);
        return ResponseEntity.ok(facilities);
    }


    @GetMapping("/search")
    public ResponseEntity<List<FacilityResponseDTO>> searchFacilities(@ModelAttribute QueryParamsDto queryParams) {
         log.info("the location is {} ",queryParams.getSiteLocation());
        List<FacilityResponseDTO> facilities = facilityService.searchFacilities(queryParams);
        return ResponseEntity.ok(facilities);
    }

}
