package org.example.Persistence;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.Business.Model.Playlist;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PlaylistJSON implements PlaylistsDAO {

    private final String FILE_PATH = "playlists.json"; // 
    private final Gson gson = new Gson();

    @Override
    public List<Playlist> loadAll() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(file)) {
            Type listType = new TypeToken<ArrayList<Playlist>>() {}.getType();
            List<Playlist> playlists = gson.fromJson(reader, listType);
            return playlists != null ? playlists : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Error leyendo playlists: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public void saveAll(List<Playlist> playlists) {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.newBuilder().setPrettyPrinting().create().toJson(playlists, writer);
        } catch (IOException e) {
            System.err.println("Error guardando playlists: " + e.getMessage());
        }
    }
}