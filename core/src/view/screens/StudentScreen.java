package view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import view.AssetManager;
import view.GdxGame;
import view.games.SpellingGameScreen;
import viewmodel.ScreenManager;

public class StudentScreen implements Screen {
    private GdxGame game;
    private Stage stage;
    private ImageButton backButton;
    private ChangeListener doOnBackButton;
    private Label title;
    private Table selectionTable;

    public StudentScreen(GdxGame gdxGame) {
        this.game = gdxGame;
        stage = new Stage(gdxGame.viewport, gdxGame.batch);
        Gdx.input.setInputProcessor(stage);

        setStage();
    }

    private void setStage() {
        Table mainTable = new Table();
        mainTable.top().left().setBounds(0, 0, GdxGame.WIDTH, GdxGame.HEIGHT);
        mainTable.setBackground(new TextureRegionDrawable(AssetManager.getTextureRegion("background")));
        stage.addActor(mainTable);

        backButton = new ImageButton(AssetManager.backButtonStyle);
        title = new Label("", AssetManager.labelStyle64);
        selectionTable = new Table();

        mainTable.add(backButton).size(150).top().left().padTop(50).padLeft(75).padRight(75);
        mainTable.add(title).padTop(20).expandX().fillX().fillY();
        mainTable.add().size(150).top().right().padTop(50).padLeft(75).padRight(75);
        mainTable.row();
        mainTable.add().height(50).row();
        mainTable.add(selectionTable).colspan(3);

        selectTeachers();
    }

    private void selectTeachers() {
        title.setText("Select a teacher:");
        String infoText = "";

        if (doOnBackButton != null) {
            backButton.removeListener(doOnBackButton);
        }
        backButton.addListener(doOnBackButton = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.nextScreen(new StartScreen(StudentScreen.this.game));
            }
        });
        ChangeListener doAfterSelectName = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                StudentScreen.this.selectStudents();
            }
        };
        ChangeListener doAfterAddRemove = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            }
        };

        selectionTable.clearChildren();
        selectionTable.add(ScreenManager.getNewSelectionTable(ScreenManager.SelectionType.TEACHERS, infoText, doAfterSelectName, doAfterAddRemove));
    }

    private void selectStudents() {
        title.setText("Select a student:");
        String infoText = "";

        backButton.removeListener(doOnBackButton);
        backButton.addListener(doOnBackButton = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                selectTeachers();
            }
        });
        ChangeListener doAfterSelectName = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                StudentScreen.this.displayGames();
            }
        };
        ChangeListener doAfterAddRemove = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            }
        };

        selectionTable.clearChildren();
        selectionTable.add(ScreenManager.getNewSelectionTable(ScreenManager.SelectionType.STUDENTS, infoText, doAfterSelectName, doAfterAddRemove));
    }

    private void displayGames() {
        // TODO
        ScreenManager.nextScreen(new SpellingGameScreen(StudentScreen.this.game));
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
