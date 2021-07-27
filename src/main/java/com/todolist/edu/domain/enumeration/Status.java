package com.todolist.edu.domain.enumeration;

/**
 * The Status enumeration.
 */
public enum Status {
    AFAZER("Afazer"),
    FAZENDO("Fazendo"),
    FEITA("Feita");

    private final String value;

    Status(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}