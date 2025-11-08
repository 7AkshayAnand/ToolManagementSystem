package com.toolmanagementsystem.demo.utility;

import com.toolmanagementsystem.demo.dto.*;
import com.toolmanagementsystem.demo.enums.ToolStatus;
import com.toolmanagementsystem.demo.enums.ToolType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class ExcelToolParser {

    public List<ToolRequestDTO> parse(MultipartFile file) {

        List<ToolRequestDTO> list = new ArrayList<>();

        try (InputStream is = file.getInputStream();
             XSSFWorkbook workbook = new XSSFWorkbook(is)) {

            XSSFSheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next(); // Skip header row

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                ToolRequestDTO dto = ToolRequestDTO.builder()
                        .toolName(getString(row, 0))
                        .toolType(ToolType.valueOf(getString(row, 1).toUpperCase()))
                        .manufacturer(getString(row, 2))
                        .modelNumber(getString(row, 3))
                        .serialNumber(getString(row, 4))
                        .status(ToolStatus.valueOf(getString(row, 5).toUpperCase()))
                        .quantity((int) row.getCell(6).getNumericCellValue())
                        .remarks(getString(row, 7))
                        .facilityId((long) row.getCell(8).getNumericCellValue())
                        .build();

                list.add(dto);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Excel: " + e.getMessage());
        }

        return list;
    }

    private String getString(Row row, int col) {
        Cell cell = row.getCell(col);
        return cell == null ? "" : cell.toString().trim();
    }
}
