package org.example.CustomExceptions;

public class SongInUseException extends Exception {

    public SongInUseException() {
        super("The song is currently in use in one or more playlists.");
    }

}
