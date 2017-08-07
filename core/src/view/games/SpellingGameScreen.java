package view.games;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import model.Word;
import view.AssetManager;
import view.GdxGame;
import view.actors.Letter;
import view.screens.StudentScreen;
import view.screens.TeamLogoSplashScreen;
import viewmodel.ScreenManager;
import viewmodel.SpellingGameStateMachine;

import java.util.ArrayList;
import java.util.Random;

public class SpellingGameScreen implements Screen {
    public ImageButton backButton;
    private GdxGame game;
    private Stage stage;
    private DragAndDrop dragAndDrop;
    private Music backgroundMusic;
    private Sound click;
    private Random random;

    int backButtonSize = 150;
    private Group actorsGroup;
    private Group animationsGroup;
    private Table letterTable;
    private Table pictureTable;
    private Table spaceTable;
    private Container<Image> pictureContainer;
    private ArrayList<Container<Letter>> letterSpaces;
    private SpellingGameStateMachine spellingGameStateMachine;
    int letterTableHeight = 360;
    int pictureSize = 400;
    int letterSpaceWidth = 150;
    int letterSpaceHeight = 195;
    int letterSize = 140;
    // Actors added to the screen are drawn in the order they were added. Actors drawn later are drawn on top of everything before.
    // These groups are used to add actors to the screen in the right order. All actors added to groups are drawn when the group is drawn.
    // Because these groups are added in this order in setStage, if all actors are added to these groups and not the screen directly then
    private Group backgroundGroup;

    public SpellingGameScreen(GdxGame gdxGame) {
        this.game = gdxGame;
        stage = new Stage(gdxGame.viewport, gdxGame.batch);
        Gdx.input.setInputProcessor(stage);
        dragAndDrop = new DragAndDrop();
        dragAndDrop.setDragTime(0);
        dragAndDrop.setDragActorPosition(letterSize / 2, -letterSize / 2);
        random = new Random(System.currentTimeMillis());

        setStage();
    }

    /**
     * Screen size in virtual pixels: WIDTH = 1920 HEIGHT = 1080;
     */
    private void setStage() {
        TeamLogoSplashScreen.getBackgroundMusic().stop();
        backgroundMusic = AssetManager.getMusic("GameMusic");
        backgroundMusic.setVolume(0.05f);
        backgroundMusic.play();
        click = Gdx.audio.newSound(Gdx.files.internal("sounds/SFX/LetterClick.mp3"));

        stage.addActor(backgroundGroup = new Group());
        stage.addActor(actorsGroup = new Group());
        stage.addActor(animationsGroup = new Group());

        Image backgroundImage = new Image(view.AssetManager.getTextureRegion("background"));
        backgroundImage.setSize(GdxGame.WIDTH, GdxGame.HEIGHT);
        backgroundGroup.addActor(backgroundImage);

        Table mainTable = new Table();
        mainTable.setBounds(0, 0, GdxGame.WIDTH, GdxGame.HEIGHT);
        actorsGroup.addActor(mainTable);

        letterTable = new Table();
        pictureTable = new Table();
        spaceTable = new Table();
        backButton = new ImageButton(AssetManager.backButtonStyle);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                backgroundMusic.stop();
                TeamLogoSplashScreen.getBackgroundMusic().play();
                ScreenManager.nextScreen(new StudentScreen(SpellingGameScreen.this.game));
            }
        });

