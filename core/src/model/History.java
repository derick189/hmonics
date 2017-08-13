package model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
/**
 * Each Student creates a History object after every game played.
 * @authors Derick Lenvik, Jared Johnson
 */
public class History {
    private String gamePlayed;
    private ArrayList<String> wordsSpelled;
    private Date date;
    private String timestamp;

    public History(String gamePlayed) {
        this.gamePlayed = gamePlayed;
        wordsSpelled = new ArrayList<String>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd HH:mm");
        date = new Date();
        timestamp = dateFormat.format(date);
    }

    public History(){} // Constructor for DataManager.load() --> data persist

    public String getGamePlayed() {
        return gamePlayed;
    }

    public ArrayList<String> getWordsSpelled() {
        return wordsSpelled;
    }

    public Date getDate() {
        return date;
    }

    public String getDateString() {
        return timestamp;
    }

    public void addWord(String word) {
        wordsSpelled.add(word);
    }
}
