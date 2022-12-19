package com.team3gdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Cook extends Entity {

	// Cook animation
	//Animation<TextureRegion> walkAnimation;
	//Texture walkSheet;
	//TextureRegion[][] frames;
	TextureRegion[][] cookparts;
	float frameIndex = 0;

	public Cook(Vector2 pos) {
		this.pos = pos;
		width = 64;
		height = 128;

		speed = 5.0f;

		texture = new Texture("entities/chef3 - front.png");

		cookparts = TextureRegion.split(texture, 32, 32);
		System.out.println();
		//walkSheet = new Texture(Gdx.files.internal("entities/player_walk.png"));
		//frames = TextureRegion.split(walkSheet, walkSheet.getWidth() / 4, walkSheet.getHeight() / 4);
		//walkAnimation = new Animation<TextureRegion>(0.25f, frames[0]);
	}

	public void update(Control control) {
		dirX = 0;
		dirY = 0;
		
		//frameIndex += .025;

		// Remove else's to allow diagonal movement.
		if (control.up && this.ck(pos.x, pos.y + speed)) {
			dirY = 1;
			//walkAnimation = new Animation<TextureRegion>(0.25f, frames[3]);
		} if (control.down && this.ck(pos.x, pos.y - speed)) {
			dirY = -1;
			//walkAnimation = new Animation<TextureRegion>(0.25f, frames[0]);
		} if (control.left && this.ck(pos.x - speed, pos.y)) {
			dirX = -1;
			//walkAnimation = new Animation<TextureRegion>(0.25f, frames[1]);
		} if (control.right && this.ck(pos.x + speed, pos.y)) {
			dirX = 1;
			//walkAnimation = new Animation<TextureRegion>(0.25f, frames[2]);
			
		} else
			frameIndex = 0;

		pos.x += dirX * speed;
		pos.y += dirY * speed;
	}

	public void draw_bot(SpriteBatch batch){
		batch.draw(cookparts[1][0], pos.x, pos.y, 64, 64);
	}
	public void draw_top(SpriteBatch batch){
		batch.draw(cookparts[0][0], pos.x, pos.y+64 , 64, 64);
	}
	Rectangle getCollideBox() {return new Rectangle(pos.x+12, pos.y - 10, 40, 25);}
	Rectangle getCollideBoxAtPosition(float x, float y) {return new Rectangle(x+12, y-10, 40, 25);}
	float[] getCollideBoxAsArray() { return new float[]{pos.x+12, pos.y - 10, 40, 25};}

	public float returnX(){
		return pos.x;
	}

	public float returnY(){
		return pos.y;
	}

	public float getCameraX() {
		return pos.x + width / 2;
	}

	public float getCameraY() {
		return pos.y + height / 2;
	}

	public Boolean ck(float cookx, float cooky){
		if (cooky -10 < 0){
			return false;
		}
		int wid = MainGameClass.CLTiles.length;
		int hi = MainGameClass.CLTiles[0].length;
		for (int x = 0; x < wid; x++){
			for (int y = 0; y < hi; y++){
				if (MainGameClass.CLTiles[x][y] != null) {
					if (Intersector.overlaps(MainGameClass.CLTiles[x][y].returnRect(), this.getCollideBoxAtPosition(cookx,cooky))) {
						return false;
					}
				}
			}
		}
		return true;
	}

}