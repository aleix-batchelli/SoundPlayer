package org.example.CustomExceptions;

public class EmptyJsonFileException extends Exception {
    public EmptyJsonFileException(String message) {
        super(message);
    }

}
