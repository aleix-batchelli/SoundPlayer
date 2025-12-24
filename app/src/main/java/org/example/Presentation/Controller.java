package org.example.Presentation;

import org.example.Business.Managers.AlbumManager;
import org.example.Business.Managers.LibraryManager;
import org.example.Business.Managers.PlaybackManager;
import org.example.Business.Model.Mood;
import org.example.Business.Model.Playlist;
import org.example.Business.Model.Song;

import java.util.List;

public class ViewController {
    private final UI ui;
    private final LibraryManager libraryManager;
    private final PlaybackManager playbackManager;
    private final AlbumManager albumManager;

    public ViewController() {
        this.ui = new UI();
        // In a real dependency injection scenario, these would be passed in.
        // Assuming default constructors for now that initialize their own DAOs.
        this.libraryManager = new LibraryManager();
        this.playbackManager = new PlaybackManager();
        this.albumManager = new AlbumManager(libraryManager);
    }

    public void start() {
        boolean running = true;
        while (running) {
            int option = ui.showMainMenu();
            switch (option) {
                case 0: // Q -> Exit
                    ui.printMessage("Saliendo del SoundPlayer...");
                    running = false;
                    break;
                case 1: // Gestionar canciones
                    handleSongsManagement();
                    break;
                case 2: // Gestionar playlists
                    handlePlaylistsManagement();
                    break;
                case 3: // Reproducir
                    handlePlayback();
                    break;
                case 4: // Generar álbum random
                    handleRandomAlbum();
                    break;
                case -1: // Input error (handled in UI, but good to have case)
                    break;
                default:
                    ui.printMessage("Opción no reconocida.");
            }
        }
    }

    // --- 4.2. Gestión de canciones [cite: 72] ---
    private void handleSongsManagement() {
        boolean back = false;
        while (!back) {
            ui.printMessage("\n--- Gestión de Canciones ---");
            ui.printMessage("1. Listar canciones");
            ui.printMessage("2. Añadir canción");
            ui.printMessage("3. Eliminar canción");
            ui.printMessage("0. Volver");
            
            String input = ui.readInput("Seleccione");
            
            switch (input) {
                case "1":
                    // 1. Listar canciones [cite: 74]
                    List<Song> songs = libraryManager.getAllSongs();
                    ui.displaySongList(songs);
                    break;
                case "2":
                    // 2. Añadir canción [cite: 78]
                    Song newSong = ui.readSongData();
                    // Optional: If playable, logic to add notes would go here
                    libraryManager.addSong(newSong);
                    ui.printMessage("Canción añadida correctamente.");
                    break;
                case "3":
                    // 3. Eliminar canción [cite: 86]
                    String id = ui.readInput("Introduzca ID de la canción a borrar");
                    libraryManager.deleteSong(id);
                    ui.printMessage("Operación finalizada (si existía el ID).");
                    break;
                case "0":
                    back = true;
                    break;
                default:
                    ui.printMessage("Opción incorrecta.");
            }
        }
    }

