package ru.project.calculations.dto.document_result;

import lombok.Builder;

import java.time.LocalDateTime;

public record DocumentResultDto(

        Long docId,

        String docName,

        String docType,

        String key,

        String size,

        long calculationId,

        long userId,

        String username,

        LocalDateTime timeStamp

) {
    @Builder
    public DocumentResultDto {

    }
}