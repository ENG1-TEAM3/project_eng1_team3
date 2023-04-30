package com.team3gdx.game.save;

public class CustomerInfo {
    public CustomerInfo() {}

    public CustomerInfo(int custNum, String order, int x, int y, int target) {

        this.custNum = custNum;
        this.order = order;
        this.x = x;
        this.y = y;
        this.target = target;
    }

    public int custNum;
    public String order;
    public int x;
    public int y;
    public int target;
}
