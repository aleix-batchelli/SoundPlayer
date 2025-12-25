package org.example.Presentation;

import org.example.Business.Managers.AlbumGenerator;
import org.example.Business.Managers.LibraryManager;
import org.example.Business.Managers.PlaybackManager;
import org.example.Business.Model.Mood;
import org.example.Business.Model.Playlist;
import org.example.Business.Model.Song;
import org.example.CustomExceptions.InvalidInputException;

import java.io.IOException;
import java.util.List;

public class Controller {
    private ConsoleView ui;
    private LibraryManager libraryManager;
    private PlaybackManager playbackManager;
    private AlbumGenerator albumManager;

    public Controller() throws IOException {
        this.ui = new ConsoleView();
        this.libraryManager = new LibraryManager();
        this.playbackManager = new PlaybackManager();
        this.albumManager = new AlbumGenerator(libraryManager);
    }

    public void start() {
        boolean running = true;
        while (running) {
            int option = ui.showMainMenu();
            switch (option) {
                case 0: // Exit
                    ui.printMessage("Saliendo del SoundPlayer...");
                    running = false;
                    break;
                case 1: // Songs
                    handleSongsManagement();
                    break;
                case 2: // Playlists
                    handlePlaylistsManagement();
                    break;
                case 3: // Playback
                    handlePlayback();
                    break;
                case 4: // Random Album
                    handleRandomAlbum();
                    break;
                default:
                    break;
            }
        }
    }

    // --- 4.2. Gestión de canciones ---
    private void handleSongsManagement() {
        boolean back = false;
        while (!back) {
            ui.printMessage("\n--- Gestión de Canciones ---");
            ui.printMessage("1. AÑADIR nueva canción");
            ui.printMessage("2. EDITAR canción existente");
            ui.printMessage("3. ELIMINAR canción");
            ui.printMessage("4. LISTAR todas las canciones"); // NEW OPTION
            ui.printMessage("0. Volver");

            int action = 0;
            try {
                action = ui.askForInt("Seleccione una opción");
            } catch (Exception e) {
                ui.printError(e.getMessage());
                continue;
            }

            if (action == 0) {
                back = true;
                continue;
            }

            try {
                // CASE 1: ADD
                if (action == 1) {
                    Song newSong = ui.readSongData();
                    libraryManager.addSong(newSong);
                    ui.printMessage("Canción añadida correctamente.");
                } 
                
                // CASE 4: LIST ONLY (NEW)
                else if (action == 4) {
                    List<Song> songs = libraryManager.getAllSongs();
                    if (songs.isEmpty()) {
                        ui.printMessage("(!) No hay canciones registradas.");
                    } else {
                        ui.printMessage("\n--- Listado Completo de Canciones ---");
                        for (int i = 0; i < songs.size(); i++) {
                            System.out.println((i + 1) + ". " + songs.get(i).toString());
                        }
                    }
                    // Loop continues automatically, showing menu again
                }

                // CASE 2 & 3: EDIT / DELETE
                else if (action == 2 || action == 3) {
                    
                    List<Song> songs = libraryManager.getAllSongs();
                    if (songs.isEmpty()) {
                        ui.printMessage("(!) No hay canciones para modificar.");
                        continue;
                    }

                    ui.printMessage("\n--- Seleccione una canción ---");
                    for (int i = 0; i < songs.size(); i++) {
                        System.out.println((i + 1) + ". " + songs.get(i).toString());
                    }
                    ui.printMessage("0. Cancelar");

                    int index = -1;
                    try {
                        // Assuming askForIntInRange is implemented in UI, otherwise use askForInt
                        index = ui.askForIntInRange("Introduzca el NÚMERO de la lista", 0, songs.size());
                        if (index == 0) continue; 
                    } catch (InvalidInputException e) {
                        ui.printError(e.getMessage());
                        continue;
                    }

                    Song selectedSong = songs.get(index - 1);

                    if (action == 2) {
                        // EDIT
                        ui.printMessage("Editando: " + selectedSong.getTitle());
                        Song newData = ui.editSongData(selectedSong);
                        
                        selectedSong.setTitle(newData.getTitle());
                        selectedSong.setArtist(newData.getArtist());
                        selectedSong.setDurationSeconds(newData.getDurationSeconds());
                        selectedSong.setMood(newData.getMood());
                        selectedSong.setPlayable(newData.isPlayable());
                        
                        libraryManager.updateSong(selectedSong); 
                        ui.printMessage("Canción editada correctamente.");
                        
                    } else {
                        // DELETE
                        libraryManager.deleteSong(selectedSong.getId());
                        ui.printMessage("Canción eliminada: " + selectedSong.getTitle());
                    }
                } else {
                    ui.printMessage("Opción incorrecta.");
                }
            } catch (Exception e) {
                ui.printError(e.getMessage());
            }
        }
    }

