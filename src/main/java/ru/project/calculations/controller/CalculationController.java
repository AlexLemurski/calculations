package ru.project.calculations.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.project.calculations.dto.calculation.CalculationPayloadNew;
import ru.project.calculations.dto.calculation.CalculationPayloadUpdate;
import ru.project.calculations.enums.ContentType;
import ru.project.calculations.service.*;

import static ru.project.calculations.enums.DocumentIndex.*;
import static ru.project.calculations.util.CalculationUtil.deleteFolders;
import static ru.project.calculations.util.DocumentsUtil.*;

@Controller
@RequestMapping("/calculations")
@RequiredArgsConstructor
public class CalculationController {

    private final CalculationService calculationService;
    private final CustomerService customerService;
    private final DocumentResourceService documentResourceService;
    private final DocumentResultService documentResultService;
    private final PartitionService partitionService;
    private final UncalculatedService uncalculatedService;

    private void getAllResourceDocuments(@PathVariable long id,
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

    @GetMapping
    public String findAllCalculations(Model model) {
        model.addAttribute("calculations", calculationService.findAllCalculations());
        model.addAttribute("customers", customerService.findAllCustomers());
        return "calculation/calculation-menu";
    }

    @GetMapping("/{id:\\d++}")
    public String findCalculationById(@PathVariable long id,
                                      Model model) {
        var resultDocument = documentResultService.findDocResultByCalcId(id);
        model.addAttribute("calculations", calculationService.findAllCalculations());
        model.addAttribute("calculation", calculationService.findCalculationById(id));
        getAllResourceDocuments(id, model);
        model.addAttribute("resultDocument", resultDocument);
        model.addAttribute("customers", customerService.findAllCustomers());
        model.addAttribute("partition", partitionService.findAllPartitionByCalcId(id));
        model.addAttribute("uncalculated", uncalculatedService.findAllUncalculatedByCalcId(id));
        return "calculation/calculation-view";
    }

    @GetMapping("/create")
    public String createCalculationForm(@ModelAttribute("calculation") CalculationPayloadNew payload,
                                        Model model) {
        model.addAttribute("calculations", calculationService.findAllCalculations());
        model.addAttribute("customers", customerService.findAllCustomers());
        return "calculation/calculation-create";
    }

    @PostMapping("/create")
    public String createCalculation(@Validated @ModelAttribute("calculation") CalculationPayloadNew payload,
                                    BindingResult bindingResult,
                                    Model model,
                                    RedirectAttributes attributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("calculations", calculationService.findAllCalculations());
            model.addAttribute("customers", customerService.findAllCustomers());
            return "calculation/calculation-create";
        } else {
            var calculation = calculationService.createCalculation(payload);
            attributes.addFlashAttribute("successMessage", "success.object.create");
            return "redirect:/calculations/%d".formatted(calculation.getId());
        }
    }

    @GetMapping("/update/{id:\\d+}")
    public String updateCalculationForm(@PathVariable long id,
                                        Model model) {
        model.addAttribute("calculation", calculationService.findCalculationById(id));
        model.addAttribute("calculations", calculationService.findAllCalculations());
        model.addAttribute("customers", customerService.findAllCustomers());
        return "calculation/calculation-update";
    }

    @PostMapping("/update")
    public String updateCalculation(@Validated @ModelAttribute("calculation") CalculationPayloadUpdate payload,
                                    BindingResult bindingResult,
                                    Model model,
                                    RedirectAttributes attributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("calculations", calculationService.findAllCalculations());
            model.addAttribute("customers", customerService.findAllCustomers());
            return "calculation/calculation-update";
        } else {
            var calculation = calculationService.updateCalculation(payload);
            attributes.addFlashAttribute("successMessage", "success.object.update");
            return "redirect:/calculations/update/%d".formatted(calculation.getId());
        }
    }

    @GetMapping("/doc_resource_update/{id:\\d+}")
    public String updateDocumentResourceForm(@PathVariable long id,
                                             Model model) {
        model.addAttribute("calculations", calculationService.findAllCalculations());
        model.addAttribute("calculation", calculationService.findCalculationById(id));
        model.addAttribute("customers", customerService.findAllCustomers());
        model.addAttribute("partitionDocuments",
                documentResourceService.findAllDocResourceByCalcIdAndIndex(id, PARTITION_DOC));
        model.addAttribute("contentTypeList", ContentType.values());
        getAllResourceDocuments(id, model);
        return "calculation/calculation-doc-resource-update";
    }

    @GetMapping("/doc_result_update/{id:\\d+}")
    public String updateDocumentResultForm(@PathVariable long id,
                                           Model model) {
        model.addAttribute("calculations", calculationService.findAllCalculations());
        model.addAttribute("calculation", calculationService.findCalculationById(id));
        model.addAttribute("resultDocument", documentResultService.findDocResultByCalcId(id));
        model.addAttribute("customers", customerService.findAllCustomers());
        model.addAttribute("partition", partitionService.findAllPartitionByCalcId(id));
        model.addAttribute("uncalculated", uncalculatedService.findAllUncalculatedByCalcId(id));
        return "calculation/calculation-doc-result-update";
    }

    @GetMapping("/delete/{id:\\d++}")
    public String deleteCalculation(@PathVariable long id,
                                    RedirectAttributes attributes) {
        var calculation = calculationService.findCalculationById(id);
        documentResultService.deleteDocumentResultCascade(id);
        uncalculatedService.deleteAllUncalculatedById(id);
        documentResourceService.deleteAllDocumentResource(id);
        calculationService.deleteeCalculation(id);
        deleteFolders(calculation.resourceFolder());
        attributes.addFlashAttribute("successMessage", "success.object.deleted");
        return "redirect:/calculations";
    }

}