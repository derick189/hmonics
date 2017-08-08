package view;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import model.DataManager;
import view.screens.TeamLogoSplashScreen;

public class GdxGame extends Game {

    public static final int WIDTH = 1920;
    public static int height = 1080;

    public OrthographicCamera camera;
    public ExtendViewport viewport;
    public SpriteBatch batch;

    public void create() {
        if (Gdx.app.getType() == Application.ApplicationType.iOS) {
            height = 1440;
        }
        camera = new OrthographicCamera();
        camera.viewportHeight = height;
        camera.viewportWidth = WIDTH;
        camera.setToOrtho(false, WIDTH, height);
        viewport = new ExtendViewport(WIDTH, height, WIDTH, 1440, camera);

        batch = new SpriteBatch();

        setScreen(new TeamLogoSplashScreen(GdxGame.this));

        AssetManager.init();
        DataManager.populate();
    }

    public void render() {
        super.render();
    } //important!

    public void dispose() {
        batch.dispose();
    }
}
