package ru.project.calculations.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.project.calculations.dto.customer.CustomerPayloadNew;
import ru.project.calculations.dto.customer.CustomerPayloadUpdate;
import ru.project.calculations.service.CalculationService;
import ru.project.calculations.service.CustomerService;

import java.security.Principal;

import static ru.project.calculations.util.CustomerUtil.getStatusListCustomerForCreate;
import static ru.project.calculations.util.CustomerUtil.getStatusListCustomerForUpdate;

@Controller
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

	private final CustomerService customerService;
	private final CalculationService calculationService;

	@GetMapping
	@PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER_VIEW')")
	public String findAllCustomers(Principal userDetails,
								   Model model) {
		model.addAttribute("customers", customerService.findAllCustomers());
		model.addAttribute("userDetails", userDetails);
		return "customer/customer-menu";
	}

	@GetMapping("/{id:\\d+}")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER_VIEW')")
	public String findAllCustomerById(@PathVariable long id,
									  Principal userDetails,
									  Model model) {
		model.addAttribute("customer", customerService.findCustomerById(id));
		model.addAttribute("customers", customerService.findAllCustomers());
		model.addAttribute("calculationsByCastId", calculationService.findAllCalculationsByCastId(id));
		model.addAttribute("userDetails", userDetails);
		return "customer/customer-view";
	}

	@GetMapping("/create")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER_CREATE')")
	public String createCustomerForm(@ModelAttribute("customer") CustomerPayloadNew payload,
									 Principal userDetails,
									 Model model) {
		model.addAttribute("customers", customerService.findAllCustomers());
		model.addAttribute("userDetails", userDetails);
		model.addAttribute("statusListCreate", getStatusListCustomerForCreate());
		return "customer/customer-create";
	}

	@PostMapping("/create")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER_CREATE')")
	public String createCustomer(@Validated @ModelAttribute("customer") CustomerPayloadNew payload,
								 BindingResult bindingResult,
								 Principal userDetails,
								 Model model,
								 RedirectAttributes attributes) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("customers", customerService.findAllCustomers());
			model.addAttribute("statusListCreate", getStatusListCustomerForCreate());
			model.addAttribute("userDetails", userDetails);
			return "customer/customer-create";
		} else {
			var customer = customerService.createCustomer(payload);
			attributes.addFlashAttribute("successMessage", "success.object.create");
			return "redirect:/customers/%d".formatted(customer.getId());
		}
	}

	@GetMapping("/update/{id:\\d*}")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER_UPDATE')")
	public String updateCustomerForm(@PathVariable long id,
									 Principal userDetails,
									 Model model) {
		var customer = customerService.findCustomerById(id);
		model.addAttribute("customer", customer);
		model.addAttribute("customers", customerService.findAllCustomers());
		model.addAttribute("userDetails", userDetails);
		model.addAttribute("statusListUpdate", getStatusListCustomerForUpdate(customer));
		return "customer/customer-update";
	}

	@PostMapping("/update")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER_UPDATE')")
	public String updateCustomer(@Validated @ModelAttribute("customer") CustomerPayloadUpdate payload,
								 BindingResult bindingResult,
								 Principal userDetails,
								 Model model,
								 RedirectAttributes attributes) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("customers", customerService.findAllCustomers());
			model.addAttribute("statusListUpdate",
				getStatusListCustomerForUpdate(customerService.findCustomerById(payload.id())));
			model.addAttribute("userDetails", userDetails);
			return "customer/customer-update";
		} else {
			var customer = customerService.updateCustomer(payload);
			attributes.addFlashAttribute("successMessage", "success.object.update");
			return "redirect:/customers/update/%d".formatted(customer.getId());
		}
	}

	@GetMapping("/delete/{id:\\d+}")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER_DELETE')")
	public String deleteCustomerById(@PathVariable long id,
									 RedirectAttributes attributes) {
		customerService.deleteCustomerById(id);
		attributes.addFlashAttribute("successMessage", "success.object.deleted");
		return "redirect:/customers";
	}

}