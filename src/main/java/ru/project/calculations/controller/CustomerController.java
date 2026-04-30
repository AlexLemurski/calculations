package ru.project.calculations.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.project.calculations.dto.customer.CustomerPayloadNew;
import ru.project.calculations.dto.customer.CustomerPayloadUpdate;
import ru.project.calculations.exception.DeleteEntityDataBaseException;
import ru.project.calculations.service.CalculationService;
import ru.project.calculations.service.CustomerService;

@Controller
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final CalculationService calculationService;

    @GetMapping
    public String findAllCustomers(Model model) {
        model.addAttribute("customers", customerService.findAllCustomers());
        return "customer/customer-menu";
    }

    @GetMapping("/{id:\\d+}")
    public String findAllCustomerById(@PathVariable long id,
                                      Model model) {
        model.addAttribute("customer", customerService.findCustomerById(id));
        model.addAttribute("customers", customerService.findAllCustomers());
        model.addAttribute("calculationsByCastId", calculationService.findAllCalculationsByCastId(id));
        return "customer/customer-view";
    }

    @GetMapping("/create")
    public String createCustomerForm(@ModelAttribute("customer") CustomerPayloadNew payload,
                                     Model model) {
        model.addAttribute("customers", customerService.findAllCustomers());
        return "customer/customer-create";
    }

    @PostMapping("/create")
    public String createCustomer(@Validated @ModelAttribute("customer") CustomerPayloadNew payload,
                                 BindingResult bindingResult,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("customers", customerService.findAllCustomers());
            return "customer/customer-create";
        } else {
            var customer = customerService.createCustomer(payload);
            return "redirect:/customers/%d".formatted(customer.getId());
        }
    }

    @GetMapping("/update/{id:\\d*}")
    public String updateCustomerForm(@PathVariable long id,
                                     Model model) {
        model.addAttribute("customer", customerService.findCustomerById(id));
        model.addAttribute("customers", customerService.findAllCustomers());
        return "customer/customer-update";
    }

    @PostMapping("/update")
    public String updateCustomer(@Validated @ModelAttribute("customer") CustomerPayloadUpdate payload,
                                 BindingResult bindingResult,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("customers", customerService.findAllCustomers());
            return "customer/customer-update";
        } else {
            var customer = customerService.updateCustomer(payload);
            return "redirect:/customers/update/%d".formatted(customer.getId());
        }
    }

    @GetMapping("/delete/{id:\\d+}")
    public String deleteCustomerById(@PathVariable long id) {
        customerService.deleteCustomerById(id);
        return "redirect:/customers";
    }

}