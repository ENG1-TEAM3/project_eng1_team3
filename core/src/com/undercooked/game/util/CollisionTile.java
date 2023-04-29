package com.undercooked.game.util;

import com.badlogic.gdx.math.Rectangle;

public class CollisionTile {
	int tileX;
	int tileY;
	int tileHeight;
	int tileWidth;

	public CollisionTile(int x, int y, int wid, int hi) {
		tileX = x;
		tileY = y;
		tileHeight = hi;
		tileWidth = wid;
	}

	public Rectangle returnRect() {
		return new Rectangle(tileX, tileY, tileWidth, tileHeight);
	}
}
