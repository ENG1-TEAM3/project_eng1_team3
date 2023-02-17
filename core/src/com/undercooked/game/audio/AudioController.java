package com.undercooked.game.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.ObjectMap;

/**
 *
 * TODO: JavaDocs
 *
 */

public class AudioController {
	private static final ObjectMap<String, Float> volumes = new ObjectMap<>();
	private static final ObjectMap<String, Music> musics = new ObjectMap<>();
	private static final ObjectMap<String, Sound> soundFX = new ObjectMap<>();

	static {
		// Maps music directory
		FileHandle files = Gdx.files.internal("./bin/main/audio/music");
		for (FileHandle file : files.list()) {
			musics.put(file.nameWithoutExtension(), Gdx.audio.newMusic(file));
		}

		// Maps soundFX directory
		files = Gdx.files.internal("./bin/main/audio/soundFX");
		for (FileHandle file : files.list()) {
			soundFX.put(file.nameWithoutExtension(), Gdx.audio.newSound(file));
		}

		// Set up the volumes
	}

	/**
	 * Adds audio file to library as music
	 * 
	 * @param fileName : String - Name of file to be added (include file extension)
	 */
	public void addMusic(String fileName) {
		FileHandle toAdd = Gdx.files.local("music/" + fileName);
		if (!musics.containsKey(toAdd.nameWithoutExtension())) {
			this.musics.put(toAdd.nameWithoutExtension(), Gdx.audio.newMusic(toAdd));
		}
	}

	/**
	 * Adds audio file to library as sound effect
	 * 
	 * @param fileName : String - Name of file to be added (include file extension)
	 */
	public void addSoundFX(String fileName) {
		FileHandle toAdd = Gdx.files.local("soundFX/" + fileName);
		if (!soundFX.containsKey(toAdd.nameWithoutExtension())) {
			this.soundFX.put(toAdd.nameWithoutExtension(), Gdx.audio.newSound(toAdd));
		}
	}

	/**
	 * Returns instance of music object
	 * 
	 * @param musicName : String - Name of music to be returned (file name without
	 *                  extension)
	 * @return Instance of music object : Music
	 */
	public Music getMusic(String musicName) {
		return this.musics.get(musicName);
	}

	/**
	 * Returns instance of sound object
	 * 
	 * @param soundName : String - Name of music to be returned (file name without
	 *                  extension)
	 * @return Instance of sound object : Sound
	 */
	public Sound getSoundFX(String soundName) {
		return this.soundFX.get(soundName);
	}

	/**
	 * Access Sound.dispose() or Music.dispose() for individual piece of audio and
	 * remove it from map
	 * 
	 * @param audioName : String - Name of audio instance to be disposed (file name
	 *                  without extension)
	 */
	public void dispose(String audioName) {
		if (soundFX.containsKey(audioName)) {
			this.soundFX.get(audioName).dispose();
			this.soundFX.remove(audioName);
		} else if (musics.containsKey(audioName)) {
			this.musics.get(audioName).dispose();
			this.musics.remove(audioName);
		}
	}

	/**
	 * Disposes every audio instance in map
	 */
	public void disposeAll() {
		for (Music x : this.musics.values()) {
			x.dispose();
		}
		for (Sound x : this.soundFX.values()) {
			x.dispose();
		}
		this.musics.clear();
		this.soundFX.clear();
	}

	/**
	 * Returns weather the given piece of audio is an instance of music or sound
	 * 
	 * @param audioName : String - Name of audio to be checked file (file name
	 *                  without extension)
	 * @return "music" if object is music or "soundFX" if object is sound : String
	 */
	public String type(String audioName) {
		if (musics.containsKey(audioName)) {
			return "music";
		}
		if (soundFX.containsKey(audioName)) {
			return "soundFX";
		}
		return null;
	}

	/**
	 * Returns position of play back in seconds for given music
	 * 
	 * @param musicName : String - Name of piece of music (file name without
	 *                  extension)
	 * @return Position of play back in seconds : float
	 */
	public float getPosition(String musicName) {
		return musics.get(musicName).getPosition();
	}

	/**
	 * Returns the volume of a given piece of music
	 * 
	 * @param musicName : String - Name of a piece of music (file name without
	 *                  extension)
	 * @return Volume of music with 1.0 equating to 100% : float
	 */
	public float getVolume(String musicName) {
		return musics.get(musicName).getVolume();
	}

	/**
	 * Returns weather a piece of music is looping
	 * 
	 * @param musicName : String - Name of a piece of music (file name without
	 *                  extension)
	 * @return Boolean
	 */
	public boolean isLooping(String musicName) {
		return musics.get(musicName).isLooping();
	}

