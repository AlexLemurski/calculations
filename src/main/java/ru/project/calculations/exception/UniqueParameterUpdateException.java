package ru.project.calculations.exception;

import lombok.Getter;
import ru.project.calculations.dto.customer.CustomerPayloadUpdate;

@Getter
public class UniqueParameterUpdateException extends RuntimeException {

    private final CustomerPayloadUpdate payload;

    public UniqueParameterUpdateException(String message,
                                          CustomerPayloadUpdate payload) {
        super(message);
        this.payload = payload;

    }
}
