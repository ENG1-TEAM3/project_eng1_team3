package com.undercooked.game.files;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.*;
import com.undercooked.game.util.Constants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Scanner;

/**
 *
 * TODO: NEEDS TO BE COMPLETED
 * TODO: Javadocs
 *
 */

public class FileControl {

    public static String getDataPath() {
        return getDataPath("\\");
    }

    public static String getDataPath(String endsWith) {
        if (System.getProperty("os.name").equals("Linux")){
            return "\\data\\" + Constants.DATA_FILE + endsWith;
        } else if (System.getProperty("os.name").startsWith("Windows")) {
            return System.getenv("APPDATA") + "\\" + Constants.DATA_FILE + endsWith;
        }
        return "\\data\\" + Constants.DATA_FILE + endsWith;
    }

    public static String formatDir(String dir) {
        return dir + (dir.endsWith("\\") || dir.endsWith("/") ? "" : "\\");
    }

    public static String dirAndName(String dir, String fileName) {
        return formatDir(dir)
                + fileName;
    }

    public static void saveToFile(String dir, String fileName, String data) {
        File directory = new File(dir);
        File file = new File(dirAndName(dir, fileName));
        try {
            if (!directory.exists()) {
                directory.mkdir();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            Files.write(file.toPath(), Collections.singleton(data), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveData(String fileName, String data) {
        saveToFile(getDataPath(), fileName, data);
    }

    public static String loadFile(String dir, String fileName) {
        File directory = new File(dir);
        // If directory isn't a directory, or it doesn't exist, then create the directory.
        if (!(directory.exists() && directory.isDirectory())) {
            System.out.println("Directory doesn't exist: " + directory);
            directory.mkdir();
        }
        File file = new File (dirAndName(dir, fileName));
        // If file isn't a file, or it doesn't exist, then create the file using the default data.
        if (!(file.exists() && file.isFile())) {
            System.out.println("File doesn't exist: " + fileName);
            try {
                file.createNewFile();
                saveToFile(dir,fileName,"{}");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Otherwise, load the Json file.
        String fileData = "";
        try {
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                fileData += reader.nextLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return fileData;
    }

    public static String loadData(String fileName) {
        return loadFile(getDataPath(), fileName);
    }

    public static JsonValue loadJsonFile(String dir, String fileName, boolean internal) {
        // If file name does not end with ".json", then add it
        if (!fileName.endsWith(".json")) {
            fileName += ".json";
        }
        FileHandle directory = null;
        dir = formatDir(dir);
        if (internal) {
            System.out.println("Internal directory doesn't exist: " + dir);
            directory = Gdx.files.internal(dir);
        } else {
            System.out.println("External directory doesn't exist: " + dir);
            directory = Gdx.files.external(dir);
        }
        // If directory isn't a directory, or it doesn't exist, then return nothing.
        if (!(directory.exists() && directory.isDirectory())) {
            return null;
        }
        FileHandle file = null;
        if (internal) {
            file = Gdx.files.internal(dirAndName(dir, fileName));
        } else {
            file = Gdx.files.external(dirAndName(dir, fileName));
        }
        // If file isn't a file, or it doesn't exist, then return nothing.
        if (!(file.exists() && file.file().isFile())) {
            if (internal) {
                System.out.println("Internal file doesn't exist: " + dirAndName(dir, fileName));
            } else {
                System.out.println("External file doesn't exist: " + dirAndName(dir, fileName));
            }
            return null;
        }
        // Otherwise, load the Json file.
        String jsonData = loadFile(dir, fileName);
        JsonReader json = new JsonReader();
        JsonValue fileData = null;
        try {
            fileData = json.parse(jsonData);
        } catch (GdxRuntimeException e) {
            e.printStackTrace();
            return null;
        }
        return fileData;
    }

    public static JsonValue loadJsonData(String fileName, boolean internal) {
        return loadJsonFile(getDataPath(), fileName, internal);
    }

    public static JsonValue loadJsonData(String fileName) {
        return loadJsonData(fileName, true);
    }

    public static FileHandle getFileHandle(String path, boolean internal) {
        if (internal) {
            return Gdx.files.internal(path);
        }
        return Gdx.files.external(path);
    }

    public static boolean isInternal(String path) {
        if (path.contains(":")) {
            if (path.startsWith("<main>")) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public static JsonValue loadJsonAsset(String assetPath, String folderName) {
        // First, change assetPath to path.
        String path = toPath(assetPath, folderName);
        // Then, depending on if it starts with "<main>:" or not, change whether it
        // uses the AppData path or internal path.
        if (assetPath.startsWith("<main>")) {
            // Remove the "<main>:"
            System.out.println(dirAndName("game/", path));
            return loadJsonFile("game/", path, true);
        } else {
            return loadJsonFile(getDataPath() + "game/", path, false);
        }

    }

    public static void saveJsonFile(String dir, String fileName, JsonValue root) {
        // Convert the JSON into text and save it
        String jsonData = root.toJson(JsonWriter.OutputType.json);
        saveToFile(dir, fileName, jsonData);
    }

    public static void saveJsonData(String fileName, JsonValue root) {
        saveJsonFile(getDataPath(), fileName, root);
    }

    public static String toPath(String assetPath, String mainFolderName) {
        mainFolderName = formatDir(mainFolderName);
        // AssetPath will be in the format group:filePath
        String[] args = assetPath.split(":", 2);
        System.out.println(assetPath);
        for (String str : args) {
            System.out.println(str);
        }
        if (args.length != 2) {
            return assetPath;
        }

        // Return the asset's path
        return mainFolderName + args[1];
    }

    public static void main(String[] args) {
        String appdata = getDataPath();
        System.out.println(appdata);
        saveToFile(appdata, "save_data.txt", "Test!");
        JsonValue jvalue = loadJsonData("settings.json");
        System.out.println(jvalue);
        jvalue.addChild("TEST", new JsonValue(JsonValue.ValueType.object));
        System.out.println(jvalue);
        saveJsonData("settings.json", jvalue);
        System.out.println(jvalue.get("TEST"));
    }

}
