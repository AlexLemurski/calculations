package ru.project.calculations.exception;

import lombok.Getter;

@Getter
public class DeleteEntityDataBaseException extends RuntimeException{

    private final long id;

    public DeleteEntityDataBaseException(long id,
                                         String message) {
        super(message);
        this.id = id;
    }

}