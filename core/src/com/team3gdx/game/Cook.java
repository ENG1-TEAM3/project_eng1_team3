package com.team3gdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.awt.*;

public class Cook extends Entity {

	// Cook animation
	//Animation<TextureRegion> walkAnimation;
	//Texture walkSheet;
	//TextureRegion[][] frames;
	TextureRegion[][] cookpartsf;
	TextureRegion[][] cookpartsl;
	TextureRegion[][] cookpartsr;
	TextureRegion[][] cookpartsb;
	TextureRegion[][] currentTextureRegion;
	private char direction;
	public Cook(Vector2 pos) {
		this.pos = pos;
		width = 64;
		height = 128;

		speed = 0.25f;


		Texture texturef = new Texture("entities/chef3_f_f.png");
		Texture texturel = new Texture("entities/chef3_f_l.png");
		Texture texturer = new Texture("entities/chef3_f_r.png");
		Texture textureb = new Texture("entities/chef3_f_b.png");


		cookpartsf = TextureRegion.split(texturef, 32, 32);
		cookpartsl = TextureRegion.split(texturel, 32, 32);
		cookpartsr = TextureRegion.split(texturer, 32, 32);
		cookpartsb = TextureRegion.split(textureb, 32, 32);
		currentTextureRegion = cookpartsf;
		direction = 'd';
	}

	public void update(Control control, float dt) {
		dirX = 0;
		dirY = 0;
		if (control.up) {
			if (this.checkCollision(pos.x, pos.y + (speed*dt))) {
				dirY = 1;
			}
			currentTextureRegion = cookpartsb;
			direction = 'u';
		}
		if (control.down) {
			if (this.checkCollision(pos.x, pos.y - (speed*dt))){
				dirY = -1;
			}
			currentTextureRegion = cookpartsf;
			direction = 'd';
		}
		if (control.left) {
			if (this.checkCollision(pos.x - (speed * dt), pos.y)){
				dirX = -1;
			}
			currentTextureRegion = cookpartsl;
			direction = 'l';
		}
		if (control.right) {
			if (this.checkCollision(pos.x + (speed * dt), pos.y)) {
				dirX = +1;
			}
			currentTextureRegion = cookpartsr;
			direction = 'r';
		}
		pos.x += dirX * speed * dt;
		pos.y += dirY * speed * dt;
	}

	public void draw_bot(SpriteBatch batch){
		batch.draw(currentTextureRegion[1][0], pos.x, pos.y, 64, 64);
	}
	public void draw_top(SpriteBatch batch){
		batch.draw(currentTextureRegion[0][0], pos.x, pos.y+64 , 64, 64);
	}
	Rectangle getCollideBox() {return new Rectangle(pos.x+12, pos.y - 10, 40, 25);}
	Rectangle getCollideBoxAtPosition(float x, float y) {return new Rectangle(x+12, y-10, 40, 25);}
	float[] getCollideBoxAsArray() { return new float[]{pos.x+12, pos.y - 10, 40, 25};}

	public float getX(){
		return pos.x;
	}

	public float getY(){
		return pos.y;
	}

	public float getWidth(){
		return width;
	}
	public float getHeight(){
		return height;
	}
	public float getCameraX() {
		return pos.x + width / 2;
	}

	public float getCameraY() {
		return pos.y + height / 2;
	}

	public char getDirection(){
		return direction;
	}

	public Boolean checkCollision(float cookx, float cooky){
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