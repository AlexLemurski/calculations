package ru.project.calculations.dto.calculation;

import lombok.Builder;

import java.time.LocalDateTime;

public record CalculationDto(

	long id,

	String status,

	String lotName,

	String projectName,

	String projectLocation,

	LocalDateTime dateOfCreate,

	String totalSum,

	int calculatedPositionCount,

	int totalPositionCount,

	String totalPercent,

	String remainder,

	String resourceFolder,

	long customerId,

	String customerName,

	long userId,

	String username

) {
	@Builder
	public CalculationDto {

	}
}
