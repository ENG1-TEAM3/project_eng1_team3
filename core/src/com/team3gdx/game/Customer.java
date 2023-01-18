package com.team3gdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Customer {
    int targetsquare;
    int posx;
    int posy;
    Texture text1;
    TextureRegion[][] testreg;
    TextureRegion[][] custparts1;
    int startposx;
    int targetpixel;
    public boolean locked;
    public boolean readyfordeletion;
    public Customer(int x,int y, int tg){
        targetsquare = tg;
        text1 = new Texture("entities/chef3_f_f.png");
        custparts1 = TextureRegion.split(text1, 32, 32);
        posx = x*64;
        posy = y*64;
        startposx = posx;
        locked = false;
    }
    public void renderCustomersTop(Batch b){b.draw(custparts1[0][0],posx,posy + 64,64,64);}
    public void renderCustomersBot(Batch b){b.draw(custparts1[1][0],posx,posy,64,64);}
    public void setTargetsquare(int tg) {targetsquare = tg;}
    public void stepTarget(){
        targetpixel = 32 + (targetsquare *64);
        if (posy < targetpixel){
            posy ++;
        }
        else if (posy > targetpixel){
            posy --;
        }
        if (posy == targetpixel){
            if (targetpixel > 0) {
                posx = startposx + 64;
                locked = true;
            }
            else{
                readyfordeletion = true;
            }
        }
    }
}
