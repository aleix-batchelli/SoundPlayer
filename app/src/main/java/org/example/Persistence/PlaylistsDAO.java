package org.example.Persistence;

import org.example.Business.Model.Playlist;
import java.util.List;

/**
 * Interfaz para la persistencia de Playlists.
 * Definida en .
 */
public interface PlaylistsDAO {
    List<Playlist> loadAll();
    void saveAll(List<Playlist> playlists);
}