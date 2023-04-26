package com.undercooked.game.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.undercooked.game.audio.SoundStateChecker;
import com.undercooked.game.files.FileControl;
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

    ObjectMap<String, VolumeGroup> musicVolumes;
    ObjectMap<String, VolumeGroup> soundVolumes;
    AssetManager assetManager;
    // SoundStateChecker soundStateChecker;

    /**
     * Constructor to set up the Maps for music, sound and volumes.
     */
    public AudioManager(AssetManager assetManager) { // , SoundStateChecker soundStateChecker) {
        musicVolumes = new ObjectMap<>();
        soundVolumes = new ObjectMap<>();
        this.assetManager = assetManager;
        // this.soundStateChecker = soundStateChecker;
        load();
    }

    /**
     * Returns whether the sound ID is playing or not.
     *
     * @param soundID The {@code soundID} as an {@code int}.
     * @return {@code boolean} : {@code True} if the {@link Sound} is playing,
     *         {@code False} if not.
     */
    // public boolean soundIsPlaying(long soundID) {
    // return soundStateChecker.isPlaying((int) soundID);
    // }

    /**
     * Loads the default assets for the music and sound for if they
     * haven't been loaded, to prevent any crashes.
     */
    private void load() {
        try {
            // Load directly into the assetManager so that they can't be
            // unloaded using this class.
            assetManager.load(Constants.DEFAULT_MUSIC, Music.class);
            assetManager.load(Constants.DEFAULT_SOUND, Music.class);
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
            if (!assetManager.isLoaded(Constants.DEFAULT_MUSIC)) {
                System.out.println("Default music not loaded.");
                return null;
            }
            return assetManager.get(Constants.DEFAULT_MUSIC);
        }
    }

    public Music getMusicAsset(String path) {
        if (path == null) {
            return getMusic(Constants.DEFAULT_SOUND);
        }
        return getMusic("game/" + FileControl.toPath(path, "sounds"));
    }

    public boolean loadMusic(String path, String audioGroup) {
        /**
         * TEMPORARY CODE
         * Should instead make it put the file to load, and then add it
         * to the volumes.
         *
         * It should then update all the audio sounds by looping through
         * the Maps in another function AFTER everything is loaded.
         */
        try {
            assetManager.load(path, Music.class);
        } catch (GdxRuntimeException e) {
            e.printStackTrace();
            // If it couldn't load, then return.
            return false;
        }
        // Check if the audioGroup doesn't have a volume yet
        if (!musicVolumes.containsKey(audioGroup)) {
            // If it doesn't, set it to the default
            musicVolumes.put(audioGroup, new VolumeGroup(Constants.DEFAULT_MUSIC_VOLUME));
        }
        // Get the VolumeGroup
        VolumeGroup thisGroup = musicVolumes.get(audioGroup);
        // Add this path to it
        thisGroup.addPath(path);
        return true;
    }

    public boolean loadMusicAsset(String path, String musicSoundGroup) {
        if (path == null) {
            return loadMusic(Constants.DEFAULT_SOUND, musicSoundGroup);
        }
        return loadMusic("game/" + FileControl.toPath(path, "sounds"), musicSoundGroup);
    }

    public void unloadMusic(String path) {
        // Only go through the unload process if it's actually loaded
        if (assetManager.isLoaded(path)) {
            // Go through all the volumeGroups and remove the path
            for (VolumeGroup vGroup : musicVolumes.values()) {
                vGroup.removePath(path);
            }
            // Now unload the path
            assetManager.unload(path);
        }
    }

    public Sound getSound(String path) {
        // Try to get the music
        if (assetManager.isLoaded(path)) {
            return assetManager.get(path);
        } else {
            System.out.println(path + " not loaded.");
            // If music is not loaded, then load the missing music
            // But return null if that isn't loaded
            if (!assetManager.isLoaded(Constants.DEFAULT_SOUND)) {
                System.out.println("Default sound not loaded.");
                return null;
            }
            return assetManager.get(Constants.DEFAULT_SOUND);
        }
    }

    public Sound getSoundAsset(String path) {
        if (path == null) {
            return getSound(Constants.DEFAULT_SOUND);
        }
        return getSound("game/" + FileControl.toPath(path, "sounds"));
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
        if (!soundVolumes.containsKey(audioGroup)) {
            // If it doesn't, set it to the default
            soundVolumes.put(audioGroup, new VolumeGroup(Constants.DEFAULT_SOUND_VOLUME));
        }
        // Get the VolumeGroup
        VolumeGroup thisGroup = soundVolumes.get(audioGroup);
        // Add this path to it
        thisGroup.addPath(path);
        // Return true as it has been loaded.
        return true;
    }

    public boolean loadSoundAsset(String path, String audioGroup) {
        return loadSound("game/" + FileControl.toPath(path, "sounds"), audioGroup);
    }

    /**
     * Post loading for the AudioManager.
     */
    public void postLoad() {
        // Update the music volumes for all the audio groups
        for (String audioGroup : musicVolumes.keys()) {
            updateMusicVolumes(audioGroup);
        }
    }

    public void updateMusicVolumes(String audioGroup) {
        // First check that the audioGroup exists
        if (!musicVolumes.containsKey(audioGroup)) {
            return;
        }
        VolumeGroup vGroup = musicVolumes.get(audioGroup);
        for (String path : vGroup.paths) {
            // Loop through the paths, get the music and update
            // their volumes.
            try {
                Music thisMusic = assetManager.get(path);
                thisMusic.setVolume(vGroup.volume);
            } catch (GdxRuntimeException e) {

            }
        }
    }

    public void setMusicVolume(float volume, String audioGroup) {
        // Check if the VolumeGroup exists
        if (!musicVolumes.containsKey(audioGroup)) {
            // If it doesn't, then add it
            musicVolumes.put(audioGroup, new VolumeGroup(volume));
            return;
        }
        // If it does, then set it to the volume input
        musicVolumes.get(audioGroup).volume = volume;
        updateMusicVolumes(audioGroup);
    }

    public void setSoundVolume(float volume, String audioGroup) {
        // Check if the VolumeGroup exists
        if (!soundVolumes.containsKey(audioGroup)) {
            // If it doesn't, then add it
            soundVolumes.put(audioGroup, new VolumeGroup(volume));
            return;
        }
        // If it does, then set it to the volume input
        soundVolumes.get(audioGroup).volume = volume;
        // Update the volume for that audioGroup
        // updateVolume("sound/" + audioGroup)
    }

    /**
     * Unloads all paths in an audio group from the {@link AssetManager}.
     * 
     * @param audioGroup  A {@link String} of the {@code audioGroup}'s name.
     * @param forgetGroup {@code boolean} for if the group should be deleted
     *                    from the map afterwards or not
     */
    public void unload(String audioGroup, boolean forgetGroup) {
        // Check if the group exists
        if (!musicVolumes.containsKey(audioGroup)) {
            // If it's not, then return
            return;
        }
        // If the audioGroup exists, then unload all the paths, if they're loaded.
        // It only unloads each path once, so if they're loaded multiple times (such
        // as for multiple Screens needing it), then it'll stay loaded for them.
        VolumeGroup group = musicVolumes.get(audioGroup);
        Array<String> pathsRemoved = new Array<>();
        for (int i = group.paths.size - 1; i >= 0; i--) {
            String path = group.paths.get(i);
            // Only continue if the current path hasn't been removed.
            if (!pathsRemoved.contains(path, false)) {
                if (assetManager.isLoaded(path)) {
                    assetManager.unload(path);
                    System.out.println("Unloaded " + path + ".");
                }
                pathsRemoved.add(path);
                group.paths.removeIndex(i);
            }
        }
        // If forgetGroup is true, then remove it from the map.
        if (forgetGroup) {
            musicVolumes.remove(audioGroup);
        }
    }

    public void unload(String audioGroup) {
        unload(audioGroup, false);
    }

    /**
     * Disposes of all music and
     */
    public void dispose() {
        // Go through all the music and sounds and unload them.
        for (VolumeGroup vGroup : musicVolumes.values()) {
            for (String path : vGroup.paths) {
                assetManager.unload(path);
            }
        }
        // Then clear the volume map
        musicVolumes.clear();
    }

}
