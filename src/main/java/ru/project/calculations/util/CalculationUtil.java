package ru.project.calculations.util;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import ru.project.calculations.entity.Calculation;
import ru.project.calculations.entity.Customer;
import ru.project.calculations.exception.DocumentsIOException;
import ru.project.calculations.repository.CustomerRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static ru.project.calculations.util.ExcelFileReaderUtil.*;

public final class CalculationUtil {

	private CalculationUtil() {
	}

	public static Map<Long, String> getCustomerNames(List<Calculation> calculations,
													 CustomerRepository customerRepository) {
		return customerRepository.findAllCustomersByIds(calculations.stream()
				.map(Calculation::getCustomerId)
				.distinct()
				.toList()).stream()
			.collect(Collectors.toMap(Customer::getId, Customer::getCustomerName));
	}

	public static void updateCalculationResultData(Calculation calculation,
												   Workbook workbook,
												   Sheet sheet) {
		String regex = "[\\s\\u00A0]+";
		calculation.setTotalSum(new BigDecimal(removeAfterLastDigit(
			getValueOfEquipments(workbook, sheet, 3, 4)
				.replaceAll(regex, "")
				.replace(",", "."))));
		calculation.setCalculatedPositionCount(Integer.parseInt(removeAfterLastDigit(
			getValueOfEquipments(workbook, sheet, 3, 5)
				.replaceAll("[,.].*", "")
				.replaceAll(regex, ""))));
		calculation.setTotalPositionCount(Integer.parseInt(removeAfterLastDigit(
			getValueOfEquipments(workbook, sheet, 3, 6)
				.replaceAll("[,.].*", "")
				.replaceAll(regex, ""))));
		calculation.setTotalPercent(Double.parseDouble(trimAfterLastDigit(
			removeAfterLastDigit(getValueOfEquipments(workbook, sheet, 3, 7)
				.replaceAll(regex, "")
				.replace(",", ".")))));
	}

	public static String createNewResourceFolder(String fileResource) {
		Path newFolderPath = Paths.get(fileResource).resolve("folder_" + UUID.randomUUID());
		try {
			Files.createDirectory(newFolderPath);
		} catch (IOException e) {
			throw new DocumentsIOException("io.exception.create.folders.message");
		}
		return String.valueOf(newFolderPath);
	}

	public static void deleteFolders(String folder) {
		try {
			Files.delete(Paths.get(folder));
		} catch (IOException e) {
			throw new DocumentsIOException("io.exception.delete.folders.message");
		}
	}

	public static int getNaturalIntIfNullOrEmpty(Object input) {
		if (input == null || input.equals("")) {
			return 0;
		}
		return Integer.parseInt(input.toString());
	}

	public static String getZeroIfNullOrEmptySumWithSuffix(Object input) {
		if (input == null || input.equals("") || input.equals("0")) {
			return "0,00";
		}
		String temp = input.toString();
		String[] parts = temp.split("\\.");
		String integerPart = parts[0];
		String decimalPart = parts.length > 1 ? parts[1] : null;
		StringBuilder reversed = new StringBuilder(integerPart).reverse();
		StringBuilder spaced = new StringBuilder();
		for (int i = 0; i < reversed.length(); i++) {
			if (i > 0 && i % 3 == 0) {
				spaced.append(' ');
			}
			spaced.append(reversed.charAt(i));
		}
		integerPart = spaced.reverse().toString();
		if (decimalPart != null && !decimalPart.isEmpty()) {
			return integerPart + "," + decimalPart + " ₽, б/НДС";
		} else {
			return integerPart;
		}
	}

	public static String getZeroIfNullOrEmptySum(Object input) {
		if (input == null || input.equals("") || input.equals("0")) {
			return "0,00";
		}
		String temp = input.toString();
		String[] parts = temp.split("\\.");
		String integerPart = parts[0];
		String decimalPart = parts.length > 1 ? parts[1] : null;
		StringBuilder reversed = new StringBuilder(integerPart).reverse();
		StringBuilder spaced = new StringBuilder();
		for (int i = 0; i < reversed.length(); i++) {
			if (i > 0 && i % 3 == 0) {
				spaced.append(' ');
			}
			spaced.append(reversed.charAt(i));
		}
		integerPart = spaced.reverse().toString();
		if (decimalPart != null && !decimalPart.isEmpty()) {
			return integerPart + "," + decimalPart;
		} else {
			return integerPart;
		}
	}

	public static String getZeroPercentIfNullOrEmptyWithSuffix(double input) {
		if (input == 0) {
			return " (0,00 %)";
		}
		return String.format(" (%.2f %%)", input);
	}

	public static String getZeroPercentIfNullOrEmpty(double input) {
		if (input == 0) {
			return " 0,00 %";
		}
		return String.format("%.2f %%", input);
	}

	public static String getRemainderPosition(int totalCount, int calculatedCount) {
		return String.format(", не расценено: %s", totalCount - calculatedCount);
	}

}