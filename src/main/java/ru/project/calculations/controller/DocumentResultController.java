package ru.project.calculations.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.project.calculations.repository.PartitionRepository;
import ru.project.calculations.service.DocumentResultService;
import ru.project.calculations.service.UncalculatedService;

import java.security.Principal;

import static ru.project.calculations.util.DocumentsUtil.*;

@Controller
@RequestMapping("/document_result")
@RequiredArgsConstructor
public class DocumentResultController {

	private final DocumentResultService documentResultService;
	private final PartitionRepository partitionRepository;
	private final UncalculatedService uncalculatedService;

	@PostMapping("/upload_result_document/{id:\\d+}")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'DOC_RESULT_REDACTOR')")
	public String uploadMultipleFiles(@PathVariable long id,
									  @RequestParam("resultDocument") MultipartFile file,
									  Principal principal) {
		documentResultService.saveDocumentResult(id, file, principal);
		return "redirect:/calculations/doc_result_update/%d".formatted(id);
	}

	@GetMapping("/download/{id:\\d+}")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'DOC_RESULT_REDACTOR')")
	public ResponseEntity<Resource> downloadFile(@PathVariable long id) {
		return returnDocResultContentType(id, documentResultService);
	}

	@GetMapping("/delete/{id:\\d+}")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'DOC_RESULT_REDACTOR')")
	public String deleteDocument(@PathVariable long id) {
		var calcId = documentResultService.findDocumentResultById(id);
		documentResultService.deleteDocumentResult(id);
		partitionRepository.deleteAllPartitionByCalcId(calcId.calculationId());
		uncalculatedService.deleteAllUncalculatedById(calcId.calculationId());
		return "redirect:/calculations/doc_result_update/%d".formatted(calcId.calculationId());
	}

}