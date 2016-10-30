package com.monkeybuisness.thimbles.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.monkeybuisness.thimbles.InnerBuilder;
import com.monkeybuisness.thimbles.OuterBuilder;
import com.monkeybuisness.thimbles.Thimble;
import javafx.stage.Stage;

public class GameFieldScene extends Scene implements OuterBuilder<GameFieldScene> {

    private static final float GAME_WORLD_HEIGHT = 100.f;
    private static final float THIMBLE_PADDING = 2.f; // TODO: dynamically ???
    private int fieldWidth = 0;
    private int fieldHeight = 0;
    private Thimbles thimbles = null;
    private Sound backgroundSound = null;
    private OrthographicCamera gameFieldCamera = null;
    private Vector2 thimbleCellSize = null;
    private float thimbleSize = 0.f;

    public GameFieldScene(int width, int height) {
        fieldWidth = width;
        fieldHeight = height;
        thimbles = new Thimbles(fieldWidth, fieldHeight);
        gameFieldCamera = new OrthographicCamera();
        Viewport viewport = new StretchViewport(0.f, GAME_WORLD_HEIGHT, gameFieldCamera);
        viewport.apply();
        setViewport(viewport);
        adjustAspectRatio(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private void adjustAspectRatio(float width, float height) {
        float aspectRatio = height / width;
        float worldWidth = GAME_WORLD_HEIGHT / aspectRatio;
        Viewport viewport = getViewport();
        viewport.setWorldWidth(worldWidth);
        viewport.apply();
        gameFieldCamera.position.set(gameFieldCamera.viewportWidth / 2.f, gameFieldCamera.viewportHeight / 2.f, 0);
        // TODO: subtract top bar menu height and advertising height
        thimbleCellSize = new Vector2(viewport.getWorldWidth() / fieldWidth, viewport.getWorldHeight() / fieldHeight);
        thimbleSize = Math.min(thimbleCellSize.x, thimbleCellSize.y) - 2.f * THIMBLE_PADDING;
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
        adjustAspectRatio(width, height);
        thimbles.adjustThimbles();
        super.resize(width, height);
    }

    @Override
    public void update(float dt) {
        super.update(dt);
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
                adjustThimble(row, col);
                addActor(thimble);
            }
            return this;
        }

        private void adjustThimbles() {
            for (int i = 0; i < thimbles[0].length; ++i)
                for (int j = 0; j < thimbles.length; ++j)
                    adjustThimble(i, j);
        }

        private void adjustThimble(int thimbleRow, int thimbleColumn) {
            Thimble thimble = thimbles[thimbleRow][thimbleColumn];
            if (thimble != null) {
                thimble
                        .position(new Vector2(
                                thimbleCellSize.x * thimbleColumn + (thimbleCellSize.x - thimbleSize) / 2.f,
                                thimbleCellSize.y * thimbleRow + (thimbleCellSize.y - thimbleSize) / 2.f))
                        .size(new Vector2(thimbleSize, thimbleSize));
            }
        }

        @Override
        public GameFieldScene build() {
            return GameFieldScene.this;
        }
    }
}