    // --- 4.3. Gestión de Playlists ---
    private void handlePlaylistsManagement() {
        boolean back = false;
        while (!back) {
            ui.printMessage("\n--- Gestión de Playlists ---");
            ui.printMessage("1. AÑADIR nueva playlist");
            ui.printMessage("2. EDITAR playlist (Añadir/Eliminar canciones)");
            ui.printMessage("3. ELIMINAR playlist");
            ui.printMessage("4. LISTAR todas las playlists"); // NEW OPTION
            ui.printMessage("0. Volver");

            int action = 0;
            try {
                action = ui.askForInt("Seleccione una opción");
            } catch (Exception e) {
                ui.printError(e.getMessage());
                continue;
            }

            if (action == 0) {
                back = true;
                continue;
            }

            try {
                // CASE 1: CREATE NEW PLAYLIST
                if (action == 1) {
                    String name = ui.readInput("Nombre de la Playlist");
                    String desc = ui.readInput("Descripción");
                    int id = libraryManager.getAllPlaylists().size() + 1; 
                    Playlist newPlaylist = new Playlist(id, name, desc);
                    libraryManager.createPlaylist(newPlaylist);
                    ui.printMessage("Playlist creada.");
                }
                
                // CASE 4: LIST ONLY (NEW)
                else if (action == 4) {
                    List<Playlist> playlists = libraryManager.getAllPlaylists();
                    if (playlists.isEmpty()) {
                        ui.printMessage("(!) No hay playlists registradas.");
                    } else {
                        ui.printMessage("\n--- Listado Completo de Playlists ---");
                        for (int i = 0; i < playlists.size(); i++) {
                            // Using standard display logic
                            System.out.println((i + 1) + ". " + playlists.get(i).toString());
                        }
                    }
                }

                // CASE 2 & 3: EDIT OR DELETE EXISTING PLAYLIST
                else if (action == 2 || action == 3) {
                    
                    List<Playlist> playlists = libraryManager.getAllPlaylists();
                    if (playlists.isEmpty()) {
                        ui.printMessage("(!) No hay playlists disponibles.");
                        continue;
                    }
                    
                    ui.printMessage("\n--- Seleccione una Playlist ---");
                    for (int i = 0; i < playlists.size(); i++) {
                        System.out.println((i + 1) + ". " + playlists.get(i).getName() + 
                                " (" + playlists.get(i).getSongIds().size() + " canciones)");
                    }

                    int pIndex = ui.askForInt("Introduzca el NÚMERO de la playlist");
                    
                    if (pIndex < 1 || pIndex > playlists.size()) {
                        ui.printError("Número inválido.");
                        continue;
                    }

                    Playlist selectedPlaylist = playlists.get(pIndex - 1);

                    if (action == 2) {
                        ui.printMessage("\nEditando: " + selectedPlaylist.getName());
                        ui.printMessage("1. AÑADIR canción");
                        ui.printMessage("2. ELIMINAR canción");
                        int subAction = ui.askForInt("Opción");

                        if (subAction == 1) {
                            // ADD SONG logic
                            List<Song> allSongs = libraryManager.getAllSongs();
                            if (allSongs.isEmpty()) {
                                ui.printMessage("No hay canciones en la biblioteca.");
                                continue;
                            }
                            ui.printMessage("--- Biblioteca General ---");
                            for (int i = 0; i < allSongs.size(); i++) {
                                System.out.println((i + 1) + ". " + allSongs.get(i).toString());
                            }
                            int sIndex = ui.askForInt("Número de la canción a AÑADIR");
                            if (sIndex < 1 || sIndex > allSongs.size()) {
                                ui.printError("Número inválido.");
                                continue;
                            }
                            Song selectedSong = allSongs.get(sIndex - 1);
                            libraryManager.addSongToPlaylist(selectedPlaylist.getId(), selectedSong.getId());
                            ui.printMessage("Canción añadida correctamente.");

                        } else if (subAction == 2) {
                            // REMOVE SONG logic
                            List<Integer> currentSongIds = selectedPlaylist.getSongIds();
                            if (currentSongIds.isEmpty()) {
                                ui.printMessage("Esta playlist está vacía.");
                                continue;
                            }
                            ui.printMessage("--- Canciones en esta Playlist ---");
                            for (int i = 0; i < currentSongIds.size(); i++) {
                                int sId = currentSongIds.get(i);
                                Song s = libraryManager.getSongById(sId);
                                String sName = (s != null) ? s.getTitle() : "[ID desconocido]";
                                System.out.println((i + 1) + ". " + sName);
                            }
                            int removeIndex = ui.askForInt("Número de la canción a ELIMINAR");
                            if (removeIndex < 1 || removeIndex > currentSongIds.size()) {
                                ui.printError("Número inválido.");
                                continue;
                            }
                            int sIdToRemove = currentSongIds.get(removeIndex - 1);
                            libraryManager.removeSongFromPlaylist(selectedPlaylist.getId(), sIdToRemove);
                            ui.printMessage("Canción eliminada de la playlist.");
                        }

                    } else {
                        // DELETE PLAYLIST
                        libraryManager.deletePlaylist(selectedPlaylist.getId());
                        ui.printMessage("Playlist eliminada correctamente.");
                    }
                }
            } catch (Exception e) {
                ui.printError(e.getMessage());
            }
        }
    }

