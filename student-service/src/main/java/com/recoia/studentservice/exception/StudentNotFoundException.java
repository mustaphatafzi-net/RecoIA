package com.recoia.studentservice.exception;

public class StudentNotFoundException extends RuntimeException {

    public StudentNotFoundException() {

        super("Profil etudiant introuvable");
    }
}
