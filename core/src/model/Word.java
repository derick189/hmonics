package model;

import viewmodel.SpellingGameStateMachine;

public class Word {
    private String wordId;
    private String englishSpelling;
    private String hmongSpelling;

    public Word(String wordId, String englishSpelling, String hmongSpelling) {
        this.wordId = wordId;
        this.englishSpelling = englishSpelling;
        this.hmongSpelling = hmongSpelling;
    }

    public String getWordId() {
        return wordId;
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
