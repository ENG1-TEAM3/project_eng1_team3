package com.undercooked.game.audio;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.undercooked.game.files.assets.Asset;

/**
 *
 * TODO: JavaDocs
 *
 */

public class SoundAsset extends Asset<Sound> {

    public SoundAsset(Sound sound) {
        super(sound);
    }

    @Override
    public void dispose() {
        asset.dispose();
    }

}
