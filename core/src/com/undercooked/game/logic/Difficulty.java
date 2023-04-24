package com.undercooked.game.logic;

public class Difficulty {
    public final static int EASY = 0;
    public final static int MEDIUM = 1;
    public final static int HARD = 2;

    public static String toString(int difficulty) {
        switch (difficulty) {
            case EASY:
                return "easy";
            case MEDIUM:
                return "medium";
            case HARD:
                return "hard";
        }
        return "default";
    }

    public static int asInt(String difficulty) {
        switch (difficulty) {
            case "easy":
                return EASY;
            case "medium":
                return MEDIUM;
            case "hard":
                return HARD;
        }
        return -1;
    }
}