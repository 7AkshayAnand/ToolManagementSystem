package com.toolmanagementsystem.demo.services;

import com.toolmanagementsystem.demo.dto.ToolRequestDTO;
import com.toolmanagementsystem.demo.dto.ToolResponseDTO;
import com.toolmanagementsystem.demo.entity.Facility;
import com.toolmanagementsystem.demo.entity.Tool;
import com.toolmanagementsystem.demo.repository.FacilityRepository;
import com.toolmanagementsystem.demo.repository.ToolRepository;
import io.swagger.v3.oas.annotations.servers.Server;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@AllArgsConstructor

@Slf4j
@Transactional
public class ToolService {

    private final ToolRepository toolRepository;
    private final FacilityRepository facilityRepository;
    private final ModelMapper modelMapper;
    public ToolResponseDTO enterTool(ToolRequestDTO toolRequestDTO) {



        Facility facility = facilityRepository.findById(toolRequestDTO.facilityId)
                .orElseThrow(() -> new RuntimeException("Facility not found"));


        Tool tool = modelMapper.map(toolRequestDTO, Tool.class);
        log.trace("inside service manufac details are "+tool.getManufacturer());


        tool.setFacility(facility);
        log.info("we have set the facility  as "+tool.getFacility());
        Tool savedTool=toolRepository.save(tool);


        ToolResponseDTO response = modelMapper.map(savedTool, ToolResponseDTO.class);
        log.trace("now in response the manufacturar is "+response.getManufacturer());

        response.location = facility.getSiteLocation();
        response.facilityId = facility.getId();

        return response;
    }
}
