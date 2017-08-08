package view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import view.AssetManager;
import view.GdxGame;
import viewmodel.ScreenManager;

public class TeamLogoSplashScreen implements Screen {
    private GdxGame game;
    private Stage stage;
    private static Music backgroundMusic;

    public TeamLogoSplashScreen(GdxGame gdxGame) {
        this.game = gdxGame;
        stage = new Stage(gdxGame.viewport, gdxGame.batch);
        Gdx.input.setInputProcessor(stage);

        setStage();
    }

    private void setStage() {
        backgroundMusic = AssetManager.getMusic("IntroMusic");
        backgroundMusic.play();
        Texture introScreenBackground = new Texture(Gdx.files.internal("packed-images/TeamLogoSplash.png"));
        Image image = new Image(introScreenBackground);
        image.setSize(GdxGame.WIDTH, GdxGame.height);
        stage.addActor(image);

        image.addAction(Actions.sequence(
                Actions.delay(2f),
                Actions.run(new Runnable() {
                    public void run() {
                        ScreenManager.start(game, new LibGDXSplashScreen(game));
                    }
                })
        ));
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

    public static Music getBackgroundMusic() {
        return backgroundMusic;
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
