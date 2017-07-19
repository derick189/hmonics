package model;

import viewmodel.ScreenManager.Language;

/**
 * A Word has an english and hmong spelling. The ID associated with a Word is the english spelling.
 */
public class Word implements Comparable<String> {
    private String englishSpelling;
    private String hmongSpelling;

    public Word(String englishSpelling, String hmongSpelling) {
        this.englishSpelling = englishSpelling;
        this.hmongSpelling = hmongSpelling;
    }

    public String getWordId() {
        return englishSpelling;
    }

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

    @Override
    public int compareTo(String o) {
        return o.compareTo(this.englishSpelling);
    }
}
