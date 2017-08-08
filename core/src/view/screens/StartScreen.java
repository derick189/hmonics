package view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import view.AssetManager;
import view.GdxGame;
import viewmodel.ScreenManager;

public class StartScreen implements Screen {
    private GdxGame game;
    private Stage stage;
    private Sound sound;

    public StartScreen(GdxGame gdxGame) {
        this.game = gdxGame;
        stage = new Stage(gdxGame.viewport, gdxGame.batch);
        Gdx.input.setInputProcessor(stage);

        setStage();
    }

    private void setStage() {
        Table mainTable = new Table();
        mainTable.setBounds(0, 0, GdxGame.WIDTH, GdxGame.height);
        stage.addActor(mainTable);

        sound = Gdx.audio.newSound(Gdx.files.internal("sounds/SFX/BigClick.mp3"));

        Texture backgroundImage= new Texture(Gdx.files.internal("packed-images/StartScreenBackground.png"));
        Image background = new Image(backgroundImage);
        mainTable.setBackground(background.getDrawable());

        TextButton teacherButton = new TextButton("Teacher", AssetManager.textButtonStyle64);
        teacherButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                sound.play();
                ScreenManager.setScreen(new TeacherScreen(StartScreen.this.game));
            }
        });

        final TextButton studentButton = new TextButton("Student", AssetManager.textButtonStyle64);
        studentButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                sound.play();
                ScreenManager.setScreen(new StudentScreen(StartScreen.this.game));
            }
        });

        mainTable.add().height(425);
        mainTable.row();
        mainTable.add(teacherButton).width(450).height(200);
        mainTable.add().width(350);
        mainTable.add(studentButton).width(450).height(200);
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