    // --- 4.3. Gestión de Playlists [cite: 89] ---
    private void handlePlaylistsManagement() {
        boolean back = false;
        while (!back) {
            ui.printMessage("\n--- Gestión de Playlists ---");
            ui.printMessage("1. Crear playlist");
            ui.printMessage("2. Añadir canción a playlist");
            ui.printMessage("3. Listar playlists");
            ui.printMessage("0. Volver");

            String input = ui.readInput("Seleccione");

            switch (input) {
                case "1":
                    // 1. Crear playlist [cite: 91]
                    String name = ui.readInput("Nombre de la Playlist");
                    String desc = ui.readInput("Descripción");
                    // Assuming Playlist constructor handles ID generation
                    Playlist newPlaylist = new Playlist(java.util.UUID.randomUUID().toString(), name, desc);
                    libraryManager.createPlaylist(newPlaylist);
                    ui.printMessage("Playlist creada.");
                    break;
                case "2":
                    // 2. Añadir canción a playlist [cite: 94]
                    // Show playlists first (simplified)
                    // In a full implementation, list playlists -> pick ID -> list songs -> pick ID
                    String pId = ui.readInput("ID de la Playlist");
                    String sId = ui.readInput("ID de la Canción");
                    try {
                        libraryManager.addSongToPlaylist(pId, sId);
                        ui.printMessage("Canción añadida.");
                    } catch (Exception e) {
                        ui.printMessage("Error: " + e.getMessage());
                    }
                    break;
                case "3":
                    // 4. Listar playlists [cite: 98]
                    // (Assuming libraryManager has getAllPlaylists - added to logic for completeness)
                    // Since LibraryManager in PlantUML didn't strictly have getAllPlaylists, 
                    // we assume access via DAOs wrapped in Manager.
                    // For now, let's assume we can fetch them or print details of one:
                    String searchId = ui.readInput("ID de Playlist a ver (o ENTER para saltar)");
                    if(!searchId.isEmpty()){
                         // Assuming a getPlaylist method exists or we implement list all logic
                         // ui.displayPlaylistDetails(fetchedPlaylist);
                         ui.printMessage("Funcionalidad pendiente de implementación en Manager.");
                    }
                    break;
                case "0":
                    back = true;
                    break;
                default:
                    ui.printMessage("Opción incorrecta.");
            }
        }
    }

    // --- 5. Reproducción [cite: 106] ---
    private void handlePlayback() {
        ui.printMessage("\n--- Reproducción ---");
        ui.printMessage("1. Reproducir Canción");
        ui.printMessage("2. Reproducir Playlist");
        ui.printMessage("0. Volver");
        
        String input = ui.readInput("Seleccione");
        
        switch (input) {
            case "1":
                // 1. Reproducir una canción [cite: 108]
                String sId = ui.readInput("ID de la Canción");
                Song s = libraryManager.getSongById(sId); // Helper needed in Manager
                if (s != null && s.isPlayable()) {
                   playbackManager.playSong(s);
                } else {
                   ui.printMessage("Canción no encontrada o no es PLAYABLE[cite: 179].");
                }
                break;
            case "2":
                // 2. Reproducir una playlist [cite: 114]
                String pId = ui.readInput("ID de la Playlist");
                // Need logic to fetch playlist object
                // Playlist p = libraryManager.getPlaylistById(pId);
                // playbackManager.playPlaylist(p);
                ui.printMessage("Iniciando reproducción de playlist (Simulado)...");
                break;
            case "0":
                break;
        }
    }

    // --- 6. Álbum Random [cite: 120] ---
    private void handleRandomAlbum() {
        ui.printMessage("\n--- Generar Álbum Aleatorio ---");
        
        // 1. El usuario elige Mood [cite: 123]
        Mood mood = null;
        while (mood == null) {
            String mStr = ui.readInput("Mood (HAPPY, SAD, RELAX, ENERGETIC)").toUpperCase();
            try {
                mood = Mood.valueOf(mStr);
            } catch (Exception e) {
                ui.printMessage("Mood inválido.");
            }
        }
        
        // 1. El usuario elige Duración [cite: 124]
        int minutes = 0;
        try {
            minutes = Integer.parseInt(ui.readInput("Duración en minutos"));
        } catch (NumberFormatException e) {
            ui.printMessage("Duración inválida.");
            return;
        }

        // 2. Generar [cite: 125]
        // Note: Logic requires fetching all songs of that mood first, 
        // usually handled inside AlbumManager or by passing the song list.
        // Based on PlantUML: generateRandomAlbum(mood, maxDuration)
        // However, AlbumManager needs access to the Library to know the songs.
        // We might pass the library list here:
        
        // Playlist randomP = albumManager.generateRandomAlbum(mood, minutes * 60, libraryManager.getAllSongs());
        
        // 3. Guardar [cite: 133]
        // libraryManager.createPlaylist(randomP);
        
        ui.printMessage("Álbum generado y guardado (Lógica simulada).");
    }
}