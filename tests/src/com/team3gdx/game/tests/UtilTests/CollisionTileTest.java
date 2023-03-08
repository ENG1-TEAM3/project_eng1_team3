package com.team3gdx.game.tests.UtilTests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import com.team3gdx.game.tests.GdxTestRunner;
import com.team3gdx.game.util.CollisionTile;

@RunWith(GdxTestRunner.class)
public class CollisionTileTest {

    @Test
    public void checkrectangle() {
        //given
        CollisionTile collisionTile1 = new CollisionTile(5,5,5,5);
        assertEquals(collisionTile1.returnRect(), new Rectangle(5,5,5,5));
    }
}
