package view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import model.DataManager;
import view.screens.TeamLogoSplashScreen;

/**
 * Used by libGDX to draw everything on the currently set screen.
 * @authors Derick Lenvik, Jared Johnson
 */
public class GdxGame extends Game {
    public static int virtualWidth = 1920;
    public static int virtualHeight = 1080;
    public static boolean isResolution43 = false;

    public OrthographicCamera camera;
    public ExtendViewport viewport;
    public SpriteBatch batch;

    public void create() {
        if (((float) Gdx.app.getGraphics().getWidth() / (float) Gdx.app.getGraphics().getHeight()) == (4f / 3f)) {
            isResolution43 = true;
            virtualHeight = 1440;
        }
        camera = new OrthographicCamera();
        camera.viewportHeight = virtualHeight;
        camera.viewportWidth = virtualWidth;
        camera.setToOrtho(false, virtualWidth, virtualHeight);
        viewport = new ExtendViewport(virtualWidth, virtualHeight, virtualWidth, 1440, camera);
        batch = new SpriteBatch();

        AssetManager.init();
        DataManager.populate();

        setScreen(new TeamLogoSplashScreen(GdxGame.this));
    }

    public void render() {
        super.render();
    } //important!

    public void dispose() {
        batch.dispose();
    }
}
