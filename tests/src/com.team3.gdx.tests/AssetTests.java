package com.team3.gdx.tests;

import com.badlogic.gdx.Gdx;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

@RunWith(GdxTestRunner.class)
public class AssetTests {

    @Test
    public void testChefAssetsExist(){
        assertTrue("Cook 1 standard asset exists test", Gdx.files.internal("entities/cook_walk_1.png").exists());
        assertTrue("Cook 2 standard asset exists test", Gdx.files.internal("entities/cook_walk_2.png").exists());
        assertTrue("Cook 3 standard asset exists test", Gdx.files.internal("entities/cook_walk_3.png").exists());
        assertTrue("Cook 1 hand asset exists test", Gdx.files.internal("entities/cook_walk_hands_1.png").exists());
        assertTrue("Cook 2 hand asset exists test", Gdx.files.internal("entities/cook_walk_hands_1.png").exists());
        assertTrue("Cook 3 hand asset exists test", Gdx.files.internal("entities/cook_walk_hands_1.png").exists());
    }
    @Test
    public void testCustAssetsExist(){
        //customer 1
        assertTrue("Customer 1 front", Gdx.files.internal("entities/cust1f.png").exists());
        assertTrue("Customer 1 back", Gdx.files.internal("entities/cust1b.png").exists());
        assertTrue("Customer 1 left", Gdx.files.internal("entities/cust1l.png").exists());
        assertTrue("Customer 2 right", Gdx.files.internal("entities/cust2r.png").exists());
        //customer 2
        assertTrue("Customer 2 front", Gdx.files.internal("entities/cust2f.png").exists());
        assertTrue("Customer 2 back", Gdx.files.internal("entities/cust2b.png").exists());
        assertTrue("Customer 2 left", Gdx.files.internal("entities/cust2l.png").exists());
        assertTrue("Customer 2 right", Gdx.files.internal("entities/cust2r.png").exists());
        //customer 3
        assertTrue("Customer 3 front", Gdx.files.internal("entities/cust3f.png").exists());
        assertTrue("Customer 3 back", Gdx.files.internal("entities/cust3b.png").exists());
        assertTrue("Customer 3 left", Gdx.files.internal("entities/cust3l.png").exists());
        assertTrue("Customer 3 right", Gdx.files.internal("entities/cust3r.png").exists());
        //customer 3
        assertTrue("Customer 4 front", Gdx.files.internal("entities/cust4f.png").exists());
        assertTrue("Customer 4 back", Gdx.files.internal("entities/cust4b.png").exists());
        assertTrue("Customer 4 left", Gdx.files.internal("entities/cust4l.png").exists());
        assertTrue("Customer 4 right", Gdx.files.internal("entities/cust4r.png").exists());
        //customer 3
        assertTrue("Customer 5 front", Gdx.files.internal("entities/cust5f.png").exists());
        assertTrue("Customer 5 back", Gdx.files.internal("entities/cust5b.png").exists());
        assertTrue("Customer 5 left", Gdx.files.internal("entities/cust5l.png").exists());
        assertTrue("Customer 5 right", Gdx.files.internal("entities/cust5r.png").exists());




    }

    @Test
    public void testAudioAssetsExist() {
        assertTrue("testmusic in music", Gdx.files.internal("audio/music/testMusic.mp3").exists());

        //music things
        assertTrue("GameMusic.mp3 in 'uielements?'", Gdx.files.internal("uielements/GameMusic.mp3").exists());
        assertTrue("GameMusic.ogg in 'uielements?'", Gdx.files.internal("uielements/GameMusic.ogg").exists());
        assertTrue("MainScreenMusic.ogg in 'uielements?'", Gdx.files.internal("uielements/GameMusic.ogg").exists());
        assertTrue("music.mp3 in 'uielements?'", Gdx.files.internal("uielements/music.mp3").exists());

        //sound effect things
        assertTrue("cash-register-opening.mp3 in audio/soundFX", Gdx.files.internal("audio/soundFX/cash-register-opening.mp3").exists());
        assertTrue("chopping.mp3 in audio/soundFX", Gdx.files.internal("audio/soundFX/chopping.mp3").exists());
        assertTrue("frying.mp3 in audio/soundFX", Gdx.files.internal("audio/soundFX/frying.mp3").exists());
        assertTrue("money-collect.mp3 in audio/soundFX", Gdx.files.internal("audio/soundFX/money-collect.mp3").exists());
        assertTrue("testSoundFX.mp3 in audio/soundFX", Gdx.files.internal("audio/soundFX/testSoundFX.mp3").exists());
        assertTrue("timer-bell-ring.mp3 in audio/soundFX", Gdx.files.internal("audio/soundFX/timer-bell-ring.mp3").exists());

    }


    @Test
    public void testItemAssets(){

        assertTrue("burger.png in items", Gdx.files.internal("items/burger.png").exists());
        assertTrue("burger_bun.png in items", Gdx.files.internal("items/burger_bun.png").exists());
        assertTrue("burger_bun_burned.png in items", Gdx.files.internal("items/burger_bun_burned.png").exists());
        assertTrue("burger_bun_cooked.png in items", Gdx.files.internal("items/burger_bun_cooked.png").exists());
        assertTrue("burger_burned.png in items", Gdx.files.internal("items/burger_burned.png").exists());
        assertTrue("burger_cooked.png in items", Gdx.files.internal("items/burger_cooked.png").exists());
        assertTrue("lettuce.png in items", Gdx.files.internal("items/lettuce.png").exists());
        assertTrue("lettuce_chopped.png in items", Gdx.files.internal("items/lettuce_chopped.png").exists());
        assertTrue("meat.png in items", Gdx.files.internal("items/meat.png").exists());
        assertTrue("meat overcooked.png in items", Gdx.files.internal("items/meat overcooked.png").exists());
        assertTrue("meat undercooked.png in items", Gdx.files.internal("items/meat undercooked.png").exists());
        assertTrue("nil.png in items", Gdx.files.internal("items/nil.png").exists());
        assertTrue("onion.png in items", Gdx.files.internal("items/onion.png").exists());
        assertTrue("onion_chopped.png in items", Gdx.files.internal("items/onion_chopped.png").exists());
        assertTrue("patty.png in items", Gdx.files.internal("items/patty.png").exists());
        assertTrue("patty_burned.png in items", Gdx.files.internal("items/patty_burned.png").exists());
        assertTrue("patty_cooked.png in items", Gdx.files.internal("items/patty_cooked.png").exists());
        assertTrue("salad.png in items", Gdx.files.internal("items/salad.png").exists());
        assertTrue("tomato.png in items", Gdx.files.internal("items/tomato.png").exists());
        assertTrue("tomato_burned.png in items", Gdx.files.internal("items/tomato_burned.png").exists());
        assertTrue("tomato_chopped.png in items", Gdx.files.internal("items/tomato_chopped.png").exists());
        assertTrue("tomato_cooked.png in items", Gdx.files.internal("items/tomato_cooked.png").exists());
        assertTrue("unformed_patty.png in items", Gdx.files.internal("items/unformed_patty.png").exists());







    }

    @Test
    public void testLeaderboardData(){

        assertTrue("playerData.txt in leaderboarddata", Gdx.files.internal("leaderboarddata/playerData.txt").exists());

    }





}