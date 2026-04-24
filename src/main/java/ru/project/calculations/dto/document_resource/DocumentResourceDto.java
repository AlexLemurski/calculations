package ru.project.calculations.dto.document_resource;

import lombok.Builder;
import ru.project.calculations.enums.ContentType;
import ru.project.calculations.enums.DocumentIndex;

import java.util.List;

public record DocumentResourceDto(

        long docId,

        String docName,

        String docType,

        String key,

        DocumentIndex documentIndex,

        String size,

        long calculationId,

        List<ContentType> contentTypes

) {
    @Builder
    public DocumentResourceDto {

    }
}