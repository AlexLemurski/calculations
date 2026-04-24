package ru.project.calculations.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.project.calculations.dto.document_result.DocumentResultDto;
import ru.project.calculations.exception.DocumentsIOException;
import ru.project.calculations.repository.CalculationRepository;
import ru.project.calculations.repository.DocumentResultRepository;
import ru.project.calculations.repository.PartitionRepository;
import ru.project.calculations.repository.UncalculatedRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;

import static ru.project.calculations.util.DocumentsUtil.*;

@Service
@RequiredArgsConstructor
public class DocumentResultServiceImpl implements DocumentResultService {

    private final DocumentResultRepository documentResultRepository;
    private final CalculationRepository calculationRepository;
    private final  PartitionRepository partitionRepository;
    private final UncalculatedRepository uncalculatedRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveDocumentResult(long id, MultipartFile file) {
        try {
            saveAllDataDocumentResult(
                    id,
                    documentResultRepository,
                    calculationRepository,
                    partitionRepository,
                    uncalculatedRepository,
                    file);
        } catch (IOException | NumberFormatException e) {
            throw new DocumentsIOException("io.exception.message");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public DocumentResultDto findDocumentResultById(long id) {
        var documentResult = documentResultRepository.findDocumentResultById(id).orElseThrow();
        return DocumentResultDto.builder()
                .docId(documentResult.getId())
                .docName(documentResult.getDocName())
                .docType(documentResult.getDocType())
                .key(documentResult.getKey())
                .size(documentResult.getSize())
                .calculationId(documentResult.getCalculationId())
                .build();
    }

    @Override
    public DocumentResultDto findDocResultByCalcId(long id) {
        var documentResult = documentResultRepository.findDocResultByCalcId(id).orElse(null);
        if (documentResult != null) {
            return DocumentResultDto.builder()
                    .docId(documentResult.getId())
                    .docName(documentResult.getDocName())
                    .docType(documentResult.getDocType())
                    .key(documentResult.getKey())
                    .size(documentResult.getSize())
                    .calculationId(documentResult.getCalculationId())
                    .build();
        } else {
            return DocumentResultDto.builder()
                    .docId(null)
                    .build();
        }
    }

    @Override
    @Transactional
    public Resource downloadDocumentResult(long id, String key) throws IOException {
        var documentResult = documentResultRepository.findDocumentResultById(id).orElseThrow(
                () -> new NoSuchElementException("element.not.found"));
        Path path = Paths.get(new File(calculationRepository
                .findCalculationById(documentResult
                        .getCalculationId()).orElseThrow().getResourceFolder()) + "/" + key);
        Resource resource = new UrlResource(path.toUri());
        if (resource.exists() || resource.isReadable()) {
            return resource;
        } else {
            throw new DocumentsIOException("io.exception.message");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDocumentResult(long id) {
        var documentResult = documentResultRepository.findDocumentResultById(id).orElse(null);
        if (documentResult != null) {
            Path path = Paths.get(new File(calculationRepository
                    .findCalculationById(documentResult
                            .getCalculationId()).orElseThrow().getResourceFolder()) + "/" + documentResult.getKey());
            try {
                Files.delete(path);
                calculationRepository.updateCalculationResult(
                        documentResult.getCalculationId(),
                        null,
                        0,
                        0,
                        0.0);
                documentResultRepository.deleteById(documentResult.getId());
            } catch (IOException e) {
                throw new DocumentsIOException("io.exception.message");
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDocumentResultCascade(long id) {
        var documentResult = documentResultRepository.findAllDocResultByCalcId(id).orElse(null);
        if (documentResult != null) {
            Path path = Paths.get(new File(calculationRepository
                    .findCalculationById(documentResult
                            .getCalculationId()).orElseThrow().getResourceFolder()) + "/" + documentResult.getKey());
            try {
                Files.delete(path);
                calculationRepository.updateCalculationResult(
                        documentResult.getCalculationId(),
                        null,
                        0,
                        0,
                        0.0);
                documentResultRepository.deleteAllDocumentResult(id);
            } catch (IOException e) {
                throw new DocumentsIOException("io.exception.message");
            }
        }
    }

}