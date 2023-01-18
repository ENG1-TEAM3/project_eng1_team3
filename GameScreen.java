package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MainScreen;
import com.mygdx.game.MainScreen.STATE;

public class GameScreen implements Screen{
	final Eng1 game;
	final MainScreen ms;
	
	int reputation;
	int timeCounter;
	int numberOfcustomer;
	
	Label Reputation;
	Label TimeCounter;
	Label NumberOfcustomer;
	
	Rectangle t1;
	Rectangle t2;
	Rectangle t3;
	Rectangle t4;
	
	Texture background;
	Texture ESC;
	Texture MENU;
	Texture BACKTOMAINSCREEN;
	Texture RESUME;
	Texture AUDIO;
	Texture audioEdit;
	Texture vControl;
	Texture vButton;
	
	ImageButton mn;
	ImageButton rs;
	ImageButton ad;
	ImageButton btms;
	
	Viewport viewport;
	
	Stage stage;
	Stage stage2;
	
	OrthographicCamera camera;
	
	enum STATE{
		Pause,Continue, main, audio, leaderboard, new_game;
	}
	
	STATE state1;
	
	public GameScreen(Eng1 game, MainScreen ms) {
		this.game = game;
		this.ms = ms;
		 

		t1 = ms.t1;
		t2 = ms.t2;
		t3 = ms.t3;
		t4 = ms.t4;
		
		
		vButton = ms.vButton;
		vControl = ms.vControl;
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false,1920,1080);
		
		state1 = STATE.Continue;
		
		viewport = new FitViewport(1920,1080,camera);
		
		stage = new Stage(viewport);
		stage2 = new Stage(viewport);
		Gdx.input.setInputProcessor(stage);
		
		background = new Texture(Gdx.files.internal("dragonflight.jpg"));
		MENU = new Texture(Gdx.files.internal("MENU.jpg"));
		ESC = new Texture(Gdx.files.internal("ESC.jpg"));
		BACKTOMAINSCREEN = new Texture(Gdx.files.internal("backtomainscreen.jpg"));
		RESUME = new Texture(Gdx.files.internal("resume.jpg"));
		AUDIO =new Texture(Gdx.files.internal("audio.jpg"));
		audioEdit = new Texture(Gdx.files.internal("background.jpg"));
		
		
		mn = new ImageButton(new TextureRegionDrawable(MENU),
				new TextureRegionDrawable(MENU));
		ad = new ImageButton(new TextureRegionDrawable(AUDIO),
				new TextureRegionDrawable(AUDIO));
		rs = new ImageButton(new TextureRegionDrawable(RESUME),
				new TextureRegionDrawable(RESUME));
		btms = new ImageButton(new TextureRegionDrawable(BACKTOMAINSCREEN),
				new TextureRegionDrawable(BACKTOMAINSCREEN));
		
		mn.setPosition(100, 980);
		ad.setPosition(825, 390);
		rs.setPosition(825, 470);
		btms.setPosition(825, 550);
		
		mn.addListener(new ClickListener(){
			public void touchUp(InputEvent event, float x, float y
					,int pointer, int button) {
					state1 = STATE.Pause;		
				super.touchUp(event, x, y, pointer, button);				
				}
		});
		rs.addListener(new ClickListener(){
			public void touchUp(InputEvent event, float x, float y
					,int pointer, int button) {
					state1 = STATE.Continue;
				super.touchUp(event, x, y, pointer, button);				
				}
		});
		ad.addListener(new ClickListener(){
			public void touchUp(InputEvent event, float x, float y
					,int pointer, int button) {
					state1 = STATE.audio;
				super.touchUp(event, x, y, pointer, button);				
				}
		});
		btms.addListener(new ClickListener(){
			public void touchUp(InputEvent event, float x, float y
					,int pointer, int button) {
					state1 = STATE.main;
				super.touchUp(event, x, y, pointer, button);				
				}
		});
		
		
		stage.addActor(mn);
		stage2.addActor(rs);
		stage2.addActor(btms);
		stage2.addActor(ad);

	}

	public void show() {
		// TODO Auto-generated method stub
		
	}

	public void render(float delta) {
		// TODO Auto-generated method stub
		ScreenUtils.clear(0,0,0,0);
		game.batch.setProjectionMatrix(camera.combined);
		game.GameMusic.play();
		
		game.batch.begin();
		game.batch.draw(background,0,0);
		game.batch.end();
		stage.act();
		stage.draw();
		
		changeScreen(state1);		
		


	}

	
	public void changeScreen(STATE state1) {
		if(state1 == STATE.main) {
			game.GameMusic.dispose();
			game.setScreen(new MainScreen(game,t1.getX(),t1.getY(),t3.getX(),t3.getY()));
		}else if(state1 == STATE.Pause) {
			Gdx.input.setInputProcessor(stage2);
			game.batch.begin();
			game.batch.draw(ESC,810,380,300,320);
			game.batch.end();
			stage2.act();
			stage2.draw();
		}else if(state1 == STATE.audio) {
			mcUpdate();
			sdUpdate();
			checkState();
			
			Gdx.input.setInputProcessor(stage2);
			game.batch.begin();
			game.batch.draw(ESC,810,380,300,320);
			game.batch.end();
			stage2.act();
			stage2.draw();
			
			game.batch.begin();
			game.batch.draw(audioEdit,860,120,200,200);
				
				
			game.batch.draw(vControl,t2.getX(),t2.getY(),t2.width,t2.height);
			game.batch.draw(vButton,t1.getX()-16,t1.getY(),t1.width,t1.height);
				
			game.batch.draw(vControl,ms.t4.getX(),t4.getY(),t4.width,ms.t4.height);
			game.batch.draw(vButton,ms.t3.getX()-16,t3.getY(),t3.width,ms.t3.height);
				
			game.batch.end();
			
		}else if(state1 == STATE.audio) {
			
		}
		}
	public void checkState() {
		if(Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			state1 = STATE.Pause;
		}
	}

	public void mcUpdate() {
		float y = Gdx.input.getY();
		y = 1080 - y;
		float x = Gdx.input.getX();
		boolean change = 345<y & y<360;
		
		if(Gdx.input.isTouched() & change == true) {
			if(x>= 910 & x<=1030) {
				ms.t1.setPosition(Gdx.input.getX(),ms.t1.getY());
				if(x < 925) {
					ms.v = 0;
				}else {
					ms.v = (Gdx.input.getX() - 910) / ms.t2.getWidth();
				}
					
			}

			game.MianScreenMusic.setVolume(ms.v);
			game.GameMusic.setVolume(ms.v);
		}
	}
	
	public void sdUpdate() {
		float y = Gdx.input.getY();
		y = 1080 - y;
		float x = Gdx.input.getX();
		boolean change = 250<y & y<275;
		
		if(Gdx.input.isTouched() & change == true) {
			if(x>= 910 & x<=1030) {
				ms.t3.setPosition(Gdx.input.getX(),ms.t3.getY());
				if(x < 925) {
					ms.s = 0;
				}else{
					ms.s = (Gdx.input.getX() - 910) / ms.t4.getWidth();
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
}
