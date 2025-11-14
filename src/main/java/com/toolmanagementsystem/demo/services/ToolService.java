package com.toolmanagementsystem.demo.services;

import com.toolmanagementsystem.demo.dto.*;
import com.toolmanagementsystem.demo.entity.Facility;
import com.toolmanagementsystem.demo.entity.Tool;
import com.toolmanagementsystem.demo.enums.ToolStatus;
import com.toolmanagementsystem.demo.enums.ToolType;
import com.toolmanagementsystem.demo.repository.FacilityRepository;
import com.toolmanagementsystem.demo.repository.ToolRepository;
import com.toolmanagementsystem.demo.spefication.ToolSpecification;
import com.toolmanagementsystem.demo.utility.ExcelToolExporter;
import com.toolmanagementsystem.demo.utility.ExcelToolParser;
import io.swagger.v3.oas.annotations.servers.Server;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
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
    private final ExcelToolExporter exporter;
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



//    public ByteArrayInputStream exportTools() {
//
//        List<Tool> tools = toolRepository.findAll();
//
//        List<ToolResponseDTO> dtoList = tools.stream()
//                .map(t -> modelMapper.map(t, ToolResponseDTO.class))
//                .toList();
//
//        return exporter.exportTools(dtoList);
//    }


    public ByteArrayInputStream exportTools(List<ToolResponseDTO> filterTools) {

        List<ToolResponseDTO> toolsToExport;

        if (filterTools == null || filterTools.isEmpty()) {
            // No filter passed → fetch all tools
            toolsToExport = toolRepository.findAll().stream()
                    .map(t -> modelMapper.map(t, ToolResponseDTO.class))
                    .toList();
        } else {
            // Filters exist → use filtered list
            toolsToExport = filterTools;
        }

        return exporter.exportTools(toolsToExport);
    }



    public ResponseEntity<ToolResponseDTO> getToolById(Long id) {

        Tool tool = toolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tool not found"));

        ToolResponseDTO dto = modelMapper.map(tool, ToolResponseDTO.class);

        return ResponseEntity.ok(dto);
    }

    public String deleteById(Long id) {
        Tool tool = toolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tool not found"));

        toolRepository.deleteById(id);

        return "Tool Successfully Deleted";

    }


    @Transactional
    public ToolResponseDTO updatePartial(Long id, ToolPatchDTO patchDto) {

        Tool tool = toolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tool not found"));

        // Update only non-null fields
        if (patchDto.getToolName() != null) tool.setToolName(patchDto.getToolName());
        if (patchDto.getToolType() != null) tool.setToolType(patchDto.getToolType());
        if (patchDto.getManufacturer() != null) tool.setManufacturer(patchDto.getManufacturer());
        if (patchDto.getModelNumber() != null) tool.setModelNumber(patchDto.getModelNumber());
        if (patchDto.getSerialNumber() != null) tool.setSerialNumber(patchDto.getSerialNumber());

        if (patchDto.getQuantity() != null) tool.setQuantity(patchDto.getQuantity());
        if (patchDto.getRemarks() != null) tool.setRemarks(patchDto.getRemarks());

        // Handle facility change
        if (patchDto.getFacilityId() != null &&
                !patchDto.getFacilityId().equals(tool.getFacility().getId())) {

            Facility newFacility = facilityRepository.findById(patchDto.getFacilityId())
                    .orElseThrow(() -> new RuntimeException("Facility not found"));
            tool.setFacility(newFacility);
        }

        Tool savedTool = toolRepository.save(tool);

        ToolResponseDTO response = modelMapper.map(savedTool, ToolResponseDTO.class);
        response.setFacilityId(savedTool.getFacility().getId());
        response.setLocation(savedTool.getFacility().getSiteLocation());

        return response;
    }

    public ToolResponseDTO updateFull(Long id, ToolRequestDTO dto) {
        Tool existingTool = toolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tool not found"));

        // Map all fields from DTO onto existing entity
        existingTool.setToolName(dto.getToolName());
        existingTool.setToolType(dto.getToolType());
        existingTool.setManufacturer(dto.getManufacturer());
        existingTool.setModelNumber(dto.getModelNumber());
        existingTool.setSerialNumber(dto.getSerialNumber());

        existingTool.setQuantity(dto.getQuantity());
        existingTool.setRemarks(dto.getRemarks());

        // Make sure the facility entity is set properly
        Facility facility = facilityRepository.findById(dto.getFacilityId())
                .orElseThrow(() -> new RuntimeException("Facility not found"));
        existingTool.setFacility(facility);


        Tool savedTool = toolRepository.save(existingTool);

        // Map entity to response DTO
        ToolResponseDTO response = modelMapper.map(savedTool, ToolResponseDTO.class);
        response.setFacilityId(facility.getId());
        response.setLocation(facility.getSiteLocation());

        return response;
    }

