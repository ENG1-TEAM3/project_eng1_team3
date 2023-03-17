package com.undercooked.game.util;

import com.badlogic.gdx.utils.JsonValue;
import com.undercooked.game.files.FileControl;

/**
 * Use this static class to manipulate the leaderboards using the enum
 * LeaderboardNames.
 * <p>
 * MUST CALL {@link #initLeaderboard()} BEFORE ANY OTHER METHOD IN
 * THIS CLASS.
 * </p>
 */
public final class Leaderboard {
	public enum LeaderboardNames {
		SCENARIO,
		ENDLESS
	};

	JsonValue entries;
	String leaderboardDir = "assets/defaults/";
	String leaderboardFile = "leaderboard.json";
	String leaderboardPath = leaderboardDir + leaderboardFile;

	/**
	 * The default leaderboard to use.
	 * <p>
	 * Initially null. Use the method {@link #setDefaultLeaderboard()}
	 * to set it.
	 * </p>
	 * Merely for convenience and you may choose not to use this, and
	 * instead specify the desired leaderboardName in each method in
	 * this class.
	 */
	LeaderboardNames leaderboardName = null;

	/**
	 * Initialise the leaderboard.
	 * <p>
	 * This method MUST be called before any other method in this class.
	 * </p>
	 */
	public void initLeaderboard() {
		entries = FileControl.loadJsonData(leaderboardPath);
		// Create leaderboard.json if it doesn't exist
		if (entries == null)
			FileControl.loadFile(leaderboardDir, leaderboardFile);
			FileControl.saveData(leaderboardFile,
			String.join("\n",
			"{",
			"\t\"Example\": \"leaderboard\",",
			"\t\"SCENARIO\": [",
			"\t\t\"scores\": []",
			"],",
			"\t\"ENDLESS\": [",
			"\t\t\"scores\": []",
			"],",
			"}")
			);
	}

	/**
	 * Set the default leaderboard to use. Saves you from having to
	 * specify the leaderboardName in each method in this class.
	 * @param leaderboardName The name of the default leaderboard to use.
	 */
	public void setDefaultLeaderboard(LeaderboardNames leaderboardName) {
		this.leaderboardName = leaderboardName;
	}

	public void addEntry(LeaderboardNames lName, int score) {
		JsonValue newEntry = new
		entries.get(lName.toString()).get("scores");
	}

	public void removeEntry(int index) {
		// TODO: Remove entry from leaderboard
	}

	public JsonValue getLeaderboard(LeaderboardNames lName) {
		return entries.get(lName.toString()).get("scores");
	}

	public void saveLeaderboard() {
		FileControl.saveJsonData(leaderboardPath, entries);
	}
}
