package view.actors;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import view.AssetManager;

public class Letter extends TextButton {
    private boolean isTone;

    public Letter(String name) {
        super(name, AssetManager.textButtonStyle);
        setName(name);
    }

    public Letter(Actor letter) {
        this(letter.getName());
    }

    public boolean isTone() {
        return isTone = true;
    }
    public void setIsTone() {
        this.isTone = true;
    }
}
