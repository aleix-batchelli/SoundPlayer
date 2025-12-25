package org.example.Business.Managers;

import org.example.Business.Model.Playlist;
import org.example.Business.Model.Song;
import org.example.CustomExceptions.EmptyJsonFileException;
import org.example.CustomExceptions.NotFoundException;
import org.example.CustomExceptions.PlaylistNotFoundException;
import org.example.CustomExceptions.SongInUseException;
import org.example.CustomExceptions.SongNotFoundException;
import org.example.CustomExceptions.SongNotFoundInPlaylistException;
import org.example.Persistence.*;
import java.io.IOException;
import java.util.List;

public class LibraryManager {

    private final SongsDAO songsDAO;
    private final PlaylistsDAO playlistsDAO;

    private List<Song> allSongs;

    public LibraryManager() throws IOException {
        this.songsDAO = new JsonSongsDAO();
        this.playlistsDAO = new JsonPlaylistsDAO();
    }

    public List<Song> getAllSongs() throws EmptyJsonFileException {
        return songsDAO.loadAll();
    }

    public Song getSongById(int id) throws EmptyJsonFileException {
        return songsDAO.loadAll().stream()
                .filter(s -> s.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void addSong(Song song) throws EmptyJsonFileException, IOException {
        List<Song> allSongs = this.songsDAO.loadAll();
        allSongs.add(song);
        songsDAO.saveAll(allSongs);
    }

    public void deleteSong(int id) throws EmptyJsonFileException, IOException, SongInUseException, NotFoundException {
        boolean isUsed = playlistsDAO.loadAll().stream()
                .anyMatch(p -> p.getSongIds().contains(id));

        if (isUsed) {
            throw new SongInUseException();
        }

        boolean removed = allSongs.removeIf(s -> s.getId() == id);
        if (removed) {
            songsDAO.saveAll(allSongs); 
        } else {
            throw new SongNotFoundException();
        }
    }

    /**
     * Removes a specific song from a playlist and saves the changes.
     */
    public void removeSongFromPlaylist(int playlistId, int songId) throws IOException, NotFoundException {
        Playlist playlist = getPlaylistById(playlistId);
        
        if (playlist != null) {
            // CAST IS CRITICAL: remove(Object) vs remove(int index)
            // We want to remove the object (the ID value), not the index.
            boolean removed = playlist.getSongIds().remove((Integer) songId);
            
            if (removed) {
                updatePlaylist(playlist);
            } else {
                throw new SongNotFoundInPlaylistException();
            }
        } else {
            throw new PlaylistNotFoundException();
        }
    }

    /**
     * Updates an existing song.
     * Replaces the song with the matching ID and saves the changes to the JSON file.
     */
    public void updateSong(Song updatedSong) throws EmptyJsonFileException, NotFoundException, IOException {
        // 1. Load the most current list of songs
        List<Song> currentSongs = songsDAO.loadAll();
        
        boolean found = false;
        
        // 2. Iterate to find the song by ID
        for (int i = 0; i < currentSongs.size(); i++) {
            if (currentSongs.get(i).getId() == updatedSong.getId()) {
                // 3. Replace the old object with the new one
                currentSongs.set(i, updatedSong);
                found = true;
                break;
            }
        }

        // 4. Save changes if the song was found
        if (found) {
            songsDAO.saveAll(currentSongs);
            // Update internal cache if you are using it
            this.allSongs = currentSongs; 
        } else {
            throw new SongNotFoundException();
        }
    }

    public List<Playlist> getAllPlaylists() {
        return playlistsDAO.loadAll();
    }

    public Playlist getPlaylistById(int id) {
        return this.playlistsDAO.loadAll().stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void createPlaylist(Playlist p) throws IOException {
        List<Playlist> allPlaylists = this.playlistsDAO.loadAll();
        allPlaylists.add(p);
        playlistsDAO.saveAll(allPlaylists);
    }

    /**
     * Adds a song to a playlist by ID.
     * Validates that both IDs exist before adding.
     */
    public void addSongToPlaylist(int playlistId, int songId) throws NotFoundException, EmptyJsonFileException, IOException {
        // Requirement: Avoid adding non-existent song IDs 
        Song song = getSongById(songId);
        if (song == null) {
            throw new SongNotFoundException();
        }

        Playlist playlist = getPlaylistById(playlistId);
        if (playlist == null) {
            throw new PlaylistNotFoundException();
        }

        // Add ID and save
        playlist.addSong(song);
        updatePlaylist(playlist);
    }

    /**
     * Updates the metadata (Name, Description) of an existing playlist.
     * Replaces the object in the list and persists changes.
     */
    public void updatePlaylist(Playlist updatedPlaylist) throws IOException, NotFoundException {
        // 1. Ensure we have the list loaded
        List<Playlist> allPlaylists = playlistsDAO.loadAll();

        boolean found = false;

        // 2. Iterate to find the playlist by ID
        for (int i = 0; i < allPlaylists.size(); i++) {
            if (allPlaylists.get(i).getId() == updatedPlaylist.getId()) {
                // 3. Replace the old object with the new one
                // This updates Name, Description, and SongIds all at once
                allPlaylists.set(i, updatedPlaylist);
                found = true;
                break;
            }
        }

        // 4. Save changes
        if (found) {
            playlistsDAO.saveAll(allPlaylists);
        } else {
            throw new PlaylistNotFoundException();
        }
    }

    public void deletePlaylist(int id) throws IOException {
        List<Playlist> playlists = playlistsDAO.loadAll();
        boolean removed = playlists.removeIf(p -> p.getId() == id);
        if (removed) {
            playlistsDAO.saveAll(playlists);
        }
    }
}