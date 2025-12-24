package org.example.Persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.example.Business.Model.Song;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SongJSON implements SongsDAO {

    private final String FILE_PATH = "songs.json"; // 
    private final Gson gson;

    public SongJSON() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    public List<Song> loadAll() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(file)) {
            Type listType = new TypeToken<ArrayList<Song>>() {}.getType();
            List<Song> songs = gson.fromJson(reader, listType);
            return songs != null ? songs : new ArrayList<>();
        } catch (IOException e) {
            System.err.println(">> Error cargando songs.json: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public void saveAll(List<Song> songs) {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(songs, writer);
        } catch (IOException e) {
            System.err.println(">> Error guardando songs.json: " + e.getMessage());
        }
    }
}