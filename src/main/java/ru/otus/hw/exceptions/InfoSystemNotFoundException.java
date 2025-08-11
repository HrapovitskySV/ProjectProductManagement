package ru.otus.hw.exceptions;

public class InfoSystemNotFoundException extends RuntimeException {

    public InfoSystemNotFoundException() {
        super("Infosystem not found");
    }

    public InfoSystemNotFoundException(String message) {
        super(message);
    }
}
