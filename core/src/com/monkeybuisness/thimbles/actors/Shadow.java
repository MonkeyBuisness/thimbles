package com.monkeybuisness.thimbles.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Shadow extends Actor {

    private static final float DEFAULT_SHADOW_ALPHA = .65f;
    private TextureRegion textureRegion = null;
    private float alpha = DEFAULT_SHADOW_ALPHA;

    public Shadow texture(Texture texture) {
        if (textureRegion == null)
            textureRegion = new TextureRegion(texture);
        else
            textureRegion.setTexture(texture);
        return this;
    }

    public Shadow position(Vector2 position) {
        setPosition(position.x, position.y);
        return this;
    }

    public Shadow size(Vector2 size) {
        setSize(size.x, size.y);
        return this;
    }

    public Shadow alpha(float alpha) {
        this.alpha = alpha;
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
        if (textureRegion != null) {
            Color batchColor = batch.getColor();
            batch.setColor(batchColor.r, batchColor.g, batchColor.b, this.alpha);
            batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(),
                    getScaleX(), getScaleY(), getRotation());
            batch.setColor(batchColor);
        }
        super.draw(batch, alpha);
    }
}