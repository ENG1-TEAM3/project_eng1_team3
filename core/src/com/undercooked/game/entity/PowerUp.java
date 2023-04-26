package com.undercooked.game.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.util.Listener;

public class PowerUp extends Entity {

    private PowerUpType type;
    boolean inUse = false;
    float useTimer = 0;
    private float despawnTimer;
    private Listener<PowerUp> removeListener;

    public PowerUp() {
        super();
        collision.setSize(30,30);
    }

    @Override
    public void postLoad(TextureManager textureManager) {
        super.postLoad(textureManager);
        sprite.setSize(48,48);
    }

    @Override
    public void update(float delta) {
        collision.x = pos.x - collision.getWidth()/2f;
        collision.y = pos.y - collision.getHeight()/2f;

        // If it's in use, then slowly decrease the timer
        if (inUse) {
            useTimer -= delta;
            // If use Timer <= 0, then remove
            if (useTimer <= 0) {
                // It has finished, so remove
                remove();
            }
            return;
        }

        // Only decrease the timer if it's >= 0
        if (despawnTimer >= 0) {
            despawnTimer -= delta;
            if (despawnTimer <= 0) {
                // If it's <= 0, then despawn
                remove();
            }
        }
    }

    public void remove() {
        if (removeListener == null) return;
        removeListener.tell(this);
    }

    public boolean checkCollision(Entity entity) {
        // If it's in use, then ignore
        if (inUse) return false;
        // Check collision
        return isColliding(entity);
    }

    public void use() {
        inUse = true;
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(sprite, pos.x-sprite.getWidth()/2f, pos.y-sprite.getHeight()/2f, sprite.getWidth(), sprite.getHeight());
    }

    public void setType(PowerUpType type) {
        this.type = type;
    }

    public void setUseTimer(float useTimer) {
        this.useTimer = useTimer;
    }

    public void setDespawnTime(float despawnTime) {
        this.despawnTimer = despawnTime;
    }

    public void setRemoveListener(Listener<PowerUp> listener) {
        this.removeListener = listener;
    }

    public PowerUpType getType() {
        return this.type;
    }

    public boolean isInUse() {
        return inUse;
    }
}
