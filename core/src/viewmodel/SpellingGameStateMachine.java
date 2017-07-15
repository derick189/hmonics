package viewmodel;

import com.badlogic.gdx.scenes.scene2d.Actor;
import model.DataManager;
import model.Word;
import view.actors.Letter;
import view.games.SpellingGameScreen;

public class SpellingGameStateMachine {
    private SpellingGameScreen spellingGameScreen;
    private State state;
    private Language language;
    private Word currentWord;

    public SpellingGameStateMachine(SpellingGameScreen spellingGameScreen) {
        this.spellingGameScreen = spellingGameScreen;
        state = State.COMPLETING_WORD;

        language = Language.ENGLISH;
        spellingGameScreen.setDisplayLanguage(language);

        currentWord = DataManager.getWord("bird");
        spellingGameScreen.setPictureAndSpaceLength(currentWord.getWordId(), currentWord.getSpelling(language).length());
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
                            spellingGameScreen.playWord(language.fileName, currentWord);
                            // TODO special animation for correct word
                            changeToNextWord();
                            break;
                        }
                        spellingGameScreen.playLetter(language.fileName, (Letter) actor);
                        spellingGameScreen.explodeEffect(actor, "gem");

                        break;
                }
                break;
        }
    }

    /**
     * Helper method to play audio for each letter dropped
     */
//    private void playLetterSound() {
//        ArrayList<Container<Letter>> letterContainers = spellingGameScreen.getLetterContainers();
////         Find last container with a letter
//        for (int i = 0; i < letterContainers.size(); i++) {
//            if (letterContainers.get(i).getActor() == null) {
//                Letter letter = letterContainers.get(i - 1).getActor();
//                Sound sound = Gdx.audio.newSound(Gdx.files.internal("sounds/" + language.toString() + "Alphabet/" + letter.getName() + ".mp3"));
//                sound.play();
////                return;
//            }
//        }
//    }
    private boolean wordIsCorrect() {
        return currentWord.getSpelling(language).equals(spellingGameScreen.getWordInSpaces());
    }

    private void recordWord() {

    }

    private void changeToNextWord() {
        currentWord = DataManager.getWord("gem");
        spellingGameScreen.setPictureAndSpaceLength(currentWord.getWordId(), currentWord.getSpelling(language).length());
    }

    public enum State {
        ANIMATION, COMPLETING_WORD, GAME_COMPLETE
    }

    public enum Event {
        DROPPED_LETTER
    }

    public enum Language {
        ENGLISH("English"), HMONG("Hmong");
        private final String fileName;

        Language(String fileName) {
            this.fileName = fileName;
        }
    }
}
