package org.example.Business.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa una lista de reproducción que contiene referencias a canciones (IDs).
 * Definido en .
 */
public class Playlist {
    
    // Identificador único [cite: 42]
    private String id;

    // Nombre de la playlist [cite: 43]
    private String name;

    // Descripción opcional [cite: 44]
    private String description;

    // Lista de IDs de canciones (no objetos canción completos) [cite: 13, 46]
    private List<String> songIds;

    // Constructor vacío para frameworks de JSON (Gson)
    public Playlist() {
        this.songIds = new ArrayList<>();
    }

    public Playlist(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.songIds = new ArrayList<>();
    }

    // --- Métodos de Gestión de IDs ---

    /**
     * [cite_start]Añade una referencia (ID) de canción a la playlist. [cite: 46]
     */
    public void addSongId(String songId) {
        if (!this.songIds.contains(songId)) {
            this.songIds.add(songId);
        }
    }

    // --- Métodos de Cálculo (Lógica de negocio ligera) ---

    /**
     * Calcula la duración total sumando las duraciones de las canciones referenciadas.
     * Requiere la lista completa de canciones (biblioteca) para buscar los IDs.
     [cite_start]* [cite: 48]
     */
    public int getTotalDuration(List<Song> library) {
        int totalSeconds = 0;
        for (String id : songIds) {
            // Buscamos la canción correspondiente al ID
            Song s = library.stream()
                    .filter(song -> song.getId().equals(id))
                    .findFirst()
                    .orElse(null);
            
            if (s != null) {
                totalSeconds += s.getDurationSeconds();
            }
        }
        return totalSeconds;
    }

    /**
     * Calcula cuántas canciones de la lista son reproducibles.
     [cite_start]* [cite: 49]
     */
    public int getPlayableCount(List<Song> library) {
        int count = 0;
        for (String id : songIds) {
            Song s = library.stream()
                    .filter(song -> song.getId().equals(id))
                    .findFirst()
                    .orElse(null);
            
            if (s != null && s.isPlayable()) {
                count++;
            }
        }
        return count;
    }

    // --- Getters y Setters ---

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<String> getSongIds() { return songIds; }
    public void setSongIds(List<String> songIds) { this.songIds = songIds; }
}