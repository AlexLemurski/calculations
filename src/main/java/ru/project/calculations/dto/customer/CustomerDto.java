package ru.project.calculations.dto.customer;

import lombok.Builder;

public record CustomerDto(

        long id,

        String customerName,

        String customerINNCode,

        String customerKPPCode,

        String customerOGRNCode,

        String mainActivity,

        String legalAddress,

        String mail,

        String phone

) {
    @Builder
    public CustomerDto {

    }

}