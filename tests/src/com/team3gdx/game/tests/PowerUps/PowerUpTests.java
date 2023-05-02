package com.team3gdx.game.tests.PowerUps;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import com.team3gdx.game.tests.GdxTestRunner;

import com.team3gdx.game.PowerUp.*;
import com.team3gdx.game.PowerUp.PowerUpService;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.team3gdx.game.util.Control;

@RunWith(GdxTestRunner.class)
public class PowerUpTests  {
    public static Control control = new Control();
	public static TiledMapRenderer tiledMapRenderer;
	public static TiledMap map1 = new TmxMapLoader().load("map/art_map/customertest.tmx");
    PowerUpService testService = new PowerUpService(map1, control);
    PowerUp test1 = PowerUps.constructionCostReduce();
    PowerUp test2 = PowerUps.cookingSpeedReduce();
    PowerUp test3 = PowerUps.customerTimeIncrease();
    PowerUp test4 = PowerUps.increasePay();
    PowerUp test5 = PowerUps.speedBoost();

    @Test
    public void spawnPowerUpTest(){
        testService.spawnPowerUp(test1);
        testService.spawnPowerUp(test2);
        testService.spawnPowerUp(test3);
        testService.spawnPowerUp(test4);
        testService.spawnPowerUp(test5);
        String checkForAll5 = "";
        for (PowerUp powerUp : testService.getSpawnedPowerUps()) {
			if (powerUp == test1){
                checkForAll5 += "x";
            }
            else if (powerUp == test2){
                checkForAll5 += "x";
            }
            else if (powerUp == test3){
                checkForAll5 += "x";
            }
            else if (powerUp == test4){
                checkForAll5 += "x";
            }
            else if (powerUp == test5){
                checkForAll5 += "x";
            }
        }
        assertEquals(checkForAll5,"xxxxx");
	}

    @Test
    public void getPowerUpTest(){
        testService.spawnPowerUp(test1);
        testService.spawnPowerUp(test2);
        testService.spawnPowerUp(test3);
        testService.spawnPowerUp(test4);
        testService.spawnPowerUp(test5);
        String checkForAll5 = "";
        for (Vector2 pos : testService.getSpawnedPowerUpsPos()) {
            testService.interact((int)pos.x,(int)pos.y);
        }
        for (PowerUp powerUp : testService.getActivePowerUps()) {
			if (powerUp == test1){
                checkForAll5 += "x";
            }
            else if (powerUp == test2){
                checkForAll5 += "x";
            }
            else if (powerUp == test3){
                checkForAll5 += "x";
            }
            else if (powerUp == test4){
                checkForAll5 += "x";
            }
            else if (powerUp == test5){
                checkForAll5 += "x";
            }
        }
        assertEquals(checkForAll5,"xxxxx");
	}
  


        
}
