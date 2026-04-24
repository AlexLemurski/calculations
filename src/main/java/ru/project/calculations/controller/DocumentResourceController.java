package ru.project.calculations.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.project.calculations.dto.document_resource.DocumentResourceDto;
import ru.project.calculations.enums.ContentType;
import ru.project.calculations.service.DocumentResourceService;

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
    public String updateDocumentResource(@PathVariable long id,
                                         @RequestParam MultiValueMap<String, String> params) {
        List<DocumentResourceDto> allDocs = documentResourceService.findAllDocResourceByCalcIdAndIndex(id, PARTITION_DOC);
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
    public String uploadMainDocuments(@PathVariable long id,
                                      @RequestParam("mainDocuments") MultipartFile[] files) {
        for (MultipartFile file : files) {
            documentResourceService.saveDocumentResource(id, MAIN_DOC, file);
        }
        return "redirect:/calculations/doc_resource_update/%d".formatted(id);
    }

    @PostMapping("/upload_partition_documents/{id:\\d+}")
    public String uploadPartitionDocuments(@PathVariable long id,
                                           @RequestParam("partitionDocuments") MultipartFile[] files) {
        for (MultipartFile file : files) {
            documentResourceService.saveDocumentResource(id, PARTITION_DOC, file);
        }
        return "redirect:/calculations/doc_resource_update/%d".formatted(id);
    }

    @PostMapping("/upload_specification_documents/{id:\\d+}")
    public String uploadSpecificationDocuments(@PathVariable long id,
                                               @RequestParam("specificationDocuments") MultipartFile[] files) {
        for (MultipartFile file : files) {
            documentResourceService.saveDocumentResource(id, SPECIFICATION_DOC, file);
        }
        return "redirect:/calculations/doc_resource_update/%d".formatted(id);
    }

    @PostMapping("/upload_material_documents/{id:\\d+}")
    public String uploadMaterialDocuments(@PathVariable long id,
                                          @RequestParam("materialDocuments") MultipartFile[] files) {
        for (MultipartFile file : files) {
            documentResourceService.saveDocumentResource(id, MATERIAL_LIST_DOC, file);
        }
        return "redirect:/calculations/doc_resource_update/%d".formatted(id);
    }

    @PostMapping("/upload_work_documents/{id:\\d+}")
    public String uploadWorkDocuments(@PathVariable long id,
                                      @RequestParam("workDocuments") MultipartFile[] files) {
        for (MultipartFile file : files) {
            documentResourceService.saveDocumentResource(id, WORK_LIST_DOC, file);
        }
        return "redirect:/calculations/doc_resource_update/%d".formatted(id);
    }

    @PostMapping("/upload_technitial_documents/{id:\\d+}")
    public String uploadTechnitialDocuments(@PathVariable long id,
                                            @RequestParam("technitialDocuments") MultipartFile[] files) {
        for (MultipartFile file : files) {
            documentResourceService.saveDocumentResource(id, TECHNITIAL_DOC, file);
        }
        return "redirect:/calculations/doc_resource_update/%d".formatted(id);
    }

    @PostMapping("/upload_separation_documents/{id:\\d+}")
    public String uploadSeparationDocuments(@PathVariable long id,
                                            @RequestParam("separationDocuments") MultipartFile[] files) {
        for (MultipartFile file : files) {
            documentResourceService.saveDocumentResource(id, SEPARATION_DOC, file);
        }
        return "redirect:/calculations/doc_resource_update/%d".formatted(id);
    }

    @PostMapping("/upload_other_documents/{id:\\d+}")
    public String uploadOtherDocuments(@PathVariable long id,
                                       @RequestParam("otherDocuments") MultipartFile[] files) {
        for (MultipartFile file : files) {
            documentResourceService.saveDocumentResource(id, OTHER_DOC, file);
        }
        return "redirect:/calculations/doc_resource_update/%d".formatted(id);
    }

    @GetMapping("/download/{id:\\d+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable long id) {
        return returnDocResourceContentType(id, documentResourceService);
    }

    @GetMapping("/delete/{id:\\d+}")
    public String deleteDocument(@PathVariable long id) {
        var calcId = documentResourceService.findDocumentResourceById(id);
        documentResourceService.deleteDocumentResource(id);
        return "redirect:/calculations/doc_resource_update/%d".formatted(calcId.calculationId());
    }

    @GetMapping("/delete_all_main_doc/{id:\\d+}")
    public String deleteAllMainDocuments(@PathVariable long id) {
        documentResourceService.deleteAllDocumentResource(id, MAIN_DOC);
        return "redirect:/calculations/doc_resource_update/%d".formatted(id);
    }

    @GetMapping("/delete_all_part_doc/{id:\\d+}")
    public String deleteAllPartitionDocuments(@PathVariable long id) {
        documentResourceService.deleteAllDocumentResource(id, PARTITION_DOC);
        return "redirect:/calculations/doc_resource_update/%d".formatted(id);
    }

    @GetMapping("/delete_all_spec_doc/{id:\\d+}")
    public String deleteAllSpecDocuments(@PathVariable long id) {
        documentResourceService.deleteAllDocumentResource(id, SPECIFICATION_DOC);
        return "redirect:/calculations/doc_resource_update/%d".formatted(id);
    }

    @GetMapping("/delete_all_material_doc/{id:\\d+}")
    public String deleteAllMaterialDocuments(@PathVariable long id) {
        documentResourceService.deleteAllDocumentResource(id, MATERIAL_LIST_DOC);
        return "redirect:/calculations/doc_resource_update/%d".formatted(id);
    }

    @GetMapping("/delete_all_work_doc/{id:\\d+}")
    public String deleteAllWorkDocuments(@PathVariable long id) {
        documentResourceService.deleteAllDocumentResource(id, WORK_LIST_DOC);
        return "redirect:/calculations/doc_resource_update/%d".formatted(id);
    }

    @GetMapping("/delete_all_technitial_doc/{id:\\d+}")
    public String deleteAllTechnitialDocuments(@PathVariable long id) {
        documentResourceService.deleteAllDocumentResource(id, TECHNITIAL_DOC);
        return "redirect:/calculations/doc_resource_update/%d".formatted(id);
    }

    @GetMapping("/delete_all_separation_doc/{id:\\d+}")
    public String deleteAllSeparationDocuments(@PathVariable long id) {
        documentResourceService.deleteAllDocumentResource(id, SEPARATION_DOC);
        return "redirect:/calculations/doc_resource_update/%d".formatted(id);
    }

    @GetMapping("/delete_all_other_doc/{id:\\d+}")
    public String deleteAllOtherDocuments(@PathVariable long id) {
        documentResourceService.deleteAllDocumentResource(id, OTHER_DOC);
        return "redirect:/calculations/doc_resource_update/%d".formatted(id);
    }

}