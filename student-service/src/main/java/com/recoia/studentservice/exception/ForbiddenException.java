package com.recoia.studentservice.exception;

public class ForbiddenException extends RuntimeException {

    public ForbiddenException() {
        super("Acces reserve aux administrateurs");
    }
}
