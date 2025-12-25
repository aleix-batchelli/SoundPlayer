package org.example.Persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.example.Business.Model.Song;
import org.example.CustomExceptions.EmptyJsonFileException;
import org.example.CustomExceptions.NoSongsException;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonSongsDAO implements SongsDAO {

    private final String FILE_PATH = "files/songs.json";
    private final Gson gson;

    public JsonSongsDAO() throws IOException {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        File file = new File(FILE_PATH);
        file.createNewFile();
    }


    @Override
    public List<Song> loadAll() throws EmptyJsonFileException {
        File file = new File(FILE_PATH);

        try (Reader reader = new FileReader(file)) {
            Type listType = new TypeToken<ArrayList<Song>>() {}.getType();
            List<Song> songs = gson.fromJson(reader, listType);
            return songs != null ? songs : new ArrayList<>();
        } catch (IOException e) {
            throw new NoSongsException();
        }
    }

    @Override
    public void saveAll(List<Song> songs) throws IOException{
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(songs, writer);
        } catch (IOException e) {
            throw new IOException("Error saving songs to JSON file.", e);
        }
    }
}