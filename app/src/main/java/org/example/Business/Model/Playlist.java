package org.example.Business.Model;

import java.util.ArrayList;
import java.util.List;

public class Playlist {
    
    private int id;
    private String name;
    private String description;
    private List<Integer> songIds;
    private int duration;

    public Playlist() {
        this.songIds = new ArrayList<>();
    }

    public Playlist(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.songIds = new ArrayList<>();
    }

    public void addSongId(int songId) {
        if (!this.songIds.contains(songId)) {
            this.songIds.add(songId);
        }
    }

    public int getTotalDuration(List<Song> library) {
        int totalSeconds = 0;
        for (Integer id : songIds) {
            Song s = library.stream()
                    .filter(song -> song.getId() == id)
                    .findFirst()
                    .orElse(null);
            
            if (s != null) {
                totalSeconds += s.getDurationSeconds();
            }
        }
        return totalSeconds;
    }

    public int getPlayableCount(List<Song> library) {
        int count = 0;
        for (Integer id : songIds) {
            Song s = library.stream()
                    .filter(song -> song.getId() == id)
                    .findFirst()
                    .orElse(null);
            
            if (s != null && s.isPlayable()) {
                count++;
            }
        }
        return count;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<Integer> getSongIds() { return songIds; }
    public void setSongIds(List<Integer> songIds) { this.songIds = songIds; }

    @Override
    public String toString() {
        int minutes = duration / 60;
        int seconds = duration % 60;

        return String.format("[ID: %d] %s - %s (%d songs) [%d:%02d]", 
                id, 
                name, 
                description, 
                songIds.size(), 
                minutes, 
                seconds);
    }
}