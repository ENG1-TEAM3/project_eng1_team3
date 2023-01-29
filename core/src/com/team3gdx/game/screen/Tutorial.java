package com.team3gdx.game.screen;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.team3gdx.game.MainGameClass;

public class Tutorial {

	private static List<PosTextPair> stages = new ArrayList<PosTextPair>();
	static {
		stages.add(new PosTextPair(new Vector2(64, 256),
				" Welcome to Piazza Panic! Customers will arrive one-by-one requesting an order. "));
		stages.add(new PosTextPair(GameScreen.cook.pos, " Control the cooks in the kitchen to gather ingredients. "));
		stages.add(new PosTextPair(new Vector2(10 * 64, 11 * 64), " Move to different stations: [Ingredient Station] to collect ingredients, "));
		stages.add(new PosTextPair(new Vector2(7 * 64, 11 * 64), " [Frying Station] to fry patties and other ingredients, "));
		stages.add(new PosTextPair(new Vector2(6 * 64, 5 * 64), " [Baking station] to bake bread buns, "));
		stages.add(new PosTextPair(new Vector2(6 * 64, 8 * 64), " [Preparation station] to form patties, slice ingredients, and prepare the order... "));
		stages.add(new PosTextPair(new Vector2(3 * 64, 9 * 64),
				" to then serve the customer in the shortest time. "));
		stages.add(new PosTextPair(GameScreen.cook.pos, " Goodluck! "));
	}

	public static boolean complete = false;
	public static int stage = 0;

	private static final ShapeRenderer shapeRenderer = new ShapeRenderer();
	private static final BitmapFont bitmapFont = new BitmapFont();
	private static GlyphLayout layout = new GlyphLayout();
	static {
		bitmapFont.setColor(Color.BLACK);
		bitmapFont.getData().setScale(2);
		layout.setText(bitmapFont, " [tab] to skip!");
		
		for (PosTextPair stage : stages) {
			stage.text = " " + stage.text + " ";
		}
	}

	private static float nextCharTimer = 0;
	private static String curText;
	
	public static void drawBox(float dT) {
		curText = stages.get(stage).text.substring(0, Math.round(nextCharTimer));
		shapeRenderer.setProjectionMatrix(MainGameClass.batch.getProjectionMatrix());
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.WHITE);
		shapeRenderer.rect(Gdx.graphics.getWidth() / 10f, 1 * Gdx.graphics.getHeight() / 10f,
				4 * Gdx.graphics.getWidth() / 5f, Gdx.graphics.getHeight() / 5f);
		shapeRenderer.end();
		shapeRenderer.setProjectionMatrix(GameScreen.worldCamera.combined);
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(Color.GREEN);
		shapeRenderer.rect(getStagePos().x, getStagePos().y, 64, 64);
		shapeRenderer.end();
		MainGameClass.batch.begin();
		bitmapFont.draw(MainGameClass.batch, curText, Gdx.graphics.getWidth() / 10f,
				Gdx.graphics.getHeight() / 10f + Gdx.graphics.getHeight() / 5f - bitmapFont.getCapHeight() / 2);
		bitmapFont.draw(MainGameClass.batch, "[tab] to skip!",
				Gdx.graphics.getWidth() / 10f + 4f * Gdx.graphics.getWidth() / 5f - layout.width,
				Gdx.graphics.getHeight() / 10f + bitmapFont.getCapHeight() * 1.5f);
		MainGameClass.batch.end();
		if (nextCharTimer < stages.get(stage).text.length()) {
			if (curText.length() > 0 && ".,!?".contains(curText.substring(curText.length() - 1))) {
				if (addDelay(10, dT)) {
					delay = 0;
					nextCharTimer += 1;
				}
			}
			if (delay == 0) {
				nextCharTimer += dT;
			}
		}
	}

	public static Vector2 getStagePos() {
		return stages.get(stage).pos;
	}

	public static void nextStage() {
		if (nextCharTimer < stages.get(stage).text.length()) {
			nextCharTimer = stages.get(stage).text.length();
			return;
		}

		nextCharTimer = 0;

		if (stage < stages.size() - 1)
			stage++;
		else
			complete = true;
	}

	static float delay = 0;

	private static boolean addDelay(float amount, float dT) {
		if (delay < amount)
			delay += dT;
		else
			return true;
		return false;
	}
}

class PosTextPair {

	public Vector2 pos;
	public String text;

	public PosTextPair(Vector2 pos, String text) {
		this.pos = pos;
		this.text = text;
	}

}
