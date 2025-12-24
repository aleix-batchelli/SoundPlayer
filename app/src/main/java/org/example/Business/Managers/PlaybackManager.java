package org.example.Business.Managers;

import org.example.Business.Audio.SoundSynthSinus;
import org.example.Business.Audio.SoundSynth;
import org.example.Business.Model.Note;
import org.example.Business.Model.Playlist;
import org.example.Business.Model.Song;

import java.util.List;

public class PlaybackManager {

    private final SoundSynth synth;

    public PlaybackManager() {
        // Inicializamos el sintetizador concreto (SinusSynth)
        this.synth = new SoundSynthSinus();
    }

    /**
     * Reproduce una canción individual nota a nota.
     [cite_start]* [cite: 108, 111, 112, 113]
     */
    public void playSong(Song s) {
        if (s == null) return;

        System.out.println(">> Reproduciendo: " + s.getTitle() + " (" + s.getArtist() + ")");

        if (!s.isPlayable()) {
            System.out.println(">> [!] La canción no es reproducible (faltan datos de notas).");    // [cite: 39]
            return;
        }

        List<Note> notes = s.getNotes();
        if (notes == null || notes.isEmpty()) {
            System.out.println(">> [!] No hay notas definidas para reproducir.");
            return;
        }

        // Recorrer la secuencia de notas e invocar al sintetizador
        for (Note n : notes) {
            // Asumimos reproducción monofónica simple
            synth.makeSound(n.getFrequency(), n.getDurationMs()); // [cite: 113, 119]
        }
        System.out.println(">> Fin de canción.");
    }

    /**
     * Reproduce una playlist completa.
     * Requiere LibraryManager para resolver los IDs de las canciones.
     [cite_start]* [cite: 114, 115, 116]
     */
    public void playPlaylist(Playlist p, LibraryManager libraryManager) {
        if (p == null) return;

        System.out.println("\n>> Iniciando Playlist: " + p.getName());
        List<String> songIds = p.getSongIds();

        if (songIds.isEmpty()) {
            System.out.println(">> La playlist está vacía.");
            return;
        }

        for (String id : songIds) {
            Song s = libraryManager.getSongById(id);
            
            if (s != null) {
                // Solo reproduce aquellas que son PLAYABLE
                if (s.isPlayable()) { // [cite: 117]
                    playSong(s);
                    
                    // Pequeña pausa entre canciones para que no suene todo seguido
                    try { Thread.sleep(1000); } catch (InterruptedException e) {}
                } else {
                    System.out.println(">> Saltando '" + s.getTitle() + "' (No reproducible).");
                }
            } else {
                System.out.println(">> [!] ID de canción no encontrado: " + id);
            }
        }
        System.out.println(">> Fin de Playlist.");
    }
}