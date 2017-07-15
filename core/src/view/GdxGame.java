package view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import model.DataManager;
import view.games.SpellingGameScreen;
import viewmodel.ScreenManager;

public class GdxGame extends Game {

    public static final int WIDTH = 1920;
    public static final int HEIGHT = 1080;

    public OrthographicCamera camera;
    public ExtendViewport viewport;
    public SpriteBatch batch;
    public int introTime = 10000;
//    public BitmapFont font;

    public void create() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WIDTH, HEIGHT);
        viewport = new ExtendViewport(WIDTH, HEIGHT, camera);

        batch = new SpriteBatch();
//        font = new BitmapFont();

        AssetManager.init();
        DataManager.populate();

        ScreenManager.start(this, new SpellingGameScreen(this));


//        Gdx.graphics.setContinuousRendering(false);
//        setScreen(new SplashScreen(this));
//
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        Gdx.graphics.requestRendering();
//        setScreen(new StartScreen(this));


//        try {
//            //setScreen(new SpellingGameScreen(this, new view.screens.StartScreen(this)));
//        } catch (UnsupportedAudioFileException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (LineUnavailableException e) {
//            e.printStackTrace();
//        }
    }

    public void render() {
        super.render();
    } //important!

    public void dispose() {
        batch.dispose();
//        font.dispose();
    }
}
