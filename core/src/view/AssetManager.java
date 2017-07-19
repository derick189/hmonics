package view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class AssetManager {
    public static TextureAtlas atlas;
    public static Skin defaultSkin;
    public static ButtonStyle defaultStyle;
    public static Drawable backplate;
    public static BitmapFont font64;
    public static LabelStyle labelStyle64;
    public static TextFieldStyle textFieldStyle;
    public static TextButtonStyle textButtonStyle;
    public static ImageButtonStyle imageButtonStyle;
    public static ImageButtonStyle backButtonStyle;

    public static void init() {
        atlas = new TextureAtlas(Gdx.files.internal("packed-images/pack.atlas"));
        defaultSkin = new Skin(Gdx.files.internal("skins/clean-crispy/clean-crispy-ui.json"));
        defaultStyle = new ButtonStyle();
        defaultStyle.up = AssetManager.defaultSkin.getDrawable("button-c");
        defaultStyle.down = AssetManager.defaultSkin.getDrawable("button-pressed-over-c");
        defaultStyle.over = AssetManager.defaultSkin.getDrawable("button-over-c");
        backplate = AssetManager.defaultSkin.getDrawable("button-c-clear");

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/open-sans/OpenSans-Semibold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.hinting = FreeTypeFontGenerator.Hinting.Full;
        parameter.color = Color.BLACK;
        parameter.size = 64;
        font64 = generator.generateFont(parameter);

        labelStyle64 = new LabelStyle(font64, Color.BLACK);
        labelStyle64.background = view.AssetManager.backplate;
        textFieldStyle = new TextFieldStyle(font64, Color.BLACK, defaultStyle.down, defaultStyle.down, defaultStyle.up);
        textButtonStyle = new TextButtonStyle(defaultStyle.up, defaultStyle.down, defaultStyle.checked, font64);

        imageButtonStyle = new ImageButtonStyle(defaultStyle);

        backButtonStyle = new ImageButtonStyle(defaultStyle);
        backButtonStyle.imageUp = new TextureRegionDrawable(getTextureRegion("BackButton"));
    }

    public static TextureRegion getTextureRegion(String fileName) {
        return new TextureRegion(AssetManager.atlas.findRegion(fileName));
    }

    public static Animation<TextureRegion> getAnimation(String fileName) {
        return new Animation<TextureRegion>(0.33f, AssetManager.atlas.findRegions(fileName));
    }

    public static Music getMusic(String fileName) {
        Music music = Gdx.audio.newMusic(Gdx.files.internal("sounds/" + fileName + ".mp3"));
        music.setLooping(true);
        music.setVolume(0.5f);
        return music;
    }
}
