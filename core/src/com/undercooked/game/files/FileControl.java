package com.undercooked.game.files;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.*;
import com.undercooked.game.util.Constants;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;

/**
 * This static class contains all methods relating to file handling
 * for the game.
 */
public class FileControl {

    /**
     * Returns the path to the data folder, ending with "/".
     * @return {@link String} : The path to the data folder.
     */
    public static String getDataPath() {
        return getDataPath("/");
    }

    /**
     * Returns the path to the data folder, ending with the string provided.
     * @param endsWith {@link String} : The {@link String} to end the path with.
     * @return {@link String} : The path to the data folder.
     */
    public static String getDataPath(String endsWith) {
        // ? OLD CODE, should remove soon
        if (System.getProperty("os.name").equals("Linux")) {
            // return "\\data\\" + Constants.DATA_FILE + endsWith;
            return System.getProperty("user.dir") + "/data/" + Constants.DATA_FILE + endsWith;
            // return "/data/" + Constants.DATA_FILE + endsWith;
        } else if (System.getProperty("os.name").startsWith("Windows")) {
            return System.getenv("APPDATA") + "/" + Constants.DATA_FILE + endsWith;
        }
        return "/data/" + Constants.DATA_FILE + endsWith;
    }

    /**
     * Format the directory path so that it ends with a "/"
     * @param dir {@link String} : The directory path.
     * @return {@link String} : The formatted path.
     */
    public static String formatDir(String dir) {
        return dir + (dir.endsWith("\\") || dir.endsWith("/") ? "" : "/");
    }

    /**
     * Combines the path to a directory and the name of a file together
     * correctly.
     * @param dir {@link String} : The path to the directory.
     * @param fileName {@link String} : The name of the file.
     * @return {@link String} : The directory path and file name combined.
     */
    public static String dirAndName(String dir, String fileName) {
        return formatDir(dir)
                + fileName;
    }

