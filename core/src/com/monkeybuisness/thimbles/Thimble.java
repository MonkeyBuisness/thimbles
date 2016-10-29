package com.monkeybuisness.thimbles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Thimble extends Actor implements OuterBuilder<Thimble> {

    private Texture texture = null;
    private Vector2 position = null;
    private Vector2 size = null;

    public Thimble() {
        position = new Vector2();
        size = new Vector2();
    }

    public Thimble texture(Texture texture) {
        this.texture = texture;
        return this;
    }

    public Thimble position(Vector2 position) {
        this.position = position;
        setPosition(position.x, position.y);
        return this;
    }

    public Thimble size(Vector2 size) {
        this.size = size;
        setSize(size.x, size.y);
        return this;
    }

    @Override
    public void draw(Batch batch, float alpha) {
        batch.draw(texture, position.x, position.y, size.x, size.y);
    }

    @Override
    public Thimble build() {
        return this;
    }
}