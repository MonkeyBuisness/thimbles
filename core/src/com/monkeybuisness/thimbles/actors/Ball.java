package com.monkeybuisness.thimbles.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Ball extends Actor {

    private TextureRegion textureRegion = null;

    public Ball texture(Texture texture) {
        if (textureRegion == null)
            textureRegion = new TextureRegion(texture);
        else
            textureRegion.setTexture(texture);
        return this;
    }

    public Ball position(Vector2 position) {
        setPosition(position.x, position.y);
        return this;
    }

    public Ball size(Vector2 size) {
        setSize(size.x, size.y);
        return this;
    }

    public void dispose() {
        if (textureRegion != null) {
            textureRegion.getTexture().dispose();
            textureRegion = null;
        }
    }

    @Override
    public void draw(Batch batch, float alpha) {
        if (textureRegion != null)
            batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(),
                    getScaleX(), getScaleY(), getRotation());
        super.draw(batch, alpha);
    }
}