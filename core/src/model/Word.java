package model;

import viewmodel.ScreenManager.Language;

/**
 * A Word has an english and hmong spelling. The ID associated with a Word is the english spelling. A hmong word has
 * a certain amount of game spaces that differ from character count.
 */
public class Word implements Comparable<String> {
    private String englishSpelling;
    private String hmongSpelling;
    private int spaces;

    public Word(String englishSpelling, String hmongSpelling, int spaces) {
        this.englishSpelling = englishSpelling;
        this.hmongSpelling = hmongSpelling;
        this.spaces = spaces;
    }

    public String getWordId() {
        return englishSpelling;
    }

    /**
     * Simply returns a string based on what language
     * @param language
     * @return
     */
    public String getSpelling(Language language) {
        switch (language) {
            case ENGLISH:
                return englishSpelling;
            case HMONG:
                return hmongSpelling;
            default:
                return null;
        }
    }
    public int getSpaces() {
        return spaces;
    }

    public void setSpaces(int spaces) {
        this.spaces = spaces;
    }

    @Override
    public int compareTo(String o) {
        return o.compareTo(this.englishSpelling);
    }
}
