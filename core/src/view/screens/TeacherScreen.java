package view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import view.AssetManager;
import view.GdxGame;
import viewmodel.ScreenManager;

public class TeacherScreen implements Screen {
    boolean changeVersion = true;
    private GdxGame game;
    private Stage stage;
    private ImageButton backButton;
    private ChangeListener doOnBackButton;
    private Label title;
    private Table selectionTable;

    public TeacherScreen(GdxGame gdxGame) {
        this.game = gdxGame;
        stage = new Stage(gdxGame.viewport, gdxGame.batch);
        Gdx.input.setInputProcessor(stage);

        setStage();
    }

    private void setStage() {
        Table mainTable = new Table();
        mainTable.setBounds(0, 0, GdxGame.WIDTH, GdxGame.HEIGHT);
        mainTable.top().left();
        stage.addActor(mainTable);

        Image background = new Image(AssetManager.getTextureRegion("background"));
        mainTable.setBackground(background.getDrawable());

        backButton = new ImageButton(AssetManager.imageButtonStyle);
        title = new Label("", AssetManager.buttonSkin);
        title.setFontScale(5);

        selectionTable = new Table();

        mainTable.add(backButton).size(200).top().padTop(50).left().padLeft(50);
        mainTable.add(title).expandX();
        mainTable.add().size(200).top().padTop(50).right().padRight(50);
        mainTable.row();
        mainTable.add().height(50).row();
        mainTable.add(selectionTable).colspan(3);

        selectTeachers();
    }

    private void selectTeachers() {
        title.setText("Select a teacher to see their students\nor change teacher");
        String text = "Enter a teacher's name";

        if (doOnBackButton != null) {
            backButton.removeListener(doOnBackButton);
        }
        backButton.addListener(doOnBackButton = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.nextScreen(new StartScreen(TeacherScreen.this.game));
            }
        });
        ChangeListener doOnSelectName = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                selectStudents();
            }
        };

        selectionTable.clearChildren();
        selectionTable.add(ScreenManager.getNewSelectionTable(ScreenManager.SelectionType.TEACHERS, doOnSelectName, changeVersion, text));
    }

    private void selectStudents() {
        title.setText("Select a student to see their history\nor change student");
        String text = "Enter a student's name";

        backButton.removeListener(doOnBackButton);
        backButton.addListener(doOnBackButton = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                selectTeachers();
            }
        });
        ChangeListener doOnSelectName = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                displayHistories();
            }
        };

        selectionTable.clearChildren();
        selectionTable.add(ScreenManager.getNewSelectionTable(ScreenManager.SelectionType.STUDENTS, doOnSelectName, changeVersion, text));
    }

    private void displayHistories() {
        // TODO
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
