package org.example.CustomExceptions;

public class SongNotFoundException extends NotFoundException{

    public SongNotFoundException() {
        super("Song not found.");
    }
}
