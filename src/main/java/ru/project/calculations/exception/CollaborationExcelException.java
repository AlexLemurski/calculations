package ru.project.calculations.exception;

import lombok.Getter;

@Getter
public class CollaborationExcelException extends Exception {

    private final long id;

    public CollaborationExcelException(long id,
                                       String message) {
        super(message);
        this.id = id;
    }
}
