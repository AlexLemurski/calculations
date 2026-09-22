package ru.project.calculations.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UsersPayloadNew(

	@NotBlank
	@Size(min = 2, max = 50, message = "{errors.size_min_to_max}")
	@Pattern(regexp = "^[А-ЯЁ]\\.[А-ЯЁ]\\.\\s[А-ЯЁ][а-яё]+$", message = "{errors.field_only_fio}")
	String userName,

	@NotBlank
	@Size(min = 2, max = 200, message = "{errors.size_min_to_max}")
	String password,

	@NotBlank
	@Size(min = 2, max = 100, message = "{errors.size_min_to_max}")
	@Pattern(regexp = "^[-а-яА-ЯёЁa\\s\\\\,.;:№()%#^*/\\d+$ –\"]+$", message = "{errors.field_only_ru}")
	String profession,

	@NotBlank
	@Size(min = 2, max = 100, message = "{errors.size_min_to_max}")
	@Pattern(regexp = "^[-а-яА-ЯёЁa\\s\\\\,.;:№()%#^*/\\d+$ –\"]+$", message = "{errors.field_only_ru}")
	String department,

	@NotBlank
	@Email
	@Size(max = 100, message = "{errors.size_min_to_max}")
	String email

) {
}