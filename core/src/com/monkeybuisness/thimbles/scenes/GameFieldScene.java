package com.monkeybuisness.thimbles.scenes;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.monkeybuisness.thimbles.InnerBuilder;
import com.monkeybuisness.thimbles.OuterBuilder;
import com.monkeybuisness.thimbles.Thimble;
import javafx.stage.Stage;

public class GameFieldScene extends Scene implements OuterBuilder<GameFieldScene> {

    private int fieldWidth = 0;
    private int fieldHeight = 0;
    private Thimbles thimbles = null;
    private Sound backgroundSound = null;

    public GameFieldScene(int width, int height) {
        fieldWidth = width;
        fieldHeight = height;
        thimbles = new Thimbles(fieldWidth, fieldHeight);
    }

    public GameFieldScene viewport(Viewport viewport) {
        setViewport(viewport);
        return this;
    }

    public GameFieldScene backgroundSound(Sound sound) {
        this.backgroundSound = sound;
        return this;
    }

    public void ready() {
        if (backgroundSound != null)
            backgroundSound.play();
    }

    public Thimbles thimbles() {
        return thimbles;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void draw() {
        super.draw();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public GameFieldScene build() {
        return this;
    }

    public class Thimbles implements InnerBuilder<GameFieldScene> {

        private Thimble [][] thimbles;

        public Thimbles(int width, int height) {
            thimbles = new Thimble[width][height];
        }

        public Thimbles add(Thimble thimble, int row, int col) {
            if (row < thimbles[0].length && col < thimbles.length) {
                thimbles[row][col] = thimble;
                // TODO: specify size and position for thimble
                addActor(thimble);
                return this;
            }
            return this;
        }

        @Override
        public GameFieldScene build() {
            return GameFieldScene.this;
        }
    }
}