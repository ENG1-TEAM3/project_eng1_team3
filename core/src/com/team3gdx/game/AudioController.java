// Sorry will add comments and docs soon(ish)

package com.team3gdx.game;

import java.util.HashMap;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioController {
	private HashMap<String, Music> musics = new HashMap<String, Music>();
	private HashMap<String, Sound> soundFX = new HashMap<String, Sound>();
	
	public AudioController() {
		FileHandle files = Gdx.files.internal("./bin/main/audio/music");
		for (FileHandle file: files.list()) {
			   this.musics.put(file.nameWithoutExtension(), Gdx.audio.newMusic(file));
		}
		
		files = Gdx.files.internal("./bin/main/audio/soundFX");
		for (FileHandle file: files.list()) {
			   this.soundFX.put(file.nameWithoutExtension(), Gdx.audio.newSound(file));
		}
	}
	
	public void addMusic(String fileName) {
		FileHandle toAdd = Gdx.files.local("music/" + fileName);
		if (!musics.containsKey(toAdd.nameWithoutExtension())) {this.musics.put(toAdd.nameWithoutExtension(), Gdx.audio.newMusic(toAdd));}
	}
	
	public void addSoundFX (String fileName) {
		FileHandle toAdd = Gdx.files.local("soundFX/" + fileName);
		if (!soundFX.containsKey(toAdd.nameWithoutExtension())) {this.soundFX.put(toAdd.nameWithoutExtension(), Gdx.audio.newSound(toAdd));}
	}
	
	public Music getMusic (String musicName) {
		return this.musics.get(musicName);
	}
	
	public Sound getSoundFX (String soundName) {
		return this.soundFX.get(soundName);
	}
	
	public void dispose(String audioName) {
		if (soundFX.containsKey(audioName)) {
			this.soundFX.get(audioName).dispose();
			this.soundFX.remove(audioName);
		}
		else if (musics.containsKey(audioName)) {
			this.musics.get(audioName).dispose();
			this.musics.remove(audioName);
		}
	}
	
	public void disposeAll() {
		for (Music x: this.musics.values()) {x.dispose();}
		for (Sound x: this.soundFX.values()) {x.dispose();}
		this.musics.clear();
		this.soundFX.clear();
	}
	
	public String type(String audioName) {
		if (musics.containsKey(audioName)) {return "music";}
		if (soundFX.containsKey(audioName)) {return "soundFX";}
		return null;
	}
	
	public float getPosition(String musicName) {
		return musics.get(musicName).getPosition();
	}
	
	public float getVolume(String musicName) {
		return musics.get(musicName).getVolume();
	}
	
	public boolean isLooping(String musicName) {
		return musics.get(musicName).isLooping();
	}
	
	public boolean isPlaying(String musicName) {
		return musics.get(musicName).isPlaying();
	}
	
	public void setPosition(String musicName, float position) {
		this.musics.get(musicName).setPosition(position);
	}
	
	public void setVolume(String musicName, float volume) {
		this.musics.get(musicName).setVolume(volume);
	}
	
	public void setVolume(String soundName, long soundID, float volume) {
		this.soundFX.get(soundName).setVolume(soundID, volume);
	}
	
	public long loop(String audioName) {
		if (soundFX.containsKey(audioName)) {
			return this.soundFX.get(audioName).loop();
		}
		if (musics.containsKey(audioName)) {
			this.musics.get(audioName).setLooping(true);
			if (!musics.get(audioName).isPlaying()) {this.musics.get(audioName).play();}
		}
		return -1;
	}
	
	public void stopLooping(String musicName) {
		this.musics.get(musicName).setLooping(false);
	}
	
	public long play(String audioName) {
		if (soundFX.containsKey(audioName)) {
			return this.soundFX.get(audioName).play();
		}
		if (musics.containsKey(audioName)) {
			this.musics.get(audioName).play();
		}
		return -1;
	}
	
	public long play(String soundName, float volume) {
		return this.soundFX.get(soundName).play(volume);
	}
	
	public void pause(String audioName) {
		if (soundFX.containsKey(audioName)) {
			this.soundFX.get(audioName).pause();
		}
		if (musics.containsKey(audioName)) {
			this.musics.get(audioName).pause();
		}
	}
	
	public void stop(String audioName) {
		if (soundFX.containsKey(audioName)) {
			this.soundFX.get(audioName).stop();
		}
		if (musics.containsKey(audioName)) {
			this.musics.get(audioName).stop();
		}
	}
	
	public void stop(String soundName, long soundID) {
		this.soundFX.get(soundName).stop(soundID);
	}
}
