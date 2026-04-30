package ru.project.calculations.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import ru.project.calculations.dto.document_resource.DocumentResourceDto;
import ru.project.calculations.entity.Calculation;
import ru.project.calculations.enums.ContentType;
import ru.project.calculations.enums.DocumentIndex;
import ru.project.calculations.exception.DocumentResultValidationUploadException;
import ru.project.calculations.exception.DocumentsIOException;
import ru.project.calculations.repository.*;
import ru.project.calculations.service.DocumentResourceService;
import ru.project.calculations.service.DocumentResultService;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import static ru.project.calculations.util.CalculationUtil.updateCalculationResultData;
import static ru.project.calculations.util.ExcelFileReaderUtil.fileOutputStream;
import static ru.project.calculations.util.ExcelFileReaderUtil.validateExcelFile;
import static ru.project.calculations.util.PartitionUtil.createPartition;
import static ru.project.calculations.util.UncalculatedUtil.createUncalculated;

public class DocumentsUtil {

    private DocumentsUtil() {
    }

    private static void getCalculationResultParameters(Calculation calculation,
                                                      String filePath,
                                                      PartitionRepository partitionRepository,
                                                      UncalculatedRepository uncalculatedRepository) throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath);
             var workbook = WorkbookFactory.create(fis)) {
            DataFormatter formatter = new DataFormatter();
            Sheet sheet = workbook.getSheetAt(0);
            updateCalculationResultData(calculation, workbook, sheet);
            createPartition(sheet, formatter, workbook, partitionRepository, calculation.getId());
            createUncalculated(sheet, formatter, workbook, uncalculatedRepository, calculation.getId());
        }
    }

    public static String generateKey(String name) {
        return DigestUtils.md5Hex(name + LocalDateTime.now());
    }

    public static ResponseEntity<Resource> returnDocResourceContentType(long id,
                                                                        DocumentResourceService
                                                                                documentResourceService) {
        var documentResource = documentResourceService.findDocumentResourceById(id);
        Resource resource;
        try {
            resource = documentResourceService.downloadDocumentResource(id, documentResource.key());
        } catch (IOException e) {
            throw new DocumentsIOException("io.exception.message");
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(documentResource.docType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename*=UTF-8''" +
                                URLEncoder.encode(documentResource.docName(), StandardCharsets.UTF_8))
                .body(resource);
    }

    public static ResponseEntity<Resource> returnDocResultContentType(long id,
                                                                      DocumentResultService
                                                                              documentResultService) {
        var DocumentResult = documentResultService.findDocumentResultById(id);
        Resource resource;
        try {
            resource = documentResultService.downloadDocumentResult(id, DocumentResult.key());
        } catch (IOException e) {
            throw new DocumentsIOException("io.exception.message");
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(DocumentResult.docType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename*=UTF-8''" +
                                URLEncoder.encode(DocumentResult.docName(), StandardCharsets.UTF_8))
                .body(resource);
    }

    public static void saveAllDataDocumentResource(long id,
                                                   String folderName,
                                                   DocumentResourceRepository documentResourceRepository,
                                                   DocumentIndex documentIndex,
                                                   MultipartFile file) throws IOException {
        String key = generateKey(file.getName());
        fileOutputStream(folderName, file, key);
        String size = String.format("%.3f Мб", (double) file.getSize() / 1_000_000);
        documentResourceRepository.createDocumentResource(
                file.getOriginalFilename(),
                file.getContentType(),
                key,
                size,
                documentIndex,
                id);
    }

    public static void saveAllDataDocumentResult(long id,
                                                 DocumentResultRepository documentResultRepository,
                                                 CalculationRepository calculationRepository,
                                                 PartitionRepository partitionRepository,
                                                 UncalculatedRepository uncalculatedRepository,
                                                 MultipartFile file) throws IOException {
        var calculation = calculationRepository.findCalculationById(id).orElseThrow();
        if (validateExcelFile(file)) {
            String key = generateKey(file.getName());
            fileOutputStream(calculation.getResourceFolder(), file, key);
            String size = String.format("%.3f Мб", (double) file.getSize() / 1_000_000);
            documentResultRepository.createDocumentResult(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    key,
                    size,
                    id);
            getCalculationResultParameters(calculation,
                    Paths.get(calculation.getResourceFolder(), key).toString(),
                    partitionRepository,
                    uncalculatedRepository);
            calculationRepository.updateCalculationResult(
                    calculation.getId(),
                    calculation.getTotalSum(),
                    calculation.getTotalPositionCount(),
                    calculation.getCalculatedPositionCount(),
                    calculation.getTotalPercent());
        } else {
            documentResultRepository.deleteById(id);
            throw new DocumentResultValidationUploadException(id, "document.result.upload.validation.exception");
        }
    }

    public static String getFilesTotalParameters(List<DocumentResourceDto> documentResources) {
        List<String> volumeString = documentResources.stream()
                .map(DocumentResourceDto::size)
                .toList();
        double totalSize = 0.0;
        for (String sizeStr : volumeString) {
            double size = Double.parseDouble(
                    sizeStr.replace(" Мб", "").replace(",", "."));
            totalSize += size;
        }
        return String.format("Содержание. Файлов: %s; Объем: %.3f Мб", documentResources.size(), totalSize);
    }

    public static String[] getContentTypeArray(List<ContentType> values) {
        return values.stream()
                .map(ContentType::toString)
                .toArray(String[]::new);
    }

}