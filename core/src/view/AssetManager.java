package view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class AssetManager {
    public static TextureAtlas atlas;
    public static Skin buttonSkin;
    public static Music spellingGameMusic;
    public static Texture introScreenBackground;
    public static ImageButtonStyle imageButtonStyle;

    public static void init() {
        atlas = new TextureAtlas(Gdx.files.internal("images/pack.atlas"));
        buttonSkin = new Skin(Gdx.files.internal("skins/skin.json"));
        introScreenBackground = new Texture("images/IntroScreenBackground.png");
        spellingGameMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/spellingGameMusic.mp3"));
        spellingGameMusic.setLooping(true);
        spellingGameMusic.setVolume(0.5f);
        imageButtonStyle = new ImageButtonStyle();
        imageButtonStyle.up = AssetManager.buttonSkin.getDrawable("buttonUp");
        imageButtonStyle.down = AssetManager.buttonSkin.getDrawable("buttonDown");
        imageButtonStyle.over = AssetManager.buttonSkin.getDrawable("buttonOver");
    }

    public static TextureRegion getTextureRegion(String fileName) {
        return new TextureRegion(AssetManager.atlas.findRegion(fileName));
    }

    public static Animation<TextureRegion> getAnimation(String fileName) {
        return new Animation<TextureRegion>(0.33f, AssetManager.atlas.findRegions(fileName));
    }
}
