package org.example.Business.Model;

import java.util.ArrayList;
import java.util.List;

public class Playlist {
    
    private int id;
    private String name;
    private String description;
    private List<Integer> songIds;

    // --- PRELOADED INFO (CACHED) ---
    private int totalDuration; // In seconds
    private int playableCount;

    public Playlist() {
        this.songIds = new ArrayList<>();
    }

    public Playlist(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.songIds = new ArrayList<>();
        this.totalDuration = 0;
        this.playableCount = 0;
    }

    // --- MANAGEMENT METHODS (Update Cache) ---

    /**
     * Adds a song and updates the cached duration and playable count.
     * Requires the full Song object to know its duration/status.
     */
    public void addSong(Song s) {
        // Prevent duplicates
        if (!this.songIds.contains(s.getId())) {
            this.songIds.add(s.getId());
            
            // Update Cache
            this.totalDuration += s.getDurationSeconds();
            if (s.isPlayable()) {
                this.playableCount++;
            }
        }
    }

    /**
     * Removes a song and updates the cached duration and playable count.
     */
    public void removeSong(Song s) {
        if (this.songIds.contains(s.getId())) {
            // Remove ID (Cast to Integer to remove by Object, not index)
            this.songIds.remove((Integer) s.getId());

            // Update Cache
            this.totalDuration -= s.getDurationSeconds();
            // Prevent negative duration just in case
            if (this.totalDuration < 0) this.totalDuration = 0; 

            if (s.isPlayable()) {
                this.playableCount--;
            }
        }
    }

    /**
     * Helper method to recalculate stats from scratch.
     * Useful after loading from JSON or if a Song's details were edited.
     */
    public void recalculateStats(List<Song> library) {
        this.totalDuration = 0;
        this.playableCount = 0;

        for (Integer id : songIds) {
            Song s = library.stream()
                    .filter(song -> song.getId() == id)
                    .findFirst()
                    .orElse(null);
            
            if (s != null) {
                this.totalDuration += s.getDurationSeconds();
                if (s.isPlayable()) {
                    this.playableCount++;
                }
            }
        }
    }

    // --- GETTERS & SETTERS ---

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<Integer> getSongIds() { return songIds; }
    public void setSongIds(List<Integer> songIds) { this.songIds = songIds; }

    public int getTotalDuration() { return totalDuration; }
    // No setter for duration, it is calculated automatically

    public int getPlayableCount() { return playableCount; }
    // No setter for playableCount, it is calculated automatically

    // --- TO STRING ---

    @Override
    public String toString() {
        int minutes = totalDuration / 60;
        int seconds = totalDuration % 60;

        // Meets requirement: Name, Num Songs, Duration, Playable Count
        return String.format("Nombre: %-15s | Canciones: %-3d | Duración: %02d:%02d | Reproducibles: %d", 
                name, 
                songIds.size(), 
                minutes, 
                seconds,
                playableCount);
    }
}