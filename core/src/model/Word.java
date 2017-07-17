package model;

import viewmodel.SpellingGameStateMachine;

/**
 * A Word has an english and hmong spelling. The ID associated with a Word is the english spelling.
 */
public class Word {
    private String englishSpelling;
    private String hmongSpelling;

    public Word(String englishSpelling, String hmongSpelling) {
        this.englishSpelling = englishSpelling;
        this.hmongSpelling = hmongSpelling;
    }

    public String getWordId() {
        return englishSpelling;
    }

    public String getSpelling(SpellingGameStateMachine.Language language) {
        switch (language) {
            case ENGLISH:
                return englishSpelling;
            case HMONG:
                return hmongSpelling;
            default:
                return null;
        }
    }
}
