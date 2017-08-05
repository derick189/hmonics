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
    private Random random;

    private String[] tones = {"koJ", "muS", "kuV", "niaM", "neeG", "siaB", "zoO", "toD"};
    // Actors added to the screen are drawn in the order they were added. Actors drawn later are drawn on top of everything before.
    // These groups are used to add actors to the screen in the right order. All actors added to groups are drawn when the group is drawn.
    // These are added in this order in setStage. All actors are added to these groups and not the screen directly.
    private Group backgroundGroup;
    private Group actorsGroup;
    private Group animationsGroup;
    private Table letterTable;
    private Table pictureTable;
    private Table spaceTable;
    private Container<Image> pictureContainer;
    private ArrayList<Container<Letter>> letterSpaces;
    private SpellingGameStateMachine spellingGameStateMachine;
    private int separator = 40;
    private int pictureSize = 400;
    private int letterSpaceSize = 140;
    private int letterButtonSize = 110;

    public SpellingGameScreen(GdxGame gdxGame) {
        this.game = gdxGame;
        stage = new Stage(gdxGame.viewport, gdxGame.batch);
        Gdx.input.setInputProcessor(stage);
        dragAndDrop = new DragAndDrop();
        dragAndDrop.setDragTime(0);
        dragAndDrop.setDragActorPosition(letterSpaceSize / 2, -letterSpaceSize / 2);
        random = new Random(System.currentTimeMillis());

        setStage();
    }

    private void setStage() {
        backgroundMusic = AssetManager.getMusic("spellingGameMusic");
        backgroundMusic.play();

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
                ScreenManager.nextScreen(new StudentScreen(SpellingGameScreen.this.game));
            }
        });
        mainTable.top().left();
        mainTable.row();
        mainTable.add(backButton).size(150).top().left().padTop(50).padLeft(75).padRight(25);
        mainTable.add().height(separator);
        mainTable.add(letterTable).height(320).padTop(separator);
        mainTable.row();
        mainTable.add().height(separator);
        mainTable.row();
        mainTable.add(pictureTable).height(pictureSize).colspan(3);
        mainTable.row();
        mainTable.add().height(separator);
        mainTable.row();
        mainTable.add(spaceTable).height(letterSpaceSize).colspan(3);
        mainTable.row();

        pictureTable.add(pictureContainer = new Container<Image>().size(pictureSize));
        letterSpaces = new ArrayList<Container<Letter>>();

        spellingGameStateMachine = new SpellingGameStateMachine(this, ScreenManager.Language.HMONG);

    }

    public void setDisplayLanguage(ScreenManager.Language language) {
        setAlphabet(language);
        // TODO: change letterSpaces to new length of word on language change
    }

    /**
     * Sets up game screen with indicated language and associated alphabet
     *
     * @param language
     */
    private void setAlphabet(ScreenManager.Language language) {
        letterTable.clearChildren();
        switch (language) {
            case ENGLISH:
                String[] alphabet = {
                        "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m",
                        "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
                int letterIndex = 0;
                for (int i = 0; i < 2; i++) { // For each row
                    for (int j = 0; j < 13; j++) { // For each column
                        if (letterIndex < alphabet.length) {
                            final Letter letter = new Letter(alphabet[letterIndex++]);

                            letterTable.add(new Container<Letter>(letter).size(letterButtonSize)).size(letterButtonSize);
                            setLetterAsDraggable(letter);
                        }
                    }
                    letterTable.row();
                }
                return;
            case HMONG:
                letterButtonSize = 80;
                String[] consonants = {"c", "ch", "d", "dh", "dl", "f", "h", "hl", "hm", "hml", "hn", "hny",
                        "k", "kh", "l", "m", "ml", "n", "nc", "nch", "ndl", "nk", "nkh", "np", "nph", "npl", "nplh", "nq",
                        "nqh", "nr", "nrh", "nt", "nth", "nts", "ntsh", "ntx", "ntxh", "ny", "p", "ph", "pl", "plh", "q",
                        "qh", "r", "rh", "s", "t", "th", "ts", "tsh", "tx", "txh", "v", "x", "xy", "y", "z"};
                String[] vowels = {"a", "aa", "ai", "au", "aw", "e", "ee", "i", "ia", "o", "oo", "u", "ua", "w"};

                letterIndex = 0;
                for (int i = 0; i < 4; i++) { // row
                    for (int j = 0; j < 20; j++) { // column
                        if (letterIndex < consonants.length + vowels.length + tones.length) {
                            final Letter letter;
                            if (letterIndex < consonants.length) { // consonant
                                letter = new Letter(consonants[letterIndex++]);
                            } else if (letterIndex < consonants.length + vowels.length) { // vowel
                                letter = new Letter(vowels[(letterIndex++) - consonants.length]);
                            } else { // tone
                                letter = new Letter(tones[(letterIndex++) - (consonants.length + vowels.length)]);
                                letter.setIsTone();
                            }
                            letter.getLabel().setFontScale(.5f);
                            letterTable.add(new Container<Letter>(letter).size(letterButtonSize)).size(letterButtonSize);
                            setLetterAsDraggable(letter);
                        }
                    }
                    letterTable.row();
                }
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
                Letter letterCopy = new Letter(getActor());
                letterCopy.setSize(letterSpaceSize, letterSpaceSize);
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
            spaceTable.add(letterContainer.size(letterSpaceSize, letterSpaceSize)).size(letterSpaceSize + 10, letterSpaceSize + 55); // offset for underline size
            letterSpaces.add(letterContainer);

            dragAndDrop.addTarget(new DragAndDrop.Target(letterContainer) { // TODO: flickering after drag
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
     * @return string
     */
    public String getWordInSpaces() {
        String currentString = "";
        for (Container<Letter> letterContainer : letterSpaces) {
            if (letterContainer.hasChildren()) {
                currentString += letterContainer.getActor().getName();
            } else {
                currentString += " ";
            }
        }
        // If the hmong word contains a tone, trim tone letter to 1 lowercase character
        for (String tone : tones) {
            if (currentString.contains(tone)) {
                int startIndex = currentString.indexOf(tone);
                currentString = currentString.substring(0, startIndex) + tone.substring(tone.length() - 1).toLowerCase();
            }
        }
        return currentString;
    }

    /**
     * Decides whether the spaces are filled with letters
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

    public void playWordSFX(Word currentWord) {
        Sound sound = Gdx.audio.newSound(Gdx.files.internal("sounds/SFX/" + currentWord.getWordId() + ".mp3"));
        sound.play();
    }



    public void playCorrectSFX(Word currentWord) {
        Sound sound = Gdx.audio.newSound(Gdx.files.internal("sounds/SFX/" + currentWord.getWordId() + ".mp3"));
        sound.play();
        Sound applause = Gdx.audio.newSound(Gdx.files.internal("sounds/SFX/applause.mp3"));
        applause.play();
    }
    public void playWrongSFX() {
        Sound buzzer = Gdx.audio.newSound(Gdx.files.internal("sounds/SFX/buzzer.mp3"));
        buzzer.play();
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
