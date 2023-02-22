package com.undercooked.game.audio;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
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

    /**
     * Constructor to set up the Maps for music, sound and volumes.
     */
    public AudioManager(AssetManager assetManager) {
        volumes = new ObjectMap<>();
        this.assetManager = assetManager;
    }

    public boolean assetLoaded(String path) {
        return assetManager.isLoaded(path);
    }

    public void loadMusic(String path, String audioGroup) {
        /**
         * TEMPORARY CODE
         * Should instead make it put the file to load, and then add it
         * to the volumes.
         *
         * It should then update all the audio sounds by looping through
         * the Maps in another function AFTER everything is loaded.
         * */
        assetManager.load(path, Music.class);
        // Check if the audioGroup doesn't have a volume yet
        if (!volumes.containsKey("music/" + audioGroup)) {
            // If it doesn't, set it to the default
            volumes.put("music/" + audioGroup, new VolumeGroup(Constants.DEFAULT_MUSIC));
        }
        // Get the VolumeGroup
        VolumeGroup thisGroup = volumes.get("music/" + audioGroup);
        // Add this path to it
        thisGroup.addPath(path);
    }

    public void unloadMusic(String path) {
        // Go through all the volumeGroups and remove the path
        for (VolumeGroup vGroup : volumes.values()) {
            vGroup.removePath(path);
        }
        // Now unload the path
        assetManager.unload(path);
    }

    public Sound loadSound(String path, String audioGroup) {
        /**
         * TEMPORARY CODE
         * Should instead make it put the file to load, and then add it
         * to the volumes.
         *
         * It should then update all the audio sounds by looping through
         * the Maps in another function AFTER everything is loaded.
         * */
        // If the music already exists, just load that.
        if (assetManager.isLoaded(path)) {
            return assetManager.get(path);
        }
        assetManager.load(path, Sound.class);
        // Load the asset by updating it the same frame.
        assetManager.update();
        // Get the asset
        Sound thisSound = assetManager.get(path, Sound.class);
        // Check if the audioGroup doesn't have a volume yet
        if (!volumes.containsKey("sound/" + audioGroup)) {
            // If it doesn't, set it to the default
            volumes.put("sound/" + audioGroup, new VolumeGroup(Constants.DEFAULT_MUSIC));
        }
        // Get the VolumeGroup
        VolumeGroup thisGroup = volumes.get("sound/" + audioGroup);
        // Add this path to it
        thisGroup.addPath(path);
        // Set the music's volume
        // thisSound.setVolume(volumes.get(audioGroup).volume);
        // Update the volume for that audioGroup
        // updateVolume("music/" + audioGroup)
        return thisSound;
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
            Music thisMusic = MainGameClass.assetManager.get(path);
            thisMusic.setVolume(vGroup.volume);
        }
    }

    public void setMusicVolume(float volume, String audioGroup) {
        // Check if the VolumeGroup exists
        if (!volumes.containsKey("music/" + audioGroup)) {
            // If it doesn't, then add it
            volumes.put("music/" + audioGroup, new VolumeGroup(volume));
            return;
        }
        // If it does, then set it to the volume input
        volumes.get("music/" + audioGroup).volume = volume;
        updateMusicVolumes("music/" + audioGroup);
    }

    public void setSoundVolume(float volume, String audioGroup) {
        // Check if the VolumeGroup exists
        if (!volumes.containsKey("sound/" + audioGroup)) {
            // If it doesn't, then add it
            volumes.put("sound/" + audioGroup, new VolumeGroup(volume));
            return;
        }
        // If it does, then set it to the volume input
        volumes.get("sound/" + audioGroup).volume = volume;
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
