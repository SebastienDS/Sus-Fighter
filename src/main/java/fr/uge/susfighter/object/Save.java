package fr.uge.susfighter.object;

import fr.uge.susfighter.mvc.GameController;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Save {

    private static final Path SAVE_PATH = Path.of("src",  "main", "resources", "fr", "uge", "susfighter", "object", "save", "save.txt");
    private static Data DATA;

    private static record Data(Set<String> characterUnlock) implements Serializable {
        public Data {
            Objects.requireNonNull(characterUnlock);
        }

        public Data() {
            this(new HashSet<>());
            try {
                var path = Path.of(Objects.requireNonNull(GameController.class.getResource("images/character/default")).toURI());
                Files.walk(path, 2)
                        .filter(Files::isDirectory)
                        .forEach(f -> characterUnlock.add(f.getFileName().toString()));


                // TODO: remove me :(
                path = Path.of(Objects.requireNonNull(GameController.class.getResource("images/character/extension")).toURI());
                Files.walk(path, 2)
                        .filter(Files::isDirectory)
                        .forEach(f -> characterUnlock.add(f.getFileName().toString()));
            }
            catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    public static void unlockCharacter(String name) {
        var data = getData();
        data.characterUnlock.add(name);
        saveData(data);
    }

    public static boolean isCharacterUnlock(String name) {
        var data = getData();
        return data.characterUnlock.contains(name);
    }

    public static Set<String> getCharactersUnlock() {
        var data = getData();
        return data.characterUnlock;
    }

    private static Data getData() {
        if (DATA == null) DATA = loadData();
        return DATA;
    }

    private static Data loadData() {
        try (var reader = Files.newInputStream(SAVE_PATH);
             var objectInputStream = new ObjectInputStream(reader)) {
            return (Data)objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new Data();
        }
    }

    private static void saveData(Data data) {
        try (var input = Files.newOutputStream(SAVE_PATH);
             var objectOutputStream = new ObjectOutputStream(input)) {
            objectOutputStream.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}