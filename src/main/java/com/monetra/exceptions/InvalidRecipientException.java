package com.monetra.exceptions;

public class InvalidRecipientException extends RuntimeException {
    public InvalidRecipientException(String message) {
        super(message);
    }
}