//        letterTable.setDebug(true);
//        pictureTable.setDebug(true);
//        spaceTable.setDebug(true);
        backButton.setBounds(0, 0, backButtonSize, backButtonSize);
        backButton.setSize(backButtonSize, backButtonSize);
        mainTable.addActor(backButton);

        letterTable.setBounds(mainTable.getWidth() / 2, (mainTable.getHeight() - letterTableHeight) - 20, 0, letterTableHeight);
        mainTable.addActor(letterTable);

        pictureTable.setBounds(mainTable.getWidth() / 2, ((mainTable.getHeight() - pictureSize) / 2) - 60, 0, pictureSize);
        mainTable.addActor(pictureTable);

        spaceTable.setBounds(mainTable.getWidth() / 2, 50, 0, letterSpaceHeight);
        mainTable.addActor(spaceTable);

        pictureTable.add(pictureContainer = new Container<Image>().size(pictureSize));
        letterSpaces = new ArrayList<Container<Letter>>();

        spellingGameStateMachine = new SpellingGameStateMachine(this, ScreenManager.Language.HMONG);
    }

    public void setDisplayLanguage(ScreenManager.Language language) {
        setAlphabet(language);
    }

    /**
     * Sets up game screen with indicated language and associated alphabet
     *
     * @param language
     */
    private void setAlphabet(ScreenManager.Language language) {
        int numRows;
        int letterSelectSize;
        letterTable.clearChildren();
        switch (language) {
            case ENGLISH:
                numRows = 2;
                letterSelectSize = letterTableHeight / 3;
                String[] alphabet = {
                        "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m",
                        "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
                for (int i = 0; i < numRows; i++) { // row
                    for (int j = 0; j < 13; j++) { // column
                        Letter letter = new Letter(alphabet[(i * 13) + j], letterSelectSize);
                        letterTable.add(new Container<Letter>(letter).size(letterSelectSize));
                        setLetterAsDraggable(letter);
                    }
                    letterTable.row();
                }
                return;
            case HMONG:
                numRows = 4;
                letterSelectSize = letterTableHeight / numRows;
                String[] consonants = {"c", "ch", "d", "dh", "dl", "f", "h", "hl", "hm", "hml", "hn", "hny",
                        "k", "kh", "l", "m", "ml", "n", "nc", "nch", "ndl", "nk", "nkh", "np", "nph", "npl", "nplh", "nq",
                        "nqh", "nr", "nrh", "nt", "nth", "nts", "ntsh", "ntx", "ntxh", "ny", "p", "ph", "pl", "plh", "q",
                        "qh", "r", "rh", "s", "t", "th", "ts", "tsh", "tx", "txh", "v", "x", "xy", "y", "z"};
                String[] vowels = {"a", "aa", "ai", "au", "aw", "e", "ee", "i", "ia", "o", "oo", "u", "ua", "w"};
                String[] tones = {"koJ", "muS", "kuV", "niaM", "neeG", "siaB", "zoO", "toD"};

                Table consonantsTable = new Table();
//                consonantsTable.setBackground(AssetManager.backplate);
                letterTable.add(consonantsTable);
                Table vowelsTable = new Table();
//                vowelsTable.setBackground(AssetManager.backplate);
                letterTable.add(vowelsTable);
                Table tonesTable = new Table();
//                tonesTable.setBackground(AssetManager.backplate);
                letterTable.add(tonesTable);

                for (int i = 0; i < numRows; i++) { // row
                    for (int j = 0; j < 15; j++) { // column
                        if ((i * 15) + j < consonants.length) { // leaves empty spaces
                            Letter letter = new Letter(consonants[(i * 15) + j], letterSelectSize);
                            consonantsTable.add(new Container<Letter>(letter).size(letterSelectSize));
                            setLetterAsDraggable(letter);
                        }
                    }
                    consonantsTable.row();
                    for (int j = 0; j < 4; j++) { // column
                        if ((i * 4) + j < vowels.length) { // leaves empty spaces
                            Letter letter = new Letter(vowels[(i * 4) + j], letterSelectSize);
                            vowelsTable.add(new Container<Letter>(letter).size(letterSelectSize));
                            setLetterAsDraggable(letter);
                        }
                    }
                    vowelsTable.row();
                    for (int j = 0; j < 2; j++) { // column
                        if ((i * 2) + j < tones.length) { // leaves empty spaces
                            Letter letter = new Letter(tones[(i * 2) + j], letterSelectSize);
                            letter.setIsTone();
                            tonesTable.add(new Container<Letter>(letter).size(letterSelectSize));
                            setLetterAsDraggable(letter);
                        }
                    }
                    tonesTable.row();
                }
//                for (int i = 0; i < 4; i++) { // row
//                    for (int j = 0; j < 20; j++) { // column
//                        if (letterIndex < consonants.length + vowels.length + tones.length) {
//                            final Letter letter;
//                            if (letterIndex < consonants.length) { // consonant
//                                letter = new Letter(consonants[letterIndex++]);
//                            } else if (letterIndex < consonants.length + vowels.length) { // vowel
//                                letter = new Letter(vowels[(letterIndex++) - consonants.length]);
//                            } else { // tone
//                                letter = new Letter(tones[(letterIndex++) - (consonants.length + vowels.length)]);
//                                letter.setIsTone();
//                            }
//                            letter.getLabel().setFontScale(.5f);
//                            letterTable.add(new Container<Letter>(letter).size(letterSelectSize)).size(letterSelectSize);
//                            setLetterAsDraggable(letter);
//                        }
//                    }
//                    letterTable.row();
//                }
                return;
        }
    }

    /**
     * Creates a copy when letter is dragged from the alphabet. The copy does not create a copy when moved.
     */
    private void setLetterAsDraggable(Letter letter) {
        dragAndDrop.addSource(new DragAndDrop.Source(letter) {
            public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                DragAndDrop.Payload payload = new DragAndDrop.Payload();
                Letter letterCopy = new Letter((Letter) getActor(), letterSize);
                payload.setDragActor(letterCopy);

                dragAndDrop.addSource(new DragAndDrop.Source(letterCopy) {
                    @Override
                    public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                        DragAndDrop.Payload payload = new DragAndDrop.Payload();
                        payload.setDragActor(getActor());
                        return payload;
                    }
                });
                return payload;
            }
        });
    }

    public void setPictureAndSpaceLength(String pictureFileName, int spaceLength) {
        pictureContainer.setActor(new Image(view.AssetManager.getTextureRegion(pictureFileName)));
        setSpaces(spaceLength);
    }

    private void setSpaces(int spaceLength) {
        spaceTable.clearChildren();
        letterSpaces.clear();
        for (int i = 0; i < spaceLength; i++) {
            Container<Letter> letterContainer = new Container<Letter>();
            letterContainer.setTouchable(Touchable.enabled);
            letterContainer.setBackground(new TextureRegionDrawable(view.AssetManager.getTextureRegion("underline")));
            spaceTable.add(letterContainer.size(letterSize, letterSize)).size(letterSpaceWidth, letterSpaceHeight); // offset for underline size
            letterSpaces.add(letterContainer);

            dragAndDrop.addTarget(new DragAndDrop.Target(letterContainer) { //TODO: flickering after drag
                @Override
                public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                    return true;
                }

                @Override
                public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                    for (Container<Letter> letterContainer : letterSpaces) {
                        if (!letterContainer.hasChildren()) {
                            letterContainer.setActor(null);
                        }
                    }
                    Container<Letter> newParent = (Container<Letter>) getActor();
                    newParent.setActor((Letter) payload.getDragActor());
                    SpellingGameScreen.this.spellingGameStateMachine.doEvent(SpellingGameStateMachine.Event.DROPPED_LETTER, payload.getDragActor());
                }
            });
        }
    }

    /**
     * Gets the string formed by the letters dropped into the spaces
     *
     * @return string
     */
    public String getWordInSpaces() {
        String currentString = "";
        for (Container<Letter> letterContainer : letterSpaces) {
            if (letterContainer.hasChildren()) {
                currentString += letterContainer.getActor().getSpelling();
            } else {
                currentString += " ";
            }
        }
        // If the hmong word contains a tone, trim tone letter to 1 lowercase character
//        for (String tone : tones) {
//            if (currentString.contains(tone)) {
//                int startIndex = currentString.indexOf(tone);
//                currentString = currentString.substring(0, startIndex) + tone.substring(tone.length() - 1).toLowerCase();
//            }
//        }
        return currentString;
    }

    /**
     * Decides whether the spaces are filled with letters
     *
     * @return
     */
    public boolean spacesFull() {
        for (Container<Letter> letterContainer : letterSpaces) {
            if (letterContainer.hasChildren()) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * Displays confetti animation
     *
     * @param subject
     * @param fileName
     */
    public void confettiEffect(Actor subject, String fileName) {
        int size = 100;
        float duration = 1f;
        int distance = 300;
        for (int i = 0; i < 10; i++) {
            Actor explosion = new Image(AssetManager.getTextureRegion(fileName));
            explosion.setTouchable(Touchable.disabled);
            explosion.setBounds(subject.getX(), subject.getY(), size, size);
            explosion.setOrigin(size / 2, size / 2);
            animationsGroup.addActor(explosion);
            explosion.addAction(Actions.moveTo(subject.getX() + (random.nextBoolean() ? random.nextInt(distance) : -random.nextInt(distance)) + random.nextInt(distance), subject.getY() + random.nextInt(distance),
                    duration, Interpolation.smooth));
            explosion.addAction(Actions.rotateBy(random.nextBoolean() ? random.nextInt(270) : -random.nextInt(270), duration));
            explosion.addAction(Actions.fadeOut(duration));
        }
    }

    public void playLetter(String language, Letter letter) {
        Sound sound = Gdx.audio.newSound(Gdx.files.internal("sounds/" + language + "Alphabet/" + letter.getName() + ".mp3"));
        sound.play();
    }

    public void playWord(String language, Word currentWord) {
        Sound sound = Gdx.audio.newSound(Gdx.files.internal("sounds/" + language + "Words/" + currentWord.getWordId() + ".mp3"));
        sound.play();
    }

    public void playSFX(String sound) {
        Sound sfx = Gdx.audio.newSound(Gdx.files.internal("sounds/SFX/" + sound + ".mp3"));
        sfx.play();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, false);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
