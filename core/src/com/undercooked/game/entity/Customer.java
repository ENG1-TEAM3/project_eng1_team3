package com.undercooked.game.entity;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.undercooked.game.MainGameClass;
import com.undercooked.game.assets.TextureManager;

public class Customer {
	int targetsquare;
	public int posx;
	public int posy;
	Texture textf;
	Texture textb;
	Texture textr;
	Texture textl;

	TextureRegion[][] custpartsf;
	TextureRegion[][] custpartsb;
	TextureRegion[][] custpartsr;
	TextureRegion[][] custpartsl;
	TextureRegion[][] currentcustparts;
	int startposx;
	int targetpixel;
	public boolean locked;
	public boolean readyfordeletion;

	private long arrivalTime;

	public String order = "";

	/**
	 * Constructor for customer class
	 * @param x - x starting pixel coordinate
	 * @param y - y starting pixel coordinate
	 * @param custno - customer number - changes texture
	 * @param textureManager The {@link TextureManager} to use
	 *                       to load and get {@link Texture}s from.
	 */
	public Customer(int x, int y, int custno, TextureManager textureManager) {
		TextureManager assetManager = textureManager;
		textf = assetManager.get("entities/cust" + custno + "f.png");
		textb = assetManager.get("entities/cust" + custno + "b.png");
		textr = assetManager.get("entities/cust" + custno + "r.png");
		textl = assetManager.get("entities/cust" + custno + "l.png");

		custpartsf = TextureRegion.split(textf, 32, 32);
		custpartsb = TextureRegion.split(textb, 32, 32);
		custpartsr = TextureRegion.split(textr, 32, 32);
		custpartsl = TextureRegion.split(textl, 32, 32);

		currentcustparts = custpartsb;

		posx = x * 64;
		posy = y * 64;
		startposx = posx;
		locked = false;
	}

	/**
	 * Set arrival time as cook has arrived
	 */
	public void arrived() {
		arrivalTime = System.currentTimeMillis();
	}

	/**
	 * Check amount of time waited in ms
	 * @return amount of time waited in ms
	 */
	public long waitTime() {
		return System.currentTimeMillis() - arrivalTime;
	}

	/**
	 * Render top of customer
	 * @param b - spritebatch to render with
	 */
	public void renderCustomersTop(Batch b) {
		b.draw(currentcustparts[0][0], posx, posy + 64, 64, 64);
	}

	/**
	 * Render bottom of customer
	 * @param b - spritebatch to render with
	 */
	public void renderCustomersBot(Batch b) {
		b.draw(currentcustparts[1][0], posx, posy, 64, 64);
	}

	/**
	 * Set target square location
	 * @param tg - tile y coordinate of target square
	 */
	public void setTargetsquare(int tg) {
		targetsquare = tg;
	}

	/**
	 * Move towards customer target tile
	 */
	public void stepTarget() {
		targetpixel = 32 + (targetsquare * 64);
		if (posy < targetpixel) {
			posy++;
		} else if (posy > targetpixel) {
			posy--;
			currentcustparts = custpartsf;
		}
		if (posy == targetpixel) {
			if (targetpixel > 0) {
				posx = startposx + 64;
				locked = true;
				currentcustparts = custpartsr;
			} else {
				readyfordeletion = true;
			}
		}
	}
}
