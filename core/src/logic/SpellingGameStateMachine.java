package logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import model.Data;
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

        language = Language.HMONG;
        spellingGameScreen.setDisplayLanguage(language);

        currentWord = Data.getWord("bird");
        spellingGameScreen.setPictureAndWordLength(currentWord.getWordId(), currentWord.getSpelling(language).length());
    }

    public void doEvent(Event event) {
        switch (state) {
            case ANIMATION:
                return;
            case COMPLETING_WORD:
                switch (event) {
                    case DROPPED_LETTER:
                        playLetterSound();
                        if (wordIsCorrect()) {
                            changeToNextWord();
                        }
                }
                break;
        }
    }

    /**
     * Helper method to play audio for each letter dropped
     */
    private void playLetterSound() {
        ArrayList<Container<Letter>> letterContainers = spellingGameScreen.getLetterContainers();
        // Find last container with a letter
        for (int i = 0; i < letterContainers.size(); i++) {
            if (letterContainers.get(i).getActor() == null) {
                Letter letter = letterContainers.get(i-1).getActor();
                Sound sound = Gdx.audio.newSound(Gdx.files.internal("sounds/" + language.toString() + "Alphabet/" + letter.getName() + ".mp3"));
                sound.play();
                return;
            }
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
        currentWord = Data.getWord("gem");
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
