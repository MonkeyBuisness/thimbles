package com.monkeybuisness.thimbles.actions.thimbles;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.monkeybuisness.thimbles.actions.ActorAction;
import com.monkeybuisness.thimbles.actions.IActorActionListener;
import com.monkeybuisness.thimbles.actors.Ball;
import com.monkeybuisness.thimbles.actors.Shadow;
import com.monkeybuisness.thimbles.actors.Thimble;
import com.monkeybuisness.thimbles.scenes.GameFieldScene;
import com.monkeybuisness.thimbles.utils.RandomUtil;

import java.util.ArrayList;

public class ThimblePickUpAction implements IThimbleAction {

    private static final float PICK_UP_DURATION = 1.0f;
    private static final float SHADOW_RELATIVE_ENDING_WIDTH = .5f;
    private int thimbleRowIndex = 0;
    private int thimbleColIndex = 0;

    public void forThimble(int thimbleRowIndex, int thimbleColIndex) {
        this.thimbleRowIndex = thimbleRowIndex;
        this.thimbleColIndex = thimbleColIndex;
    }

    @Override
    public void perform(final Thimble[][] thimbles, GameFieldScene scene, final IActorActionListener listener) {
        int fieldRowsCount = thimbles.length;
        int fieldColumnsCount = thimbles[0].length;

        final Thimble thimble = thimbles[thimbleRowIndex][thimbleColIndex];
        if (thimble == null) {
            if (listener != null)
                listener.onThimbleActionComplete();
            return;
        }
        final Ball ball =  thimble.getBall();
        if (ball != null) {
            scene.addActor(ball);
            ball.setZIndex(1);
        }
        final Shadow shadow = thimble.getShadow();
        if (shadow != null) {
            scene.addActor(shadow);
            shadow.setZIndex(0);
            ActorAction shadowAction = new ActorAction();
            ScaleToAction shadowScaleAction = new ScaleToAction();
            shadowScaleAction.setScale(SHADOW_RELATIVE_ENDING_WIDTH, shadow.getScaleY());
            shadowScaleAction.setDuration(PICK_UP_DURATION);
            shadowAction
                    .sequenceActions()
                        .add(shadowScaleAction)
                    .build();
            shadow.addAction(shadowAction.instance());
        }

        ActorAction thimbleAction = new ActorAction();
        MoveToAction moveUpAction = new MoveToAction();
        moveUpAction.setPosition(thimble.getX(), thimble.getY() + thimble.getHeight());
        moveUpAction.setDuration(PICK_UP_DURATION);
        thimbleAction
                .sequenceActions()
                    .add(moveUpAction)
                    .completeCallback(new IActorActionListener() {
                        @Override
                        public void onThimbleActionComplete() {
                            if (ball != null) {
                                ball.remove();
                                ball.dispose();
                            }
                            if (shadow != null) {
                                shadow.remove();
                                shadow.dispose();
                            }
                            thimble.remove();
                            thimble.dispose();
                            thimbles[thimbleRowIndex][thimbleColIndex] = null;
                            if (listener != null)
                                listener.onThimbleActionComplete();
                        }
                    })
                .build();
        thimble.addAction(thimbleAction.instance());
        thimble.setZIndex(fieldRowsCount + fieldColumnsCount);
    }
}