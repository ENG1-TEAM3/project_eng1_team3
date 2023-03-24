package com.undercooked.game.util;

import com.badlogic.gdx.math.Rectangle;

public class MathUtil {

    public static double distanceBetweenRectangles(Rectangle rect1, Rectangle rect2) {

        float x1 = rect1.x + rect1.width/2,
                y1 = rect1.y + rect1.height/2;
        float x2 = rect2.x + rect2.width/2,
                y2 = rect2.y + rect2.height/2;

        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

}
