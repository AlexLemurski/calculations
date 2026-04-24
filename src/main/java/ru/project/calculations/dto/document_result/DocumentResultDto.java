package ru.project.calculations.dto.document_result;

import lombok.Builder;
import ru.project.calculations.enums.DocumentIndex;

public record DocumentResultDto(

        Long docId,

        String docName,

        String docType,

        String key,

        String size,

        long calculationId

) {
    @Builder
    public DocumentResultDto {

    }
}