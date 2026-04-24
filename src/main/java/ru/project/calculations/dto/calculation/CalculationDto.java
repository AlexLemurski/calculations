package ru.project.calculations.dto.calculation;

import lombok.Builder;

import java.time.LocalDate;

public record CalculationDto(

        long id,

        String lotName,

        String projectName,

        String projectLocation,

        LocalDate dateOfCreate,

        String totalSum,

        int calculatedPositionCount,

        int totalPositionCount,

        String totalPercent,

        String remainder,

        String resourceFolder,

        long customerId,

        String customerName

) {
    @Builder
    public CalculationDto {

    }
}
