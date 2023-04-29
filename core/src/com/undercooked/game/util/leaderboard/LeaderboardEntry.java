package com.undercooked.game.util.leaderboard;

import com.undercooked.game.util.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LeaderboardEntry {
    public String name;
    public float score;
    private Date date;

    public LeaderboardEntry(String name, float score) {
        this.name = name;
        this.score = score;
        setDateNow();
    }

    public void setDateNow() {
        // Set time to now
        setDate(new Date());
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDate(String date) {
        // Try to parse the date and set it
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_TIME);
        try {
            setDate(dateFormat.parse(date));
        } catch (ParseException e) {
            // If it fails, then just set date to null
            this.date = null;
        }
    }

    public Date getDate() {
        return date;
    }

    public String dateAsString() {
        // If date is null, then return an empty string
        if (date == null) return Constants.UNKNOWN_DATE;
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_TIME);

        // Return the formatted date
        return dateFormat.format(date);
    }
}
