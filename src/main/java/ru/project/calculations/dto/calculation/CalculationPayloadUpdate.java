package ru.project.calculations.dto.calculation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CalculationPayloadUpdate(

	long id,

	String status,

	@NotBlank
	@Size(min = 2, max = 1000, message = "{errors.size_min_to_max}")
	@Pattern(regexp = "^[-а-яА-ЯёЁa-zA-Z\\s\\\\,.;:№()%#^*/\\d+$ –\"]+$", message = "{errors.field_only_ru}")
	String lotName,

	@NotBlank
	@Size(min = 2, max = 3000, message = "{errors.size_min_to_max}")
	@Pattern(regexp = "^[-а-яА-ЯёЁa-zA-Z\\s\\\\,.;:№()%#^*/\\d+$ –\"]+$", message = "{errors.field_only_ru}")
	String projectName,

	@NotBlank
	@Size(min = 2, max = 1000, message = "{errors.size_min_to_max}")
	@Pattern(regexp = "^[-а-яА-ЯёЁa-zA-Z\\s\\\\,.;:№()%#^*/\\d+$ –\"]+$", message = "{errors.field_only_ru}")
	String projectLocation,

	Long customerId

) {

}