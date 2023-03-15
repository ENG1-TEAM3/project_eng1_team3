package com.undercooked.game.util;

public class StringUtil {

    public static String convertToTitleCase(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        StringBuilder output = new StringBuilder();

        boolean convertNext = true;
        for (char ch : text.toCharArray()) {
            if (!Character.isAlphabetic(ch)) {
                convertNext = true;
            } else if (convertNext) {
                ch = Character.toUpperCase(ch);
                convertNext = false;
            } else {
                ch = Character.toLowerCase(ch);
            }
            output.append(ch);
        }
        return output.toString();
    }

    public static String formatTime(int hours, int minutes, int seconds) {
        // Seconds and minutes can only be, at most, 59
        if (seconds >= 60) {
            minutes += seconds / 60;
            seconds %= 60;
        }
        if (minutes >= 60) {
            hours += minutes / 60;
            minutes %= 60;
        }
        // Format time
        String output = "";
        // If hours is provided
        if (hours > 0) {
            // Add it
            output += hours + ":";
            // If minutes < 10, then add a 0
            if (minutes < 10) {
                output += "0";
            }
        }
        // Add minutes
        output += minutes + ":";
        // Add a 0 if seconds is < 10
        if (seconds < 10) {
            output += "0";
        }
        // Add seconds
        output += seconds;

        // Return the formatted time
        return output;
    }

    public static String formatTime(int minutes, int seconds) {
        return formatTime(0, minutes, seconds);
    }

    public static String formatTime(int seconds) {
        return formatTime(0, 0, seconds);
    }

    public static String formatSeconds(int seconds) {
        return formatTime(seconds);
    }

    public static String formatSeconds(float seconds) {
        return formatSeconds((int) seconds);
    }

}
