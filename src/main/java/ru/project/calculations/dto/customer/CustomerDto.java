package ru.project.calculations.dto.customer;

import lombok.Builder;

public record CustomerDto(

        long id,

        String customerName,

        String customerAddress

) {
    @Builder
    public CustomerDto{

    }
}
