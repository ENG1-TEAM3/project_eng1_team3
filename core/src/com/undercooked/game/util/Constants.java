package com.undercooked.game.util;

/** A class filled with public static final variables so that they can be accessed from anywhere. */
public class Constants {

    // Camera
    public static final String WORLD_CAMERA_ID = "WORLD";
    public static final String UI_CAMERA_ID = "UI";

    // Window
    public static final int V_WIDTH = 1920;
    public static final int V_HEIGHT = 1080;

    // Screens
    public static final String MAIN_SCREEN_ID = "main";
    public static final String GAME_SCREEN_ID = "game";
    public static final String LEADERBOARD_SCREEN_ID = "leaderboard";
    public static final String PAUSE_SCREEN_ID = "pause";
    public static final String SETTINGS_SCREEN_ID = "settings";

    // Files
    public static final String DATA_FILE = "PiazzaPanic_UnderCooked";
    public static final String MENU_SONG_ID = "musicSong";
    public static final String GAME_SONG_ID = "gameSong";
    public static final String MENU_TEXTURE_ID = "main";
    public static final String GAME_TEXTURE_ID = "game";
    public static final String PAUSE_TEXTURE_ID = "pause";
    public static final String LEADERBOARD_TEXTURE_ID = "leaderboard";

    // Sounds
    public static final String MUSIC_GROUP = "music";
    public static final String GAME_GROUP = "game";

    // Textures
    public static final int NUM_COOK_TEXTURES = 3;
    public static int NUM_CUSTOMER_TEXTURES = 5;

    // Defaults
    public static final float DEFAULT_MUSIC_VOLUME = 0.5F;
    public static final float DEFAULT_SOUND_VOLUME = 0.5F;
    public static final String DEFAULT_MAP = "map/art_map/customertest.tmx";
    public static final String DEFAULT_TEXTURE = "items/missing.png";
    public static final String DEFAULT_MUSIC = "audio/music/GameMusic.mp3";
    public static final String DEFAULT_SOUND = "uielements/testsound.mp3";

    // public static final ... DEFAULT_CONTROLS = ...;

    // public static final... DEFAULT_SETTINGS = ...;
}
