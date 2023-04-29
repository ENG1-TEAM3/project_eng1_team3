package com.undercooked.game.util.leaderboard;

import com.badlogic.gdx.utils.Array;

import java.util.Comparator;

public class Leaderboard {

    public String name;
    private Comparator<LeaderboardEntry> entryComparator;
    private Array<LeaderboardEntry> entries;

    public Leaderboard() {
        entries = new Array<>();
    }

    public LeaderboardEntry addLeaderboardEntry(String name, float score) {
        // Make the entry
        LeaderboardEntry newEntry = new LeaderboardEntry(name, score);
        // And add it
        addLeaderboardEntry(newEntry);
        // And return the new entry
        return newEntry;
    }

    public void setComparator(Comparator<LeaderboardEntry> comparator) {
        this.entryComparator = comparator;
    }

    public void addLeaderboardEntry(LeaderboardEntry newEntry) {
        // And add it, in the order using the comparator
        // If the comparator doesn't exist, just add it to the end
        if (entryComparator != null) {
            // Otherwise, add it based on the comparator
            for (int i = 0 ; i < entries.size ; i++) {
                // Loop until the comparator is 1
                if (entryComparator.compare(newEntry, entries.get(i)) == 1) {
                    // If it is, then insert it here
                    entries.insert(i, newEntry);
                    // And return
                    return;
                }
            }
        }
        // Add the entity to the end if it reaches here
        // This is if it reaches the end of the array, and is the worst,
        // or if the entity comparator doesn't exist
        entries.add(newEntry);

    }

    public Array<LeaderboardEntry> copyLeaderboard() {
        // Create a new array
        Array<LeaderboardEntry> copy = new Array<>();
        // Copy over all the values
        for (LeaderboardEntry entry : entries) {
            copy.add(entry);
        }
        // Return the copy
        return copy;
    }

    protected Array<LeaderboardEntry> getLeaderboard() {
        return entries;
    }

    public boolean remove(String id, int index) {
        // Check that the range is valid
        if (index < 0 || index >= entries.size) return false;

        // Then remove the index
        entries.removeIndex(index);
        return true;
    }

    public boolean removeEntry(String id, String name) {
        // Loop through all the entries, backwards
        boolean entryRemoved = false;
        for (int i = entries.size-1; i >= 0 ; i--) {
            // Check if the name matches
            if (entries.get(i).name.equals(name)) {
                // If it does, remove the index and set to return true
                entries.removeIndex(i);
                entryRemoved = true;
            }
        }
        return entryRemoved;
    }

    public int size() {
        return entries.size;
    }
}
