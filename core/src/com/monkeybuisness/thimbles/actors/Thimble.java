package com.monkeybuisness.thimbles.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.monkeybuisness.thimbles.OuterBuilder;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class Thimble extends Actor implements OuterBuilder<Thimble> {

    private static final float BALL_RELATIVE_SIZE = .3f;
    private static final float SHADOW_HEIGHT_RELATIVE_SIZE = .2f;
    private TextureRegion thimbleTextureRegion = null;
    private Ball ball = null;
    private Shadow thimbleShadow = null;

    private void adjustBallSize() {
        if (ball != null) {
            float ballSize = getWidth() * BALL_RELATIVE_SIZE;
            ball
                    .size(new Vector2(ballSize, ballSize));
            ball.setOrigin(ball.getWidth() / 2.f, ball.getHeight() / 2.f);
            adjustBallPosition();
        }
    }

    private void adjustBallPosition() {
        if (ball != null)
            ball
                    .position(new Vector2(getX() + (getWidth() - ball.getWidth()) / 2.f, getY()));
    }

    private void adjustShadowSize() {
        if (thimbleShadow != null) {
            thimbleShadow
                    .size(new Vector2(getWidth(), SHADOW_HEIGHT_RELATIVE_SIZE * getHeight()));
            thimbleShadow.setOrigin(thimbleShadow.getWidth() / 2.f, thimbleShadow.getOriginY());
            adjustShadowPosition();
        }
    }

    private void adjustShadowPosition() {
        if (thimbleShadow != null)
            thimbleShadow
                    .position(new Vector2(
                            getX() + (getWidth() - thimbleShadow.getWidth()) / 2.f,
                            getY() - thimbleShadow.getHeight() / 2.f));
    }

    public Thimble texture(Texture texture) {
        if (thimbleTextureRegion == null)
            thimbleTextureRegion = new TextureRegion(texture);
        else
            thimbleTextureRegion.setTexture(texture);
        return this;
    }

    public Thimble shadow(Shadow shadow) {
        thimbleShadow = shadow;
        adjustShadowPosition();
        return this;
    }

    public Shadow getShadow() {
        adjustShadowSize();
        return thimbleShadow;
    }

    public Thimble position(Vector2 position) {
        setPosition(position.x, position.y);
        adjustBallPosition();
        adjustShadowPosition();
        return this;
    }

    public Thimble size(Vector2 size) {
        setSize(size.x, size.y);
        adjustBallSize();
        adjustShadowSize();
        return this;
    }

    public boolean hasBall() {
        return ball != null;
    }

    public Thimble ball(Ball ball) {
        this.ball = ball;
        adjustBallSize();
        return this;
    }

    public Ball getBall() {
        adjustBallSize();
        return ball;
    }

    public void dispose() {
        if (thimbleTextureRegion != null)
            thimbleTextureRegion.getTexture().dispose();
        if (ball != null)
            ball.dispose();
        if (thimbleShadow != null)
            thimbleShadow.dispose();

    }

    @Override
    public void draw(Batch batch, float alpha) {
        if (thimbleTextureRegion != null)
            batch.draw(thimbleTextureRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(),
                    getScaleX(), getScaleY(), getRotation());
        super.draw(batch, alpha);
    }

    @Override
    public Thimble build() {
        return this;
    }
}