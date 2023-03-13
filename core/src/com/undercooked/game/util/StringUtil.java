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

}
