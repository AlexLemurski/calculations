package ru.project.calculations.dto.partition;

import lombok.Builder;
import ru.project.calculations.enums.ContentType;

public record PartitionDto(

        long id,

        ContentType contentType,

        String position,

        String partition,

        String sum,

        int calculated,

        int total,

        String percent,

        long calculationId

) {
    @Builder
    public PartitionDto {

    }

}