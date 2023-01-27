package com.team3gdx.game.entity;

import java.util.Stack;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.team3gdx.game.food.Ingredient;
import com.team3gdx.game.util.CollisionTile;
import com.team3gdx.game.util.Control;

public class Cook extends Entity {

	public Stack<Ingredient> heldItems = new Stack<Ingredient>();
	private final int MAX_STACK_SIZE = 5;

	// Cook animation
	// Animation<TextureRegion> walkAnimation;
	// Texture walkSheet;
	// TextureRegion[][] frames;
	TextureRegion[][] cookpartsf;
	TextureRegion[][] cookpartsl;
	TextureRegion[][] cookpartsr;
	TextureRegion[][] cookpartsb;

	TextureRegion[][] ucookpartsf;
	TextureRegion[][] ucookpartsl;
	TextureRegion[][] ucookpartsr;
	TextureRegion[][] ucookpartsb;

	TextureRegion[][] currentTextureRegion;
	private char direction;
	int cookno;

	public Cook(Vector2 pos, int cooknum) {
		this.cookno = cooknum;
		this.pos = pos;
		width = 64;
		height = 128;

		speed = 0.25f;
		Texture texturef = new Texture("entities/chef"+cookno+"_f_f.png");
		Texture texturel = new Texture("entities/chef"+cookno+"_f_l.png");
		Texture texturer = new Texture("entities/chef"+cookno+"_f_r.png");
		Texture textureb = new Texture("entities/chef"+cookno+"_f_b.png");

		Texture utexturef = new Texture("entities/chef"+cookno+"_u_f.png");
		Texture utexturel = new Texture("entities/chef"+cookno+"_u_l.png");
		Texture utexturer = new Texture("entities/chef"+cookno+"_u_r.png");
		Texture utextureb = new Texture("entities/chef"+cookno+"_u_b.png");

		cookpartsf = TextureRegion.split(texturef, 32, 32);
		cookpartsl = TextureRegion.split(texturel, 32, 32);
		cookpartsr = TextureRegion.split(texturer, 32, 32);
		cookpartsb = TextureRegion.split(textureb, 32, 32);

		ucookpartsf = TextureRegion.split(utexturef, 32, 32);
		ucookpartsl = TextureRegion.split(utexturel, 32, 32);
		ucookpartsr = TextureRegion.split(utexturer, 32, 32);
		ucookpartsb = TextureRegion.split(utextureb, 32, 32);

		currentTextureRegion = cookpartsf;
		direction = 'd';
	}

	public void update(Control control, float dt, CollisionTile[][] cl) {
		dirX = 0;
		dirY = 0;
		if (control.up && !control.down) {
			if (this.checkCollision(pos.x, pos.y + (speed * dt), cl)) {
				dirY = 1;
			}
			if (this.heldItems.size() > 0){
				currentTextureRegion = ucookpartsb;
			}
			else {
				currentTextureRegion = cookpartsb;
			}
			direction = 'u';
		} else if (control.down && !control.up) {
			if (this.checkCollision(pos.x, pos.y - (speed * dt), cl)) {
				dirY = -1;
			}
			if (this.heldItems.size() > 0){
				currentTextureRegion = ucookpartsf;
			}
			else {
				currentTextureRegion = cookpartsf;
			}
			direction = 'd';
		}
		if (control.left && !control.right) {
			if (this.checkCollision(pos.x - (speed * dt), pos.y, cl)) {
				dirX = -1;
			}
			if (this.heldItems.size() > 0){
				currentTextureRegion = ucookpartsl;
			}
			else {
				currentTextureRegion = cookpartsl;
			}
			direction = 'l';
		} else if (control.right && !control.left) {
			if (this.checkCollision(pos.x + (speed * dt), pos.y, cl)) {
				dirX = +1;
			}
			if (this.heldItems.size() > 0){
				currentTextureRegion = ucookpartsr;
			}
			else {
				currentTextureRegion = cookpartsr;
			}
			direction = 'r';
		}
		pos.x += dirX * speed * dt;
		pos.y += dirY * speed * dt;

	}

	public void pickUpItem(Ingredient item) {
		item.cooking = false;
		item.slicing = false;
		if (heldItems.size() < 1) {
			changeTexture("entities/chef"+cookno+"_u_f.png");
		}
		if (!full())
			heldItems.push(item);
	}

	public boolean full() {
		return heldItems.size() >= MAX_STACK_SIZE;
	}

	public Ingredient dropItem() {
		if (heldItems.size() > 0) {
			return heldItems.pop();
		}

		return null;
	}

	private void changeTexture(String texturePath) {
		texture = new Texture(texturePath);
		currentTextureRegion = TextureRegion.split(texture, 32, 32);
	}

	public void draw_bot(SpriteBatch batch) {
		batch.draw(currentTextureRegion[1][0], pos.x, pos.y, 64, 64);
	}

	public void draw_top(SpriteBatch batch) {
		batch.draw(currentTextureRegion[0][0], pos.x, pos.y + 64, 64, 64);
	}
	
	public void draw_top(SpriteBatch batch, Vector2 position) {
		batch.draw(currentTextureRegion[0][0], position.x, position.y + 128, 128, 128);
	}

	Rectangle getCollideBox() {
		return new Rectangle(pos.x + 12, pos.y - 10, 40, 25);
	}

	Rectangle getCollideBoxAtPosition(float x, float y) {
		return new Rectangle(x + 12, y - 10, 40, 25);
	}

	float[] getCollideBoxAsArray() {
		return new float[] { pos.x + 12, pos.y - 10, 40, 25 };
	}

	public float getX() {
		return pos.x;
	}

	public float getY() {
		return pos.y;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public float getCameraX() {
		return pos.x + width / 2;
	}

	public float getCameraY() {
		return pos.y + height / 2;
	}

	public char getDirection() {
		return direction;
	}

	public Boolean checkCollision(float cookx, float cooky, CollisionTile[][] cltiles) {
		if (cooky - 10 < 0) {
			return false;
		}
		int wid = cltiles.length;
		int hi = cltiles[0].length;
		for (int x = 0; x < wid; x++) {
			for (int y = 0; y < hi; y++) {
				if (cltiles[x][y] != null) {
					if (Intersector.overlaps(cltiles[x][y].returnRect(), this.getCollideBoxAtPosition(cookx, cooky))) {
						return false;
					}
				}
			}
		}
		return true;
	}

}