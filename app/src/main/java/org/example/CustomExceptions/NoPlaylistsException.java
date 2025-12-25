package org.example.CustomExceptions;

public class NoPlaylistsException extends EmptyJsonFileException {

    public NoPlaylistsException() {
        super("No playlists available.");
    }

}