//below is only filter not pagination and sorting
 /*   public List<ToolResponseDTO> searchTools(ToolQueryParamsDTO params) {

        // Convert Strings to Enums (safe)
        ToolType toolTypeEnum = params.getToolType() == null
                ? null
                : ToolType.valueOf(params.getToolType().toUpperCase());

//        ToolStatus statusEnum = params.getStatus() == null
//                ? null
//                : ToolStatus.valueOf(params.getStatus().toUpperCase());

        // Use Specification.allOf similar to your Facility code
        Specification<Tool> spec = Specification.allOf(

                ToolSpecification.hasToolType(toolTypeEnum),
                ToolSpecification.hasStatuses(params.getStatuses()),
                ToolSpecification.hasToolName(params.getToolName()),
                ToolSpecification.hasFacility(params.getFacilityId()),

                ToolSpecification.createdFrom(params.getCreatedFrom()),
                ToolSpecification.createdTo(params.getCreatedTo()),
                ToolSpecification.updatedFrom(params.getUpdatedFrom()),
                ToolSpecification.updatedTo(params.getUpdatedTo())
        );

        List<Tool> tools = toolRepository.findAll(spec);

        return tools.stream()
                .map(tool -> {
                    ToolResponseDTO dto = modelMapper.map(tool, ToolResponseDTO.class);
                    dto.setFacilityId(tool.getFacility().getId());
                    dto.setLocation(tool.getFacility().getSiteLocation()); // FIXED
                    return dto;
                })
                .toList();
    }




    public Page<ToolResponseDTO> searchToolsWithPagination(ToolQueryParamsDTO params) {

        // Convert Strings to Enums
        ToolType toolTypeEnum = params.getToolType() == null
                ? null
                : ToolType.valueOf(params.getToolType().toUpperCase());

        // Build Specifications
        Specification<Tool> spec = Specification.allOf(
                ToolSpecification.hasToolType(toolTypeEnum),
                ToolSpecification.hasStatuses(params.getStatuses()),
                ToolSpecification.hasToolName(params.getToolName()),
                ToolSpecification.hasFacility(params.getFacilityId()),
                ToolSpecification.createdFrom(params.getCreatedFrom()),
                ToolSpecification.createdTo(params.getCreatedTo()),
                ToolSpecification.updatedFrom(params.getUpdatedFrom()),
                ToolSpecification.updatedTo(params.getUpdatedTo())
        );

        // Sorting
        Sort sort = params.getSortDir().equalsIgnoreCase("desc")
                ? Sort.by(params.getSortBy()).descending()
                : Sort.by(params.getSortBy()).ascending();

        // Pagination
        Pageable pageable = PageRequest.of(params.getPage(), params.getSize(), sort);

        // Fetch paginated & sorted tools
        Page<Tool> toolPage = toolRepository.findAll(spec, pageable);

        // Convert to DTO Page
        return toolPage.map(tool -> {
            ToolResponseDTO dto = modelMapper.map(tool, ToolResponseDTO.class);
            dto.setFacilityId(tool.getFacility().getId());
            dto.setLocation(tool.getFacility().getSiteLocation());
            return dto;
        });
    }*/







    public List<ToolResponseDTO> searchTools(ToolQueryParamsDTO params) {

        Specification<Tool> spec = buildToolSpecification(params);

        List<Tool> tools = toolRepository.findAll(spec);

        return tools.stream()
                .map(tool -> {
                    ToolResponseDTO dto = modelMapper.map(tool, ToolResponseDTO.class);
                    dto.setFacilityId(tool.getFacility().getId());
                    dto.setLocation(tool.getFacility().getSiteLocation());
                    return dto;
                })
                .toList();
    }

    public Page<ToolResponseDTO> searchToolsWithPagination(ToolQueryParamsDTO params) {

        Specification<Tool> spec = buildToolSpecification(params);

        Sort sort = params.getSortDir().equalsIgnoreCase("desc")
                ? Sort.by(params.getSortBy()).descending()
                : Sort.by(params.getSortBy()).ascending();

        Pageable pageable = PageRequest.of(params.getPage(), params.getSize(), sort);

        Page<Tool> toolPage = toolRepository.findAll(spec, pageable);

        return toolPage.map(tool -> {
            ToolResponseDTO dto = modelMapper.map(tool, ToolResponseDTO.class);
            dto.setFacilityId(tool.getFacility().getId());
            dto.setLocation(tool.getFacility().getSiteLocation());
            return dto;
        });
    }




    private Specification<Tool> buildToolSpecification(ToolQueryParamsDTO params) {

        ToolType toolTypeEnum = params.getToolType() == null
                ? null
                : ToolType.valueOf(params.getToolType().toUpperCase());

        return Specification.allOf(
                ToolSpecification.hasToolType(toolTypeEnum),
                ToolSpecification.hasStatuses(params.getStatuses()),
                ToolSpecification.hasToolName(params.getToolName()),
                ToolSpecification.hasFacility(params.getFacilityId()),
                ToolSpecification.createdFrom(params.getCreatedFrom()),
                ToolSpecification.createdTo(params.getCreatedTo()),
                ToolSpecification.updatedFrom(params.getUpdatedFrom()),
                ToolSpecification.updatedTo(params.getUpdatedTo())
        );
    }

    public List<ToolResponseDTO> getToolsByFacilityId(Long facilityId) {

        List<Tool> tools = toolRepository.findByFacilityId(facilityId);

        return tools.stream()
                .map(tool -> {
                    ToolResponseDTO dto = modelMapper.map(tool, ToolResponseDTO.class);
                    dto.setFacilityId(tool.getFacility().getId());
                    dto.setLocation(tool.getFacility().getSiteLocation()); // IMPORTANT
                    return dto;
                })
                .toList();
    }
}
