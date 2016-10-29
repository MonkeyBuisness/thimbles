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
    }

    protected void setViewport(Viewport viewport) {
        sceneStage.setViewport(viewport);
        sceneStage.getViewport().update(viewport.getScreenWidth(), viewport.getScreenHeight());
    }

    protected void dispose() {
        sceneStage.dispose();
    }

    protected void draw() {
        sceneStage.draw();
    }

    protected void addActor(Actor actor) {
        sceneStage.addActor(actor);
    }

    protected void resize(int width, int height) {
        sceneStage.getViewport().update(width, height);
    }

    public void sss(Matrix4 m) {
        sceneStage.getBatch().setProjectionMatrix(m);
    }
}