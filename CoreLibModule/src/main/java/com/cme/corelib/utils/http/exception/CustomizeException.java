package com.cme.corelib.utils.http.exception;

/**
 * Created by JokAr on 16/7/5.
 */
public class CustomizeException extends RuntimeException {

    public CustomizeException(String message, Throwable cause) {
        super(message, cause);
    }
}
