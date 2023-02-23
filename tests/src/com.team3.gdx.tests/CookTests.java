package com.team3.gdx.tests;

import com.badlogic.gdx.math.Vector2;
import com.team3gdx.game.entity.Cook;
import com.team3gdx.game.util.CollisionTile;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.team3gdx.game.util.Control;

@RunWith(GdxTestRunner.class)
public class CookTests {
    @Test
    public void testMovement(){
        Cook cook = new Cook(new Vector2(15, 15), 1);

        Control control = new Control();
        boolean[] bools = {false, true};
        for (boolean UP : bools){
            for (boolean DOWN : bools){
                for (boolean LEFT : bools){
                    for (boolean RIGHT : bools){
                        control.up = UP;
                        control.down = DOWN;
                        control.left = LEFT;
                        control.right = RIGHT;

                        float x = cook.pos.x;
                        float y = cook.pos.y;
                        CollisionTile[][] collisionTiles = new CollisionTile[5][5];
                        cook.update(control, 5f, collisionTiles);

                        if (control.up && !control.down) {
                            assertEquals(cook.pos.y, y + cook.speed * 5f, 0.0);
                        }
                        else if (control.down && !control.up) {
                            assertEquals(cook.pos.y, y - cook.speed * 5f, 0.0);
                        }

                        if (control.left && !control.right) {
                            assertEquals(cook.pos.x, x - cook.speed * 5f, 0.0);
                        }
                        else if (control.right && !control.left) {
                            assertEquals(cook.pos.x, x + cook.speed * 5f, 0.0);
                        }
                    }
                }
            }
        }
    }
}
