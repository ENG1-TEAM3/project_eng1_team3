package com.team3gdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Cook extends Entity {

	// Cook animation
	Animation<TextureRegion> walkAnimation;
	Texture walkSheet;
	TextureRegion[][] frames;
	float frameIndex = 0;

	public Cook(Vector2 pos) {
		this.pos = pos;
		width = 64;
		height = 64;

		speed = 3.5f;

		texture = new Texture("entities/cook.png");

		walkSheet = new Texture(Gdx.files.internal("entities/player_walk.png"));
		frames = TextureRegion.split(walkSheet, walkSheet.getWidth() / 4, walkSheet.getHeight() / 4);
		walkAnimation = new Animation<TextureRegion>(0.25f, frames[0]);
	}

	public void update(Control control) {
		dirX = 0;
		dirY = 0;
		
		frameIndex += .025;

		// Remove else's to allow diagonal movement.
		if (control.up) {
			dirY = 1;
			walkAnimation = new Animation<TextureRegion>(0.25f, frames[3]);
		} else if (control.down) {
			dirY = -1;
			walkAnimation = new Animation<TextureRegion>(0.25f, frames[0]);
		} else if (control.left) {
			dirX = -1;
			walkAnimation = new Animation<TextureRegion>(0.25f, frames[1]);
		} else if (control.right) {
			dirX = 1;
			walkAnimation = new Animation<TextureRegion>(0.25f, frames[2]);
			
		} else
			frameIndex = 0;

		pos.x += dirX * speed;
		pos.y += dirY * speed;
	}
	
	public void draw(SpriteBatch batch, float elapsedTime) {
		// Get current frame of animation for the current stateTime
		TextureRegion currentFrame = walkAnimation.getKeyFrame(frameIndex, true);
		batch.draw(currentFrame, pos.x, pos.y);
	}

	// Get boundary rectangle (box around cook).
	Rectangle getBounds() {
		return new Rectangle(pos.x, pos.y, width, height);
	}

	public float getCameraX() {
		return pos.x + width / 2;
	}

	public float getCameraY() {
		return pos.y + height / 2;
	}

}