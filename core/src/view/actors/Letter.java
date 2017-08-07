package view.actors;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import view.AssetManager;

public class Letter extends TextButton {
    // Can have special spelling for tones
    private String spelling;

    public Letter(String name, int size) {
        super(name, AssetManager.textButtonStyle64);
        if (size < 100) {
            setStyle(AssetManager.textButtonStyle32);
        }
        setName(name);
        spelling = name;
        setSize(size, size);
    }

    public Letter(Letter letter, int size) {
        this(letter.getName(), size);
        spelling = letter.spelling;
    }

    // If is a tone, trim actual spelling to 1 lowercase character
    public void setIsTone() {
        spelling = getName().substring(getName().length() - 1).toLowerCase();
    }

    public String getSpelling() {
        return spelling;
    }
}
