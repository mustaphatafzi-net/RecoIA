package com.recoia.recommendationservice.exception;

/** Un autre service (catalogue, profil, interactions) ne repond pas. */
public class ServiceUnavailableException extends RuntimeException {

    public ServiceUnavailableException(String message) {
        super(message);
    }
}