	/**
	 * Returns weather a piece of music is playing
	 * 
	 * @param musicName : String - Name of a piece of music (file name without
	 *                  extension)
	 * @return Boolean
	 */
	public boolean isPlaying(String musicName) {
		return musics.get(musicName).isPlaying();
	}

	/**
	 * Sets the position of play back in seconds for a given piece of music
	 * 
	 * @param musicName : String - Name of a piece of music (file name without
	 *                  extension)
	 * @param position  : float - Play back position in seconds
	 */
	public void setPosition(String musicName, float position) {
		this.musics.get(musicName).setPosition(position);
	}

	/**
	 * Sets the volume of a given piece of music
	 * 
	 * @param musicName : String - Name of a piece of music (file name without
	 *                  extension)
	 * @param volume    : float - Volume for music (with 1.0 corresponding to 100%)
	 */
	public void setVolume(String musicName, float volume) {
		this.musics.get(musicName).setVolume(volume);
	}

	/**
	 * Sets the volume of a given sound effect
	 * 
	 * @param soundName : String - Name of a sound effect (file name without
	 *                  extension)
	 * @param soundID   : long - ID of a sound instance (Returned when sound was
	 *                  played)
	 * @param volume    : float - Volume for music (with 1.0 corresponding to 100%)
	 */
	public void setVolume(String soundName, long soundID, float volume) {
		this.soundFX.get(soundName).setVolume(soundID, volume);
	}

	/**
	 * Plays and loops a given piece of audio. Music will start paying if it isn't
	 * already
	 * 
	 * @param audioName : String - Name of a piece of audio (file name without
	 *                  extension)
	 * @return soundID of a sound effect (-1 if not a sound effect given) : long
	 */
	public long loop(String audioName) {
		if (soundFX.containsKey(audioName)) {
			return this.soundFX.get(audioName).loop();
		}
		if (musics.containsKey(audioName)) {
			this.musics.get(audioName).setLooping(true);
			if (!musics.get(audioName).isPlaying()) {
				this.musics.get(audioName).play();
			}
		}
		return -1;
	}

	/**
	 * Will stop a piece of music from looping - it will still play to the end of
	 * the piece (Will not stop a sound effect where the .stop() method must be
	 * used)
	 * 
	 * @param musicName : String - Name of a piece of music (file name without
	 *                  extension)
	 */
	public void stopLooping(String musicName) {
		this.musics.get(musicName).setLooping(false);
	}

	/**
	 * Plays a given piece of audio
	 * 
	 * @param audioName : String - Name of a piece of audio (file name without
	 *                  extension)
	 * @return soundID of a sound effect (-1 if not a sound effect given) : long
	 */
	public long play(String audioName) {
		if (soundFX.containsKey(audioName)) {
			return this.soundFX.get(audioName).play();
		}
		if (musics.containsKey(audioName)) {
			this.musics.get(audioName).play();
		}
		return -1;
	}

	/**
	 * Plays a given sound effect at a specified volume
	 * 
	 * @param soundName : String - Name of a sound effect (file name without
	 *                  extension)
	 * @param volume    : float - Volume to play audio at (1.0 equating to 100%)
	 * @return soundID of a sound effect (-1 if not a sound effect given) : long
	 */
	public long play(String soundName, float volume) {
		return this.soundFX.get(soundName).play(volume);
	}

	/**
	 * Pauses a piece of audio - .play() method will resume from pre-paused position
	 * (all instances of a sound effect)
	 * 
	 * @param audioName : String - Name of a piece of audio (file name without
	 *                  extension)
	 */
	public void pause(String audioName) {
		if (soundFX.containsKey(audioName)) {
			this.soundFX.get(audioName).pause();
		}
		if (musics.containsKey(audioName)) {
			this.musics.get(audioName).pause();
		}
	}

	/**
	 * Stops a piece of audio (all instances of a sound effect)
	 * 
	 * @param audioName : String - Name of a piece of audio (file name without
	 *                  extension)
	 */
	public void stop(String audioName) {
		if (soundFX.containsKey(audioName)) {
			this.soundFX.get(audioName).stop();
		}
		if (musics.containsKey(audioName)) {
			this.musics.get(audioName).stop();
		}
	}

	/**
	 * Stops a specific instance of a sound effect given
	 * 
	 * @param soundName : String - Name of a sound effect (file name without
	 *                  extension)
	 * @param soundID   : long - ID of a sound instance (Returned when sound was
	 *                  played)
	 */
	public void stop(String soundName, long soundID) {
		this.soundFX.get(soundName).stop(soundID);
	}
}
