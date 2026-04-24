package ru.project.calculations.dto.calculation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CalculationPayloadUpdate(

        long id,

        @NotBlank
        @Size(min = 2, max = 1000)
        String lotName,

        @NotBlank
        @Size(min = 2, max = 3000)
        String projectName,

        @NotBlank
        @Size(min = 2, max = 1000)
        String projectLocation,

        @PastOrPresent
        LocalDate dateOfCreate,

        Long customerId

) {

}