package ru.project.calculations.exception;

import lombok.Getter;

@Getter
public class DocumentResultValidationUploadException extends RuntimeException {

    private final long id;

    public DocumentResultValidationUploadException(long id,
                                                   String msg) {
        super(msg);
        this.id = id;
    }

}