package com.monkeybuisness.thimbles.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.monkeybuisness.thimbles.InnerBuilder;
import com.monkeybuisness.thimbles.OuterBuilder;
import com.monkeybuisness.thimbles.actors.Thimble;
import com.monkeybuisness.thimbles.actions.IActorActionListener;
import com.monkeybuisness.thimbles.actions.thimbles.IThimbleAction;
import com.monkeybuisness.thimbles.advertisement.IAdvertisementBanner;

import java.util.ArrayList;

public class GameFieldScene extends Scene implements OuterBuilder<GameFieldScene>, IActorActionListener {

    private static final float GAME_WORLD_HEIGHT = 100.f;
    private static final float THIMBLE_PADDING = 10.f; // TODO: dynamically ???
    private static final float ROWS_HEIGHT_TO_PERCENTS = 100.f;
    private static final float ROW_HEIGHT_DECREASE_TO_PERCENTS = 15.f;
    private float gameWorldWidth = 0.f;
    private int fieldRowsCount = 0;
    private int fieldColumnsCount = 0;
    private Thimbles thimbles = null;
    private BackgroundMusic backgroundMusic = null;
    private OrthographicCamera gameFieldCamera = null;
    private Vector2 thimbleCellSize = null;
    private float thimbleSize = 0.f;
    private ArrayList<IThimbleAction> actionsList = null;
    private IThimbleAction currentAction = null;
    private boolean isGameFieldReady = false;
    private IAdvertisementBanner advertisementBanner = null;
    private float advertisementBannerHeight = 0.f;

