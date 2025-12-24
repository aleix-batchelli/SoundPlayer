package org.example.Persistence;

import org.example.Business.Model.Song;
import java.util.List;

/**
 * Interface for Song persistence.
 * Defined in .
 */
public interface SongsDAO {
    List<Song> loadAll();
    void saveAll(List<Song> songs);
}