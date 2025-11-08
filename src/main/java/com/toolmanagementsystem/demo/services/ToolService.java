package com.toolmanagementsystem.demo.services;

import com.toolmanagementsystem.demo.dto.BulkImportResult;
import com.toolmanagementsystem.demo.dto.ToolRequestDTO;
import com.toolmanagementsystem.demo.dto.ToolResponseDTO;
import com.toolmanagementsystem.demo.entity.Facility;
import com.toolmanagementsystem.demo.entity.Tool;
import com.toolmanagementsystem.demo.repository.FacilityRepository;
import com.toolmanagementsystem.demo.repository.ToolRepository;
import com.toolmanagementsystem.demo.utility.ExcelToolParser;
import io.swagger.v3.oas.annotations.servers.Server;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor

@Slf4j
@Transactional
public class ToolService {

    private final ToolRepository toolRepository;
    private final FacilityRepository facilityRepository;
    private final ModelMapper modelMapper;


    private final ExcelToolParser excelToolParser;
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



    public BulkImportResult importTools(MultipartFile file) {

        List<ToolRequestDTO> rows = excelToolParser.parse(file);

        List<String> errors = new ArrayList<>();
        int successCount = 0;
        List<Tool> batch = new ArrayList<>();

        for (int i = 0; i < rows.size(); i++) {

            ToolRequestDTO dto = rows.get(i);
            int rowNum = i + 1;

            try {
                validateRow(dto, rowNum);
                Tool entity = mapToEntity(dto);
//                toolRepository.save(entity);
                batch.add(entity);


            } catch (Exception ex) {
                errors.add("Row " + rowNum + ": " + ex.getMessage());
            }
        }
        if (!batch.isEmpty()) {
            toolRepository.saveAll(batch);
        }

        return BulkImportResult.builder()
                .total(rows.size())
                .success(batch.size())
                .failed(errors.size())
                .errors(errors)
                .build();
    }

    private void validateRow(ToolRequestDTO dto, int rowNum) {

        if (!facilityRepository.existsById(dto.facilityId)) {
            throw new RuntimeException("Invalid facilityId: " + dto.facilityId);
        }

        if (dto.quantity == null || dto.quantity <= 0) {
            throw new RuntimeException("Quantity invalid");
        }

        if (dto.toolType == null) {
            throw new RuntimeException("ToolType missing");
        }

        if (dto.status == null) {
            throw new RuntimeException("Status missing");
        }
    }

    private Tool mapToEntity(ToolRequestDTO dto) {
        return Tool.builder()
                .toolName(dto.toolName)
                .toolType(dto.toolType)
                .manufacturer(dto.manufacturer)
                .modelNumber(dto.modelNumber)
                .serialNumber(dto.serialNumber)
                .status(dto.status)
                .quantity(dto.quantity)
                .remarks(dto.remarks)
                .facility(Facility.builder().id(dto.facilityId).build())
                .build();
    }


}
