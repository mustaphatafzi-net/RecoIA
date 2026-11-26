package com.recoia.authservice.exception;

public class ForbiddenException extends RuntimeException {

    public ForbiddenException() {
        super("Acces reserve aux administrateurs");
    }
}
