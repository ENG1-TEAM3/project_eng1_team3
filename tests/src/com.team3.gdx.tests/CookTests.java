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
                            assertEquals("up and not down", cook.pos.y, y + cook.speed * 5f, 0.0);
                        }
                        else if (control.down && !control.up) {
                            assertEquals("down and not up", cook.pos.y, y - cook.speed * 5f, 0.0);
                        }

                        if (control.left && !control.right) {
                            assertEquals("left and not right", cook.pos.x, x - cook.speed * 5f, 0.0);
                        }
                        else if (control.right && !control.left) {
                            assertEquals("right and not left", cook.pos.x, x + cook.speed * 5f, 0.0);
                        }

                        if (!control.left && !control.right && !control.up && !control.down){
                            assertEquals("none pressed x", cook.pos.x, x, 0.0);
                            assertEquals("none pressed y", cook.pos.y, y, 0.0);
                        }

                        if (control.left && control.right){
                            assertEquals("left and right", cook.pos.x, x, 0.0);
                        }

                        if (control.up && control.down){
                            assertEquals("up and down", cook.pos.y, y, 0.0);
                        }
                    }
                }
            }
        }
    }

    @Test
    public void testPickUp(){

    }

    @Test
    public void testPlace(){

    }

    private void testCollisionAttempt(Cook cook, Control control, CollisionTile[][] cltiles, String assertMessage){
        cook.update(control, 1f, cltiles);
        assertTrue(assertMessage, cook.pos.x == 50 && cook.pos.y == 50);
        cook.pos.x = 50;
        cook.pos.y = 50;
        control.up = false;
        control.down = false;
        control.left = false;
        control.right = false;
    }
    @Test
    public void testCollision(){


        Cook cook = new Cook(new Vector2(50, 50), 1);
        Control control = new Control();
        CollisionTile[][] cltiles = new CollisionTile[3][3];

        // make all tiles same with and height as cook rectangle to make things easier
        int x = 22;
        int y = 65;
        for(int i = 0; i < cltiles.length; i++){
            x = 22;
            for(int j = 0; j < cltiles[i].length; j++){
                if(!(i == 1 && j == 1)){
                    cltiles[i][j] = new CollisionTile(x, y, 40, 25);
                    x += 40;
                }
            }
            y -= 25;
        }

        // no key pressed
        cook.update(control, 1f, cltiles);
        assertTrue("no key pressed", cook.pos.x == 50 && cook.pos.y == 50);

        // up and left
        control.up = true;
        control.left = true;
        testCollisionAttempt(cook, control, cltiles, "up and left pressed");

        // up
        control.up = true;
        testCollisionAttempt(cook, control, cltiles, "up pressed");

        // up and right
        control.up = true;
        control.right = true;
        testCollisionAttempt(cook, control, cltiles, "up and right pressed");

        // left
        control.left = true;
        testCollisionAttempt(cook, control, cltiles, "left pressed");

        // right
        control.right = true;
        testCollisionAttempt(cook, control, cltiles, "right pressed");

        // down and left
        control.down = true;
        control.left = true;
        testCollisionAttempt(cook, control, cltiles, "down and left pressed");

        // down
        control.down = true;
        testCollisionAttempt(cook, control, cltiles, "down pressed");

        // down and right
        control.down = true;
        control.right = true;
        testCollisionAttempt(cook, control, cltiles, "down and right pressed");
    }
}
