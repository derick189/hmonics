package view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import model.DataManager;
import view.games.SpellingGameScreen;
import view.start.SplashScreen;
import view.start.StartScreen;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class GdxGame extends Game {

    public static final int WIDTH = 1920;
    public static final int HEIGHT = 1080;

    public OrthographicCamera camera;
    public ExtendViewport viewport;
    public SpriteBatch batch;
//    public BitmapFont font;

    public void create() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WIDTH, HEIGHT);
        viewport = new ExtendViewport(WIDTH, HEIGHT, camera);

//        font = new BitmapFont();
        AssetManager.init();
        DataManager.populate();

        batch = new SpriteBatch();

        setScreen(new SplashScreen(this));

        setScreen(new StartScreen(this));


//        try {
//            //setScreen(new SpellingGameScreen(this, new view.start.StartScreen(this)));
//        } catch (UnsupportedAudioFileException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (LineUnavailableException e) {
//            e.printStackTrace();
//        }
    }

    public void render() { super.render(); } //important!

    public void dispose() {
        batch.dispose();
//        font.dispose();
    }
}
