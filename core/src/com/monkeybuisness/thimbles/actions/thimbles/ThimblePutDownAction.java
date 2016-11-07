package com.monkeybuisness.thimbles.actions.thimbles;

import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SizeToAction;
import com.monkeybuisness.thimbles.actions.ActorAction;
import com.monkeybuisness.thimbles.actions.IActorActionListener;
import com.monkeybuisness.thimbles.actors.Ball;
import com.monkeybuisness.thimbles.actors.Shadow;
import com.monkeybuisness.thimbles.actors.Thimble;
import com.monkeybuisness.thimbles.scenes.GameFieldScene;
import com.monkeybuisness.thimbles.utils.RandomUtil;

import java.util.ArrayList;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class ThimblePutDownAction implements IThimbleAction {

    private static final float PUT_DOWN_RELATIVE_INITIAL_HEIGHT = .6f;
    private static final float PUT_DOWN_DURATION = 1.0f;
    private static final float SHADOW_RELATIVE_INITIAL_WIDTH = .5f;
    private int thimbleRowIndex = ThimbleActionHelper.RANDOM_VALUE;
    private int thimbleColIndex = ThimbleActionHelper.RANDOM_VALUE;

    public void forThimble(int thimbleRowIndex, int thimbleColIndex) {
        this.thimbleRowIndex = thimbleRowIndex;
        this.thimbleColIndex = thimbleColIndex;
    }

    @Override
    public void perform(Thimble[][] thimbles, GameFieldScene scene, final IActorActionListener listener) {
        int fieldRowsCount = thimbles.length;
        int fieldColumnsCount = thimbles[0].length;

        if (ThimbleActionHelper.isRandomValue(thimbleRowIndex) && ThimbleActionHelper.isRandomValue(thimbleColIndex)) {
            ArrayList<Integer> thimblesWithBallsIndexes = new ArrayList<Integer>();
            for (int i = 0; i < fieldRowsCount; ++i)
                for (int j = 0; j < fieldColumnsCount; ++j)
                    if (thimbles[i][j] != null && thimbles[i][j].hasBall())
                        thimblesWithBallsIndexes.add(i * fieldColumnsCount + j);
            if (thimblesWithBallsIndexes.isEmpty()) {
                if (listener != null)
                    listener.onThimbleActionComplete();
                return;
            }
            int thimbleIndex = thimblesWithBallsIndexes.get(RandomUtil.nextInt(0, thimblesWithBallsIndexes.size() - 1));
            thimbleRowIndex = thimbleIndex / fieldColumnsCount;
            thimbleColIndex = thimbleIndex % fieldColumnsCount;
        }
        else if (ThimbleActionHelper.isRandomValue(thimbleRowIndex)) {
            ArrayList<Integer> thimblesWithBallsIndexes = new ArrayList<Integer>();
            for (int i = 0; i < fieldRowsCount; ++i)
                if (thimbles[i][thimbleColIndex] != null && thimbles[i][thimbleColIndex].hasBall())
                    thimblesWithBallsIndexes.add(i);
            if (thimblesWithBallsIndexes.isEmpty()) {
                if (listener != null)
                    listener.onThimbleActionComplete();
                return;
            }
            thimbleRowIndex = thimblesWithBallsIndexes.get(RandomUtil.nextInt(0, thimblesWithBallsIndexes.size() - 1));
        }
        else {
            ArrayList<Integer> thimblesWithBallsIndexes = new ArrayList<Integer>();
            for (int i = 0; i < fieldColumnsCount; ++i)
                if (thimbles[thimbleRowIndex][i] != null && thimbles[thimbleRowIndex][i].hasBall())
                    thimblesWithBallsIndexes.add(i);
            if (thimblesWithBallsIndexes.isEmpty()) {
                if (listener != null)
                    listener.onThimbleActionComplete();
                return;
            }
            thimbleColIndex = thimblesWithBallsIndexes.get(RandomUtil.nextInt(0, thimblesWithBallsIndexes.size() - 1));
        }

        Thimble thimble = thimbles[thimbleRowIndex][thimbleColIndex];
        if (thimble == null || !thimble.hasBall()) {
            if (listener != null)
                listener.onThimbleActionComplete();
            return;
        }
        float thimbleInitialYPos = thimble.getY();
        final Ball ball =  thimble.getBall();
        scene.addActor(ball);
        ball.setZIndex(1);
        final Shadow shadow = thimble.getShadow();
        if (shadow != null) {
            shadow
                    .size(new Vector2(SHADOW_RELATIVE_INITIAL_WIDTH * thimble.getWidth(), shadow.getHeight()))
                    .position(new Vector2(
                            thimble.getX() + (thimble.getWidth() - shadow.getWidth()) / 2.f,
                            shadow.getY()));
            shadow.setOriginX(shadow.getWidth() / 2.f);
            scene.addActor(shadow);
            shadow.setZIndex(0);
            ActorAction shadowAction = new ActorAction();
            ScaleToAction shadowScaleAction = new ScaleToAction();
            shadowScaleAction.setScale(thimble.getWidth() / shadow.getWidth(), shadow.getScaleY());
            shadowScaleAction.setDuration(PUT_DOWN_DURATION);
            shadowAction
                    .sequenceActions()
                        .add(shadowScaleAction)
                    .build();
            shadow.addAction(shadowAction.instance());
        }

        thimble.setY(thimble.getY() + PUT_DOWN_RELATIVE_INITIAL_HEIGHT * thimble.getHeight());
        ActorAction thimbleAction = new ActorAction();
        MoveToAction moveDownAction = new MoveToAction();
        moveDownAction.setPosition(thimble.getX(), thimbleInitialYPos);
        moveDownAction.setDuration(PUT_DOWN_DURATION);
        thimbleAction
                .sequenceActions()
                        .add(moveDownAction)
                        .completeCallback(new IActorActionListener() {
                            @Override
                            public void onThimbleActionComplete() {
                                ball.remove();
                                if (shadow != null)
                                    shadow.remove();
                                if (listener != null)
                                    listener.onThimbleActionComplete();
                            }
                        })
                .build();
        thimble.addAction(thimbleAction.instance());
        thimble.setZIndex(fieldRowsCount + fieldColumnsCount);
    }
}