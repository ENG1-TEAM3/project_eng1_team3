package com.undercooked.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument

/**
 * The Launcher class for the game.
 */
public class DesktopLauncher {
	/**
	 * The main method for the entire program.
	 * 
	 * @param arg {@link String} : The arguments for the program.
	 */
	public static void main(String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("eng1team3game");
		// config.setWindowedMode(1920,1080);
		// config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());
		new Lwjgl3Application(new MainGameClass(), config);
		// new Lwjgl3Application(new MainGameClass(new ALStateChecker()), config);
	}
}
