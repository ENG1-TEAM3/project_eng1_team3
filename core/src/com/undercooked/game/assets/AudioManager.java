package com.undercooked.game.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.undercooked.game.MainGameClass;
import com.undercooked.game.util.Constants;

/**
 * A class to control the Audio within the game. Use this to allow for
 * ease of changing volumes of sound / music based on an audio group.
 */
public class AudioManager {
    private class VolumeGroup {
        float volume;
        Array<String> paths;

        public VolumeGroup(float volume) {
            this.paths = new Array<>();
            this.volume = volume;
        }

        public VolumeGroup() {
            this(1);
        }

        public Array<String> getPaths() {
            return paths;
        }

        public void addPath(String path) {
            // If not already in the paths, add the path
            if (!paths.contains(path, false)) {
                paths.add(path);
            }
        }

        public void removePath(String path) {
            paths.removeValue(path, false);
        }
    }
    ObjectMap<String, VolumeGroup> volumes;
    AssetManager assetManager;
    String DEFAULT_MUSIC = "audio/music/GameMusic.mp3";
    String DEFAULT_SOUND = "uielements/testsound.mp3";

    /**
     * Constructor to set up the Maps for music, sound and volumes.
     */
    public AudioManager(AssetManager assetManager) {
        volumes = new ObjectMap<>();
        this.assetManager = assetManager;
        load();
    }

    /**
     * Loads the default assets for the music and sound for if they
     * haven't been loaded, to prevent any crashes.
     */
    private void load() {
        try {
            // Load directly into the assetManager so that they can't be
            // unloaded using this class.
            assetManager.load(DEFAULT_MUSIC, Music.class);
            assetManager.load(DEFAULT_SOUND, Sound.class);
        } catch (GdxRuntimeException e) {
            // Of course, make sure it actually doesn't crash if they can't load
            System.out.println("Couldn't load default music.");
            e.printStackTrace();
        }
    }

    public boolean assetLoaded(String path) {
        return assetManager.isLoaded(path);
    }

    public Music getMusic(String path) {
        // Try to get the music
        if (assetManager.isLoaded(path)) {
            return assetManager.get(path);
        } else {
            System.out.println(path + " not loaded.");
            // If music is not loaded, then load the missing music
            // But return null if that isn't loaded
            if (!assetManager.isLoaded(DEFAULT_MUSIC)) {
                System.out.println("Default music not loaded.");
                return null;
            }
            return assetManager.get(DEFAULT_MUSIC);
        }
    }

    public boolean loadMusic(String path, String audioGroup) {
        /**
         * TEMPORARY CODE
         * Should instead make it put the file to load, and then add it
         * to the volumes.
         *
         * It should then update all the audio sounds by looping through
         * the Maps in another function AFTER everything is loaded.
         * */
        try {
            assetManager.load(path, Music.class);
        } catch (GdxRuntimeException e) {
            e.printStackTrace();
            // If it couldn't load, then return.
            return false;
        }
        // Check if the audioGroup doesn't have a volume yet
        if (!volumes.containsKey(audioGroup)) {
            // If it doesn't, set it to the default
            volumes.put(audioGroup, new VolumeGroup(Constants.DEFAULT_MUSIC));
        }
        // Get the VolumeGroup
        VolumeGroup thisGroup = volumes.get(audioGroup);
        // Add this path to it
        thisGroup.addPath(path);
        return true;
    }

    public void unloadMusic(String path) {
        // Only go through the unload process if it's actually loaded
        if (assetManager.isLoaded(path)) {
            // Go through all the volumeGroups and remove the path
            for (VolumeGroup vGroup : volumes.values()) {
                vGroup.removePath(path);
            }
            // Now unload the path
            assetManager.unload(path);
        }
    }

    public Sound getSound(String path) {
        // Try to get the music
        try {
            return assetManager.get(path);
        } catch (GdxRuntimeException e) {
            // If sound is not loaded, then load the missing music
            // But return null if that isn't loaded
            if (!assetManager.isLoaded(DEFAULT_SOUND)) {
                System.out.println();
                return null;
            }
            return assetManager.get(DEFAULT_SOUND);
        }
    }

    public boolean loadSound(String path, String audioGroup) {
        // Try to load the sound
        try {
            assetManager.load(path, Sound.class);
        } catch (GdxRuntimeException e) {
            System.out.println(e);
            // Return false if it can't
            return false;
        }
        // Check if the audioGroup doesn't have a volume yet
        if (!volumes.containsKey(audioGroup)) {
            // If it doesn't, set it to the default
            volumes.put(audioGroup, new VolumeGroup(Constants.DEFAULT_MUSIC));
        }
        // Get the VolumeGroup
        VolumeGroup thisGroup = volumes.get(audioGroup);
        // Add this path to it
        thisGroup.addPath(path);
        // Return true as it has been loaded.
        return true;
    }

    public void updateMusicVolumes(String audioGroup) {
        // First check that the audioGroup exists
        if (!volumes.containsKey(audioGroup)) {
            return;
        }
        VolumeGroup vGroup = volumes.get(audioGroup);
        for (String path : vGroup.paths) {
            // Loop through the paths, get the music and update
            // their volumes.
            Music thisMusic = assetManager.get(path);
            thisMusic.setVolume(vGroup.volume);
        }
    }

    public void setMusicVolume(float volume, String audioGroup) {
        // Check if the VolumeGroup exists
        if (!volumes.containsKey(audioGroup)) {
            // If it doesn't, then add it
            volumes.put(audioGroup, new VolumeGroup(volume));
            return;
        }
        // If it does, then set it to the volume input
        volumes.get(audioGroup).volume = volume;
        updateMusicVolumes(audioGroup);
    }

    public void setSoundVolume(float volume, String audioGroup) {
        // Check if the VolumeGroup exists
        if (!volumes.containsKey(audioGroup)) {
            // If it doesn't, then add it
            volumes.put(audioGroup, new VolumeGroup(volume));
            return;
        }
        // If it does, then set it to the volume input
        volumes.get(audioGroup).volume = volume;
        // Update the volume for that audioGroup
        // updateVolume("sound/" + audioGroup)
    }

    /**
     * Disposes of all music and
     */
    public void dispose() {
        // Go through all the music and sounds and unload them.
        for (VolumeGroup vGroup : volumes.values()) {
            for (String path : vGroup.paths) {
                assetManager.unload(path);
            }
        }
        // Then clear the volume map
        volumes.clear();
    }

}
