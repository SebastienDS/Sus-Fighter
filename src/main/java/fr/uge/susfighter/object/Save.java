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

    private static final Path SAVE_PATH = Path.of("src", "main", "resources", "fr", "uge", "susfighter", "object", "save", "save.txt");
    private static Data DATA;

    private static class Data implements Serializable {
        Set<String> characterUnlock = new HashSet<>();
        int campaignLevel = 0;
        int campaignStep = 0;

        public Data() {
            try {
                var path = Path.of(Objects.requireNonNull(GameController.class.getResource("images/character/default")).toURI());
                Files.walk(path, 2)
                        .filter(Files::isDirectory)
                        .sorted()
                        .forEach(f -> characterUnlock.add(f.getFileName().toString()));
            }
            catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Unlock a specific character
     * @param name character to unlock
     */
    public static void unlockCharacter(String name) {
        var data = getData();
        data.characterUnlock.add(name);
        saveData(data);
    }

    /**
     * Check if a character is unlocked
     * @param name character
     * @return true if the character is unlocked
     */
    public static boolean isCharacterUnlock(String name) {
        var data = getData();
        return data.characterUnlock.contains(name);
    }

    /**
     * Get all the unlocked characters
     * @return unlocked characters
     */
    public static Set<String> getCharactersUnlock() {
        var data = getData();
        return data.characterUnlock;
    }

    /**
     * Get the actual level of the campaign mode
     * @return level
     */
    public static int getCampaignLevel() {
        var data = getData();
        return data.campaignLevel;
    }

    /**
     * Get the actual step of the campaign mode
     * @return step
     */
    public static int getCampaignStep() {
        var data = getData();
        return data.campaignStep;
    }

    /**
     * Set the level of the campaign mode
     * @param level level
     */
    public static void setCampaignLevel(int level) {
        var data = getData();
        data.campaignLevel = level;
        saveData(data);
    }

    /**
     * Set the step of the campaign mode
     * @param step step
     */
    public static void setCampaignStep(int step) {
        var data = getData();
        data.campaignStep = step;
        saveData(data);
    }

    /**
     * Get the saved data of the game
     * @return Data
     */
    private static Data getData() {
        if (DATA == null) DATA = loadData();
        return DATA;
    }

    /**
     * Load the saved data of the game for the Save file
     * @return Data
     */
    private static Data loadData() {
        try (var reader = Files.newInputStream(SAVE_PATH);
             var objectInputStream = new ObjectInputStream(reader)) {
            return (Data)objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new Data();
        }
    }

    /**
     * Save the data of the game to the Save file
     * @param data data
     */
    private static void saveData(Data data) {
        try (var input = Files.newOutputStream(SAVE_PATH);
             var objectOutputStream = new ObjectOutputStream(input)) {
            objectOutputStream.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}