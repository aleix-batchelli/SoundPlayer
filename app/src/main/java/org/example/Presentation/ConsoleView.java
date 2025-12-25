package org.example.Presentation;

import org.example.Business.Model.Mood;
import org.example.Business.Model.Playlist;
import org.example.Business.Model.Song;
import org.example.CustomExceptions.InvalidInputException;
import java.util.List;
import java.util.Scanner;

public class ConsoleView {
    private final Scanner scanner;

    public ConsoleView() {
        this.scanner = new Scanner(System.in);
    }

    public void printMessage(String msg) {
        System.out.println(msg);
    }

    public String readInput(String prompt) {
        System.out.print(prompt + ": ");
        return scanner.nextLine().trim();
    }

    public int askForInt(String prompt) throws InvalidInputException {
        for (int attempts = 0; attempts < 3; attempts++) {
            System.out.println(prompt);
            try {
                String input = scanner.nextLine();
                return Integer.parseInt(input); // Success: returns immediately
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }

        throw new InvalidInputException("Maximum attempts (3) reached for input.");
    }

    public int askForIntInRange (String prompt, int min, int max) throws InvalidInputException {
        for (int attempts = 0; attempts < 3; attempts++) {
            System.out.println(prompt + " (between " + min + " and " + max + "): ");
            try {
                String input = scanner.nextLine();
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) {
                    return value;
                } else {
                    System.out.println("Input out of range. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }

        throw new InvalidInputException("Maximum attempts (3) reached for input.");

    }

    /**
     * Displays the main menu and returns the user's choice as an integer.
     * Maps "0" to 0 for exit.
     */
    public int showMainMenu() {
        try {
            System.out.println("\n--- SoundPlayer ---");
            System.out.println("1. Gestionar canciones");
            System.out.println("2. Gestionar playlists");
            System.out.println("3. Reproducir");
            System.out.println("4. Generar álbum random por mood");
            System.out.println("0. Salir");
            int input = askForIntInRange("Seleccione una opción: ", 0, 4);
            if (input == 0) {
                return 0; 
            }
            return input;

        } catch (InvalidInputException e) {
            printError(e.getMessage());
            return -1; 
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

        for (Song s : songs) {
            System.out.println(s);
        }
    }

    /**
     * Displays details of a specific playlist.
     * Updated to accept 'library' because Playlist needs it to calculate duration/counts.
     */
    public void displayPlaylistDetails(Playlist p, List<Song> library) {
        if (p == null) {
            printError("Playlist no encontrada.");
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
        int id = (int)System.currentTimeMillis() % 100000; // Simple unique ID based on time
        
        String title = readInput("Título");
        while(title.isEmpty()) title = readInput("Título (no puede estar vacío)");
        
        String artist = readInput("Artista");
        int duration = 0;
        try {
            duration = askForInt("Duración (segundos)");
        } catch (InvalidInputException e) {
            printError(e.getMessage());
            return null;
        }

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
        boolean playable = false;
        String playableInput = readInput("¿Es reproducible? (s/n)").toLowerCase();
        if (playableInput.equals("s") || playableInput.equals("y") || playableInput.equals("si")) {
            playable = true;
        }
        
        Song newSong = new Song(id, title, artist, duration, style, playable, mood);
        
        return newSong;
    }

    /**
     * Asks for song data to update. 
     * Shows the current value in brackets [Value].
     * If the user presses ENTER (empty input), the original value is kept.
     */
    public Song editSongData(Song current) {
        System.out.println("\n--- Editar Canción (Presione ENTER para mantener el valor actual) ---");

        // 1. TITLE
        String titleInput = readInput("Título [" + current.getTitle() + "]");
        String newTitle = titleInput.isEmpty() ? current.getTitle() : titleInput;

        // 2. ARTIST
        String artistInput = readInput("Artista [" + current.getArtist() + "]");
        String newArtist = artistInput.isEmpty() ? current.getArtist() : artistInput;

        // 3. DURATION (Int parsing logic)
        int newDuration = current.getDurationSeconds();
        String durInput = readInput("Duración [" + newDuration + "]");
        if (!durInput.isEmpty()) {
            try {
                newDuration = Integer.parseInt(durInput);
            } catch (NumberFormatException e) {
                printError("Número inválido. Se mantendrá el valor original.");
            }
        }

        // 4. STYLE
        String styleInput = readInput("Estilo [" + current.getStyle() + "]");
        String newStyle = styleInput.isEmpty() ? current.getStyle() : styleInput;

        // 5. PLAYABLE (Boolean parsing logic)
        boolean newPlayable = current.isPlayable();
        String playInput = readInput("Es reproducible? (y/n) [" + (newPlayable ? "y" : "n") + "]");
        if (!playInput.isEmpty()) {
            newPlayable = playInput.equalsIgnoreCase("y") || playInput.equalsIgnoreCase("true");
        }

        // 6. MOOD (Enum parsing logic)
        Mood newMood = current.getMood();
        String moodInput = readInput("Mood [" + newMood + "]");
        if (!moodInput.isEmpty()) {
            try {
                newMood = Mood.valueOf(moodInput.toUpperCase());
            } catch (IllegalArgumentException e) {
                printError("Mood desconocido. Se mantendrá el valor original.");
            }
        }

        // Return a new Song object with the ID from the original and the updated fields
        // (This object will be used by the Controller to update the actual list)
        return new Song(
                current.getId(), 
                newTitle, 
                newArtist, 
                newDuration, 
                newStyle, 
                newPlayable, 
                newMood
        );
    }

    /**
     * Prints a formatted error message to stderr with a consistent prefix.
     */
    public void printError(String msg) {
        if (msg == null) msg = "";
        System.err.println(">> Error: " + msg);
    }
}