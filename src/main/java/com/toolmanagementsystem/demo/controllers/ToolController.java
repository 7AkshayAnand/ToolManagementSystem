package com.toolmanagementsystem.demo.controllers;

import com.toolmanagementsystem.demo.dto.*;
import com.toolmanagementsystem.demo.services.ToolService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.List;

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


    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadTools(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        if (!file.getOriginalFilename().endsWith(".xlsx")) {
            return ResponseEntity.badRequest().body("Only .xlsx files allowed");
        }

        log.info("Bulk import triggered for file {}", file.getOriginalFilename());


//        BulkImportResult result = bulkImportService.importTools(file);
         BulkImportResult result=toolService.importTools(file);
        return ResponseEntity.ok(result);
    }

    @GetMapping("getById/{id}")
    public ResponseEntity<ToolResponseDTO> getToolById(@PathVariable Long id){
        return toolService.getToolById(id);
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportTools() {

        ByteArrayInputStream in = toolService.exportTools();

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=tools.xlsx")
                .body(in.readAllBytes());
    }

    @DeleteMapping("/delete/{Id}")
    public ResponseEntity<String> deleteById(Long Id){
        String result=toolService.deleteById(Id);
        return new ResponseEntity<>(result,HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/updateTool/{id}")
    public ResponseEntity<ToolResponseDTO> patchTool(@PathVariable Long id,@RequestBody ToolPatchDTO patchDto) {

        ToolResponseDTO updatedTool = toolService.updatePartial(id, patchDto);
        return ResponseEntity.ok(updatedTool);
    }

    @PutMapping("/updateAll/{id}")
    public ResponseEntity<ToolResponseDTO> putTool(@PathVariable Long id,@RequestBody @Valid ToolRequestDTO toolRequestDTO) {

        ToolResponseDTO updatedTool = toolService.updateFull(id, toolRequestDTO);
        return ResponseEntity.ok(updatedTool);
    }


    @GetMapping("/search")
    public List<ToolResponseDTO> searchTools(ToolQueryParamsDTO params) {
        return toolService.searchTools(params);
    }

    @GetMapping("/facility/{facilityId}")
    public ResponseEntity<List<ToolResponseDTO>> getToolsByFacilityId(
            @PathVariable Long facilityId) {

        List<ToolResponseDTO> tools = toolService.getToolsByFacilityId(facilityId);
        return ResponseEntity.ok(tools);
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