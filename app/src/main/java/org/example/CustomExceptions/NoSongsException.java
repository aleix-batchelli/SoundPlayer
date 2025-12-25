package org.example.CustomExceptions;

public class NoSongsException extends EmptyJsonFileException {

    public NoSongsException() {
        super("No songs available.");
    }

}
