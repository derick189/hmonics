package view.actors;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import view.AssetManager;

public class Letter extends Stack {
    public Label nameLabel;
    private ImageButton imageButton;

    public Letter(String name) {
        setName(name);
        imageButton = new ImageButton(AssetManager.imageButtonStyle);
        add(imageButton);
        nameLabel = new Label(name, AssetManager.buttonSkin);
        nameLabel.setTouchable(Touchable.disabled);
        nameLabel.setAlignment(1);
        nameLabel.setFontScale(3);
        addActor(nameLabel);
    }

    public Letter(Actor letter) {
        this(letter.getName());
    }
}
