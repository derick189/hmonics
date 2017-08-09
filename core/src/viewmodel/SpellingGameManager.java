package viewmodel;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import model.DataManager;
import model.History;
import model.Student;
import model.Word;
import view.actors.Letter;
import view.games.SpellingGameScreen;

import java.util.ArrayList;
import java.util.Random;

/**
 * This class handles each letter drop in the Spelling Game.
 */
public class SpellingGameManager {
    private Random random;
    private SpellingGameScreen spellingGameScreen;
    private ScreenManager.Language currentLanguage;
    private Student currentStudent;
    private ArrayList<Word> sessionWordList;
    private Word currentWord;

    public SpellingGameManager(SpellingGameScreen spellingGameScreen) {
        this.random = new Random(System.currentTimeMillis());
        this.spellingGameScreen = spellingGameScreen;

        this.currentLanguage = ScreenManager.selectedLanguage;
        spellingGameScreen.setDisplayLanguage(currentLanguage);

        this.currentStudent = ScreenManager.getSelectedStudent();
        this.currentStudent.startNewCurrentHistory(new History("Spelling Game"));

        this.sessionWordList = new ArrayList<Word>(DataManager.getWordList());
        changeToNextWord();
    }

    public void droppedLetter(final Actor actor) {
        spellingGameScreen.playSFX("LetterClick");
        System.out.println("Current word: " + spellingGameScreen.getWordInSpaces());
        if (wordIsCorrect()) {
            recordWord();
            actor.addAction(Actions.sequence(
                    Actions.run(new Runnable() {
                        public void run() {
                            spellingGameScreen.playLetter(currentLanguage.fileName, (Letter) actor);
                        }
                    }),
                    Actions.delay(0.5f),
                    Actions.run(new Runnable() {
                        public void run() { // Hooray!!!
                            spellingGameScreen.winConfetti(currentWord.getWordId());
                        }
                    }),
                    Actions.delay(0.5f),
                    Actions.run(new Runnable() {
                        public void run() { // Once final correct letter is dropped, say word
                            spellingGameScreen.playWord(currentLanguage.fileName, currentWord);
                        }
                    }),
                    Actions.delay(2f),
                    Actions.run(new Runnable() {
                        public void run() { // Then play word SFX
                            spellingGameScreen.playSFX(currentWord.getWordId());
                        }
                    }),
                    Actions.delay(1f),
                    Actions.run(new Runnable() {
                        public void run() {
                            spellingGameScreen.playSFX("applause");
                            changeToNextWord();
                        }
                    })
            ));
        } else { // Word isn't correct (yet): Play letter after every drop
            spellingGameScreen.playLetter(currentLanguage.fileName, (Letter) actor);
            if (spellingGameScreen.spacesFull()) { // Spaces full & not correct
                spellingGameScreen.playSFX("buzzer");
            }
        }
    }

    private boolean wordIsCorrect() {
        return currentWord.getSpelling(currentLanguage).equalsIgnoreCase(spellingGameScreen.getWordInSpaces());
    }

    private void recordWord() {
        ScreenManager.getSelectedStudent().addToCurrentHistory(currentWord.getSpelling(currentLanguage));
    }

    /**
     * Change word until all 15 words have been either spelled or skipped over
     */
    public void changeToNextWord() {
        if (sessionWordList.size() > 1) {
            sessionWordList.remove(currentWord);
            currentWord = getNextWord();
            // Set the amount of spaces for this word and replace the hint popup.
            spellingGameScreen.setPictureAndSpaceLength(currentWord.getWordId(), currentWord.getSpaceLength(currentLanguage));
            spellingGameScreen.hintPopup.clearActions();
            spellingGameScreen.hintPopup.getColor().a = 0;
            spellingGameScreen.hintPopup.setText(currentWord.getSpelling(currentLanguage));
        } else {
            gameComplete();
        }
    }

    private Word getNextWord() {
        if (sessionWordList.size() > 1) {
            return sessionWordList.get(random.nextInt(sessionWordList.size() - 1));
        } else {
            return sessionWordList.get(0);
        }
    }

    private void gameComplete() {
        InputEvent event = new InputEvent();
        event.setType(InputEvent.Type.touchDown);
        spellingGameScreen.backButton.fire(event);
        event.setType(InputEvent.Type.touchUp);
        spellingGameScreen.backButton.fire(event);
    }
}
