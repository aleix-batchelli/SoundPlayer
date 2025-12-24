package org.example.Business.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa una canción con sus metadatos y (opcionalmente) su secuencia de notas.
 * Definido en .
 */
public class Song {

    private String id;               
    private String title;            
    private String artist;           
    private int durationSeconds;    
    private String style;           
    private Mood mood;              
    private boolean playable;       
    private List<Note> notes;

    /**
     * Constructor vacío requerido para la deserialización de JSON (Gson).
     */
    public Song() {
        this.notes = new ArrayList<>();
    }

    /**
     * Constructor básico con metadatos.
     */
    public Song(String id, String title, String artist, int durationSeconds, String style, boolean playable, Mood mood) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.durationSeconds = durationSeconds;
        this.style = style;
        this.playable = playable;
        this.mood = mood;
        this.notes = new ArrayList<>();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getArtist() { return artist; }
    public void setArtist(String artist) { this.artist = artist; }

    public int getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(int durationSeconds) { this.durationSeconds = durationSeconds; }

    public String getStyle() { return style; }
    public void setStyle(String style) { this.style = style; }

    public Mood getMood() { return mood; }
    public void setMood(Mood mood) { this.mood = mood; }

    /**
     * Indica si la canción tiene datos para ser reproducida.
     * 
     */
    public boolean isPlayable() { return playable; }
    public void setPlayable(boolean playable) { this.playable = playable; }

    public List<Note> getNotes() { return notes; }
    public void setNotes(List<Note> notes) { this.notes = notes; }

    /**
     * Método helper para añadir una nota individualmente.
     */
    public void addNote(Note note) {
        if (this.notes == null) {
            this.notes = new ArrayList<>();
        }
        this.notes.add(note);
    }
}