    // --- 5. Reproducción (Unchanged) ---
    private void handlePlayback() {
        ui.printMessage("\n--- Reproducción ---");
        ui.printMessage("1. Reproducir Canción");
        ui.printMessage("2. Reproducir Playlist");
        ui.printMessage("0. Volver");
        
        String input = ui.readInput("Seleccione");
        
        try {
            switch (input) {
                case "1":
                    List<Song> songs = libraryManager.getAllSongs();
                    if (songs.isEmpty()) {
                        ui.printMessage("Biblioteca vacía.");
                        return;
                    }
                    for (int i = 0; i < songs.size(); i++) {
                        System.out.println((i + 1) + ". " + songs.get(i).toString());
                    }
                    int sIndex = ui.askForInt("Número de la canción a reproducir");
                    if (sIndex > 0 && sIndex <= songs.size()) {
                        Song s = songs.get(sIndex - 1);
                        if (s.isPlayable()) {
                            playbackManager.playSong(s);
                        } else {
                            ui.printMessage("Esta canción no es reproducible.");
                        }
                    } else {
                        ui.printError("Número inválido.");
                    }
                    break;

                case "2":
                    List<Playlist> playlists = libraryManager.getAllPlaylists();
                    if (playlists.isEmpty()) {
                        ui.printMessage("No hay playlists.");
                        return;
                    }
                    for (int i = 0; i < playlists.size(); i++) {
                        System.out.println((i + 1) + ". " + playlists.get(i).getName());
                    }
                    int pIndex = ui.askForInt("Número de la playlist a reproducir");
                    if (pIndex > 0 && pIndex <= playlists.size()) {
                        Playlist p = playlists.get(pIndex - 1);
                        playbackManager.playPlaylist(p, libraryManager);
                    } else {
                        ui.printError("Número inválido.");
                    }
                    break;
                case "0":
                    break;
            }
        } catch (Exception e) {
            ui.printError(e.getMessage());
        }
    }

    // --- 6. Random Album (Unchanged) ---
    private void handleRandomAlbum() {
        ui.printMessage("\n--- Generar Álbum Aleatorio ---");
        
        Mood mood = null;
        while (mood == null) {
            String mStr = ui.readInput("Mood (HAPPY, SAD, RELAX, ENERGETIC)").toUpperCase();
            try {
                mood = Mood.valueOf(mStr);
            } catch (Exception e) {
                ui.printMessage("Mood inválido.");
            }
        }
        
        try {
            int minutes = ui.askForInt("Duración en minutos");
            AlbumGenerator generator = new AlbumGenerator(libraryManager);
            Playlist p = generator.generateRandomAlbum(mood, minutes * 60);
            ui.printMessage("Álbum '" + p.getName() + "' generado con éxito.");
        } catch (Exception e) {
            ui.printError("Error: " + e.getMessage());
        }
    }
}