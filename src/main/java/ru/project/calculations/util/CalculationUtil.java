package ru.project.calculations.util;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.ui.Model;
import ru.project.calculations.dto.calculation.CalculationDto;
import ru.project.calculations.entity.Calculation;
import ru.project.calculations.entity.Customer;
import ru.project.calculations.enums.Status;
import ru.project.calculations.exception.DocumentsIOException;
import ru.project.calculations.repository.CustomerRepository;
import ru.project.calculations.service.DocumentResourceService;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.stream.Collectors.toMap;
import static ru.project.calculations.enums.DocumentIndex.*;
import static ru.project.calculations.enums.DocumentIndex.MATERIAL_LIST_DOC;
import static ru.project.calculations.enums.DocumentIndex.OTHER_DOC;
import static ru.project.calculations.enums.DocumentIndex.SEPARATION_DOC;
import static ru.project.calculations.enums.DocumentIndex.TECHNITIAL_DOC;
import static ru.project.calculations.enums.DocumentIndex.WORK_LIST_DOC;
import static ru.project.calculations.enums.Status.RAW;
import static ru.project.calculations.util.DocumentsUtil.getFilesTotalParameters;
import static ru.project.calculations.util.ExcelFileReaderUtil.*;

public final class CalculationUtil {

	private CalculationUtil() {
	}

	public static List<Status> getStatusListCalculationForCreate() {
		return Arrays.stream(Status.values())
			.filter(e -> !e.equals(Status.ACTUAL))
			.filter(e -> !e.equals(Status.CLOSED))
			.toList();
	}

	public static List<Status> getStatusListCalculationForUpdate(CalculationDto calculationDto) {
		if (calculationDto.status().equals("Черновик")) {
			return Arrays.stream(Status.values())
				.filter(e -> !e.equals(Status.CLOSED))
				.filter(e -> !e.equals(Status.ACTUAL))
				.toList();
		} else {
			return Arrays.stream(Status.values())
				.filter(e -> !e.equals(RAW))
				.filter(e -> !e.equals(Status.ACTUAL))
				.toList();
		}
	}

	public static Map<Long, String> getCustomerNames(List<Calculation> calculations,
													 CustomerRepository customerRepository) {
		if (calculations == null || calculations.isEmpty()) {
			return null;
		}
		return customerRepository.findAllCustomersByIds(calculations.stream()
				.map(Calculation::getCustomerId)
				.distinct()
				.toList()).stream()
			.collect(toMap(Customer::getId, Customer::getCustomerName));
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

	public static void getAllResourceDocuments(long id,
											   DocumentResourceService documentResourceService,
											   Model model) {
		var mainDocuments = documentResourceService.findAllDocResourceByCalcIdAndIndex(id, MAIN_DOC);
		model.addAttribute("mainDocuments", mainDocuments);
		model.addAttribute("mainDocumentsResource", getFilesTotalParameters(mainDocuments));

		var partitionDocuments = documentResourceService.findAllDocResourceByCalcIdAndIndex(id, PARTITION_DOC);
		model.addAttribute("partitionDocuments", partitionDocuments);
		model.addAttribute("partitionDocumentsResource", getFilesTotalParameters(partitionDocuments));

		var specificationDocuments = documentResourceService.findAllDocResourceByCalcIdAndIndex(id, SPECIFICATION_DOC);
		model.addAttribute("specificationDocuments", specificationDocuments);
		model.addAttribute("specificationDocumentsResource", getFilesTotalParameters(specificationDocuments));

		var materialDocuments = documentResourceService.findAllDocResourceByCalcIdAndIndex(id, MATERIAL_LIST_DOC);
		model.addAttribute("materialDocuments", materialDocuments);
		model.addAttribute("materialDocumentsResource", getFilesTotalParameters(materialDocuments));

		var workDocuments = documentResourceService.findAllDocResourceByCalcIdAndIndex(id, WORK_LIST_DOC);
		model.addAttribute("workDocuments", workDocuments);
		model.addAttribute("workDocumentsResource", getFilesTotalParameters(workDocuments));

		var technitialDocuments = documentResourceService.findAllDocResourceByCalcIdAndIndex(id, TECHNITIAL_DOC);
		model.addAttribute("technitialDocuments", technitialDocuments);
		model.addAttribute("technitialDocumentsResource", getFilesTotalParameters(technitialDocuments));

		var separationDocuments = documentResourceService.findAllDocResourceByCalcIdAndIndex(id, SEPARATION_DOC);
		model.addAttribute("separationDocuments", separationDocuments);
		model.addAttribute("separationDocumentsResource", getFilesTotalParameters(separationDocuments));

		var otherDocuments = documentResourceService.findAllDocResourceByCalcIdAndIndex(id, OTHER_DOC);
		model.addAttribute("otherDocuments", otherDocuments);
		model.addAttribute("otherDocumentsResource", getFilesTotalParameters(otherDocuments));
	}

}