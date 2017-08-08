package model;

import viewmodel.ScreenManager.Language;

/**
 * A Word has an english and hmong spelling. The ID associated with a Word is the english spelling. A hmong word has
 * a certain amount of game spaces that differ from character count.
 */
public class Word implements Comparable<String> {
    private String englishSpelling;
    private String hmongSpelling;
    private int hmongSpaceLength;

    public Word(String englishSpelling, String hmongSpelling, int hmongSpaceLength) {
        this.englishSpelling = englishSpelling;
        this.hmongSpelling = hmongSpelling;
        this.hmongSpaceLength = hmongSpaceLength;
    }

    public String getWordId() {
        return englishSpelling;
    }

    /**
     * Simply returns a string based on what language
     *
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

    /**
     * Returns the number of spaces based on language.
     *
     * @return
     */
    public int getSpaceLength(Language language) {
        switch (language) {
            case ENGLISH:
                return englishSpelling.length();
            case HMONG:
                return hmongSpaceLength;
            default:
                return 0;
        }
    }

    public void setHmongSpaceLength(int hmongSpaceLength) {
        this.hmongSpaceLength = hmongSpaceLength;
    }

    @Override
    public int compareTo(String o) {
        return o.compareTo(this.englishSpelling);
    }
}
