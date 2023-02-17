package com.undercooked.game.files;

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

    public static String dirAndName(String dir, String fileName) {
        return dir
                + (dir.endsWith("\\") || dir.endsWith("/") ? "" : "/")
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

    public static String loadfile(String dir, String fileName) {
        return loadFile(dir, fileName, "");
    }

    public static String loadFile(String dir, String fileName, String defaultData) {
        File directory = new File(dir);
        // If directory isn't a directory, or it doesn't exist, then create the directory.
        if (!(directory.exists() && directory.isDirectory())) {
            directory.mkdir();
        }
        File file = new File (dirAndName(dir, fileName));
        // If file isn't a file, or it doesn't exist, then create the file using the default data.
        if (!(file.exists() && file.isFile())) {
            try {
                file.createNewFile();
                saveToFile(dir,fileName,defaultData);
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

    /*public static JsonValue loadJsonFile(String dir, String fileName) {
        return loadJsonFile(dir, fileName, new JsonValue());
    }

    public static JsonValue loadJsonFile(String dir, String fileName, JsonValue defaultJson) {
        File directory = new File(dir);
        // If directory isn't a directory, or it doesn't exist, then return nothing.
        if (!(directory.exists() && directory.isDirectory())) {
            return null;
        }
        File file = new File (dirAndName(dir, fileName));
        // If file isn't a file, or it doesn't exist, then return nothing.
        if (!(file.exists() && file.isFile())) {
            return null;
        }
        // Otherwise, load the Json file.
        JsonReader reader = new JsonReader();
        JsonValue fileData = null;
        try {
            fileData = reader.parse(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return fileData;
    }*/

    /*

    Testing

    public static void main(String[] args) {
        String appdata = getDataPath();
        System.out.println(appdata);
        saveToFile(appdata, "save_data.txt", "Test!");
    }
     */

}
