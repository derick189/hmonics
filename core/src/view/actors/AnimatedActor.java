package view.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class AnimatedActor extends Actor {
    private Animation<TextureRegion> animation;
    private float time = 0f;
    private boolean flip;

    public AnimatedActor(Animation<TextureRegion> animation, String fileName) {
//        setDebug(true);
        this.setName(fileName);
        this.animation = animation;
        setSize(animation.getKeyFrame(0).getRegionWidth(), animation.getKeyFrame(0).getRegionHeight());
        flip = false;
    }

    public void changeAnimation(Animation<TextureRegion> animation, String fileName) {
        this.setName(fileName);
        this.animation = animation;
        setSize(animation.getKeyFrame(0).getRegionWidth(), animation.getKeyFrame(0).getRegionHeight());
        time = 0f;
    }

    public boolean isFlip() {
        return flip;
    }

    public void setFlip(boolean flip) {
        this.flip = flip;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        time += delta;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        TextureRegion currentFrame = animation.getKeyFrame(time);
        batch.draw(currentFrame, (flip ? getWidth() : 0) + getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), (flip ? -1 : 1) * getScaleX(), getScaleY(), getRotation());
//        drawChildren(batch, parentAlpha);
    }
}
