package ru.project.calculations.util;

import org.apache.poi.ss.usermodel.*;
import ru.project.calculations.entity.Uncalculated;
import ru.project.calculations.repository.UncalculatedRepository;

import java.util.ArrayList;
import java.util.List;

import static ru.project.calculations.util.ExcelFileReaderUtil.getValueOfEquipments;

public final class UncalculatedUtil {

    private UncalculatedUtil() {
    }

    public static void createUncalculated(Sheet sheet,
                                          DataFormatter formatter,
                                          Workbook workbook,
                                          UncalculatedRepository uncalculatedRepository,
                                          long id) {
        List<Uncalculated> uncalculatedList = new ArrayList<>();
        int startRow = 6;
        int lastRow = sheet.getLastRowNum();
        for (int i = startRow; i <= lastRow; i++) {
            Row currentRow = sheet.getRow(i);
            if (currentRow == null) continue;
            boolean rowHasData = false;
            for (int col = 9; col <= 15; col++) {
                Cell cell = currentRow.getCell(col);
                if (cell != null && !formatter.formatCellValue(cell).trim().isEmpty()) {
                    rowHasData = true;
                    break;
                }
            }
            if (!rowHasData) {
                break;
            }
            int rowNum = currentRow.getRowNum();
            Uncalculated uncalculated = Uncalculated.builder()
                    .position(getValueOfEquipments(workbook, sheet, rowNum, 9))
                    .partition(getValueOfEquipments(workbook, sheet, rowNum, 10))
                    .name(getValueOfEquipments(workbook, sheet, rowNum, 11))
                    .standart(getValueOfEquipments(workbook, sheet, rowNum, 12))
                    .quality(getValueOfEquipments(workbook, sheet, rowNum, 13))
                    .quantity(getValueOfEquipments(workbook, sheet, rowNum, 14)
                            .replaceAll(",0+$", "")
                            .replaceAll("[\\s\\u00A0]+", " "))
                    .comment(getValueOfEquipments(workbook, sheet, rowNum, 15))
                    .calculationId(id)
                    .build();
            uncalculatedList.add(uncalculated);
        }
        uncalculatedRepository.saveAll(uncalculatedList);
    }

}