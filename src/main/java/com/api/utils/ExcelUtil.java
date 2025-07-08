package com.api.utils;

import java.io.FileInputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 📊 ExcelUtil.java
 *
 * 📌 Utility class to read Excel files for data-driven testing.
 * Supports `.xlsx` files using Apache POI.
 */
public class ExcelUtil {

    /**
     * Read data from Excel by specifying sheet name, row, and cell.
     */
    public static String getCellValue(String filePath, String sheetName, int rowNum, int cellNum) {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            Sheet sheet = workbook.getSheet(sheetName);
            Row row = sheet.getRow(rowNum);
            Cell cell = row.getCell(cellNum);
            return cell.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
