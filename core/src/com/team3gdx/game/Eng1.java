package com.team3gdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class Eng1 extends Game {
	public SpriteBatch batch;
	public BitmapFont font;
	public Music MianScreenMusic;
	public Music GameMusic;
	public Array<Sound> soundL;
	public Sound sound;
	public long soundid;
	public PlayerManager playerManager;
	
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont(Gdx.files.internal("font.fnt"),Gdx.files.internal("font.png"),false);
		font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		playerManager = new PlayerManager();
		
		MianScreenMusic = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
		GameMusic= Gdx.audio.newMusic(Gdx.files.internal("GameMusic.mp3"));
		//sound = Gdx.audio.newSound(Gdx.files.internal("assets/En1sound.wav"));
		//soundL.add(sound);
		MianScreenMusic.setLooping(false);
		MianScreenMusic.setVolume(0.5f);
		GameMusic.setLooping(false);
		GameMusic.setVolume(0.5f);
		this.setScreen(new StartScreen(this));
		

	}
	
	public void changeFontScale(float s) {
		this.font.getData().setScale(s);
	}
	
	public void render () {
		super.render();
	}
	
	public void dispose () {
		batch.dispose();
		font.dispose();
	}
}
