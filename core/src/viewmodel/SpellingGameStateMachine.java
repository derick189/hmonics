package viewmodel;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import model.DataManager;
import model.Word;
import view.actors.Letter;
import view.games.SpellingGameScreen;

import java.util.ArrayList;

public class SpellingGameStateMachine {
    private State state;
    private SpellingGameScreen spellingGameScreen;
    private Language language;
    private Word currentWord;

    public SpellingGameStateMachine(SpellingGameScreen spellingGameScreen) {
        state = State.COMPLETING_WORD;
        this.spellingGameScreen = spellingGameScreen;

        language = Language.ENGLISH;
        spellingGameScreen.setDisplayLanguage(language);

        currentWord = DataManager.getWord("bird");
        spellingGameScreen.setPictureAndWordLength(currentWord.getWordId(), currentWord.getSpelling(language).length());
    }

    public void doEvent(Event event) {
        switch (state) {
            case ANIMATION:
                return;
            case COMPLETING_WORD:
                switch (event) {
                    case DROPPED_LETTER:
                        if (wordIsCorrect()) {
                            changeToNextWord();
                        }
                }
                break;
        }
    }

    private boolean wordIsCorrect() {
        ArrayList<Container<Letter>> letterContainers = spellingGameScreen.getLetterContainers();
        String currentString = "";
        for (Container<Letter> letterContainer : letterContainers) {
            if (letterContainer.getActor() == null) {
                currentString += " ";
            } else {
                currentString += letterContainer.getActor().getName();
            }
        }
        return currentWord.getSpelling(language).equals(currentString);
    }

    private void changeToNextWord() {
        currentWord = DataManager.getWord("gem");
        spellingGameScreen.setPictureAndWordLength(currentWord.getWordId(), currentWord.getSpelling(language).length());
    }

    public enum State {
        ANIMATION, COMPLETING_WORD, GAME_COMPLETE
    }

    public enum Event {
        DROPPED_LETTER
    }

    public enum Language {
        ENGLISH, HMONG
    }
}
