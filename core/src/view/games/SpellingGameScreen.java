package view.games;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import model.Word;
import view.AssetManager;
import view.GdxGame;
import view.actors.Letter;
import view.screens.TeamLogoSplashScreen;
import viewmodel.ScreenManager;
import viewmodel.SpellingGameStateMachine;

import java.util.ArrayList;
import java.util.Random;

public class SpellingGameScreen implements Screen {
    private GdxGame game;
    private Stage stage;
    public ImageButton backButton;
    private DragAndDrop dragAndDrop;
    private Music backgroundMusic;
    private Sound click;
    private Random random;
    public TextButton skipButton;
    // Actors added to the screen are drawn in the order they were added. Actors drawn later are drawn on top of everything before.
    // These groups are used to add actors to the screen in the right order. All actors added to groups are drawn when the group is drawn.
    // Because these groups are added in this order in setStage, if all actors are added to these groups and not the screen directly then
    private Group backgroundGroup;
    private Group actorsGroup;
    private Group animationsGroup;

    private Table letterTable;
    private Table pictureTable;
    private Table spaceTable;
    private Container<Image> pictureContainer;
    private ArrayList<Container<Letter>> letterSpaces;
    private SpellingGameStateMachine spellingGameStateMachine;
    public TextButton hintButton;
    public Label hintPopup;
    private int letterTableHeight = 360;
    private int pictureSize = 400;
    private int letterSpaceWidth = 150;
    private int letterSpaceHeight = 195;
    private int letterSize = 140;
    private int buttonWidth = 300;
    private int buttonHeight = 150;

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
     * Screen size in virtual pixels: WIDTH = 1920 height = 1080;
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
        backgroundImage.setSize(GdxGame.WIDTH, GdxGame.height);
        backgroundGroup.addActor(backgroundImage);

        Table mainTable = new Table();
        mainTable.setBounds(0, 0, GdxGame.WIDTH, GdxGame.height);
        actorsGroup.addActor(mainTable);

        letterTable = new Table();
        letterTable.setBounds(mainTable.getWidth() / 2, (mainTable.getHeight() - letterTableHeight) - 20, 0, letterTableHeight);
        mainTable.addActor(letterTable);
        pictureTable = new Table();
        pictureTable.setBounds(mainTable.getWidth() / 2, ((mainTable.getHeight() - pictureSize) / 2) - 60, 0, pictureSize);
        // Move picture table to fit the Hmong alphabet
        if (Gdx.app.getType() == Application.ApplicationType.iOS && ScreenManager.selectedLanguage == ScreenManager.Language.HMONG) {
            pictureTable.setBounds(mainTable.getWidth() / 2, ((mainTable.getHeight() - pictureSize) / 4) + 60, 0, pictureSize);
            letterTable.setBounds(mainTable.getWidth() / 2, (mainTable.getHeight() - letterTableHeight) - 170, 0, letterTableHeight);
        }
        mainTable.addActor(pictureTable);
        spaceTable = new Table();
        spaceTable.setBounds(mainTable.getWidth() / 2, 50, 0, letterSpaceHeight);
        mainTable.addActor(spaceTable);

        pictureTable.add(pictureContainer = new Container<Image>().size(pictureSize));
        letterSpaces = new ArrayList<Container<Letter>>();

