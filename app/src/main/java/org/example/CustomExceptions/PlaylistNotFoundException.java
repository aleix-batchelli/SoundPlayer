package org.example.CustomExceptions;

public class PlaylistNotFoundException extends NotFoundException {
    public PlaylistNotFoundException() {
        super("Playlist not found.");
    }
}
