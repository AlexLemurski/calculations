package ru.project.calculations.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import ru.project.calculations.dto.document_result.DocumentResultDto;

import java.io.IOException;

public interface DocumentResultService {

    void saveDocumentResult(long id,
                            MultipartFile file);

    DocumentResultDto findDocumentResultById(long id);

    DocumentResultDto findDocResultByCalcId(long id);

    Resource downloadDocumentResult(long id,
                                    String key) throws IOException;

    void deleteDocumentResult(long id);

    void deleteDocumentResultCascade(long id);
    
}