package org.example.Business.Managers;

import org.example.Business.Model.Playlist;
import org.example.Business.Model.Song;
import org.example.Persistence.*;

import java.util.ArrayList;
import java.util.List;

public class LibraryManager {

    // Dependencies: Data Access Objects (Interfaces) 
    private final SongsDAO songsDAO;
    private final PlaylistsDAO playlistsDAO;

    // In-memory cache to avoid reading JSON on every request
    private List<Song> allSongs;
    private List<Playlist> allPlaylists;

    public LibraryManager() {
        // Initialize concrete DAOs (Json implementation) [cite: 64, 65]
        this.songsDAO = new SongJSON();
        this.playlistsDAO = new PlaylistJSON();

        // Load initial data into memory [cite: 54, 56]
        this.allSongs = songsDAO.loadAll();
        if (this.allSongs == null) this.allSongs = new ArrayList<>();
        
        this.allPlaylists = playlistsDAO.loadAll();
        if (this.allPlaylists == null) this.allPlaylists = new ArrayList<>();
    }

    // --- SONG MANAGEMENT [cite: 15, 72] ---

    public List<Song> getAllSongs() {
        return allSongs;
    }

    public Song getSongById(String id) {
        return allSongs.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void addSong(Song song) {
        allSongs.add(song);
        songsDAO.saveAll(allSongs); // Persist changes [cite: 88]
    }

    public void deleteSong(String id) {
        // Requirement: Check if song is used in any playlist before deleting 
        boolean isUsed = allPlaylists.stream()
                .anyMatch(p -> p.getSongIds().contains(id));

        if (isUsed) {
            System.out.println(">> Error: No se puede borrar la canción. Está en uso en una o más playlists.");
            return;
        }

        boolean removed = allSongs.removeIf(s -> s.getId().equals(id));
        if (removed) {
            songsDAO.saveAll(allSongs); // Persist changes
            System.out.println(">> Canción eliminada correctamente.");
        } else {
            System.out.println(">> Error: ID de canción no encontrado.");
        }
    }

    // --- PLAYLIST MANAGEMENT [cite: 16, 89] ---

    public List<Playlist> getAllPlaylists() {
        return allPlaylists;
    }

    public Playlist getPlaylistById(String id) {
        return allPlaylists.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void createPlaylist(Playlist p) {
        allPlaylists.add(p);
        playlistsDAO.saveAll(allPlaylists); // Persist changes [cite: 105]
    }

    /**
     * Adds a song to a playlist by ID.
     * Validates that both IDs exist before adding.
     */
    public void addSongToPlaylist(String playlistId, String songId) throws Exception {
        // Requirement: Avoid adding non-existent song IDs 
        Song song = getSongById(songId);
        if (song == null) {
            throw new Exception("El ID de la canción no existe.");
        }

        Playlist playlist = getPlaylistById(playlistId);
        if (playlist == null) {
            throw new Exception("El ID de la playlist no existe.");
        }

        // Add ID and save
        playlist.addSongId(songId);
        playlistsDAO.saveAll(allPlaylists);
    }

    public void deletePlaylist(String id) {
        boolean removed = allPlaylists.removeIf(p -> p.getId().equals(id));
        if (removed) {
            playlistsDAO.saveAll(allPlaylists);
        }
    }
}