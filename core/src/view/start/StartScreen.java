package view.start;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import view.GdxGame;
import view.games.SpellingGameScreen;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class StartScreen implements Screen {

    private Stage stage;
    private GdxGame game;

    public StartScreen(GdxGame gdxGame) {
        this.game = gdxGame;
        stage = new Stage(gdxGame.viewport, gdxGame.batch);
        Gdx.input.setInputProcessor(stage);

        setStage();
    }

    private void setStage() {
        Table mainTable = new Table();
//        mainTable.setDebug(true);
        mainTable.setBounds(0, 0, GdxGame.WIDTH, GdxGame.HEIGHT);
        stage.addActor(mainTable);

        Image titleImage = new Image(view.AssetManager.getTextureRegion("gem"));
        ImageButton studentButton = new ImageButton(view.AssetManager.buttonSkin);
        ImageButton teacherButton = new ImageButton(view.AssetManager.buttonSkin);
        view.AssetManager.introMusic.play();
        studentButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                try {
                    StartScreen.this.game.setScreen(new SpellingGameScreen(game, StartScreen.this));
                } catch (UnsupportedAudioFileException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (LineUnavailableException e) {
                    e.printStackTrace();
                }
            }
        });
        teacherButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

            }
        });

        mainTable.row().height(300);
        mainTable.add(titleImage).size(80);
        mainTable.row().height(300);
        mainTable.add(studentButton).size(80);
        mainTable.add(teacherButton).size(80);
//        mainTable.row().height();
    }

    public void changeScreen(Screen screen) {
        game.setScreen(screen);
        this.dispose();
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
