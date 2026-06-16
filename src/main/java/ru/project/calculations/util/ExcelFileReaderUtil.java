package ru.project.calculations.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;

public class ExcelFileReaderUtil {

    public ExcelFileReaderUtil() {
    }

    public static String getValueOfEquipments(Workbook workbook,
                                              Sheet sheet,
                                              int rowIndex,
                                              int columnIndex) {
        String value = null;
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        DataFormatter formatter = new DataFormatter();
        Row row = sheet.getRow(rowIndex);
        if (row != null) {
            Cell cell = row.getCell(columnIndex);
            if (cell != null) {
                if (cell.getCellType() == CellType.FORMULA) {
                    value = formatter.formatCellValue(cell, evaluator);
                } else {
                    value = formatter.formatCellValue(cell);
                }
            }
        }
        return value;
    }

    public static boolean validateExcelFile(MultipartFile file) throws IOException {
        Pattern patternDocumentType = Pattern.compile("(СО|ВОМ|ВР|ОЛ|ТЗ|АЛ)");
        Pattern patternMainContent = Pattern.compile("^[0-9\\s\\u00A0,.%]+$");

        int column = 2;
        int[] columns = {4, 5, 6, 7};

        try (InputStream is = file.getInputStream();
             var workbook = WorkbookFactory.create(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            DataFormatter formatter = new DataFormatter();
            List<CellRangeAddress> mergedRegions = sheet.getMergedRegions();
            for (int rowIndex = 6; ; rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) break; // конец строк

                Cell cellDocType = row.getCell(column);
                if (cellDocType == null) continue;
                for (CellRangeAddress range : mergedRegions) {
                    if (range.isInRange(rowIndex, column)) return false;
                }
                String cellValueDocType = formatter.formatCellValue(cellDocType);
                if (cellValueDocType.trim().isEmpty()) break;
                if (!patternDocumentType.matcher(cellValueDocType).matches()) return false;

                for (int col : columns) {
                    Cell cellMainContent = row.getCell(col);
                    if (cellMainContent == null) continue;
                    for (CellRangeAddress range : mergedRegions) {
                        if (range.isInRange(rowIndex, col)) return false;
                    }
                    String cellValueMainContent = cellMainContent.getCellType() == CellType.FORMULA
                            ? formatter.formatCellValue(cellMainContent, evaluator)
                            : formatter.formatCellValue(cellMainContent);
                    if (cellValueMainContent.trim().isEmpty()) continue;
                    if (!patternMainContent.matcher(cellValueMainContent).matches()) return false;
                }
            }
            return true;
        }
    }

    public static void fileOutputStream(String folderName,
                                        MultipartFile file,
                                        String key) throws IOException {
        Path path = Paths.get(String.valueOf(new File(folderName)), key);
        Path filePath = Files.createFile(path);
        try (FileOutputStream stream = new FileOutputStream(filePath.toString())) {
            stream.write(file.getBytes());
        }
    }

    public static String removeAfterLastDigit(String input) {
        int lastDigitIndex = -1;
        for (int i = input.length() - 1; i >= 0; i--) {
            if (Character.isDigit(input.charAt(i))) {
                lastDigitIndex = i;
                break;
            }
        }
        if (lastDigitIndex >= 0) {
            return input.substring(0, lastDigitIndex + 1).trim();
        } else {
            return input;
        }
    }

    public static String trimAfterLastDigit(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        int lastDigitIndex = -1;
        for (int i = input.length() - 1; i >= 0; i--) {
            if (Character.isDigit(input.charAt(i))) {
                lastDigitIndex = i;
                break;
            }
        }
        if (lastDigitIndex == -1) {
            return "";
        }
        return input.substring(0, lastDigitIndex + 1);
    }

}