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

	public boolean slice(SpriteBatch batch, float dT) {

		shapeRenderer.setProjectionMatrix(MainGameClass.batch.getProjectionMatrix());

		if (dT / width * width <= width) {
			drawStatusBar(dT / width, 1);
		} else {
			slices++;
			texture = new Texture("items/" + name + "_chopped.png");
			return true;
		}

		MainGameClass.batch.begin();
		(new BitmapFont()).draw(MainGameClass.batch, String.valueOf(slices), pos.x * 64 + 64 + 8, pos.y * 64 + 64 + 16);
		MainGameClass.batch.end();

		draw(batch);
		return false;
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
			if (cookedTime / idealCookedTime * width > idealCookedTime * width) {
				texture = new Texture("items/" + name + "_cooked.png");
				status = Status.COOKED;
			}
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
		else
			shapeRenderer.setColor(Color.RED);

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

		if (compareTo.name.equals(name) && compareTo.idealSlices >= idealSlices && compareTo.status == status)
			return true;

		return false;
	}

}

enum Status {
	RAW, COOKED, BURNED
}
