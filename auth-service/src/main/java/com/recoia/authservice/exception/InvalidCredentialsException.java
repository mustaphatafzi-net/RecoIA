package com.recoia.authservice.exception;

public class InvalidCredentialsException extends RuntimeException {

    // Meme message dans tous les cas : on ne revele pas si l'email existe
    public InvalidCredentialsException() {
        super("Email ou mot de passe incorrect");
    }
}
