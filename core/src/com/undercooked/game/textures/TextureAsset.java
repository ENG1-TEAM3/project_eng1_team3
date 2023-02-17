package com.undercooked.game.textures;

import com.badlogic.gdx.graphics.Texture;
import com.undercooked.game.files.assets.Asset;

/**
 *
 * TODO: JavaDocs
 *
 */

public class TextureAsset extends Asset<Texture> {

    public TextureAsset(Texture texture) {
        super(texture);
    }

    @Override
    public void dispose() {
        asset.dispose();
    }

}
