package org.example.Persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.example.Business.Model.Playlist;
import org.example.CustomExceptions.EmptyJsonFileException;
import org.example.CustomExceptions.NoPlaylistsException;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonPlaylistsDAO implements PlaylistsDAO {

    private final String FILE_PATH = "files/playlists.json"; // 
    private final Gson gson;

    public JsonPlaylistsDAO() throws IOException {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        File file = new File(FILE_PATH);
        file.createNewFile();
    }

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
            return new ArrayList<>();
        }
    }

    @Override
    public void saveAll(List<Playlist> playlists) throws IOException {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.newBuilder().setPrettyPrinting().create().toJson(playlists, writer);
        } catch (IOException e) {
            throw new IOException("Error saving playlists to JSON file.", e);
        }
    }
}