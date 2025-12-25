package org.example.CustomExceptions;

public class SongNotFoundInPlaylistException extends NotFoundException {

    public SongNotFoundInPlaylistException() {
        super("Song not found in the specified playlist.");
    }
}
