package com.undercooked.game.util.leaderboard;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.undercooked.game.files.FileControl;
import com.undercooked.game.util.Constants;
import com.undercooked.game.util.StringUtil;
import com.undercooked.game.util.json.JsonFormat;

/**
 * Use this static class to manipulate the leaderboards using the enum
 * LeaderboardNames.
 * <p>
 * {@link #loadLeaderboard()} must be called before any of the functions
 * for interacting with the leaderboards in this class can be used.
 * Otherwise, they will all fail.
 * </p>
 */
public final class Leaderboard {

	/**
	 * Converts one of the {@link LeaderboardType} to a {@link String}
	 * value. This is the name of them inside the json file.
	 * @param leaderboardType {@link LeaderboardType} : The name to get.
	 * @return {@link String} : The name of the leaderboard.
	 */
	public static String getLeaderboardName(LeaderboardType leaderboardType) {
		// If it's null, return null
		if (leaderboardType == null) return "null";
		// Otherwise, return the name
		switch (leaderboardType) {
			case ENDLESS:
				return "endless";
			case SCENARIO:
				return "scenarios";
		}
		// Shouldn't reach here
		return "error";
	}

	/**
	 * Converts a score value to a {@link String} formatted based
	 * on what {@link LeaderboardType} it should use.
	 *
	 * @param currentLType {@link LeaderboardType} : The leaderboard to use.
	 * @param score {@code float} : The score to get a {@link String} of.
	 * @return
	 */
	public static String scoreToString(LeaderboardType currentLType, float score) {
		if (currentLType == null) return Float.toString(score);
		switch (currentLType) {
			case SCENARIO:
				return StringUtil.formatSeconds(score);
			case ENDLESS:
				return Integer.toString((int) score);
		}
		// Shouldn't reach here
		return "error";
	}

	/**
	 * Returns a comparison of the scores based on {@link LeaderboardType}.
	 * This is because Scenarios favours lower time taken, while Endless mode
	 * favours higher customers served.
	 * <br>0 means they are equal.
	 * <br>1 means score1 is better.
	 * <br>-1 means score2 is better.
	 *
	 * @param leaderboardType {@link LeaderboardType} : The leaderboard to check.
	 * @param score1 {@link float} : The score of the first entry.
	 * @param score2 {@link float} : The score of the second entry.
	 * @return
	 */
	public static int compareScore(LeaderboardType leaderboardType, float score1, float score2) {
		if (leaderboardType == null) return 0;
		switch (leaderboardType) {
			case SCENARIO:
				// score being lower is better
				if (score1 < score2) {
					return 1;
				} else if (score2 < score1) {
					return -1;
				}
				return 0;

			case ENDLESS:
				// score being higher is better
				if (score1 > score2) {
					return 1;
				} else if (score2 > score1) {
					return -1;
				}
				return 0;
		}
		// Shouldn't reach here
		return 0;
	}

	static Json json;
	/** Root JsonValue of Leaderboard. Contains:
	 * <ul>
		* <li>"Example": "leaderboard",
		* <li>"SCENARIO": [...],<br>
		* <li>"ENDLESS": [...]
	 * </ul>
	 */
	static JsonValue root;
	static String leaderboardFile = "leaderboard.json";

	/**
	 * Loads the leaderboard.
	 * <p>
	 * This method MUST be called before any other method in this class.
	 * </p>
	 */
	public static void loadLeaderboard() {
		json = new Json();
		root = FileControl.loadJsonData(leaderboardFile);
		// Create leaderboard.json if it doesn't exist
		if (root == null) {
			// Create a new JsonValue, and then format it using the leaderboard format
			root = new JsonValue(JsonValue.ValueType.object);
		}
		// Format the Json
		JsonFormat.formatJson(root, Constants.DefaultJson.leaderboardFormat());
	}

	/**
	 * Unloads the leaderboard, if it is no longer needed.
	 * <br>
	 * If the leaderboard is not needed, such as when the game
	 * is running, then this function can be run and the leaderboard
	 * entries will be deleted by the garbage collector.
	 */
	public static void unloadLeaderboard() {
		root = null;
	}

	public static void addEntry(LeaderboardType lType, String id, String name, float score) {
		// Only continue if root is not null
		if (root == null) return;

		// Get the leaderboard by id
		JsonValue leaderboard = getLeaderboard(lType, id);
		// If it's null, then add the id
		if (leaderboard == null) {
			leaderboard = addLeaderboard(lType, id);
		}

		// Create the JsonValue for it
		JsonValue newEntry = new JsonValue(JsonValue.ValueType.object);
		JsonValue nameEntry = new JsonValue(name);
		JsonValue scoreEntry = new JsonValue(score, "score");

		newEntry.addChild("name", nameEntry);
		newEntry.addChild("score", scoreEntry);

		// And then add it to the leaderboard
		leaderboard.get("scores").addChild(id, newEntry);
	}

