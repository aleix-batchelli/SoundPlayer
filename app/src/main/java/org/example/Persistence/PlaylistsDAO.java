package org.example.Persistence;

import org.example.Business.Model.Playlist;
import org.example.CustomExceptions.EmptyJsonFileException;
import java.io.IOException;
import java.util.List;

/**
 * Interfaz para la persistencia de Playlists.
 * Definida en .
 */
public interface PlaylistsDAO {

    List<Playlist> loadAll();

    void saveAll(List<Playlist> playlists) throws IOException;
}