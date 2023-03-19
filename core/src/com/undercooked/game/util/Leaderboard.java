package com.undercooked.game.util;

import com.badlogic.gdx.utils.Json;
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
	private class LeaderboardEntry {
		public String name;
		public int time;

		public LeaderboardEntry(String name, int time) {
			this.name = name;
			this.time = time;
		}
	}

	public enum LeaderboardNames {
		SCENARIO,
		ENDLESS
	};

	static Json json;
	/** Root JsonValue of Leaderboard. Contains:
	 * <ul>
		* <li>"Example": "leaderboard",
		* <li>"SCENARIO": [...],<br>
		* <li>"ENDLESS": [...]
	 * </ul>
	 */
	static JsonValue root;
	static String leaderboardDir = "assets/defaults/";
	static String leaderboardFile = "leaderboard.json";
	static String leaderboardPath = leaderboardDir + leaderboardFile;

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
	static LeaderboardNames leaderboardName = null;

	/**
	 * Initialise the leaderboard.
	 * <p>
	 * This method MUST be called before any other method in this class.
	 * </p>
	 */
	public static void initLeaderboard() {
		json = new Json();
		root = FileControl.loadJsonData(leaderboardPath);
		// Create leaderboard.json if it doesn't exist
		if (root == null)
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
	 * @param leaderboardNameIn The name of the default leaderboard to use.
	 */
	public static void setDefaultLeaderboard(LeaderboardNames leaderboardNameIn) {
		leaderboardName = leaderboardNameIn;
	}

	public static void addEntry(LeaderboardNames lName, String name, int score) {
		LeaderboardEntry entry = new LeaderboardEntry(name, score);
		root.get(lName.toString()).get("scores").addChild(new JsonValue(json.toJson(entry)));
	}

	public static void removeEntry(LeaderboardNames lName, int index) {
		root.get(lName.toString()).get("scores").remove(index);
	}

	public static JsonValue getLeaderboard(LeaderboardNames lName) {
		return root.get(lName.toString()).get("scores");
	}

	public static void saveLeaderboard() {
		FileControl.saveJsonData(leaderboardPath, root);
	}
	public static void main(String[] args) {
		Leaderboard.initLeaderboard();
		Leaderboard.addEntry(LeaderboardNames.SCENARIO, "test", 100);
		Leaderboard.saveLeaderboard();
	}
}
