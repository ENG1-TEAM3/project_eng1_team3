package com.team3gdx.game;

import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.Arrays;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;

public class WorldController {
    private int[][] worldData = new int[10][10];
    private int[] tileSize;

    private Map gameMap;


    public WorldController(int[] tlsz){
        tileSize = tlsz;
        setupWorldData();
        System.out.println(Arrays.deepToString(worldData));
    }

    public void setupWorldData(){
        try {
            File worldDataFile = new File("worldData.txt");
            Scanner worldDataReader = new Scanner(worldDataFile);
            int currentLine = 0;
            while (worldDataReader.hasNextLine()) {
                 String data = worldDataReader.nextLine();
                 char[] arraydata = data.toCharArray();
                 int[] tempArray = new int[arraydata.length];
                 for(int ctr = 0; ctr < arraydata.length; ctr++){
                     char currentchar = arraydata[ctr];
                     tempArray[ctr] = Character.getNumericValue(currentchar);
                 }
                 worldData[currentLine] = tempArray;
                 currentLine++;
            }
            worldDataReader.close();
        }
        catch (FileNotFoundException f) {
            System.out.println("An error occurred while trying to read worldData");
            f.printStackTrace();
        }
    }
    public void renderMap(){

        for(int y = 0; y < worldData.length; y++){
            for(int x = 0; x < worldData[0].length; x++){

            }
        }
    }
    public int[][] returnWorldData(){
        return worldData;
    }
}
