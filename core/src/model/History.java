package model;

import java.util.ArrayList;

public class History { // todo: date
    private String gamePlayed;
    private ArrayList<String> wordsSpelled;

    public History(String gamePlayed) {
        this.gamePlayed = gamePlayed;
        wordsSpelled = new ArrayList<String>();
    }

    public String getGamePlayed() {
        return gamePlayed;
    }

    public ArrayList<String> getWordsSpelled() {
        return wordsSpelled;
    }

    public void addWord(String wordId) {
        wordsSpelled.add(wordId);
    }
}
