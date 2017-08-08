package view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import view.AssetManager;
import view.GdxGame;
import view.games.SpellingGameScreen;
import viewmodel.ScreenManager;

/**
 *
 */
public class StudentScreen implements Screen {
    private GdxGame game;
    private Stage stage;
    private Table mainTable;
    private Sound clickSound;

    public StudentScreen(GdxGame gdxGame) {
        this.game = gdxGame;
        stage = new Stage(gdxGame.viewport, gdxGame.batch);
        Gdx.input.setInputProcessor(stage);

        setStage();
    }

    private void setStage() {
        mainTable = new Table();
        mainTable.top().left().setBounds(0, 0, GdxGame.WIDTH, GdxGame.HEIGHT);
        mainTable.setBackground(new TextureRegionDrawable(AssetManager.getTextureRegion("background")));
        stage.addActor(mainTable);

        clickSound = Gdx.audio.newSound(Gdx.files.internal("sounds/SFX/BigClick.mp3"));

        selectTeachers();
    }

    private void selectTeachers() {
        String titleText = "Select a teacher.";
        ChangeListener doOnBackButton = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                clickSound.play();
                ScreenManager.nextScreen(new StartScreen(StudentScreen.this.game));
            }
        };
        ChangeListener doAfterSelectItem = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                clickSound.play();
                StudentScreen.this.selectStudents();
            }
        };

        mainTable.clearChildren();
        mainTable.addActor(ScreenManager.screenFactory(ScreenManager.SelectionType.TEACHERS, titleText, doOnBackButton, doAfterSelectItem, null, null));
    }

    private void selectStudents() {
        String titleText = "Select a student.";
        ChangeListener doOnBackButton = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                clickSound.play();
                selectTeachers();
            }
        };
        ChangeListener doAfterSelectItem = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                clickSound.play();
                StudentScreen.this.displayGames();
            }
        };

        mainTable.clearChildren();
        mainTable.addActor(ScreenManager.screenFactory(ScreenManager.SelectionType.STUDENTS, titleText, doOnBackButton, doAfterSelectItem, null, null));
    }

    private void displayGames() {
        String titleText = "Select a game.";
        ChangeListener doOnBackButton = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                clickSound.play();
                selectStudents();
            }
        };
        ChangeListener doAfterSelectItem = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                clickSound.play();
                clickSound = Gdx.audio.newSound(Gdx.files.internal("sounds/SFX/cheer.mp3"));
                clickSound.play();
                ScreenManager.nextScreen(new SpellingGameScreen(StudentScreen.this.game, ScreenManager.Language.HMONG));
            }
        };

        mainTable.clearChildren();
        mainTable.addActor(ScreenManager.screenFactory(ScreenManager.SelectionType.GAMES, titleText, doOnBackButton, doAfterSelectItem, null, null));
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
        clickSound.dispose();
        stage.dispose();
    }
}
