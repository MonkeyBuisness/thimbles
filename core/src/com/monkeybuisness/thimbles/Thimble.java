package com.monkeybuisness.thimbles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class Thimble extends Actor implements OuterBuilder<Thimble> {

    private TextureRegion textureRegion = null;

    public Thimble texture(Texture texture) {
        if (textureRegion == null)
            textureRegion = new TextureRegion(texture);
        else
            textureRegion.setTexture(texture);
        return this;
    }

    public Thimble position(Vector2 position) {
        setPosition(position.x, position.y);
        return this;
    }

    public Thimble size(Vector2 size) {
        setSize(size.x, size.y);
        return this;
    }

    @Override
    public void draw(Batch batch, float alpha) {
        if (textureRegion != null)
            batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(),
                    getScaleX(), getScaleY(), getRotation());
        super.draw(batch, alpha);
    }

    @Override
    public Thimble build() {
        return this;
    }
}