package com.toolmanagementsystem.demo.services;

import com.toolmanagementsystem.demo.dto.FacilityRequestDTO;
import com.toolmanagementsystem.demo.dto.FacilityResponseDTO;
import com.toolmanagementsystem.demo.entity.Facility;
import com.toolmanagementsystem.demo.repository.FacilityRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class FacilityService {

    private final ModelMapper modelMapper;
    private final FacilityRepository facilityRepository;

    public FacilityResponseDTO createFacility(@Valid FacilityRequestDTO facilityRequestDTO) {

        log.info("incoming DTO Facility Code: {}", facilityRequestDTO.getFacilityCode());


        Facility  facility=modelMapper.map(facilityRequestDTO,Facility.class);
        log.info(" mapped Entity Facility Code: {}", facility.getFacilityCode());
        if(facilityRepository.existsByFacilityCode(facility.getFacilityCode())) {
            throw new RuntimeException("Facility code already exists: " + facility.getFacilityCode());
        }
//        log.info("dto converted to entity ");

        Facility savedFacility=facilityRepository.save(facility);
        log.info("facility saved successfully  with code "+savedFacility.getFacilityCode());


        return modelMapper.map(savedFacility, FacilityResponseDTO.class);
    }



    @Transactional
    public List<FacilityResponseDTO> createFacilityBulk(List<FacilityRequestDTO> facilityRequestDTOList) {

        List<Facility> facilitiesToSave = new ArrayList<>();
        List<String> duplicateCodes = new ArrayList<>();

        // 1. Convert DTOs to Entities and check duplicates
        for (FacilityRequestDTO dto : facilityRequestDTOList) {
            if (facilityRepository.existsByFacilityCode(dto.getFacilityCode())) {
                duplicateCodes.add(dto.getFacilityCode());
                log.warn("Skipping duplicate facility code: {}", dto.getFacilityCode());
                continue;
            }
            Facility facility = modelMapper.map(dto, Facility.class);
            facilitiesToSave.add(facility);
        }

        // 2. Save all valid facilities in batch
        List<Facility> savedFacilities = facilityRepository.saveAll(facilitiesToSave);

        // 3. Map saved entities to response DTOs
        List<FacilityResponseDTO> responseDTOs = savedFacilities.stream()
                .map(f -> modelMapper.map(f, FacilityResponseDTO.class))
                .toList();

        for(FacilityResponseDTO dto:responseDTOs){
            log.info("location is "+dto.getSiteLocation());
        }

        if (!duplicateCodes.isEmpty()) {
            log.warn("Skipped {} duplicate facility codes: {}", duplicateCodes.size(), duplicateCodes);
        }

        return responseDTOs;
    }

}