        // Back button
        backButton = new ImageButton(AssetManager.backButtonStyle);
        backButton.setBounds(50, 50, buttonHeight, buttonHeight);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                backgroundMusic.stop();
                TeamLogoSplashScreen.getBackgroundMusic().play();
                ScreenManager.setScreen(ScreenManager.getPreviousScreen());
            }
        });
        backButton.setSize(buttonHeight, buttonHeight);
        mainTable.addActor(backButton);

        // Skip button
        skipButton = new TextButton("Skip", AssetManager.textButtonStyle64);
        skipButton.setBounds(mainTable.getWidth() - buttonWidth - 50, 50, buttonWidth, buttonHeight);
        skipButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SpellingGameScreen.this.spellingGameStateMachine.changeToNextWord();
            }
        });
        mainTable.addActor(skipButton);

        // Hint button
        hintButton = new TextButton("Hint", AssetManager.textButtonStyle64);
        hintButton.setBounds(mainTable.getWidth() - buttonWidth - 50, buttonHeight + 50, buttonWidth, buttonHeight);
        hintButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                hintPopup.clearActions();
                hintPopup.addAction(Actions.sequence(
                        Actions.fadeIn(2),
                        Actions.delay(2),
                        Actions.fadeOut(2)
                ));
            }
        });
        mainTable.addActor(hintButton);

        hintPopup = new Label("", AssetManager.labelStyle64Clear);
        hintPopup.setBounds(mainTable.getWidth() - buttonWidth - 50, buttonHeight * 2 + 50, buttonWidth, buttonHeight);
        hintPopup.getColor().a = 0;
        hintPopup.setAlignment(Align.center);
        mainTable.addActor(hintPopup);

        // Start complementary state machine last
        spellingGameStateMachine = new SpellingGameStateMachine(this);
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
        int numRows = 3;
        int iosCol = 5;
        int letterSelectSize = 89;
        if (Gdx.app.getType() == Application.ApplicationType.iOS) {
            letterSelectSize += 20;
            numRows += 1;
            iosCol -= 5;
        }
        letterTable.clearChildren();
        switch (language) {
            case ENGLISH:
                numRows = 2;
                letterSelectSize += 31;
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
                numRows += 1;
                String[] consonants = {"c", "ch", "d", "dh", "dl", "f", "h", "hl", "hm", "hml", "hn", "hny",
                        "k", "kh", "l", "m", "ml", "n", "nc", "nch", "ndl", "nk", "nkh", "np", "nph", "npl", "nplh", "nq",
                        "nqh", "nr", "nrh", "nt", "nth", "nts", "ntsh", "ntx", "ntxh", "ny", "p", "ph", "pl", "plh", "q",
                        "qh", "r", "rh", "s", "t", "th", "ts", "tsh", "tx", "txh", "v", "x", "xy", "y", "z"};
                String[] vowels = {"a", "aa", "ai", "au", "aw", "e", "ee", "i", "ia", "o", "oo", "u", "ua", "w"};
                String[] tones = {"koJ", "muS", "kuV", "niaM", "neeG", "siaB", "zoO", "toD"};

                Table consonantsTable = new Table();
                consonantsTable.setBackground(AssetManager.backPlate);
                letterTable.add(consonantsTable);
                Table vowelsTable = new Table();
                vowelsTable.setBackground(AssetManager.backPlate);
                letterTable.add(vowelsTable);
                Table tonesTable = new Table();
                tonesTable.setBackground(AssetManager.backPlate);
                letterTable.add(tonesTable);

                for (int i = 0; i < numRows; i++) { // row
                    for (int j = 0; j < 10 + iosCol; j++) { // column
                        if ((i * (10 + iosCol)) + j < consonants.length) { // leaves empty spaces
                            Letter letter = new Letter(consonants[(i * (10 + iosCol)) + j], letterSelectSize - 10);
                            consonantsTable.add(new Container<Letter>(letter).size(letterSelectSize));
                            setLetterAsDraggable(letter);
                        }
                    }
                    consonantsTable.row();
                    for (int j = 0; j < 4; j++) { // column
                        if ((i * 4) + j < vowels.length) { // leaves empty spaces
                            Letter letter = new Letter(vowels[(i * 4) + j], letterSelectSize - 10);
                            vowelsTable.add(new Container<Letter>(letter).size(letterSelectSize));
                            setLetterAsDraggable(letter);
                        }
                    }
                    vowelsTable.row();
                    for (int j = 0; j < 2; j++) { // column
                        if ((i * 2) + j < tones.length) { // leaves empty spaces
                            Letter letter = new Letter(tones[(i * 2) + j], letterSelectSize - 10);
                            letter.setIsTone();
                            tonesTable.add(new Container<Letter>(letter).size(letterSelectSize));
                            setLetterAsDraggable(letter);
                        }
                    }
                    tonesTable.row();
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
            spaceTable.add(letterContainer.size(letterSize, letterSize)).size(letterSpaceWidth, letterSpaceHeight);
            letterSpaces.add(letterContainer);

            dragAndDrop.addTarget(new DragAndDrop.Target(letterContainer) {
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

    public void winConfetti(String fileName) {
        for (Container<Letter> letterContainer : letterSpaces) {
            if (letterContainer.hasChildren()) {
                confettiEffect(letterContainer.getActor(), fileName);
            }
        }
    }

    /**
     * Confetti animation from the center of the subject actor.
     *
     * @param subject
     * @param fileName
     */
    public void confettiEffect(Actor subject, String fileName) {
        int size = 100;
        float duration = 1.5f;
        int distance = 300;
        Vector2 vector2 = subject.localToStageCoordinates(new Vector2(subject.getX(), subject.getY()));
        for (int i = 0; i < 10; i++) {
            Actor explosion = new Image(AssetManager.getTextureRegion(fileName));
            explosion.setTouchable(Touchable.disabled);
            animationsGroup.addActor(explosion);

            explosion.setBounds(vector2.x, vector2.y, size, size);
            explosion.setOrigin(size / 2, size / 2);
            explosion.addAction(Actions.moveTo(vector2.x + (random.nextBoolean() ? random.nextInt(distance) : -random.nextInt(distance)) + random.nextInt(distance), vector2.y + random.nextInt(distance),
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
