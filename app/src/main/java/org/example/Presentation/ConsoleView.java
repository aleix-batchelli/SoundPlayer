package org.example.Presentation;

import org.example.Business.Model.Mood;
import org.example.Business.Model.Playlist;
import org.example.Business.Model.Song;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class UI {
    private final Scanner scanner;

    public UI() {
        this.scanner = new Scanner(System.in);
    }

    public void printMessage(String msg) {
        System.out.println(msg);
    }

    public String readInput(String prompt) {
        System.out.print(prompt + ": ");
        return scanner.nextLine().trim();
    }

    /**
     * Displays the main menu and returns the user's choice as an integer.
     * Maps "Q" to 0 for exit.
     */
    public int showMainMenu() {
        System.out.println("\n--- SoundPlayer ---");
        System.out.println("1. Gestionar canciones");
        System.out.println("2. Gestionar playlists");
        System.out.println("3. Reproducir");
        System.out.println("4. Generar álbum random por mood");
        System.out.println("Q. Salir");
        System.out.print("Seleccione una opción: ");

        String input = scanner.nextLine().trim();

        if (input.equalsIgnoreCase("Q")) {
            return 0; // 0 represents Exit
        }

        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println(">> Error: Por favor introduzca un número válido o 'Q'.");
            return -1; // -1 represents invalid input
        }
    }

    /**
     * Displays the list of songs with details (Title, Artist, Duration, Mood, Style, Playable).
     */
    public void displaySongList(List<Song> songs) {
        System.out.println("\n--- Biblioteca de Canciones ---");
        if (songs.isEmpty()) {
            System.out.println("(La biblioteca está vacía)");
            return;
        }

        // Header
        System.out.printf("%-36s %-20s %-20s %-10s %-12s %-15s%n",
                "ID", "TÍTULO", "ARTISTA", "DURACIÓN", "MOOD", "ESTADO");
        System.out.println("--------------------------------------------------------------------------------------------------------------------");

        for (Song s : songs) {
            String duration = (s.getDurationSeconds() / 60) + ":" + String.format("%02d", s.getDurationSeconds() % 60);
            String playableStr = s.isPlayable() ? "[PLAYABLE]" : "[NOT PLAYABLE]";
            
            System.out.printf("%-36s %-20s %-20s %-10s %-12s %-15s%n",
                    s.getId(),
                    truncate(s.getTitle(), 20),
                    truncate(s.getArtist(), 20),
                    duration,
                    s.getMood(),
                    playableStr
            );
        }
    }

    /**
     * Displays details of a specific playlist.
     */
    /**
     * Displays details of a specific playlist.
     * Updated to accept 'library' because Playlist needs it to calculate duration/counts.
     */
    public void displayPlaylistDetails(Playlist p, List<Song> library) {
        if (p == null) {
            System.out.println(">> Playlist no encontrada.");
            return;
        }

        // We must pass the library list to calculate totals
        int totalSeconds = p.getTotalDuration(library);
        String durationFmt = (totalSeconds / 60) + " min " + (totalSeconds % 60) + " s";

        System.out.println("\n--- Detalles de la Playlist ---");
        System.out.println("ID          : " + p.getId());
        System.out.println("Nombre      : " + p.getName());
        System.out.println("Descripción : " + p.getDescription());
        System.out.println("Canciones   : " + p.getSongIds().size());
        System.out.println("Duración    : " + durationFmt);
        // We must pass the library list here as well
        System.out.println("Playable    : " + p.getPlayableCount(library));
        System.out.println("-------------------------------");
    }

    /**
     * Prompts user for data to create a new Song.
     * Includes robust reading for Enums and Integers.
     */
    public Song readSongData() {
        System.out.println("\n--- Nueva Canción ---");
        
        // ID generation (Auto-generated for simplicity, though PDF mentions ID is String/int)
        String id = UUID.randomUUID().toString(); 
        
        String title = readInput("Título");
        while(title.isEmpty()) title = readInput("Título (no puede estar vacío)");
        
        String artist = readInput("Artista");
        
        int duration = readInt("Duración (segundos)");

        // Read Mood with validation
        Mood mood = null;
        while (mood == null) {
            System.out.print("Mood (HAPPY, SAD, RELAX, ENERGETIC): ");
            try {
                mood = Mood.valueOf(scanner.nextLine().trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println(">> Mood inválido. Intente de nuevo.");
            }
        }

        String style = readInput("Estilo (Genre)");

        // Read Playable status
        boolean playable = false;
        String playableInput = readInput("¿Es reproducible? (s/n)").toLowerCase();
        if (playableInput.equals("s") || playableInput.equals("y") || playableInput.equals("si")) {
            playable = true;
        }

        // Note: The PDF mentions asking for specific notes if playable. 
        // For this UI method, we return the basic Song object. 
        // Logic for adding notes could be handled here or inside the Song constructor.
        // Assuming a constructor: Song(id, title, artist, duration, style, playable, mood)
        
        Song newSong = new Song(id, title, artist, duration, style, playable, mood);
        
        return newSong;
    }

    // --- Helper Methods ---

    /**
     * Helper to read an integer safely.
     */
    private int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt + ": ");
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println(">> Por favor, introduzca un número entero válido.");
            }
        }
    }

    /**
     * Helper to truncate strings for table display.
     */
    private String truncate(String str, int width) {
        if (str == null) return "";
        if (str.length() > width) {
            return str.substring(0, width - 3) + "...";
        }
        return str;
    }
}