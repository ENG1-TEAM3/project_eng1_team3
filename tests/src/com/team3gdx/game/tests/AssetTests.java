package com.team3gdx.game.tests;

import com.badlogic.gdx.Gdx;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class AssetTests {

    @Test
    public void testPotatoAssetExists(){
        assertTrue(Gdx.files.internal("items/Cheese.png").exists());
        assertTrue(Gdx.files.internal("items/Cheesy-Baked_Potato.png").exists());
        assertTrue(Gdx.files.internal("items/Cooked_Potato.png").exists());
        assertTrue(Gdx.files.internal("items/Cut_Cooked_Potato.png").exists());
        assertTrue(Gdx.files.internal("items/GratedCheese.png").exists());
        assertTrue(Gdx.files.internal("items/Mushy_Grated_Cheese.png").exists());
        assertTrue(Gdx.files.internal("items/Potato.png").exists());
        assertTrue(Gdx.files.internal("items/Burnt-Potato.png").exists());
    }

    @Test
    public void testPizzaAssetExists(){
        assertTrue(Gdx.files.internal("items/burned_pizza.png").exists());
        assertTrue(Gdx.files.internal("items/Dough.png").exists());
        assertTrue(Gdx.files.internal("items/Dough_Tomato.png").exists());
        assertTrue(Gdx.files.internal("items/Raw_pizza.png").exists());
        assertTrue(Gdx.files.internal("items/Rolled_Dough.png").exists());
        assertTrue(Gdx.files.internal("items/Tomato_sauce.png").exists());
    }

    @Test
    public void testCookAssetExists() {
        assertTrue(Gdx.files.internal("entities/cook_walk_1.png").exists());
        assertTrue(Gdx.files.internal("entities/cook_walk_2.png").exists());
        assertTrue(Gdx.files.internal("entities/cook_walk_3.png").exists());
        assertTrue(Gdx.files.internal("entities/cook_walk_hands_1.png").exists());
        assertTrue(Gdx.files.internal("entities/cook_walk_hands_2.png").exists());
        assertTrue(Gdx.files.internal("entities/cook_walk_hands_3.png").exists());
    }
    @Test
    public void testCustomerAssetExists() {
        assertTrue(Gdx.files.internal("entities/cust1b.png").exists());
        assertTrue(Gdx.files.internal("entities/cust1f.png").exists());
        assertTrue(Gdx.files.internal("entities/cust1l.png").exists());
        assertTrue(Gdx.files.internal("entities/cust1r.png").exists());
        assertTrue(Gdx.files.internal("entities/cust2b.png").exists());
        assertTrue(Gdx.files.internal("entities/cust2f.png").exists());
        assertTrue(Gdx.files.internal("entities/cust2l.png").exists());
        assertTrue(Gdx.files.internal("entities/cust2r.png").exists());
        assertTrue(Gdx.files.internal("entities/cust3b.png").exists());
        assertTrue(Gdx.files.internal("entities/cust3f.png").exists());
        assertTrue(Gdx.files.internal("entities/cust3l.png").exists());
        assertTrue(Gdx.files.internal("entities/cust3r.png").exists());
        assertTrue(Gdx.files.internal("entities/cust4b.png").exists());
        assertTrue(Gdx.files.internal("entities/cust4f.png").exists());
        assertTrue(Gdx.files.internal("entities/cust4l.png").exists());
        assertTrue(Gdx.files.internal("entities/cust4r.png").exists());
    }

    @Test
    public void testSoundAssetExists() {
        assertTrue(Gdx.files.internal("audio/music/testMusic.mp3").exists());
        assertTrue(Gdx.files.internal("audio/soundFX/cash-register-opening.mp3").exists());
        assertTrue(Gdx.files.internal("audio/soundFX/chopping.mp3").exists());
        assertTrue(Gdx.files.internal("audio/soundFX/frying.mp3").exists());
        assertTrue(Gdx.files.internal("audio/soundFX/money-collect.mp3").exists());
        assertTrue(Gdx.files.internal("audio/soundFX/testSoundFX.mp3").exists());
        assertTrue(Gdx.files.internal("audio/soundFX/timer-bell-ring.mp3").exists());
    }

    @Test
    public void testParticlesAssetExists() {
        assertTrue(Gdx.files.internal("particles/flame.png").exists());
        assertTrue(Gdx.files.internal("particles/smoke.png").exists());
    }

    @Test
    public void testPowerUpAssetExists() {
        assertTrue(Gdx.files.internal("PowerUps/construction_cost_reduce.png").exists());
        assertTrue(Gdx.files.internal("PowerUps/cooking_speed_reduce.png").exists());
        assertTrue(Gdx.files.internal("PowerUps/Customer_time_increase.png").exists());
        assertTrue(Gdx.files.internal("PowerUps/Increase_pay.png").exists());
        assertTrue(Gdx.files.internal("PowerUps/speed_boost.png").exists());
    }
}