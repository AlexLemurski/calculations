package ru.project.calculations.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.project.calculations.dto.document_resource.DocumentResourceDto;
import ru.project.calculations.entity.DocumentResource;
import ru.project.calculations.enums.DocumentIndex;
import ru.project.calculations.exception.DocumentsIOException;
import ru.project.calculations.repository.CalculationRepository;
import ru.project.calculations.repository.DocumentResourceRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

import static ru.project.calculations.util.DocumentsUtil.saveAllDataDocumentResource;

@Service
@RequiredArgsConstructor
public class DocumentResourceServiceImpl implements DocumentResourceService {

    private final DocumentResourceRepository documentResourceRepository;
    private final CalculationRepository calculationRepository;

    private void getFilePath(long id,
                             DocumentResource documentResources) {
        Path path = Paths.get(new File(calculationRepository
                .findCalculationById(id).orElseThrow().getResourceFolder())
                + "/" + documentResources.getKey());
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new DocumentsIOException("io.exception.message");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveDocumentResource(long id,
                                     DocumentIndex documentIndex,
                                     MultipartFile file) {
        try {
            saveAllDataDocumentResource(
                    id,
                    calculationRepository.findCalculationById(id).orElseThrow().getResourceFolder(),
                    documentResourceRepository,
                    documentIndex,
                    file);
        } catch (IOException e) {
            throw new DocumentsIOException("io.exception.message");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public DocumentResourceDto findDocumentResourceById(long id) {
        var documentResource = documentResourceRepository.findDocumentResourceById(id).orElseThrow(
                () -> new NoSuchElementException("element.not.found"));
        return DocumentResourceDto.builder()
                .docName(documentResource.getDocName())
                .docName(documentResource.getDocName())
                .docType(documentResource.getDocType())
                .key(documentResource.getKey())
                .documentIndex(documentResource.getDocumentIndex())
                .size(documentResource.getSize())
                .calculationId(documentResource.getCalculationId())
                .contentTypes(documentResource.getContentTypes())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentResourceDto> findAllDocResourceByCalcIdAndIndex(long id,
                                                                        DocumentIndex documentIndex) {
        return documentResourceRepository.findAllDocResourceByCalcIdAndIndex(id, documentIndex).stream()
                .map(documentResource -> DocumentResourceDto.builder()
                        .docId(documentResource.getId())
                        .docName(documentResource.getDocName())
                        .docType(documentResource.getDocType())
                        .key(documentResource.getKey())
                        .documentIndex(documentResource.getDocumentIndex())
                        .size(documentResource.getSize())
                        .calculationId(documentResource.getCalculationId())
                        .contentTypes(documentResource.getContentTypes())
                        .build()
                ).toList().stream()
                .sorted(Comparator.comparingLong(DocumentResourceDto::docId))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Resource downloadDocumentResource(long id, String key) throws IOException {
        var documentResource = documentResourceRepository.findDocumentResourceById(id).orElseThrow(
                () -> new NoSuchElementException("element.not.found"));
        Path path = Paths.get(new File(calculationRepository
                .findCalculationById(documentResource.getCalculationId()).orElseThrow().getResourceFolder()) + "/" + key);
        Resource resource = new UrlResource(path.toUri());
        if (resource.exists() || resource.isReadable()) {
            return resource;
        } else {
            throw new DocumentsIOException("io.exception.message");
        }
    }

    @Override
    @Transactional
    public void updateDocumentResource(long id, String[] contentTypes) {
        documentResourceRepository.updateDocumentResource(
                id,
                contentTypes);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDocumentResource(long id) {
        var documentResource = documentResourceRepository.findDocumentResourceById(id).orElseThrow(
                () -> new NoSuchElementException("element.not.found"));
        Path path = Paths.get(new File(calculationRepository
                .findCalculationById(documentResource.getCalculationId()).orElseThrow().getResourceFolder())
                + "/" + documentResource.getKey());
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new DocumentsIOException("io.exception.message");
        }
        documentResourceRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteAllDocumentResource(long id) {
        var documentResource = documentResourceRepository.findAllDocResourceByCalcId(id);
        for (var document : documentResource) {
            getFilePath(id, document);
        }
        documentResourceRepository.deleteAllDocumentResource(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAllDocumentResource(long id, DocumentIndex documentIndex) {
        var documentResource = documentResourceRepository.findAllDocResourceByCalcIdAndIndex(id, documentIndex);
        for (var document : documentResource) {
            getFilePath(id, document);
        }
        documentResourceRepository.deleteAllDocumentResource(id, documentIndex);
    }

}
