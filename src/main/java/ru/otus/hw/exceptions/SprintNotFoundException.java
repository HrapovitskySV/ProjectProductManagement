package ru.otus.hw.exceptions;

public class SprintNotFoundException extends RuntimeException {

    public SprintNotFoundException() {
        super("Sprint not found");
    }

    public SprintNotFoundException(String message) {
        super(message);
    }
}