	private static JsonValue addLeaderboard(LeaderboardType lType, String id) {
		// First, get the leaderboards
		JsonValue leaderboards = getLeaderboard(lType);
		// Set up the new leaderboard json
		JsonValue newLeaderboard = new JsonValue(JsonValue.ValueType.object);
		JsonValue leaderboardID = new JsonValue(id);
		JsonValue leaderboardScores = new JsonValue(JsonValue.ValueType.array);
		newLeaderboard.addChild("id", leaderboardID);
		newLeaderboard.addChild("scores", leaderboardScores);

		// And then add it
		leaderboards.addChild(id, newLeaderboard);
		// And return the new leaderboard that was created
		return newLeaderboard;
	}

	/**
	 * Returns the sorted scores of a leaderboard using the id provided.
	 * @param lType {@link LeaderboardType} : The leaderboard to use.
	 * @param id {@link String} : The id of the leaderboard.
	 * @return
	 */
	public static Array<LeaderboardEntry> getEntries(LeaderboardType lType, String id) {
		// Get the leaderboard by id
		JsonValue leaderboard = getLeaderboard(lType, id);
		// Only continue if it's not null
		if (leaderboard == null) return null;

		// Now create an array, and put all the values into an
		// array of LeaderboardEntries
		Array<LeaderboardEntry> entryArray = new Array<>();
		for (JsonValue entryData : leaderboard.get("scores")) {
			// System.out.println(entryData);
			// Make a new LeaderboardEntry with the name and score
			LeaderboardEntry newEntry = new LeaderboardEntry(entryData.getString("name"), entryData.getFloat("score"));

			// Add it to the array
			addToEntryArray(lType, entryArray, newEntry);
		}

		// Then return the array
		return entryArray;
	}

	public static void addToEntryArray(LeaderboardType lType, Array<LeaderboardEntry> entryArray, LeaderboardEntry newEntry) {
		// Add it to the array, sorted from best to worst
		for (int i = 0 ; i < entryArray.size ; i++){
			// Check if it's better than the current index
			if (compareScore(lType, newEntry.score, entryArray.get(i).score) == 1) {
				// If it's better, then insert it here
				entryArray.insert(i, newEntry);
				// Stop the function here.
				return;
			}
		}
		// If it doesn't succeed, then just add it to the end
		entryArray.add(newEntry);
	}

	private static JsonValue getScores(JsonValue array, String id) {
		// If the data hasn't been loaded, then return null
		if (root == null) return null;
		// If array is null, return null
		if (array == null) return null;

		// If it's not an array, return
		if (!array.isArray()) return null;
		// Loop through the array, and find the matching id
		for (int i = 0 ; i < array.size ; i++) {
			// Get the current index entry
			JsonValue thisEntry = array.get(i);
			// Check if it matches
			if (thisEntry.getString("id").equals(id)) {
				// If that's the case, then return it
				return thisEntry;
			}
		}
		// Otherwise, if nothing matching is found, return null
		return null;
	}

	public static boolean removeEntry(LeaderboardType lType, String id, int index) {
		// Get the leaderboard by id
		JsonValue leaderboard = getLeaderboard(lType, id);
		// Only continue if it's not null
		if (leaderboard == null) return false;

		// Check that the index is a valid number
		if (index < 0 || index >= leaderboard.size) return false;

		// Then, remove it
		leaderboard.remove(index);

		// Return true, as it was successfully removed
		return true;
	}

	public static boolean removeEntry(LeaderboardType lType, String id, String name) {
		// Get the leaderboard by id
		JsonValue leaderboard = getLeaderboard(lType, id);
		// Only continue if it's not null
		if (leaderboard == null) return false;

		boolean entryRemoved = false;

		// Loop through all the entries, checking for a matching name
		for (int i = leaderboard.size-1 ; i >= 0 ; i--) {
			// Check if the name matches
			if (leaderboard.getString("name").equals(name)) {
				// If it does, remove the index and set to return true
				leaderboard.remove(i);
				entryRemoved = true;
			}
		}

		// Return whether an entry was or wasn't removed
		return entryRemoved;
	}

	private static JsonValue getLeaderboard(LeaderboardType lType, String id) {
		// Get the array of entries
		JsonValue leaderboards = getLeaderboard(lType);
		// Return the matching entry id
		return getScores(leaderboards, id);
	}

	private static JsonValue getLeaderboard(LeaderboardType lType) {
		// If root is null, return null
		if (root == null) return null;

		// If the leaderboard doesn't exist, create it
		if (!root.has(getLeaderboardName(lType))) {
			root.addChild(getLeaderboardName(lType), new JsonValue(JsonValue.ValueType.object));
		}
		// Return the leaderboard
		return root.get(getLeaderboardName(lType));
	}

	public static void saveLeaderboard() {
		// If root is null, stop
		if (root == null) return;
		FileControl.saveJsonData(leaderboardFile, root);
	}
	public static void main(String[] args) {
		Leaderboard.loadLeaderboard();
		// System.out.println(root);
		Leaderboard.addEntry(LeaderboardType.SCENARIO, "<main>:main", "test", 3.5F);
		// System.out.println(getEntries(LeaderboardTypes.SCENARIO, "<main>:main"));
		Leaderboard.saveLeaderboard();
		Leaderboard.unloadLeaderboard();
	}
}
