package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class DataManager {
    private static ArrayList<String> wordIdList;
    private static HashMap<String, Word> wordIdToWord;

    public static void populate() {
        wordIdList = new ArrayList<String>();
        wordIdList.addAll(Arrays.asList("apple", "bird", "cherry", "gem", "money", "pear"));

        wordIdToWord = new HashMap<String, Word>();
        for (String wordId : wordIdList) {
            wordIdToWord.put(wordId, new Word(wordId, wordId, wordId));
        }
    }

    public static ArrayList<String> getWordIdList() {
        return wordIdList;
    }

    public static Word getWord(String wordId) {
        return wordIdToWord.get(wordId);
    }
}
