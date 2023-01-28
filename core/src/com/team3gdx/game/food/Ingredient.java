package com.team3gdx.game.food;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.team3gdx.game.MainGameClass;
import com.team3gdx.game.entity.Entity;
import com.team3gdx.game.screen.GameScreen;
import com.team3gdx.game.screen.GameScreen.STATE;

public class Ingredient extends Entity {

	public int slices = 0;
	public int idealSlices;
	public float cookedTime = 0;
	public float idealCookedTime;
	public boolean flipped = false;

	public Status status = Status.RAW;

	public boolean cooking = false;
	public boolean slicing = false;

	public String name;

	private static ShapeRenderer shapeRenderer = new ShapeRenderer();

	public Ingredient(Vector2 pos, float width, float height, String name, int idealSlices, float idealCookedTime) {
		this.pos = pos;
		this.width = width;
		this.height = height;
		this.name = name;
		this.texture = new Texture("items/" + name + ".png");
		this.idealSlices = idealSlices;
		this.idealCookedTime = idealCookedTime;

	}

	public Ingredient(Ingredient ingredient) {
		this.pos = ingredient.pos;
		this.width = ingredient.width;
		this.height = ingredient.height;
		this.name = ingredient.name;
		this.texture = ingredient.texture;
		this.idealSlices = ingredient.idealSlices;
		this.idealCookedTime = ingredient.idealCookedTime;
		this.cookedTime = ingredient.cookedTime;
		this.slices = ingredient.slices;
		this.flipped = ingredient.flipped;
		this.status = ingredient.status;
	}

	public boolean flip() {
		if (cookedTime / idealCookedTime < idealCookedTime)
			return false;
		return (flipped = true);
	}

	public int slice() {
		shapeRenderer.setProjectionMatrix(MainGameClass.batch.getProjectionMatrix());
		drawStatusBar(slices / idealSlices, idealSlices);
		return ++slices;
	}

	BitmapFont flipText = new BitmapFont();

	public double cook(float dT, SpriteBatch batch) {
		shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
		if (!flipped && cookedTime / idealCookedTime * width > idealCookedTime * width) {
			batch.begin();
			flipText.draw(batch, "Flip [f]", pos.x, pos.y);
			batch.end();
		}
		if (cookedTime / idealCookedTime * width <= width) {
			if (GameScreen.state1 == STATE.Continue)
				cookedTime += dT;
			drawStatusBar(cookedTime / idealCookedTime, idealCookedTime);
		} else {
			status = Status.BURNED;
			texture = new Texture("items/" + name + "_burned.png");
		}

		draw(batch);
		return cookedTime;
	}

	// Add stages (e.g. x amount of bars in total, i current, j optimal)
	private void drawStatusBar(float percentage, float optimum) {
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.WHITE);
		shapeRenderer.rect(pos.x, pos.y + height + height / 10, width, height / 8);

		if (percentage * width <= optimum * width)
			shapeRenderer.setColor(Color.GREEN);
		else {
			status = Status.COOKED;
			shapeRenderer.setColor(Color.RED);
			texture = new Texture("items/" + name + "_cooked.png");
		}

		shapeRenderer.rect(pos.x, pos.y + height + height / 10, percentage * width, height / 10);

		shapeRenderer.setColor(Color.BLACK);
		shapeRenderer.rect(pos.x + optimum * width, pos.y + height + height / 10, height / 10, 2 * height / 10);
		shapeRenderer.end();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Ingredient))
			return false;

		Ingredient compareTo = (Ingredient) o;

		if (compareTo.name.equals(name) && compareTo.flipped == flipped && compareTo.idealSlices >= idealSlices
				&& compareTo.status == status)
			return true;

		return false;
	}

}

enum Status {
	RAW, COOKED, BURNED
}
