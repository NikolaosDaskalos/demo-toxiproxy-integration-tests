package org.demo.toxiproxy.service.exception;

import java.io.Serial;

public class BadRequestException extends Exception {
    @Serial
    private final static long serialVersionUID = 2L;

    public BadRequestException(String message) {
        super(message);
    }
}
