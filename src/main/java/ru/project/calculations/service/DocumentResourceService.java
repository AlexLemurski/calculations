package ru.project.calculations.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import ru.project.calculations.dto.document_resource.DocumentResourceDto;
import ru.project.calculations.enums.DocumentIndex;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

public interface DocumentResourceService {

	void saveDocumentResource(long id,
							  DocumentIndex documentIndex,
							  Principal principal,
							  MultipartFile file);

	DocumentResourceDto findDocumentResourceById(long id);

	List<DocumentResourceDto> findAllDocResourceByCalcIdAndIndex(long id,
																 DocumentIndex documentIndex,
																 Principal principal);

	Resource downloadDocumentResource(long id, String key) throws IOException;

	void updateDocumentResource(long id, String[] contentTypes);

	void deleteDocumentResource(long id);

	void deleteAllDocumentResource(long id);

	void deleteAllDocumentResource(long id,
								   DocumentIndex documentIndex);

}