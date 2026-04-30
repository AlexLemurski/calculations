package ru.project.calculations.dto.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CustomerPayloadUpdate(

        long id,

        @NotBlank
        @Size(min = 2, max = 1000, message = "{errors.size_min_to_max}")
        @Pattern(regexp = "^[-а-яА-ЯёЁ\\s'\",.;:№()^\\d+$«»–]+$", message = "{errors.field_only_ru}")
        String customerName,

        @NotBlank
        @Size(min = 10, max = 10, message = "{errors.size_min_to_max}")
        @Pattern(regexp = "^\\d+$", message = "{errors.only_digits}")
        String customerINNCode,

        @NotBlank
        @Size(min = 9, max = 9, message = "{errors.size_min_to_max}")
        @Pattern(regexp = "^\\d+$", message = "{errors.only_digits}")
        String customerKPPCode,

        @NotBlank
        @Size(min = 13, max = 13, message = "{errors.size_min_to_max}")
        @Pattern(regexp = "^\\d+$", message = "{errors.only_digits}")
        String customerOGRNCode,

        @NotBlank
        @Size(min = 2, max = 500, message = "{errors.size_min_to_max}")
        @Pattern(regexp = "^[-а-яА-ЯёЁ\\s'\",.;:№()^\\d+$«»–]+$", message = "{errors.field_only_ru}")
        String mainActivity,

        @NotBlank
        @Size(min = 2, max = 3000, message = "{errors.size_min_to_max}")
        @Pattern(regexp = "^[-а-яА-ЯёЁ\\s'\",.;:№()^\\d+$«»–]+$", message = "{errors.field_only_ru}")
        String legalAddress,

        @NotBlank
        @Email(regexp = "^[\\S+@\\S+\\.\\S]+$", message = "{errors.mail_format}")
        String mail,

        @NotBlank
        @Pattern(regexp = "[+7]*[(]*[0-9]*[)]*[0-9]*-*[0-9]*-*[0-9]*", message = "{errors.phone_number_format}")
        String phone

) {
}