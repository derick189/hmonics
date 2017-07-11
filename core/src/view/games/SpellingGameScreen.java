package view.games;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import viewmodel.SpellingGameStateMachine;
import view.GdxGame;
import view.actors.Letter;
import view.start.StartScreen;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.ArrayList;

public class SpellingGameScreen implements Screen {
    private final int separatorHeight = 40;
    private final int letterSize = 110;
    private final int imageSize = 400;
    private final int spaceSize = 140;
    private Stage stage;
    private GdxGame game;
    private Screen parentScreen;
    private DragAndDrop dragAndDrop;
    // Actors added to the screen are drawn in the order they were added. Actors drawn later are drawn on top of everything before.
    // These groups are used to add actors to the screen in the right order. All actors added to groups are drawn when the group is drawn.
    // These are added in this order in setStage. All actors are added to these groups and not the screen directly.
    private Group backgroundGroup;
    private Group actorsGroup;
    private Group animationsGroup;

    private ImageButton backButton;
    private Table letterTable;
    private Table pictureTable;
    private Table spaceTable;
    private Container<Image> pictureContainer;
    private ArrayList<Container<Letter>> letterContainers;
    private SpellingGameStateMachine spellingGameStateMachine;

    public SpellingGameScreen(final GdxGame gdxGame, Screen parentScreen) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        this.game = gdxGame;
        stage = new Stage(gdxGame.viewport, gdxGame.batch);
        Gdx.input.setInputProcessor(stage);
        dragAndDrop = new DragAndDrop();
        dragAndDrop.setDragTime(0);

        this.parentScreen = parentScreen;


        setStage();
    }

    private void setStage() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        view.AssetManager.spellingGameMusic.play();
        stage.addActor(backgroundGroup = new Group());
        stage.addActor(actorsGroup = new Group());
        stage.addActor(animationsGroup = new Group());

        Image backgroundImage = new Image(view.AssetManager.getTextureRegion("background"));
        backgroundImage.setSize(GdxGame.WIDTH, GdxGame.HEIGHT);
        backgroundGroup.addActor(backgroundImage);

        Table mainTable = new Table();
//        mainTable.setDebug(true);
        mainTable.setBounds(0, 0, GdxGame.WIDTH, GdxGame.HEIGHT);
        actorsGroup.addActor(mainTable);

        backButton = new ImageButton(view.AssetManager.imageButtonStyle);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SpellingGameScreen.this.game.setScreen(new StartScreen(game));
            }
        });
        letterTable = new Table();
        pictureTable = new Table();
        spaceTable = new Table();

//        mainTable.add(backButton).size(80).left();
        mainTable.row();
        mainTable.add().height(separatorHeight);
        mainTable.row();
        mainTable.add(letterTable).height(letterSize * 2);
        mainTable.row();
        mainTable.add().height(separatorHeight);
        mainTable.row();
        mainTable.add(pictureTable).height(imageSize);
        mainTable.row();
        mainTable.add().height(separatorHeight);
        mainTable.row();
        mainTable.add(spaceTable).height(spaceSize);
        mainTable.row();
        mainTable.add().height(GdxGame.HEIGHT - ((separatorHeight * 3) + (letterSize * 2) + imageSize + spaceSize));

        pictureTable.add(pictureContainer = new Container<Image>().size(imageSize));
        letterContainers = new ArrayList<Container<Letter>>();

        spellingGameStateMachine = new SpellingGameStateMachine(this);
    }

    public void setDisplayLanguage(SpellingGameStateMachine.Language language) {
        setAlphabet(language);
    }

    private void setAlphabet(SpellingGameStateMachine.Language language) {
        letterTable.clearChildren();
        switch (language) {
            case ENGLISH:
                String[] alphabet = {
                        "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m",
                        "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
                int letterIndex = 0;
                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < 13; j++) {
                        if (letterIndex < alphabet.length) {
                            final Letter letter = new Letter(alphabet[letterIndex++]);
                            letterTable.add(new Container<Letter>(letter).size(letterSize)).size(letterSize);

                            // Creates a copy when letter is dragged from the alphabet. The copy does not create a copy when moved.
                            dragAndDrop.addSource(new DragAndDrop.Source(letter) {
                                public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                                    DragAndDrop.Payload payload = new DragAndDrop.Payload();
                                    Letter letterCopy = new Letter(getActor());
                                    letterCopy.setSize(spaceSize, spaceSize);
                                    payload.setObject(letterCopy);
                                    payload.setDragActor(letterCopy);
                                    dragAndDrop.addSource(new DragAndDrop.Source(letterCopy) {
                                        public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                                            DragAndDrop.Payload payload = new DragAndDrop.Payload();
                                            payload.setObject(getActor());
                                            payload.setDragActor(getActor());
                                            return payload;
                                        }
                                    });
                                    return payload;
                                }
                            });
                        }
                    }
                    letterTable.row();
                }
                return;
            case HMONG:

                return;
        }
    }

    public void setPictureAndWordLength(String pictureId, int wordLength) {
        pictureContainer.setActor(new Image(view.AssetManager.getTextureRegion(pictureId)));
        setSpaces(wordLength);
    }

    private void setSpaces(int spaces) {
        spaceTable.clearChildren();
        letterContainers.clear();
        for (int i = 0; i < spaces; i++) {
            Container<Letter> letterContainer = new Container<Letter>();
            letterContainer.setTouchable(Touchable.enabled);
            letterContainer.setBackground(new TextureRegionDrawable(view.AssetManager.getTextureRegion("underline")));
            spaceTable.add(letterContainer.size(spaceSize, spaceSize)).size(spaceSize + 10, spaceSize + 50);
            letterContainers.add(letterContainer);

            dragAndDrop.addTarget(new DragAndDrop.Target(letterContainer) {
                public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                    return true;
                }

                public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                    Container<Letter> container = (Container) getActor();
                    container.setActor((Letter) payload.getObject());
                    SpellingGameScreen.this.spellingGameStateMachine.doEvent(SpellingGameStateMachine.Event.DROPPED_LETTER);
                }
            });
        }
    }

    public ArrayList<Container<Letter>> getLetterContainers() {
        return letterContainers;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.3f, 0, 0, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
//        stage.getBatch().setColor(1,1,1,1);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, false);
    }

    @Override
    public void show() {
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
