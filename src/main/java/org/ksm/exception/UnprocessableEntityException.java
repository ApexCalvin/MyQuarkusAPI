package org.ksm.exception;

import jakarta.ws.rs.ClientErrorException;

public class UnprocessableEntityException extends ClientErrorException {

    /**
     * Constructs a new "unprocessable entity" exception with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method)
     */
    public UnprocessableEntityException(String message) {
        super(message, 422);
    }  
}