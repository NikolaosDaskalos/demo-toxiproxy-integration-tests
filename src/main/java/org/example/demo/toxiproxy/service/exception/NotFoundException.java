package org.example.demo.toxiproxy.service.exception;

import java.io.Serial;

public class NotFoundException extends Exception {
    @Serial
    private final static long serialVersionUID = 1L;

    public NotFoundException(String message) {
        super(message);
    }
}
