package ru.project.calculations.util;

import org.apache.poi.ss.usermodel.*;
import ru.project.calculations.entity.Partition;
import ru.project.calculations.enums.ContentType;
import ru.project.calculations.repository.PartitionRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static ru.project.calculations.util.ExcelFileReaderUtil.*;

public class PartitionUtil {

    private PartitionUtil() {
    }

    public static void createPartition(Sheet sheet,
                                        DataFormatter formatter,
                                        Workbook workbook,
                                        PartitionRepository partitionRepository,
                                        long id) {
        List<Partition> partitionList = new ArrayList<>();
        int startRow = 6;
        int lastRow = sheet.getLastRowNum();
        for (int i = startRow; i <= lastRow; i++) {
            Row currentRow = sheet.getRow(i);
            if (currentRow == null) continue;
            boolean rowHasData = false;
            for (int col = 1; col <= 7; col++) {
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
            Partition partition = Partition.builder()
                    .position(getValueOfEquipments(workbook, sheet, rowNum, 1))
                    .contentType(ContentType.valueOf(getValueOfEquipments(workbook, sheet, rowNum, 2)))
                    .partition(getValueOfEquipments(workbook, sheet, rowNum, 3))
                    .sum(new BigDecimal(removeAfterLastDigit(getValueOfEquipments(
                            workbook, sheet, rowNum, 4)
                            .replaceAll("[\\s\\u00A0]+", "")
                            .replace(",", "."))))
                    .calculated(Integer.parseInt(removeAfterLastDigit(getValueOfEquipments(
                            workbook, sheet, rowNum, 5)
                            .replaceAll("[,.].*", "")
                            .replaceAll("[\\s\\u00A0]+", ""))))
                    .total(Integer.parseInt(removeAfterLastDigit(getValueOfEquipments(
                            workbook, sheet, rowNum, 6)
                            .replaceAll("[,.].*", "")
                            .replaceAll("[\\s\\u00A0]+", ""))))
                    .percent(Double.parseDouble(trimAfterLastDigit(removeAfterLastDigit(getValueOfEquipments(
                            workbook, sheet, rowNum, 7)
                            .replaceAll("[\\s\\u00A0]+", "")
                            .replace(",", ".")))))
                    .calculationId(id)
                    .build();
            partitionList.add(partition);
        }
        partitionRepository.saveAll(partitionList);
    }

}