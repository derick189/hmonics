package viewmodel;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import model.DataManager;
import model.History;
import model.Student;
import model.Word;
import view.actors.Letter;
import view.games.SpellingGameScreen;

import java.util.ArrayList;
import java.util.Random;

public class SpellingGameStateMachine {
    private Random random;
    private SpellingGameScreen spellingGameScreen;
    private State state;
    private ScreenManager.Language currentLanguage;
    private ArrayList<Word> sessionWordList;
    private Word currentWord;
    private Student currentStudent;

    public SpellingGameStateMachine(SpellingGameScreen spellingGameScreen, ScreenManager.Language currentLanguage) {
        this.random = new Random(System.currentTimeMillis());
        this.spellingGameScreen = spellingGameScreen;
        this.state = State.COMPLETING_WORD;
        this.currentLanguage = currentLanguage;
        this.sessionWordList = new ArrayList<Word>(DataManager.getWordList());
        this.currentWord = getNextWord();

        this.currentStudent = DataManager.getTeachers().get(ScreenManager.selectedTeacherIndex).getStudents().get(ScreenManager.selectedStudentIndex);
        this.currentStudent.startNewCurrentHistory(new History("Spelling Game"));

        // Set game language (set in screen manager for now)
        // TODO create language button option in game
        spellingGameScreen.setDisplayLanguage(currentLanguage);
        // Set the amount of spaces for word
        spellingGameScreen.setPictureAndSpaceLength(currentWord.getWordId(), currentWord.getSpaces());
    }

    public void doEvent(Event event, Actor actor) {
        switch (state) {
            case ANIMATION:
                break;
            case COMPLETING_WORD:
                switch (event) {
                    case DROPPED_LETTER:
                        System.out.println("Current word: " + spellingGameScreen.getWordInSpaces());
                        if (wordIsCorrect()) {
                            // TODO special animation for correct word
//                            spellingGameScreen.playWord(currentLanguage.fileName, currentWord);
                            spellingGameScreen.playLetter(currentLanguage.fileName, (Letter) actor);
                            recordWord();
                            if (sessionWordList.size() > 1) {
                                changeToNextWord();
                            } else {
                                gameComplete();
                            }
                        } else {
                            spellingGameScreen.playLetter(currentLanguage.fileName, (Letter) actor);
                            spellingGameScreen.confettiEffect(actor, "gem");
                        }
                        break;
                }
                break;
            case GAME_COMPLETE:

                break;
        }
    }

    private boolean wordIsCorrect() {
        return currentWord.getSpelling(currentLanguage).equals(spellingGameScreen.getWordInSpaces());
    }

    private void recordWord() {
        DataManager.getStudents(ScreenManager.selectedTeacherIndex).get(ScreenManager.selectedStudentIndex).addToCurrentHistory(currentWord.getWordId());
    }

    private void changeToNextWord() {
        sessionWordList.remove(currentWord);
        currentWord = getNextWord();
        spellingGameScreen.setPictureAndSpaceLength(currentWord.getWordId(), currentWord.getSpaces());
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

    public enum State {
        ANIMATION, COMPLETING_WORD, GAME_COMPLETE
    }

    public enum Event {
        DROPPED_LETTER
    }

}
