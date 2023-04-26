package com.team3gdx.game.PowerUp;

import com.badlogic.gdx.math.Vector2;

public class PowerUps {
    
    public static PowerUp constructionCostReduce() {
        return new PowerUp(null, 32, 32, "construction_cost_reduce", 60000);
    }
	public static PowerUp cookingSpeedReduce(){
        return new PowerUp(null, 32, 32, "cooking_speed_reduce", 60000);
    }
	public static PowerUp customerTimeIncrease() {
        return new PowerUp(null, 32, 32, "Costumer_time_increase", 60000);
    }
    public static PowerUp increasePay() {
        return new PowerUp(null, 32, 32, "Increase_pay", 60000);
    }
    public static PowerUp speedBoost() {
        return new PowerUp(null, 32, 32, "speed_boost", 60000);
    }
}
