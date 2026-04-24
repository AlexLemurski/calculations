package ru.project.calculations.dto.uncalculated;

import lombok.Builder;

public record UncalculatedDto(

        long id,

        String position,

        String nomenclature,

        String partition,

        String name,

        String standart,

        String quantity,

        String quality,

        String comment,

        long calculationId

) {
    @Builder
    public UncalculatedDto {

    }
}