package com.undercooked.game.util;

import com.badlogic.gdx.utils.JsonValue;
import com.undercooked.game.util.json.*;
import com.undercooked.game.util.leaderboard.LeaderboardController;

/** A class filled with public static final variables so that they can be accessed from anywhere. */
public final class Constants {

    // Variable Constants
    /** The width of the window video. */
    public static final int V_WIDTH = 1920;
    /** The height of the window video. */
    public static final int V_HEIGHT = 1080;
    /** The formatting for the date and time strings. */
    public static final String DATE_TIME = "yyyy/MM/dd HH:mm:ss";
    /** The string to use for an unknown date. */
    public static final String UNKNOWN_DATE = "????/??/?? ??:??:??";
    /** The maximum length of a name input for a {@link com.undercooked.game.util.leaderboard.LeaderboardEntry} */
    public static final int MAX_NAME_LENGTH = 200;

    // Camera
    /** The ID of the World {@link com.badlogic.gdx.graphics.OrthographicCamera}. */
    public static final String WORLD_CAMERA_ID = "WORLD";
    /** The ID of the User Interface {@link com.badlogic.gdx.graphics.OrthographicCamera}. */
    public static final String UI_CAMERA_ID = "UI";

    // Screens
    /** The ID for the {@link com.undercooked.game.screen.MainScreen}. */
    public static final String MAIN_SCREEN_ID = "main";
    /** The ID for the {@link com.undercooked.game.screen.GameScreen}. */
    public static final String GAME_SCREEN_ID = "game";
    /** The ID for the {@link com.undercooked.game.screen.LeaderboardScreen}. */
    public static final String LEADERBOARD_SCREEN_ID = "leaderboard";
    /** The ID for the {@link com.undercooked.game.screen.PauseScreen}. */
    public static final String PAUSE_SCREEN_ID = "pause";
    /** The ID for the {@link com.undercooked.game.screen.WinScreen}. */
    public static final String WIN_SCREEN_ID = "win";
    /** The ID for the {@link com.undercooked.game.screen.LossScreen}. */
    public static final String LOSS_SCREEN_ID = "loss";
    /** The ID for the SettingsScreen. */
    public static final String SETTINGS_SCREEN_ID = "settings";
    /** The ID for the {@link com.undercooked.game.screen.PlayScreen}. */
    public static final String PLAY_SCREEN_ID = "play";

    // Files and Textures
    /** The folder to store the external data of the game into. */
    public static final String DATA_FILE = "PiazzaPanic_UnderCooked";
    /** The ID for the Menu's song. */
    public static final String MENU_SONG_ID = "musicSong";
    /** The ID for the Game's song. */
    public static final String GAME_SONG_ID = "gameSong";
    /** The texture ID for the textures on the {@link com.undercooked.game.screen.MainScreen} */
    public static final String MENU_TEXTURE_ID = "main";
    /** The texture ID for the textures on the {@link com.undercooked.game.screen.GameScreen} */
    public static final String GAME_TEXTURE_ID = "game";
    /** The texture ID for the textures on the {@link com.undercooked.game.screen.PauseScreen} */
    public static final String PAUSE_TEXTURE_ID = "pause";
    /** The texture ID for the textures on the {@link com.undercooked.game.screen.LeaderboardScreen} */
    public static final String LEADERBOARD_TEXTURE_ID = "leaderboard";
    /** The texture ID for the textures on the {@link com.undercooked.game.screen.LossScreen} */
    public static final String LOSS_TEXTURE_ID = "loss";
    /** The texture ID for the textures on the {@link com.undercooked.game.screen.PlayScreen} */
    public static final String PLAY_TEXTURE_ID = "play";
    /** The register id, used for finding where {@link com.undercooked.game.entity.customer.Customer}s need to wait. */
    public static final String REGISTER_ID = "<main>:register";

    // Music and Sounds
    /** {@link com.undercooked.game.assets.AudioManager} group for the game's Music. */
    public static final String MUSIC_GROUP = "music";
    /** {@link com.undercooked.game.assets.AudioManager} group for the game's music audio. */
    public static final String GAME_GROUP = "game";
    /** {@link com.undercooked.game.assets.AudioManager} group for the game's sound audio. */
    public static final String GAME_SOUND_GROUP = "game_sound";

    // Textures
    /**
     * The number of {@link com.undercooked.game.entity.cook.Cook} {@link com.badlogic.gdx.graphics.Texture}s
     * in the asset folder.
     */
    public static final int NUM_COOK_TEXTURES = 3;
    /**
     * The number of {@link com.undercooked.game.entity.customer.Customer} {@link com.badlogic.gdx.graphics.Texture}s
     * in the asset folder.
     */
    public static int NUM_CUSTOMER_TEXTURES = 5;

    // Defaults
    /** The default texture for the Music. */
    public static final float DEFAULT_MUSIC_VOLUME = 0.5F;
    /** The default volume for the Sound. */
    public static final float DEFAULT_SOUND_VOLUME = 0.5F;
    /** The default {@link com.undercooked.game.map.Map} to load if one fails to load. */
    public static final String DEFAULT_MAP = "<main>:empty";
    /** The default {@link com.badlogic.gdx.graphics.Texture} to load if one fails to load. */
    public static final String DEFAULT_TEXTURE = "items/missing.png";
    public static final String DEFAULT_FLOOR_TILE = "<main>:floor/floor_tile.png";
    /** The default {@link com.badlogic.gdx.audio.Music} to load if one fails to load. */
    public static final String DEFAULT_MUSIC = "game/sounds/frying.mp3";
    /** The default {@link com.badlogic.gdx.audio.Sound} to load if one fails to load. */
    public static final String DEFAULT_SOUND = "game/sounds/chopping.mp3";

    // public static final ... DEFAULT_CONTROLS = ...;

    // public static final... DEFAULT_SETTINGS = ...;
}
