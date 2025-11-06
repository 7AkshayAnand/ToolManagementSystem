package com.toolmanagementsystem.demo.services;

import com.toolmanagementsystem.demo.dto.FacilityRequestDTO;
import com.toolmanagementsystem.demo.dto.FacilityResponseDTO;
import com.toolmanagementsystem.demo.dto.QueryParamsDto;
import com.toolmanagementsystem.demo.entity.Facility;
import com.toolmanagementsystem.demo.enums.SiteLocation;
import com.toolmanagementsystem.demo.enums.SiteType;
import com.toolmanagementsystem.demo.repository.FacilityRepository;
import com.toolmanagementsystem.demo.spefication.FacilitySpecification;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

    public List<FacilityResponseDTO> getAllFacilityDetail() {

        List<Facility> facilities=facilityRepository.findAll();

        List<FacilityResponseDTO> responseDTOs = facilities.stream()
                .map(f -> modelMapper.map(f, FacilityResponseDTO.class))
                .toList();
        return responseDTOs;
    }

    public List<FacilityResponseDTO> getFacilityByPage(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by("id").descending());
        Page<Facility> facilityPage = facilityRepository.findAll(pageable);

        return facilityPage.getContent()
                .stream()
                .map(facility -> modelMapper.map(facility, FacilityResponseDTO.class))
                .collect(Collectors.toList());
    }


    public List<FacilityResponseDTO> searchFacilities(QueryParamsDto queryParams) {

        // 1️⃣ Convert Strings to enums safely
        log.info("now we are in service package and location is "+queryParams.getSiteLocation());
        SiteLocation siteLocationEnum = queryParams.getSiteLocation() == null
                ? null
                : SiteLocation.valueOf(queryParams.getSiteLocation().toUpperCase());

        log.info("converted sitelocation to enum and value is "+siteLocationEnum);

        SiteType siteTypeEnum = queryParams.getSiteType() == null
                ? null
                : SiteType.valueOf(queryParams.getSiteType().toUpperCase());


// 2️⃣ Pass enums to Specification
        Specification<Facility> specification = Specification.allOf(
                FacilitySpecification.hasSiteLocation(siteLocationEnum),
                FacilitySpecification.hasSiteType(siteTypeEnum),
                FacilitySpecification.hasFacilityCode(queryParams.getFacilityCode()),
                FacilitySpecification.isActive(queryParams.getIsActive())
        );


        List<Facility> facilities = facilityRepository.findAll(specification);
        log.info("after db call to findall method  "+facilities.get(0).getSiteLocation());
        // Map each Facility to FacilityResponseDTO using ModelMapper
        List<FacilityResponseDTO> facilityDTOs = facilities.stream()
                .map(facility -> modelMapper.map(facility, FacilityResponseDTO.class))
                .toList();

        log.info("before returning the value dto "+facilityDTOs.get(0).getSiteLocation());

        return facilityDTOs;
    }
}