    /**
     * Saves a {@link String} to a text file.
     * @param dir {@link String} : The directory path to save to.
     * @param fileName {@link String} : The file to store the data in.
     * @param data {@link String} : The data to store.
     */
    public static void saveToFile(String dir, String fileName, String data) {
        File directory = new File(dir);
        File file = new File(dirAndName(dir, fileName));
        try {
            if (!directory.exists() && !directory.mkdirs()) {
                throw new RuntimeException("Could not create directory: " + directory.getAbsolutePath());
            }
            if (!file.exists() && !file.createNewFile()) {
                throw new RuntimeException("Could not create file: " + file.getAbsolutePath());
            }
            Files.write(file.toPath(), Collections.singleton(data), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves a {@link String} to a file in the data folder.
     * @param fileName {@link String} : The file to store the data in.
     * @param data {@link String} : The data to store.
     */
    public static void saveData(String fileName, String data) {
        saveToFile(getDataPath(), fileName, data);
    }

    /**
     * Loads a file's data as a {@link String}.
     * @param dir {@link String} : The directory path to load from.
     * @param fileName {@link String} : The name of the file to load from.
     * @param internal {@code boolean} : Whether the file is internal or external.
     * @return {@link String} : The file's data, or {@code null} if it couldn't load.
     */
    public static String loadFile(String dir, String fileName, boolean internal) {
        FileHandle directory;
        if (internal) {
            directory = Gdx.files.internal(dir);
        } else {
            directory = new FileHandle(dir);
        }
        // If directory isn't a directory, or it doesn't exist, then
        // create the directory.
        if (!directory.isDirectory() && !internal && !
                directory.file().mkdirs()) {
            throw new RuntimeException("Could not create directory: " + directory.file().getAbsolutePath());
        }
        FileHandle file;
        if (internal) {
            file = Gdx.files.internal(dirAndName(dir, fileName));
        } else {
            file = new FileHandle(dirAndName(dir, fileName));
        }
        // If file isn't a file, or it doesn't exist, then create the
        // file using the default data.
        if (!file.file().isFile() && !internal) {
            System.out.println("File doesn't exist: " + fileName);
            try {
                if (!file.file().createNewFile()) {
                    throw new RuntimeException("Could not create file: " + file.file().getAbsolutePath());
                }
                saveToFile(dir, fileName, "{}");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Otherwise, load the Json file.
        String fileData = "";
        try {
            fileData = file.readString();
        } catch (GdxRuntimeException e) {
            e.printStackTrace();
        }
        return fileData;
    }

    /**
     * Loads an internal file's data as a {@link String}.
     * @param dir {@link String} : The directory path to load from.
     * @param fileName {@link String} : The name of the file to load from.
     * @return {@link String} : The file's data, or {@code null} if it couldn't load.
     */
    public static String loadFile(String dir, String fileName) {
        return loadFile(dir, fileName, true);
    }

    /**
     * Loads a file from the data folder as a {@link String}.
     * @param fileName {@link String} : The name of the file to load from.
     * @return {@link String} : The file's data, or {@code null} if it couldn't load.
     */
    public static String loadData(String fileName) {
        return loadFile(getDataPath(), fileName, false);
    }

    /**
     * Loads a file's data as a {@link JsonValue}.
     * @param dir {@link String} : The directory path to load from.
     * @param fileName {@link String} : The name of the file to load from.
     * @param internal {@code boolean} : Whether the file is internal or external.
     * @return {@link JsonValue} : The file's data, or {@code null} if it couldn't load.
     */
    public static JsonValue loadJsonFile(String dir, String fileName, boolean internal) {
        // If file name does not end with ".json", then add it
        if (!fileName.endsWith(".json")) {
            fileName += ".json";
        }
        FileHandle directory;
        dir = formatDir(dir);
        if (internal) {
            directory = Gdx.files.internal(dir);
        } else {
            directory = new FileHandle(dir);
        }
        // If directory isn't a directory, or it doesn't exist, then
        // return nothing.
        if (!directory.isDirectory() && !internal) {
            System.out.println("External directory doesn't exist: " + dir);
            return null;
        }
        FileHandle file;
        if (internal) {
            file = Gdx.files.internal(dirAndName(dir, fileName));
        } else {
            file = new FileHandle(dirAndName(dir, fileName));
        }
        // If file isn't a file, or it doesn't exist, then return
        // nothing.
        if (!file.file().isFile() && !internal) {
            System.out.println("External file doesn't exist: " + dirAndName(dir, fileName));
            return null;
        }
        // Otherwise, load the Json file.
        String jsonData = loadFile(dir, fileName, internal);
        JsonReader json = new JsonReader();
        JsonValue fileData;
        try {
            fileData = json.parse(jsonData);
        } catch (GdxRuntimeException e) {
            e.printStackTrace();
            return null;
        }
        return fileData;
    }

    /**
     * Loads a file from the data folder as a {@link JsonValue}.
     * @param fileName {@link String} : The name of the file to load from.
     * @return {@link JsonValue} : The file's data, or {@code null} if it couldn't load.
     */
    public static JsonValue loadJsonData(String fileName) {
        System.out.println("Loading json from " + dirAndName(getDataPath(), fileName));
        return loadJsonFile(getDataPath(), fileName, false);
    }

    /**
     * Get a {@link FileHandle} from a location, depending on if it's
     * internal or external.
     * @param path {@link String} : The path to the file or directory.
     * @param internal {@code boolean} : If the file is internal or external.
     * @return {@link FileHandle} : The {@link FileHandle} of the file requested.
     */
    public static FileHandle getFileHandle(String path, boolean internal) {
        if (internal) {
            return Gdx.files.internal(path);
        }
        return new FileHandle(path);
    }

    /**
     * Returns whether an asset path is internal or external
     * @param assetPath {@link String} : The path to the asset.
     * @return {@code boolean} : Whether the asset is ({@code true}) or
     *                           isn't ({@code false}) internal.
     */
    public static boolean isInternal(String assetPath) {
        return assetPath.startsWith("<main>:");
    }

    /**
     * Loads a file from an asset folder as a {@link JsonValue}.
     * @param assetPath {@link String} : The path to the asset.
     * @param folderName {@link String} : The folder that the asset resides in.
     * @return {@link JsonValue} : The file's data, or {@code null} if it couldn't load.
     */
    public static JsonValue loadJsonAsset(String assetPath, String folderName) {
        // First, change assetPath to path.
        String path = toPath(assetPath, folderName);
        // Then, depending on if it starts with "<main>:" or not,
        // change whether it uses the AppData path or internal path.
        if (assetPath.startsWith("<main>:")) {
            // Remove the "<main>:"
            return loadJsonFile("game/", path, true);
        } else {
            return loadJsonFile(getDataPath() + "game/" + getDir(assetPath), path, false);
        }

    }

    /**
     * Saves a {@link JsonValue} to the specified location.
     * @param dir {@link String} : The directory path.
     * @param fileName {@link String} : The file to store the data in.
     * @param root {@link JsonValue} : The data to save.
     */
    public static void saveJsonFile(String dir, String fileName, JsonValue root) {
        // Convert the JSON into text and save it
        String jsonData = root.toJson(JsonWriter.OutputType.json);
        saveToFile(dir, fileName, jsonData);
    }

    /**
     * Saves a {@link JsonValue} to a file in the data folder.
     * @param fileName {@link String} : The file to store the data in.
     * @param root {@link JsonValue} : The data to save.
     */
    public static void saveJsonData(String fileName, JsonValue root) {
        saveJsonFile(getDataPath(), fileName, root);
    }

    /**
     * Converts an asset path into a local or data folder path.
     * @param assetPath {@link String} : The path to the asset.
     * @param mainFolderName {@link String} : The folder that the asset resides in.
     * @return {@link String} : The asset path as a path.
     */
    public static String toPath(String assetPath, String mainFolderName) {
        mainFolderName = formatDir(mainFolderName);
        // AssetPath will be in the format group:filePath
        String[] args = assetPath.split(":", 2);
        if (args.length != 2) {
            return assetPath;
        }

        // Return the asset's path
        return mainFolderName + args[1];
    }

    /**
     * Returns the directory of an assetPath.
     * @param assetPath {@link String} : the path to the asset.
     * @return {@link String} : The asset's directory.
     */
    public static String getDir(String assetPath) {
        // AssetPath will be in the format group:filePath
        String[] args = assetPath.split(":", 2);
        if (args.length != 2) {
            return "";
        }

        // Return the asset's path
        return args[0] + "/";
    }

    /**
     * Returns the path of the asset path.
     * @param assetPath {@link String} : The path to the asset.
     * @param folderName {@link String} : The folder that the asset resides in.
     * @return {@link String} : The exact path to the asset.
     */
    public static String getAssetPath(String assetPath, String folderName) {
        boolean internal = assetPath.startsWith("<main>:");
        String rootFolder = "";

        //If it contains ":", then it is split into at least 2 parts.
        if (!internal && assetPath.contains(":")) {
            String[] args = assetPath.split(":", 2);
            rootFolder = formatDir(args[0]);
        }

        String pathToAsset = toPath(assetPath, folderName);

        String finalPath;
        if (internal) {
            finalPath = "";
        } else {
            finalPath = getDataPath();
        }
        finalPath += "game/" + rootFolder + pathToAsset;

        return finalPath;
    }

}
