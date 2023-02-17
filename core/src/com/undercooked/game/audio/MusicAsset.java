package com.undercooked.game.audio;

import com.badlogic.gdx.audio.Music;
import com.undercooked.game.files.assets.Asset;

/**
 *
 * TODO: JavaDocs
 *
 */

public class MusicAsset extends Asset<Music> {

    public MusicAsset(Music music) {
        super(music);
    }

    @Override
    public void dispose() {
        asset.dispose();
    }

    public void setVolume(float volume) {
        asset.setVolume(volume);
    }

}
