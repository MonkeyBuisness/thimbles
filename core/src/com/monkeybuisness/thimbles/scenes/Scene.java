package com.monkeybuisness.thimbles.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class Scene {

    private Stage sceneStage = null;

    public Scene() {
        sceneStage = new Stage();
        Gdx.input.setInputProcessor(sceneStage);
    }

    protected void setViewport(Viewport viewport) {
        sceneStage.setViewport(viewport);
    }

    protected Viewport getViewport() {
        return sceneStage.getViewport();
    }

    protected void dispose() {
        sceneStage.dispose();
    }

    protected void draw() {
        sceneStage.getBatch().setProjectionMatrix(sceneStage.getCamera().combined);
        sceneStage.draw();
    }

    protected void update(float dt) {
        sceneStage.getCamera().update();
        sceneStage.act(dt);
    }

    protected void addActor(Actor actor) {
        sceneStage.addActor(actor);
    }

    protected void resize(int width, int height) {
        sceneStage.getViewport().update(width, height);
    }
}