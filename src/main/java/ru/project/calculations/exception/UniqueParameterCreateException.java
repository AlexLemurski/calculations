package ru.project.calculations.exception;

import lombok.Getter;
import ru.project.calculations.dto.customer.CustomerPayloadNew;

@Getter
public class UniqueParameterCreateException extends RuntimeException {

    private final CustomerPayloadNew payload;

    public UniqueParameterCreateException(String message,
                                          CustomerPayloadNew payload) {
        super(message);
        this.payload = payload;
    }

}