package org.example.exception;

public class CalendarLoadingException extends RuntimeException {

    public CalendarLoadingException(String message) {
        super(message);
    }

    public CalendarLoadingException(String message, Throwable cause) {
        super(message, cause);
    }
}
