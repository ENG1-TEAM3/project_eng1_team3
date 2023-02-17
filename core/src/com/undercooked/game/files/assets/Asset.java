package com.undercooked.game.files.assets;

/**
 *
 * TODO: JavaDocs
 *
 */

public abstract class Asset<T> {

    protected T asset;

    public Asset() { }

    public Asset(T asset) {
        setAsset(asset);
    }

    public void setAsset(T asset) {
        this.asset = asset;
    }

    public T getAsset() {
        return asset;
    }

    public void dispose() {

    }
}
