package com.undercooked.game.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.ObjectMap;
import com.undercooked.game.files.assets.Asset;
import com.undercooked.game.files.assets.AssetController;
import com.undercooked.game.util.Constants;

/**
 *
 * TODO: JavaDocs
 *
 */

public class Audio extends AssetController
{

    /**
     * This class is a Singleton, because it needs
     *  to be able to use different classes.
     */
    protected Audio() { }

    private static Audio INSTANCE;

    public static Audio getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Audio();
        }
        return INSTANCE;
    }

    private ObjectMap<String, Float> volumes = new ObjectMap<>();

    public Music loadMusic(String path, String musicID, String audioGroup) {
        String mapKey = "music_" + audioGroup;
        MusicAsset output = (MusicAsset) loadAsset(musicFile(path), musicID, mapKey);
        // If mapKey is not in volumes, add it.
        if (!volumes.containsKey(mapKey)) {
            volumes.put(mapKey, Constants.DEFAULT_SOUND);
        }
        // Otherwise, set the volume of the Asset.
        output.setVolume(volumes.get(mapKey));
        return output.getAsset();
    }

    public Music loadMusic(String path, String musicID) {
        return loadMusic(path, musicID, "default");
    }

    public Music getMusic(String musicID, String audioGroup) {
        return (Music) getAsset(musicID, "music_" + audioGroup).getAsset();
    }

    public Music getMusic(String musicID) {
        return getMusic(musicID, "default");
    }

    public Sound loadSound(String path, String soundID, String audioGroup) {
        return (Sound) loadAsset(soundFile(path), soundID, "sound_" + audioGroup).getAsset();
    }

    public Sound loadSound(String path, String soundID) {
        return loadSound(path,soundID,"default");
    }

    public Sound getSound(String soundID, String audioGroup) {
        return (Sound) getAsset(soundID, "sound_" + audioGroup).getAsset();
    }

    public Sound getSound(String soundID) {
        return getSound(soundID,"default");
    }

    protected MusicAsset musicFile(String path) {
        return new MusicAsset(Gdx.audio.newMusic(Gdx.files.internal(path)));
    }

    protected MusicAsset soundFile(String path) {
        return new MusicAsset(Gdx.audio.newMusic(Gdx.files.internal(path)));
    }

    public void setMapVolume(float volume, String mapKey) {
        volumes.put(mapKey, volume);
    }

    public void setMusicVolume(float volume, String audioGroup) {
        // Before doing anything further, just set the volume in the volumes map.
        setMapVolume(volume, "music_" + audioGroup);

        ObjectMap<String, Asset> musicMap = assetMaps.get("music_" + audioGroup);
        // If there is no ObjectMap for the audioGroup, return here.
        if (musicMap == null) {
            return;
        }
        for (Asset asset : musicMap.values()) {
            ((Music) asset.getAsset()).setVolume(volume);
        }
    }

    public void setMusicVolume(float volume) {
        setMusicVolume(volume, "default");
    }

    public void setSoundVolume(float volume, String audioGroup) {
        ObjectMap<String, Asset> soundMap = assetMaps.get("sound_" + audioGroup);
        // If there is no ObjectMap for the audioGroup, just return.
        if (soundMap == null) {
            return;
        }
        for (Asset asset : soundMap.values()) {
            ((Sound) asset.getAsset()).setVolume(0, volume);
        }
        setMapVolume(volume, "sound_" + audioGroup);
    }

    public void setSoundVolume(float volume) {
        setSoundVolume(volume, "default");
    }

    public void dispose() {
        for (ObjectMap<String, Asset> assetMap : assetMaps.values()) {
            for (Asset asset : assetMap.values()) {
                asset.dispose();
            }
        }
        volumes.clear(0);
    }
}