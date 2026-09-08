package ru.project.calculations.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
import static ru.project.calculations.util.CalculationUtil.*;

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

	@GetMapping
	@PreAuthorize("hasAnyAuthority('ADMIN', 'CALCULATION_VIEW')")
	public String findAllCalculations(@AuthenticationPrincipal UserDetails userDetails,
									  Model model) {
		model.addAttribute("calculations", calculationService.findAllCalculations());
		model.addAttribute("customers", customerService.findAllCustomers());
		model.addAttribute("userDetails", userDetails);
		return "calculation/calculation-menu";
	}

	@GetMapping("/{id:\\d++}")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'CALCULATION_VIEW')")
	public String findCalculationById(@PathVariable long id,
									  @AuthenticationPrincipal UserDetails userDetails,
									  Model model) {
		var resultDocument = documentResultService.findDocResultByCalcId(id);
		model.addAttribute("calculations", calculationService.findAllCalculations());
		model.addAttribute("calculation", calculationService.findCalculationById(id));
		getAllResourceDocuments(id, documentResourceService, model);
		model.addAttribute("resultDocument", resultDocument);
		model.addAttribute("customers", customerService.findAllCustomers());
		model.addAttribute("partition", partitionService.findAllPartitionByCalcId(id));
		model.addAttribute("uncalculated", uncalculatedService.findAllUncalculatedByCalcId(id));
		model.addAttribute("userDetails", userDetails);
		return "calculation/calculation-view";
	}

	@GetMapping("/create")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'CALCULATION_CREATE')")
	public String createCalculationForm(@ModelAttribute("calculation") CalculationPayloadNew payload,
										@AuthenticationPrincipal UserDetails userDetails,
										Model model) {
		model.addAttribute("calculations", calculationService.findAllCalculations());
		model.addAttribute("customers", customerService.findAllCustomers());
		model.addAttribute("userDetails", userDetails);
		model.addAttribute("statusListCreate", getStatusListCalculationForCreate());
		return "calculation/calculation-create";
	}

	@PostMapping("/create")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'CALCULATION_CREATE')")
	public String createCalculation(@Validated @ModelAttribute("calculation") CalculationPayloadNew payload,
									BindingResult bindingResult,
									Model model,
									RedirectAttributes attributes) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("calculations", calculationService.findAllCalculations());
			model.addAttribute("customers", customerService.findAllCustomers());
			model.addAttribute("statusListCreate", getStatusListCalculationForCreate());
			return "calculation/calculation-create";
		} else {
			var calculation = calculationService.createCalculation(payload);
			attributes.addFlashAttribute("successMessage", "success.object.create");
			return "redirect:/calculations/%d".formatted(calculation.getId());
		}
	}

	@GetMapping("/update/{id:\\d+}")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'CALCULATION_UPDATE')")
	public String updateCalculationForm(@PathVariable long id,
										@AuthenticationPrincipal UserDetails userDetails,
										Model model) {
		var calculation = calculationService.findCalculationById(id);
		model.addAttribute("calculation", calculation);
		model.addAttribute("calculations", calculationService.findAllCalculations());
		model.addAttribute("customers", customerService.findAllCustomers());
		model.addAttribute("userDetails", userDetails);
		model.addAttribute("statusListUpdate", getStatusListCalculationForUpdate(calculation));
		return "calculation/calculation-update";
	}

	@PostMapping("/update")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'CALCULATION_UPDATE')")
	public String updateCalculation(@Validated @ModelAttribute("calculation") CalculationPayloadUpdate payload,
									BindingResult bindingResult,
									Model model,
									RedirectAttributes attributes) {
		if (bindingResult.hasErrors()) {
			var calculation = calculationService.findCalculationById(payload.id());
			model.addAttribute("calculations", calculationService.findAllCalculations());
			model.addAttribute("customers", customerService.findAllCustomers());
			model.addAttribute("statusListUpdate", getStatusListCalculationForUpdate(calculation));
			return "calculation/calculation-update";
		} else {
			var calculation = calculationService.updateCalculation(payload);
			attributes.addFlashAttribute("successMessage", "success.object.update");
			return "redirect:/calculations/update/%d".formatted(calculation.getId());
		}
	}

	@GetMapping("/doc_resource_update/{id:\\d+}")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'DOC_RESOURCE_REDACTOR')")
	public String updateDocumentResourceForm(@PathVariable long id,
											 @AuthenticationPrincipal UserDetails userDetails,
											 Model model) {
		model.addAttribute("calculations", calculationService.findAllCalculations());
		model.addAttribute("calculation", calculationService.findCalculationById(id));
		model.addAttribute("customers", customerService.findAllCustomers());
		model.addAttribute("partitionDocuments",
			documentResourceService.findAllDocResourceByCalcIdAndIndex(id, PARTITION_DOC));
		model.addAttribute("contentTypeList", ContentType.values());
		getAllResourceDocuments(id, documentResourceService, model);
		model.addAttribute("userDetails", userDetails);
		return "calculation/calculation-doc-resource-update";
	}

	@GetMapping("/doc_result_update/{id:\\d+}")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'DOC_RESULT_REDACTOR')")
	public String updateDocumentResultForm(@PathVariable long id,
										   @AuthenticationPrincipal UserDetails userDetails,
										   Model model) {
		model.addAttribute("calculations", calculationService.findAllCalculations());
		model.addAttribute("calculation", calculationService.findCalculationById(id));
		model.addAttribute("resultDocument", documentResultService.findDocResultByCalcId(id));
		model.addAttribute("customers", customerService.findAllCustomers());
		model.addAttribute("partition", partitionService.findAllPartitionByCalcId(id));
		model.addAttribute("uncalculated", uncalculatedService.findAllUncalculatedByCalcId(id));
		model.addAttribute("userDetails", userDetails);
		return "calculation/calculation-doc-result-update";
	}

	@GetMapping("/delete/{id:\\d++}")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'CALCULATION_DELETE')")
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