package com.undercooked.game.util.leaderboard;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.undercooked.game.files.FileControl;
import com.undercooked.game.util.Constants;
import com.undercooked.game.util.StringUtil;
import com.undercooked.game.util.json.JsonFormat;

import java.util.Comparator;

/**
 * Use this static class to manipulate the leaderboards using the enum
 * LeaderboardNames.
 * <p>
 * {@link #loadLeaderboard()} must be called before any of the functions
 * for interacting with the leaderboards in this class can be used.
 * Otherwise, they will all fail.
 * </p>
 */
public final class LeaderboardController {

	/**
	 * Converts one of the {@link LeaderboardType} to a {@link String}
	 * value. This is the id of them inside the json file.
	 * @param leaderboardType {@link LeaderboardType} : The name to get.
	 * @return {@link String} : The name of the leaderboard.
	 */
	public static String getLeaderboardID(LeaderboardType leaderboardType) {
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
	 * Converts one of the {@link LeaderboardType} to a {@link String}
	 * value. This is the name to display them as.
	 * @param leaderboardType {@link LeaderboardType} : The name to get.
	 * @return {@link String} : The name of the leaderboard.
	 */
	public static String getLeaderboardName(LeaderboardType leaderboardType) {
		// If it's null, return null
		if (leaderboardType == null) return "null";
		// Otherwise, return the name
		switch (leaderboardType) {
			case ENDLESS:
				return "Endless";
			case SCENARIO:
				return "Scenario";
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
				return StringUtil.formatSeconds(score, 2);
			case ENDLESS:
				return Integer.toString((int) score);
		}
		// Shouldn't reach here
		return "error";
	}

	private static Comparator<LeaderboardEntry> scenarioComparator = new Comparator<LeaderboardEntry>() {
		@Override
		public int compare(LeaderboardEntry o1, LeaderboardEntry o2) {
			// score being lower is better
			if (o1.score < o2.score) {
				return 1;
			} else if (o2.score < o1.score) {
				return -1;
			}
			return 0;
		}
	};

	private static Comparator<LeaderboardEntry> endlessComparator = new Comparator<LeaderboardEntry>() {
		@Override
		public int compare(LeaderboardEntry o1, LeaderboardEntry o2) {
			// score being lower is better
			if (o1.score > o2.score) {
				return 1;
			} else if (o2.score > o1.score) {
				return -1;
			}
			return 0;
		}
	};

	/**
	 * Returns a comparator of the scores based on {@link LeaderboardType}.
	 * This is because Scenarios favours lower time taken, while Endless mode
	 * favours higher customers served.
	 * <br>0 means they are equal.
	 * <br>1 means the left side is better.
	 * <br>-1 means the right side is better.
	 *
	 * @param leaderboardType {@link LeaderboardType} : The leaderboard to use.
	 * @return
	 */
	public static Comparator<LeaderboardEntry> getScoreComparator(LeaderboardType leaderboardType) {
		if (leaderboardType == null) return new Comparator<LeaderboardEntry>() {
			@Override
			public int compare(LeaderboardEntry o1, LeaderboardEntry o2) {
				return 0;
			}
		};

		switch (leaderboardType) {
			case SCENARIO:
				return scenarioComparator;

			case ENDLESS:
				return endlessComparator;
		}
		// Shouldn't reach here
		return null;
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
	static ObjectMap<LeaderboardType, ObjectMap<String, Leaderboard>> leaderboardData = new ObjectMap<>();
	static String leaderboardFile = "leaderboard.json";

	/**
	 * Loads the leaderboard.
	 * <p>
	 * This method MUST be called before any other method in this class.
	 * </p>
	 */
	public static void loadLeaderboard() {
		// If leaderboardData is not null, then unload it first
		if (leaderboardData != null) unloadLeaderboard();

		JsonValue root = FileControl.loadJsonData(leaderboardFile);
		// Create leaderboard.json if it doesn't exist
		if (root == null) {
			// Create a new JsonValue, and then format it using the leaderboard format
			root = new JsonValue(JsonValue.ValueType.object);
		}
		// Format the Json
		JsonFormat.formatJson(root, Constants.DefaultJson.leaderboardFormat());

		// Load it into the ObjectMap.
		for (LeaderboardType lType : LeaderboardType.values()) {
			// Add the ObjectMap for the type
			ObjectMap<String, Leaderboard> newObjectMap = new ObjectMap<>();
			leaderboardData.put(lType, newObjectMap);
			// Then, copy all the leaderboard values over
			for (JsonValue leaderboard : root.get(getLeaderboardID(lType))) {
				// If the leaderboard id is already in the ObjectMap, then skip
				if (newObjectMap.containsKey(leaderboard.getString("id"))) {
					continue;
				}
				// Otherwise, make the leaderboard
				Leaderboard newLeaderboard = new Leaderboard();
				newLeaderboard.name = leaderboard.getString("name");
				// Set the comparator
				newLeaderboard.setComparator(getScoreComparator(lType));
				// And add it to the leaderboard to the leaderboard type
				newObjectMap.put(leaderboard.getString("id"), newLeaderboard);

				// Now loop through the entries and add them
				for (JsonValue entry : leaderboard.get("scores")) {
					LeaderboardEntry newEntry = newLeaderboard.addLeaderboardEntry(
							entry.getString("name"),
							entry.getFloat("score")
					);

					// Make sure to set the date of the entry
					newEntry.setDate(entry.getString("date"));
				}
			}
		}
	}

	/**
	 * Unloads the leaderboard, if it is no longer needed.
	 * <br>
	 * If the leaderboard is not needed, such as when the game
	 * is running, then this function can be run and the leaderboard
	 * entries will be deleted by the garbage collector.
	 */
	public static void unloadLeaderboard() {
		leaderboardData.clear();
	}

	public static void addEntry(LeaderboardType lType, String id, String leaderboardName, String name, float score) {
		// Only continue if leaderboardData is not null
		if (leaderboardData == null) return;

		// Get the leaderboard by id
		Leaderboard leaderboard = getLeaderboard(lType, id);
		// If it's null, then add the id
		if (leaderboard == null) {
			leaderboard = addLeaderboard(lType, leaderboardName, id);
		}

		// Add it to the leaderboard
		leaderboard.addLeaderboardEntry(name, score);
	}

	private static Leaderboard addLeaderboard(LeaderboardType lType, String name, String id) {
		// First, get the leaderboards
		ObjectMap<String, Leaderboard> leaderboards = getLeaderboards(lType);
		// If it exists, just ignore
		if (leaderboards.containsKey(id)) return leaderboards.get(id);

		// If it doesn't exist, create it
		Leaderboard newLeaderboard = new Leaderboard();
		newLeaderboard.name = name;
		newLeaderboard.setComparator(getScoreComparator(lType));
		// Add it to the ObjectMap
		leaderboards.put(id, newLeaderboard);
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
		Leaderboard leaderboard = getLeaderboard(lType, id);
		// Only continue if it's not null
		if (leaderboard == null) return null;

		// Now return a copy of the array
		return leaderboard.copyLeaderboard();
	}

	public static boolean removeEntry(LeaderboardType lType, String id, int index) {
		// Get the leaderboard by id
		Leaderboard leaderboard = getLeaderboard(lType, id);
		// Only continue if it's not null
		if (leaderboard == null) return false;

		// Check that the index is a valid number
		if (index < 0 || index >= leaderboard.size()) return false;

		// Then, remove it
		leaderboard.remove(id, index);

		// Return true, as it was successfully removed
		return true;
	}

	public static boolean removeEntry(LeaderboardType lType, String id, String name) {
		// Get the leaderboard by id
		Leaderboard leaderboard = getLeaderboard(lType, id);
		// Only continue if it's not null
		if (leaderboard == null) return false;

		// Tell the leaderboard to remove the entry
		return leaderboard.removeEntry(id, name);
	}

	public static Leaderboard getLeaderboard(LeaderboardType lType, String id) {
		return getLeaderboard(lType, id, false);
	}

	private static Leaderboard getLeaderboard(LeaderboardType lType, String id, boolean addIfNull) {
		// Get the array of entries
		ObjectMap<String, Leaderboard> leaderboards = getLeaderboards(lType);
		// If the id doesn't exist, add it if addIfNull is true
		if (!leaderboards.containsKey(id)) {
			if (!addIfNull) return null;
			return addLeaderboard(lType, "missing name", id);
		}
		// Then return the leaderboard
		return leaderboards.get(id);
	}

	private static ObjectMap<String, Leaderboard> getLeaderboards(LeaderboardType lType) {
		return getLeaderboards(lType, false);
	}

	private static ObjectMap<String, Leaderboard> getLeaderboards(LeaderboardType lType, boolean addIfNull) {
		// If leaderboard data is null, return null
		if (leaderboardData == null) return null;

		// If the leaderboard type doesn't exist, return null
		if (!leaderboardData.containsKey(lType)) {
			if (!addIfNull) return null;
			ObjectMap<String, Leaderboard> newLeaderboards = new ObjectMap<>();
			leaderboardData.put(lType, newLeaderboards);
			return newLeaderboards;
		}
		// Return the leaderboards
		return leaderboardData.get(lType);
	}

	public static Array<String> getIDs(LeaderboardType leaderboardType) {
		// If the leaderboard type doesn't exist, return null
		ObjectMap<String, Leaderboard> leaderboards = getLeaderboards(leaderboardType);
		if (leaderboards == null) return null;

		// Return an array of the keys
		return leaderboards.keys().toArray();
	}

	public static void saveLeaderboard() {
		// If leaderboard data is null, stop
		if (leaderboardData == null) return;
		// Convert all the data into a JsonValue
		FileControl.saveJsonData(leaderboardFile, asJsonValue());
	}

	public static JsonValue asJsonValue() {
		JsonValue root = new JsonValue(JsonValue.ValueType.object);
		// Loop through each of the keys in the object map
		for (LeaderboardType lType : leaderboardData.keys()) {
			// Add the type
			JsonValue leaderboards = new JsonValue(JsonValue.ValueType.array);
			root.addChild(getLeaderboardID(lType), leaderboards);
			// Then loop through the children
			for (String leaderboardID : leaderboardData.get(lType).keys()) {
				// Make the JsonValue for it
				Leaderboard leaderboard = leaderboardData.get(lType).get(leaderboardID);
				JsonValue leaderboardJson = new JsonValue(JsonValue.ValueType.object);
				leaderboardJson.addChild("id", new JsonValue(leaderboardID));
				leaderboardJson.addChild("name", new JsonValue(leaderboard.name));

				JsonValue scores = new JsonValue(JsonValue.ValueType.array);
				leaderboardJson.addChild("scores", scores);

				leaderboards.addChild(leaderboardJson);
				int index = 0;
				// Then get the scores and loop through them
				for (LeaderboardEntry entry : leaderboard.getLeaderboard()) {
					JsonValue entryJson = new JsonValue(JsonValue.ValueType.object);
					entryJson.addChild("name", new JsonValue(entry.name));
					entryJson.addChild("score", new JsonValue(entry.score));
					entryJson.addChild("date", new JsonValue(entry.dateAsString()));
					// And add it to the leaderboard
					scores.addChild(Integer.toString(index), entryJson);
					index++;
				}
			}
		}
		return root;
	}

	public static void main(String[] args) {
		loadLeaderboard();
		// System.out.println(root);
		addEntry(LeaderboardType.SCENARIO, "<main>:main", "Main Scenario", "Player 1", 30);
		addEntry(LeaderboardType.ENDLESS, "<main>:main", "Main Scenario", "Player 2", 13);
		addEntry(LeaderboardType.SCENARIO, "<main>:main", "Main Scenario", "Player 3", 27);
		addEntry(LeaderboardType.ENDLESS, "<main>:main", "Main Scenario", "Player 4", 42);
		addEntry(LeaderboardType.SCENARIO, "<main>:main", "Main Scenario", "Player 5", 18);
		addEntry(LeaderboardType.SCENARIO, "<main>:main", "Main Scenario", "Player 6", 13);
		// System.out.println(getEntries(LeaderboardTypes.SCENARIO, "<main>:main"));
		saveLeaderboard();
		unloadLeaderboard();
	}
}
