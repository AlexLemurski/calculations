package ru.project.calculations.exception;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.formula.CollaboratingWorkbooksEnvironment;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.project.calculations.service.CalculationService;
import ru.project.calculations.service.CustomerService;
import ru.project.calculations.service.DocumentResultService;
import ru.project.calculations.service.UncalculatedService;

import java.util.Locale;
import java.util.NoSuchElementException;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;
    private final CalculationService calculationService;
    private final UncalculatedService uncalculatedService;
    private final CustomerService customerService;
    private final DocumentResultService documentResultService;

    @ExceptionHandler
    public String handleException(NoSuchElementException exception,
                                  Model model,
                                  HttpServletResponse response,
                                  Locale locale) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        model.addAttribute("calculations", calculationService.findAllCalculations());
        model.addAttribute("customers", customerService.findAllCustomers());
        model.addAttribute("not_found_message", messageSource.getMessage(
                exception.getMessage(),
                new Object[0],
                exception.getMessage(),
                locale));
        return "errors/io-error";
    }

    @ExceptionHandler
    public String handleException(DocumentResultValidationUploadException exception,
                                  Model model,
                                  HttpServletResponse response,
                                  Locale locale) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        model.addAttribute("calculations", calculationService.findAllCalculations());
        model.addAttribute("calculation", calculationService.findCalculationById(exception.getId()));
        model.addAttribute("resultDocument", documentResultService.findDocResultByCalcId(exception.getId()));
        model.addAttribute("uncalculated", uncalculatedService.findAllUncalculatedByCalcId(exception.getId()));
        model.addAttribute("doc_validation_exception_message", messageSource.getMessage(
                exception.getMessage(),
                new Object[0],
                exception.getMessage(),
                locale));
        return "calculation/calculation-doc-result-update";
    }

    @ExceptionHandler
    public String handleException(DocumentsIOException exception,
                                  Model model,
                                  HttpServletResponse response,
                                  Locale locale) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        model.addAttribute("calculations", calculationService.findAllCalculations());
        model.addAttribute("customers", customerService.findAllCustomers());
        model.addAttribute("io_exception_message", messageSource.getMessage(
                exception.getMessage(),
                new Object[0],
                exception.getMessage(),
                locale));
        return "errors/io-error";
    }

    @ExceptionHandler(CollaboratingWorkbooksEnvironment.WorkbookNotFoundException.class)
    public String handleWorkbookNotFound(CollaboratingWorkbooksEnvironment.WorkbookNotFoundException exception,
                                         Model model,
                                         HttpServletResponse response,
                                         Locale locale) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        model.addAttribute("calculations", calculationService.findAllCalculations());
        model.addAttribute("customers", customerService.findAllCustomers());
        model.addAttribute("callaborating_exception_message", messageSource.getMessage(
                "collaborating.exception.warning",
                new Object[]{exception.getMessage()},
                locale));
        return "errors/io-error";
    }

}