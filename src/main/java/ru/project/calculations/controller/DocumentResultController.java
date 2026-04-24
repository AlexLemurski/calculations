package ru.project.calculations.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.project.calculations.repository.PartitionRepository;
import ru.project.calculations.service.DocumentResultService;
import ru.project.calculations.service.UncalculatedService;

import static ru.project.calculations.util.DocumentsUtil.*;

@Controller
@RequestMapping("/document_result")
@RequiredArgsConstructor
public class DocumentResultController {

    private final DocumentResultService documentResultService;
    private final PartitionRepository partitionRepository;
    private final UncalculatedService uncalculatedService;

    @PostMapping("/upload_result_document/{id:\\d+}")
    public String uploadMultipleFiles(@PathVariable long id,
                                      @RequestParam("resultDocument") MultipartFile file) {
        documentResultService.saveDocumentResult(id, file);
        return "redirect:/calculations/doc_result_update/%d".formatted(id);
    }

    @GetMapping("/download/{id:\\d+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable long id) {
        return returnDocResultContentType(id, documentResultService);
    }

    @GetMapping("/delete/{id:\\d+}")
    public String deleteDocument(@PathVariable long id) {
        var calcId = documentResultService.findDocumentResultById(id);
        documentResultService.deleteDocumentResult(id);
        partitionRepository.deleteAllPartitionByCalcId(calcId.calculationId());
        uncalculatedService.deleteAllUncalculatedById(calcId.calculationId());
        return "redirect:/calculations/doc_result_update/%d".formatted(calcId.calculationId());
    }

}