package com.team3.gdx.tests;

import com.badlogic.gdx.math.Vector2;
import com.team3gdx.game.entity.Cook;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

import com.team3gdx.game.util.Control;

@RunWith(GdxTestRunner.class)
public class CookTests {
    @Test
    public void testMovement(){
        Cook cook = new Cook(new Vector2(0, 0), 1);

        Control control = new Control();
        boolean[] bools = {true, false};
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
                        cook.update(control, 5f, null);

                        if (control.up && !control.down) {
                            assertTrue(cook.pos.y == y + cook.speed * 5f);
                        }
                        else if (control.down && !control.up) {
                            assertTrue(cook.pos.y == y - cook.speed * 5f);
                        }

                        if (control.left && !control.right) {
                            assertTrue(cook.pos.x == x + cook.speed * 5f);
                        }
                        else if (control.right && !control.left) {
                            assertTrue(cook.pos.x == x - cook.speed * 5f);
                        }
                    }
                }
            }
        }
    }
}
