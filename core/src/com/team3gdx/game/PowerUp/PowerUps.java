package com.team3gdx.game.PowerUp;

import com.badlogic.gdx.math.Vector2;

public class PowerUps {
    
    public static PowerUp constructionCostReduce() {
        return new PowerUp(null, 64, 64, "construction_cost_reduce", 60);
    }
	public static PowerUp cookingSpeedReduce(){
        return new PowerUp(null, 64, 64, "cooking_speed_reduce", 60);
    }
	public static PowerUp customerTimeIncrease() {
        return new PowerUp(null, 64, 64, "Customer_time_increase", 60);
    }
    public static PowerUp increasePay() {
        return new PowerUp(null, 64, 64, "Increase_pay", 60);
    }
    public static PowerUp speedBoost() {
        return new PowerUp(null, 64, 64, "speed_boost", 60);
    }
}
