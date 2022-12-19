package com.team3gdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Intersector;
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
		if (control.up && checkCollision(pos.x, pos.y + speed)) {
			dirY = 1;
			walkAnimation = new Animation<TextureRegion>(0.25f, frames[3]);
		} else if (control.down && checkCollision(pos.x, pos.y - speed)) {
			dirY = -1;
			walkAnimation = new Animation<TextureRegion>(0.25f, frames[0]);
		} else if (control.left && checkCollision(pos.x - speed, pos.y)) {
			dirX = -1;
			walkAnimation = new Animation<TextureRegion>(0.25f, frames[1]);
		} else if (control.right && checkCollision(pos.x + speed, pos.y)) {
			dirX = 1;
			walkAnimation = new Animation<TextureRegion>(0.25f, frames[2]);

		} else
			frameIndex = 0;

		pos.x += dirX * speed;
		pos.y += dirY * speed;
	}

	// Basic collision detection (Doesn't work)!
	private boolean checkCollision(float x, float y) {
		// Loop over every solid object.
		for (RectangleMapObject rectangleObject : MainGameClass.solidObjects.getByType(RectangleMapObject.class)) {
			Rectangle rectangle = rectangleObject.getRectangle();
			if (Intersector.overlaps(rectangle, new Rectangle(x + 16, y, width - 32, height - 32))) {
				// Get the rectangle's position.
				float cellX = rectangleObject.getRectangle().getX(), cellY = rectangleObject.getRectangle().getY();
				// Check where the cell is relative to the player and update position in
				// opposite direction.
				if (cellX < x || cellX > x || cellY < y || cellY > y)
					return false;
			}
		}

		return true;
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