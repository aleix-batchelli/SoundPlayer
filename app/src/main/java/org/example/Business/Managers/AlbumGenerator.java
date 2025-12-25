package org.example.Business.Managers;

import org.example.Business.Model.Mood;
import org.example.Business.Model.Playlist;
import org.example.Business.Model.Song;
import org.example.CustomExceptions.EmptyJsonFileException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Manages the logic for generating random albums based on specific criteria.
 * Logic defined in Source[cite: 154].
 */
public class AlbumGenerator {

    private final LibraryManager libraryManager;

    /**
     * Constructor requires LibraryManager to access the song library.
     * Corresponds to the relationship: AlbumManager ..> LibraryManager : uses
     */
    public AlbumGenerator(LibraryManager libraryManager) {
        this.libraryManager = libraryManager;
    }

    /**
     * Generates a random playlist (album) for a specific mood and target duration.
     *
     * @param mood The target mood (HAPPY, SAD, etc.)[cite: 123].
     * @param maxDurationSeconds The target duration in seconds[cite: 124].
     * @return A new Playlist object containing the random selection.
     */
    public Playlist generateRandomAlbum(Mood mood, int maxDurationSeconds) throws EmptyJsonFileException, IOException {
        // 1. Filter songs: Must match Mood and be Playable 
        List<Song> candidates = libraryManager.getAllSongs().stream()
                .filter(s -> s.getMood() == mood)
                .filter(Song::isPlayable) // "Preferiblemente, solo reproducibles" [cite: 127]
                .collect(Collectors.toList());

        // 2. Shuffle the candidates to ensure randomness [cite: 137]
        Collections.shuffle(candidates);

        // 3. Select songs to fill the duration
        List<Song> selectedSongs = new ArrayList<>();
        int currentDuration = 0;

        // Logic: Keep adding while we are under the target duration.
        // This allows the last song to exceed the limit slightly (Greedy algorithm) [cite: 131, 138]
        for (Song s : candidates) {
            if (currentDuration >= maxDurationSeconds) {
                break;
            }
            selectedSongs.add(s);
            currentDuration += s.getDurationSeconds();
        }

        // 4. Create the Playlist object
        // "La playlist generada debe... ser visible en el listado normal" [cite: 134]
        int id = (int) (System.currentTimeMillis() % 100000); // Simple unique ID based on timestamp
        String name = "Random " + mood + " Album";
        String description = "Generated album (" + (maxDurationSeconds / 60) + " min target)";

        Playlist randomPlaylist = new Playlist(id, name, description);

        // Add the selected songs to the playlist object
        for (Song s : selectedSongs) {
            randomPlaylist.addSong(s);
        }

        // 5. Requirement: Save to playlists.json
        // Since AlbumManager depends on LibraryManager, we delegate the saving to it.
        // "La playlist generada debe... Guardarse en playlists.json" 
        libraryManager.createPlaylist(randomPlaylist);

        return randomPlaylist;
    }
}