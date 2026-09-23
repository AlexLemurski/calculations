package ru.project.calculations.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.project.calculations.dto.document_resource.DocumentResourceDto;
import ru.project.calculations.enums.ContentType;
import ru.project.calculations.service.DocumentResourceService;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import static ru.project.calculations.enums.DocumentIndex.*;
import static ru.project.calculations.util.DocumentsUtil.getContentTypeArray;
import static ru.project.calculations.util.DocumentsUtil.returnDocResourceContentType;

@Controller
@RequestMapping("/document_resource")
@RequiredArgsConstructor
public class DocumentResourceController {

	private final DocumentResourceService documentResourceService;

	@PostMapping("/update/{id:\\d+}")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'DOC_RESOURCE_REDACTOR')")
	public String updateDocumentResource(@PathVariable long id,
										 @RequestParam MultiValueMap<String, String> params,
										 Principal principal) {
		List<DocumentResourceDto> allDocs =
			documentResourceService.findAllDocResourceByCalcIdAndIndex(id, PARTITION_DOC);
		for (var doc : allDocs) {
			documentResourceService.updateDocumentResource(doc.docId(), new String[0]);
		}
		for (String key : params.keySet()) {
			if (key.startsWith("content_")) {
				List<String> selectedValues = params.get(key);
				if (selectedValues != null) {
					long documentId = Long.parseLong(key.substring(8, key.length() - 2));
					List<ContentType> selectedTypes = selectedValues.stream()
						.map(ContentType::valueOf)
						.collect(Collectors.toList());
					documentResourceService.updateDocumentResource(documentId, getContentTypeArray(selectedTypes));
				}
			}
		}
		return "redirect:/calculations/doc_resource_update/%d".formatted(id);
	}

	@PostMapping("/upload_main_documents/{id:\\d+}")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'DOC_RESOURCE_REDACTOR')")
	public String uploadMainDocuments(@PathVariable long id,
									  @RequestParam("mainDocuments") MultipartFile[] files,
									  Principal principal) {
		for (MultipartFile file : files) {
			documentResourceService.saveDocumentResource(id, MAIN_DOC, principal, file);
		}
		return "redirect:/calculations/doc_resource_update/%d".formatted(id);
	}

	@PostMapping("/upload_partition_documents/{id:\\d+}")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'DOC_RESOURCE_REDACTOR')")
	public String uploadPartitionDocuments(@PathVariable long id,
										   @RequestParam("partitionDocuments") MultipartFile[] files,
										   Principal principal) {
		for (MultipartFile file : files) {
			documentResourceService.saveDocumentResource(id, PARTITION_DOC, principal, file);
		}
		return "redirect:/calculations/doc_resource_update/%d".formatted(id);
	}

	@PostMapping("/upload_specification_documents/{id:\\d+}")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'DOC_RESOURCE_REDACTOR')")
	public String uploadSpecificationDocuments(@PathVariable long id,
											   @RequestParam("specificationDocuments") MultipartFile[] files,
											   Principal principal) {
		for (MultipartFile file : files) {
			documentResourceService.saveDocumentResource(id, SPECIFICATION_DOC, principal, file);
		}
		return "redirect:/calculations/doc_resource_update/%d".formatted(id);
	}

	@PostMapping("/upload_material_documents/{id:\\d+}")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'DOC_RESOURCE_REDACTOR')")
	public String uploadMaterialDocuments(@PathVariable long id,
										  @RequestParam("materialDocuments") MultipartFile[] files,
										  Principal principal) {
		for (MultipartFile file : files) {
			documentResourceService.saveDocumentResource(id, MATERIAL_LIST_DOC, principal, file);
		}
		return "redirect:/calculations/doc_resource_update/%d".formatted(id);
	}

	@PostMapping("/upload_work_documents/{id:\\d+}")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'DOC_RESOURCE_REDACTOR')")
	public String uploadWorkDocuments(@PathVariable long id,
									  @RequestParam("workDocuments") MultipartFile[] files,
									  Principal principal) {
		for (MultipartFile file : files) {
			documentResourceService.saveDocumentResource(id, WORK_LIST_DOC, principal, file);
		}
		return "redirect:/calculations/doc_resource_update/%d".formatted(id);
	}

	@PostMapping("/upload_technitial_documents/{id:\\d+}")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'DOC_RESOURCE_REDACTOR')")
	public String uploadTechnitialDocuments(@PathVariable long id,
											@RequestParam("technitialDocuments") MultipartFile[] files,
											Principal principal) {
		for (MultipartFile file : files) {
			documentResourceService.saveDocumentResource(id, TECHNITIAL_DOC, principal, file);
		}
		return "redirect:/calculations/doc_resource_update/%d".formatted(id);
	}

	@PostMapping("/upload_separation_documents/{id:\\d+}")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'DOC_RESOURCE_REDACTOR')")
	public String uploadSeparationDocuments(@PathVariable long id,
											@RequestParam("separationDocuments") MultipartFile[] files,
											Principal principal) {
		for (MultipartFile file : files) {
			documentResourceService.saveDocumentResource(id, SEPARATION_DOC, principal, file);
		}
		return "redirect:/calculations/doc_resource_update/%d".formatted(id);
	}

	@PostMapping("/upload_other_documents/{id:\\d+}")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'DOC_RESOURCE_REDACTOR')")
	public String uploadOtherDocuments(@PathVariable long id,
									   @RequestParam("otherDocuments") MultipartFile[] files,
									   Principal principal) {
		for (MultipartFile file : files) {
			documentResourceService.saveDocumentResource(id, OTHER_DOC, principal, file);
		}
		return "redirect:/calculations/doc_resource_update/%d".formatted(id);
	}

	@GetMapping("/download/{id:\\d+}")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'DOC_RESOURCE_REDACTOR')")
	public ResponseEntity<Resource> downloadFile(@PathVariable long id) {
		return returnDocResourceContentType(id, documentResourceService);
	}

	@GetMapping("/delete/{id:\\d+}")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'DOC_RESOURCE_REDACTOR')")
	public String deleteDocument(@PathVariable long id) {
		var calcId = documentResourceService.findDocumentResourceById(id);
		documentResourceService.deleteDocumentResource(id);
		return "redirect:/calculations/doc_resource_update/%d".formatted(calcId.calculationId());
	}

	@GetMapping("/delete_all_main_doc/{id:\\d+}")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'DOC_RESOURCE_REDACTOR')")
	public String deleteAllMainDocuments(@PathVariable long id) {
		documentResourceService.deleteAllDocumentResource(id, MAIN_DOC);
		return "redirect:/calculations/doc_resource_update/%d".formatted(id);
	}

	@GetMapping("/delete_all_part_doc/{id:\\d+}")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'DOC_RESOURCE_REDACTOR')")
	public String deleteAllPartitionDocuments(@PathVariable long id) {
		documentResourceService.deleteAllDocumentResource(id, PARTITION_DOC);
		return "redirect:/calculations/doc_resource_update/%d".formatted(id);
	}

	@GetMapping("/delete_all_spec_doc/{id:\\d+}")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'DOC_RESOURCE_REDACTOR')")
	public String deleteAllSpecDocuments(@PathVariable long id) {
		documentResourceService.deleteAllDocumentResource(id, SPECIFICATION_DOC);
		return "redirect:/calculations/doc_resource_update/%d".formatted(id);
	}

	@GetMapping("/delete_all_material_doc/{id:\\d+}")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'DOC_RESOURCE_REDACTOR')")
	public String deleteAllMaterialDocuments(@PathVariable long id) {
		documentResourceService.deleteAllDocumentResource(id, MATERIAL_LIST_DOC);
		return "redirect:/calculations/doc_resource_update/%d".formatted(id);
	}

	@GetMapping("/delete_all_work_doc/{id:\\d+}")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'DOC_RESOURCE_REDACTOR')")
	public String deleteAllWorkDocuments(@PathVariable long id) {
		documentResourceService.deleteAllDocumentResource(id, WORK_LIST_DOC);
		return "redirect:/calculations/doc_resource_update/%d".formatted(id);
	}

	@GetMapping("/delete_all_technitial_doc/{id:\\d+}")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'DOC_RESOURCE_REDACTOR')")
	public String deleteAllTechnitialDocuments(@PathVariable long id) {
		documentResourceService.deleteAllDocumentResource(id, TECHNITIAL_DOC);
		return "redirect:/calculations/doc_resource_update/%d".formatted(id);
	}

	@GetMapping("/delete_all_separation_doc/{id:\\d+}")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'DOC_RESOURCE_REDACTOR')")
	public String deleteAllSeparationDocuments(@PathVariable long id) {
		documentResourceService.deleteAllDocumentResource(id, SEPARATION_DOC);
		return "redirect:/calculations/doc_resource_update/%d".formatted(id);
	}

	@GetMapping("/delete_all_other_doc/{id:\\d+}")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'DOC_RESOURCE_REDACTOR')")
	public String deleteAllOtherDocuments(@PathVariable long id) {
		documentResourceService.deleteAllDocumentResource(id, OTHER_DOC);
		return "redirect:/calculations/doc_resource_update/%d".formatted(id);
	}

}