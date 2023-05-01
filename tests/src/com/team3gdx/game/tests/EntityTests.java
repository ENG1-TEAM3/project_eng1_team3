package com.team3gdx.game.tests;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.team3gdx.game.entity.Entity;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class EntityTests {

    @Test
    public void testDraw(){
        SpriteBatch batch = mock(SpriteBatch.class);
        Entity en = new Entity();
        en.pos = new Vector2(2, 2);
        en.width = 10;
        en.height = 10;
        en.draw(batch);
        verify(batch, atLeastOnce()).begin();
        verify(batch, atLeastOnce()).end();
    }

}
