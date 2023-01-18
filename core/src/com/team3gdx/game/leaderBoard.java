package com.team3gdx.game;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;
import java.util.Set;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;


public class leaderBoard implements Screen, TextInputListener{
	final Eng1 game;
	
	Texture background;
	Texture line;
	Texture line2;
	Texture line3;
	Texture line4;
	Texture line5;
	Texture line6;
	Texture leaderboard;
	OrthographicCamera camera;
	FitViewport viewport;
	MainScreen ms;
	Vector2 linePosition;
	Vector2 stringPosition;
	ArrayList<String> playerData;
	ArrayList<Integer> values;
	int length;
	int count = 0;
	
	enum STATE{
		Pause,Continue, main, audio, leaderboard, new_game, draw;
	}
	STATE state;
	
	public leaderBoard(Eng1 game, MainScreen ms) {
		this.game = game;
		this.ms = ms;
		
		playerData = new ArrayList<String> ();
		values = new ArrayList<Integer> ();
		stringPosition = new Vector2(420,900);
		
		player newPlayer = game.playerManager.createPlayer("dingzhen", 95);
		player newPlayer2 = game.playerManager.createPlayer("shaluan", 88);
		player newPlayer3 = game.playerManager.createPlayer("hifumi", 80);
		player newPlayer4 = game.playerManager.createPlayer("mika", 0);
		player newPlayer5 = game.playerManager.createPlayer("mikadegou", 69);
		player newPlayer6 = game.playerManager.createPlayer("waao", 10);
		game.playerManager.savePlayer(newPlayer);
		game.playerManager.savePlayer(newPlayer2);
		game.playerManager.savePlayer(newPlayer3);
		game.playerManager.savePlayer(newPlayer4);
		game.playerManager.savePlayer(newPlayer5);
		game.playerManager.savePlayer(newPlayer6);
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false,1920,1080);
		
		viewport = new FitViewport(1920,1080,camera);
		
		background = new Texture(Gdx.files.internal("dragonflight.jpg"));
		leaderboard = new Texture(Gdx.files.internal("leaderboard .jpg"));
		line = new Texture(Gdx.files.internal("line.jpg"));
		line2 = new Texture(Gdx.files.internal("line.jpg"));
		line3 = new Texture(Gdx.files.internal("line.jpg"));
		line4 = new Texture(Gdx.files.internal("line.jpg"));
		line5 = new Texture(Gdx.files.internal("line.jpg"));
		line6 = new Texture(Gdx.files.internal("line.jpg"));
		
		game.playerManager.cleanPlayerData(game.playerManager.playerData);
		game.playerManager.reOrder(game.playerManager.playerName,game.playerManager.playerValues);
		playerData = game.playerManager.getName();
		values = game.playerManager.getValues();
		
		length = this.playerData.size();
	}
	
	

	public void show() {
		// TODO Auto-generated method stub

	}

	public void render(float delta) {
		// TODO Auto-generated method stub
		ScreenUtils.clear(0,0,0,0);
		game.batch.setProjectionMatrix(camera.combined);
		
		game.batch.begin();
		game.batch.draw(background,0,0);
		game.batch.draw(leaderboard,320,180);
		game.font.draw(game.batch,"Name",480,1000);
		game.font.draw(game.batch,"Score",1080,1000);
		game.batch.draw(line,320,780,1280,10);
		game.batch.draw(line3,320,660,1280,10);
		game.batch.draw(line4,320,540,1280,10);
		game.batch.draw(line5,320,420,1280,10);
		game.batch.draw(line6,320,300,1280,10);
		game.batch.draw(line,320,180,1280,10);
		drawString();
		game.batch.end();

		
		Gdx.input.setInputProcessor(new InputAdapter(){
			public boolean keyDown(int key) {
				if(key == Input.Keys.ESCAPE) {
					state = STATE.main;
					changeScreen(state);
				}
			return super.keyDown(key);
			}
		});
	}

	public void drawString() {
		if(length >= 1) {
			game.font.draw(game.batch,this.playerData.get(0), 420, 900);
			game.font.draw(game.batch,game.playerManager.playerData.get(this.playerData.get(0)).toString(),1120,900);
		}
		if(length >= 2) {
			game.font.draw(game.batch,this.playerData.get(1), 420, 780);
			game.font.draw(game.batch,game.playerManager.playerData.get(this.playerData.get(1)).toString(),1120,780);
		}
		if(length >= 3) {
			game.font.draw(game.batch,this.playerData.get(2), 420, 660);
			game.font.draw(game.batch,game.playerManager.playerData.get(this.playerData.get(2)).toString(),1120,660);
		}
		if(length >= 4) {
			game.font.draw(game.batch,this.playerData.get(3), 420, 540);
			game.font.draw(game.batch,game.playerManager.playerData.get(this.playerData.get(3)).toString(),1120,540);
		}
		if(length >= 5) {
			game.font.draw(game.batch,this.playerData.get(4), 420, 420);
			game.font.draw(game.batch,game.playerManager.playerData.get(this.playerData.get(4)).toString(),1120,420);
		}
		if(length >= 6) {
			game.font.draw(game.batch,this.playerData.get(5), 420, 320);
			game.font.draw(game.batch,game.playerManager.playerData.get(this.playerData.get(5)).toString(),1120,320);
		}
	}
	
	
	public void changeScreen(STATE state) {
		if(state == STATE.main) {
			game.GameMusic.pause();
			game.setScreen(new MainScreen(game,ms.t1.getX(),ms.t1.getY(),ms.t3.getX(),ms.t3.getY()));
			}
		}
	
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		viewport.update(width, height);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	public void create() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void input(String text) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void canceled() {
		// TODO Auto-generated method stub
		
	}
}
