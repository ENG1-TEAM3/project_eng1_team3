package com.team3gdx.game.util;

import com.badlogic.gdx.math.Rectangle;

public class CollisionTile {
	int tilex;
	int tiley;
	int tileheight;
	int tilewidth;

	public CollisionTile(int x, int y, int wid, int hi) {
		tilex = x;
		tiley = y;
		tileheight = hi;
		tilewidth = wid;
	}

	public Rectangle returnRect() {
		return new Rectangle(tilex, tiley, tilewidth, tileheight);
	}
}
