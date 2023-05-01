package com.team3gdx.game.tests.PowerUps;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import com.team3gdx.game.tests.GdxTestRunner;

import com.team3gdx.game.PowerUp.*;

@RunWith(GdxTestRunner.class)
public class PowerUpTests {
    PowerUp test1 = PowerUp(constructionCostReduce());
    PowerUp test2 = PowerUp(cookingSpeedReduce());
    PowerUp test3 = PowerUp(customerTimeIncrease());
    PowerUp test4 = PowerUp(increasePay());
    PowerUp test5 = PowerUp(speedBoost());
}