    public GameFieldScene(int rows, int columns) {
        fieldRowsCount = rows;
        fieldColumnsCount = columns;
        thimbles = new Thimbles(fieldRowsCount, fieldColumnsCount);
        actionsList = new ArrayList<IThimbleAction>();
        gameFieldCamera = new OrthographicCamera();
        Viewport viewport = new StretchViewport(0.f, GAME_WORLD_HEIGHT, gameFieldCamera);
        viewport.apply();
        setViewport(viewport);
        adjustAspectRatio(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        backgroundMusic = new BackgroundMusic();
    }

    private void adjustAspectRatio(float width, float height) {
        float aspectRatio = height / width;
        gameWorldWidth = GAME_WORLD_HEIGHT / aspectRatio;
        Viewport viewport = getViewport();
        viewport.setWorldWidth(gameWorldWidth);
        viewport.apply();
        gameFieldCamera.position.set(gameFieldCamera.viewportWidth / 2.f, gameFieldCamera.viewportHeight / 2.f, 0);
        // TODO: subtract top bar menu height
        thimbleCellSize = new Vector2(
                viewport.getWorldWidth() / fieldColumnsCount,
                (viewport.getWorldHeight() - advertisementBannerHeight) / fieldRowsCount);
        thimbleSize = Math.min(thimbleCellSize.x, thimbleCellSize.y) - 2.f * THIMBLE_PADDING;
    }

    private float toAspectHeightValue(float screenHeightValue) {
        Viewport viewport = getViewport();
        int screenHeight = viewport.getScreenHeight();
        if (screenHeight <= 0)
            screenHeight = Gdx.graphics.getHeight();
        float aspect =  GAME_WORLD_HEIGHT / screenHeight;
        return aspect * screenHeightValue;
    }

    public BackgroundMusic backgroundMusic() {
        return backgroundMusic;
    }

    public GameFieldScene actions(ArrayList<IThimbleAction> actions) {
        actionsList.clear();
        actionsList.addAll(actions);
        return this;
    }

    public GameFieldScene advertisementBanner(IAdvertisementBanner advertisementBanner) {
        this.advertisementBanner = advertisementBanner;
        if (advertisementBanner != null)
            advertisementBannerHeight = toAspectHeightValue(advertisementBanner.bannerSize().y);
        if (isGameFieldReady)
            adjustAspectRatio(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        return this;
    }

    public void ready() {
        backgroundMusic.play();
        if (advertisementBanner != null)
            advertisementBanner.setVisibility(true);
        isGameFieldReady = true;
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
        if (!actionsList.isEmpty() && currentAction == null) {
            currentAction = actionsList.remove(0);
            currentAction.perform(thimbles.thimbles, this, this);
        }
        super.update(dt);
    }

    @Override
    public void addActor(Actor actor) {
        super.addActor(actor);
    }

    @Override
    public void draw() {
        super.draw();
    }

    @Override
    public void dispose() {
        backgroundMusic.dispose();
        super.dispose();
    }

    @Override
    public GameFieldScene build() {
        return this;
    }

    @Override
    public void onThimbleActionComplete() {
        currentAction = null;
    }

    public class Thimbles implements InnerBuilder<GameFieldScene> {

        private Thimble [][] thimbles;

        public Thimbles(int rows, int columns) {
            thimbles = new Thimble[rows][columns];
        }

        public Thimbles add(Thimble thimble, int row, int col) {
            if (row < thimbles.length && col < thimbles[0].length) {
                thimbles[row][col] = thimble;
                adjustThimble(row, col);
                addActor(thimble);
            }
            return this;
        }

        public Vector2 thimblePosition(int thimbleRow, int thimbleColumn) {
            Vector2 thimbleSize = thimbleSize(thimbleRow);
            return new Vector2(
                    thimbleCellSize.x * thimbleColumn + (thimbleCellSize.x - thimbleSize.x) / 2.f,
                    thimbleCellSize.y * thimbleRow + (thimbleCellSize.y - thimbleSize.y) / 2.f +
                            advertisementBannerHeight);
        }

        public Vector2 thimbleSize(int thimbleRow) {
            float size = thimbleSize *
                    (ROWS_HEIGHT_TO_PERCENTS - ROW_HEIGHT_DECREASE_TO_PERCENTS * thimbleRow) / ROWS_HEIGHT_TO_PERCENTS;
            return new Vector2(size, size);
        }

        private void adjustThimbles() {
            for (int i = 0; i < thimbles.length; ++i)
                for (int j = 0; j < thimbles[0].length; ++j)
                    adjustThimble(i, j);
        }

        private void adjustThimble(int thimbleRow, int thimbleColumn) {
            Thimble thimble = thimbles[thimbleRow][thimbleColumn];
            if (thimble != null) {
                thimble
                        .position(thimblePosition(thimbleRow, thimbleColumn))
                        .size(thimbleSize(thimbleRow));
            }
        }

        @Override
        public GameFieldScene build() {
            return GameFieldScene.this;
        }
    }

    public class BackgroundMusic implements InnerBuilder<GameFieldScene>, Music.OnCompletionListener {

        private ArrayList<Music> backgroundMusic = new ArrayList<Music>();
        private Music currentBackgroundMusic = null;

        public BackgroundMusic add(Music music) {
            backgroundMusic.add(music);
            return this;
        }

        private void play() {
            if (!backgroundMusic.isEmpty()) {
                if (currentBackgroundMusic == null)
                    currentBackgroundMusic = backgroundMusic.get(0);
                else {
                    int indexOfCurrentMusic = backgroundMusic.indexOf(currentBackgroundMusic);
                    if (indexOfCurrentMusic < 0 || indexOfCurrentMusic + 1 >= backgroundMusic.size())
                        indexOfCurrentMusic = -1;
                    currentBackgroundMusic = backgroundMusic.get(indexOfCurrentMusic + 1);
                }
                currentBackgroundMusic.setOnCompletionListener(this);
                currentBackgroundMusic.play();
            }
        }

        public void dispose() {
            for (Music music : backgroundMusic) {
                if (music.isPlaying())
                    music.stop();
                music.dispose();
            }
            backgroundMusic.clear();
        }

        @Override
        public GameFieldScene build() {
            return GameFieldScene.this;
        }

        @Override
        public void onCompletion(Music music) {
            music.dispose();
            play();
        }
    }
}