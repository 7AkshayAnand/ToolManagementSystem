package com.toolmanagementsystem.demo.utility;

import com.toolmanagementsystem.demo.dto.ToolResponseDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Component
public class ExcelToolExporter {

    public ByteArrayInputStream exportTools(List<ToolResponseDTO> tools) {

        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Tools");

            // Header
            Row header = sheet.createRow(0);
            String[] cols = {
                    "Tool Name", "Tool Type", "Manufacturer",
                    "Model Number", "Serial Number", "Status",
                    "Quantity", "Remarks", "Facility Id"
            };

            for (int i = 0; i < cols.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(cols[i]);
            }

            // Data rows
            int rowIdx = 1;
            for (ToolResponseDTO dto : tools) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(dto.getToolName());
                row.createCell(1).setCellValue(dto.getToolType().name());
                row.createCell(2).setCellValue(dto.getManufacturer());
                row.createCell(3).setCellValue(dto.getModelNumber());
                row.createCell(4).setCellValue(dto.getSerialNumber());
                row.createCell(5).setCellValue(dto.getStatus().name());
                row.createCell(6).setCellValue(dto.getQuantity());
                row.createCell(7).setCellValue(dto.getRemarks());
                row.createCell(8).setCellValue(dto.getFacilityId());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (Exception e) {
            throw new RuntimeException("Failed to export Excel: " + e.getMessage());
        }
    }
}
