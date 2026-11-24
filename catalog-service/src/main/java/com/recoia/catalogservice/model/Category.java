package com.recoia.catalogservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity                                   // cette classe devient une table
@Getter @Setter @NoArgsConstructor        // Lombok génère getters, setters, constructeur vide
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)   // id auto-incrémenté
    private Long id;

    private String name;
    private String description;
}