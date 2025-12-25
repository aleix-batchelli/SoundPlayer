package org.example.Persistence;

import org.example.Business.Model.Song;
import org.example.CustomExceptions.EmptyJsonFileException;

import java.io.IOException;
import java.util.List;

/**
 * Interface for Song persistence.
 * Defined in .
 */
public interface SongsDAO {
    
    List<Song> loadAll() throws EmptyJsonFileException;
    
    void saveAll(List<Song> songs) throws IOException;
}