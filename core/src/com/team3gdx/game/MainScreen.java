package com.team3gdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainScreen implements Screen{
	final Eng1 game;
	float v = 0;
	float s = 0;
	float audiox;
	float audioy;
	float soundx;
	float soundy;
	
	ImageButton sb;
	ImageButton lb;
	ImageButton ad;
	ImageButton eg;
	
	Rectangle t1;
	Rectangle t2;
	Rectangle t3;
	Rectangle t4;
	
	OrthographicCamera camera;
	Viewport viewport;
	
	Texture vButton;
	Texture vControl;
	Texture background;
	Texture startButton;
	Texture leaderBoard;
	Texture exitGame;
	Texture audio;
	Texture audioEdit;
	
	Stage stage;
	enum STATE{
		main, audio, leaderboard, new_game;
	}
	STATE state;
	
	public MainScreen(final Eng1 game, float mx, float my,float sx,float sy) {
		this.game = game;
		audiox = mx;
		audioy = my;
		soundx = sx;
		soundy = sy;
		
		t1 = new Rectangle();
		t1.width = 32;
		t1.height = 32;
		
		t2 = new Rectangle();
		t2.width = 120;
		t2.height = 20;
		
		t3 = new Rectangle();
		t3.width = 32;
		t3.height = 32;
		
		t4 = new Rectangle();
		t4.width = 120;
		t4.height = 20;
		
		t1.setPosition(audiox,audioy);
		t2.setPosition(910,265);
		t3.setPosition(soundx,soundy);
		t4.setPosition(910,165);
		
		
		state = STATE.main;
		camera = new OrthographicCamera();
		camera.setToOrtho(false,1920,1080);
		viewport = new FitViewport(1920,1080,camera);
		
		vButton = new Texture(Gdx.files.internal("vButton.jpg"));
		vControl = new Texture(Gdx.files.internal("vControl.jpg"));
		startButton = new Texture(Gdx.files.internal("startButton.jpg"));
		background = new Texture(Gdx.files.internal("dragonflight.jpg"));
		leaderBoard = new Texture(Gdx.files.internal("leaderBoard.jpg"));
		audio = new Texture(Gdx.files.internal("Audio.jpg"));
		audioEdit = new Texture(Gdx.files.internal("background.jpg"));
		exitGame = new Texture(Gdx.files.internal("Exit.jpg"));
		
		sb = new ImageButton(new TextureRegionDrawable(startButton),
					new TextureRegionDrawable(startButton));
		
		lb = new ImageButton(new TextureRegionDrawable(leaderBoard),
				new TextureRegionDrawable(leaderBoard));
		
		ad = new ImageButton(new TextureRegionDrawable(audio),
				new TextureRegionDrawable(audio));
		
		eg = new ImageButton(new TextureRegionDrawable(exitGame),
				new TextureRegionDrawable(exitGame));
		
		
		sb.setPosition(823, 600);
		lb.setPosition(823, 480);
		ad.setPosition(823, 360);
		eg.setPosition(823, 240);
		
		ad.addListener(new ClickListener(){
			public void touchUp(InputEvent event, float x, float y
					,int pointer, int button) {
				state = STATE.audio;
				super.touchUp(event, x, y, pointer, button);				
				}
			});
		sb.addListener(new ClickListener(){
			public void touchUp(InputEvent event, float x, float y
					,int pointer, int button) {
				state = STATE.new_game;
				super.touchUp(event, x, y, pointer, button);				
				}
			});
		lb.addListener(new ClickListener(){
			public void touchUp(InputEvent event, float x, float y
					,int pointer, int button) {
				state = STATE.leaderboard;
				super.touchUp(event, x, y, pointer, button);				
				}
			});
		eg.addListener(new ClickListener(){
			public void touchUp(InputEvent event, float x, float y
					,int pointer, int button) {
				if(state == STATE.main) {
					Gdx.app.exit();
				}
				super.touchUp(event, x, y, pointer, button);				
				}
			});

		stage = new Stage(viewport,game.batch);
		Gdx.input.setInputProcessor(stage);
		
		stage.addActor(sb);
		stage.addActor(lb);
		stage.addActor(ad);
		stage.addActor(eg);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		ScreenUtils.clear(0,0,0,0);
		game.batch.setProjectionMatrix(camera.combined);
		game.MianScreenMusic.play();

		game.batch.begin();
		game.batch.draw(background,0,0);
		game.batch.end();
		stage.act();
		stage.draw();
		changeScreen(state);
		
		if(Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			state = STATE.main;
			}
	}
	
	public void changeScreen(STATE state) {
		if(state == STATE.new_game) {
			game.MianScreenMusic.dispose();
			game.setScreen(new GameScreen(game,this));
		}
		if(state == STATE.leaderboard) {
			game.MianScreenMusic.dispose();
			game.setScreen(new leaderBoard(game,this));
		}
		if(state == STATE.audio) {
			mcUpdate();
			sdUpdate();
			
			game.batch.begin();
			game.batch.draw(audioEdit,760,140,400,200);
				
			game.batch.draw(vControl,t2.getX(),t2.getY(),t2.width,t2.height);
			game.batch.draw(vButton,t1.getX()-16,t1.getY(),t1.width,t1.height);
				
			game.batch.draw(vControl,t4.getX(),t4.getY(),t4.width,t4.height);
			game.batch.draw(vButton,t3.getX()-16,t3.getY(),t3.width,t3.height);
				
			game.batch.end();
		}
	}
	
	public void mcUpdate() {
		float y = Gdx.input.getY();
		y = 1080 - y;
		float x = Gdx.input.getX();
		boolean change = 345<y & y<360;
		
		if(Gdx.input.isTouched() & change == true) {
			if(x>= 910 & x<=1030) {
				t1.setPosition(Gdx.input.getX(),t1.getY());
				if(x < 925) {
					v = 0;
				}else {
					v = (Gdx.input.getX() - 910) / t2.getWidth();
				}
					
			}

			game.MianScreenMusic.setVolume(v);
			game.GameMusic.setVolume(v);
		}
	}
	
	public void sdUpdate() {
		float y = Gdx.input.getY();
		y = 1080 - y;
		float x = Gdx.input.getX();
		boolean change = 250<y & y<275;
		
		if(Gdx.input.isTouched() & change == true) {
			if(x>= 910 & x<=1030) {
				t3.setPosition(Gdx.input.getX(),t3.getY());
				if(x < 925) {
					s = 0;
				}else{
					s = (Gdx.input.getX() - 910) / t4.getWidth();
			}
			//game.sound.setVolume(game.soundid, s);
			}
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

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}
}
