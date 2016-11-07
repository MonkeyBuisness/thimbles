package com.monkeybuisness.thimbles.actions.thimbles;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateToAction;
import com.monkeybuisness.thimbles.actions.ActorAction;
import com.monkeybuisness.thimbles.actors.Ball;
import com.monkeybuisness.thimbles.actors.Thimble;
import com.monkeybuisness.thimbles.actions.IActorActionListener;
import com.monkeybuisness.thimbles.scenes.GameFieldScene;
import com.monkeybuisness.thimbles.utils.RandomUtil;

import java.util.ArrayList;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class ThimbleRollBallAction implements IThimbleAction {

    private static final float ROTATE_THIMBLE_ANGLE = 30.f;
    private static final float ROTATE_THIMBLE_DURATION = 0.6f;
    private static final float DEFAULT_ROLL_DURATION = 0.5f;
    private static final float BALL_FULL_ROTATION_BY_ROLL = 360.f;
    private static final int MIN_ROLLS_COUNT = 1;
    private static final int MAX_ROLLS_COUNT = 10;
    private int rowIndex = ThimbleActionHelper.RANDOM_VALUE;
    private int firstThimbleColIndex = ThimbleActionHelper.RANDOM_VALUE;
    private int secondThimbleColIndex = ThimbleActionHelper.RANDOM_VALUE;
    private float rollDuration = DEFAULT_ROLL_DURATION;
    private int rollsCount = ThimbleActionHelper.RANDOM_VALUE;

    public void forThimblesInRow(int rowIndex, int firstThimbleColIndex, int secondThimbleColIndex) {
        this.rowIndex = rowIndex;
        this.firstThimbleColIndex = firstThimbleColIndex;
        this.secondThimbleColIndex = secondThimbleColIndex;
    }

    public void setRollDuration(float duration) {
        rollDuration = duration;
    }

    public void setRollsCount(int rollsCount) {
        this.rollsCount = rollsCount;
    }

    @Override
    public void perform(final Thimble[][] thimbles, final GameFieldScene scene, final IActorActionListener listener) {
        int fieldRowsCount = thimbles.length;
        int fieldColumnsCount = thimbles[0].length;

        if (fieldColumnsCount <= 1) {
            if (listener != null)
                listener.onThimbleActionComplete();
            return;
        }

        if (ThimbleActionHelper.isRandomValue(rowIndex))
            rowIndex = RandomUtil.nextInt(0, fieldRowsCount - 1);

        if (ThimbleActionHelper.isRandomValue(firstThimbleColIndex)) {
            if (ThimbleActionHelper.isRandomValue(secondThimbleColIndex)) {
                ArrayList<Integer> inRowThimblesIndexes = new ArrayList<Integer>();
                for (int i = 0; i < fieldColumnsCount; ++i)
                    if (thimbles[rowIndex][i] != null && thimbles[rowIndex][i].hasBall())
                        inRowThimblesIndexes.add(i);
                if (inRowThimblesIndexes.isEmpty()) {
                    if (listener != null)
                        listener.onThimbleActionComplete();
                    return;
                }
                firstThimbleColIndex = inRowThimblesIndexes.get(RandomUtil.nextInt(0, inRowThimblesIndexes.size() - 1));
            }
            else {
                if (secondThimbleColIndex == 0)
                    firstThimbleColIndex = secondThimbleColIndex + 1;
                else if (secondThimbleColIndex == fieldColumnsCount - 1)
                    firstThimbleColIndex = secondThimbleColIndex - 1;
                else
                    firstThimbleColIndex = secondThimbleColIndex + ThimbleActionHelper.offset(1);
            }
        }

        if (ThimbleActionHelper.isRandomValue(secondThimbleColIndex)) {
            if (firstThimbleColIndex == 0)
                secondThimbleColIndex = firstThimbleColIndex + 1;
            else if (firstThimbleColIndex == fieldColumnsCount - 1)
                secondThimbleColIndex = firstThimbleColIndex - 1;
            else
                secondThimbleColIndex = firstThimbleColIndex + ThimbleActionHelper.offset(1);
        }

        final Thimble firstThimble = thimbles[rowIndex][firstThimbleColIndex];
        final Thimble secondThimble = thimbles[rowIndex][secondThimbleColIndex];
        if (firstThimble == null || secondThimble == null) {
            if (listener != null)
                listener.onThimbleActionComplete();
            return;
        }
        if ((firstThimble.hasBall() && secondThimble.hasBall()) ||
                !(firstThimble.hasBall() || secondThimble.hasBall())) {
            if (listener != null)
                listener.onThimbleActionComplete();
            return;
        }
        Vector2 firstThimblePosition = scene.thimbles().thimblePosition(rowIndex, firstThimbleColIndex);
        Vector2 secondThimblePosition = scene.thimbles().thimblePosition(rowIndex, secondThimbleColIndex);
        float firstThimbleInitialRotation = firstThimble.getRotation();
        float secondThimbleInitialRotation = secondThimble.getRotation();
        float rightThimbleInitialOriginX;
        float firstThimbleRotateAngle = ROTATE_THIMBLE_ANGLE;
        float secondThimbleRotateAngle = ROTATE_THIMBLE_ANGLE;
        final Ball ball = firstThimble.hasBall() ? firstThimble.getBall() : secondThimble.getBall();
        scene.addActor(ball);
        ball.setZIndex(0);

        if (firstThimbleColIndex > secondThimbleColIndex) {
            rightThimbleInitialOriginX = firstThimble.getOriginX();
            firstThimble.setOriginX(rightThimbleInitialOriginX + firstThimble.getWidth());
            firstThimbleRotateAngle = -ROTATE_THIMBLE_ANGLE;
        }
        else {
            rightThimbleInitialOriginX = secondThimble.getOriginX();
            secondThimble.setOriginX(rightThimbleInitialOriginX + secondThimble.getWidth());
            secondThimbleRotateAngle = -ROTATE_THIMBLE_ANGLE;
        }
        final float finalRightThimbleInitialOriginX = rightThimbleInitialOriginX;

        if (ThimbleActionHelper.isRandomValue(rollsCount))
            rollsCount = RandomUtil.nextInt(MIN_ROLLS_COUNT, MAX_ROLLS_COUNT);

        // TODO: *2 WHY????????????????????????
        DelayAction rollDelayAction = new DelayAction(2.f * rollDuration * rollsCount);

        ActorAction firstThimbleAction = new ActorAction();
        RotateToAction rotateOpeningFirstThimbleAction = new RotateToAction();
        rotateOpeningFirstThimbleAction.setRotation(firstThimbleRotateAngle);
        rotateOpeningFirstThimbleAction.setDuration(ROTATE_THIMBLE_DURATION);
        RotateToAction rotateClosingFirstThimbleAction = new RotateToAction();
        rotateClosingFirstThimbleAction.setRotation(firstThimbleInitialRotation);
        rotateClosingFirstThimbleAction.setDuration(ROTATE_THIMBLE_DURATION);
        firstThimbleAction
                .sequenceActions()
                    .add(rotateOpeningFirstThimbleAction)
                    .add(rollDelayAction)
                    .add(rotateClosingFirstThimbleAction)
                .build();
        firstThimble.setZIndex(fieldRowsCount + fieldColumnsCount);
        firstThimble.addAction(firstThimbleAction.instance());

        DelayAction rotateDelayAction = new DelayAction(ROTATE_THIMBLE_DURATION);
        ActorAction ballAction = new ActorAction();
        ballAction
                .sequenceActions()
                    .add(rotateDelayAction)
                .build();
        boolean isLeftToRightRoll = (firstThimbleColIndex < secondThimbleColIndex && firstThimble.hasBall()) ||
                (secondThimbleColIndex < firstThimbleColIndex && secondThimble.hasBall());
        for (int i = 0; i < rollsCount; ++i) {
            MoveToAction rollAction = new MoveToAction();
            if (isLeftToRightRoll) {
                if (firstThimbleColIndex < secondThimbleColIndex)
                    rollAction.setPosition(
                            secondThimblePosition.x + (secondThimble.getWidth() - ball.getWidth()) / 2.f,
                            secondThimblePosition.y);
                else
                    rollAction.setPosition(
                            firstThimblePosition.x + (firstThimble.getWidth() - ball.getWidth()) / 2.f,
                            firstThimblePosition.y);
            }
            else {
                if (firstThimbleColIndex > secondThimbleColIndex)
                    rollAction.setPosition(
                            secondThimblePosition.x + (secondThimble.getWidth() - ball.getWidth()) / 2.f,
                            secondThimblePosition.y);
                else
                    rollAction.setPosition(
                            firstThimblePosition.x + (firstThimble.getWidth() - ball.getWidth()) / 2.f,
                            firstThimblePosition.y);
            }
            rollAction.setDuration(rollDuration);
            RotateToAction rotateBallAction = new RotateToAction();
            rotateBallAction.setRotation(isLeftToRightRoll ? -BALL_FULL_ROTATION_BY_ROLL : BALL_FULL_ROTATION_BY_ROLL);
            rotateBallAction.setDuration(rollDuration);
            ballAction
                    .sequenceActions()
                        .add(parallel(rollAction, rotateBallAction));
            isLeftToRightRoll = !isLeftToRightRoll;
        }
        ball.addAction(
                ballAction
                    .sequenceActions().build().instance());

        ActorAction secondThimbleAction = new ActorAction();
        RotateToAction rotateSecondThimbleAction = new RotateToAction();
        rotateSecondThimbleAction.setRotation(secondThimbleRotateAngle);
        rotateSecondThimbleAction.setDuration(ROTATE_THIMBLE_DURATION);
        RotateToAction rotateClosingSecondThimbleAction = new RotateToAction();
        rotateClosingSecondThimbleAction.setRotation(secondThimbleInitialRotation);
        rotateClosingSecondThimbleAction.setDuration(ROTATE_THIMBLE_DURATION);
        secondThimbleAction
                .sequenceActions()
                    .add(rotateSecondThimbleAction)
                    .add(rollDelayAction)
                    .add(rotateClosingSecondThimbleAction)
                    .completeCallback(new IActorActionListener() {
                        @Override
                        public void onThimbleActionComplete() {
                            ball.remove();
                            if (firstThimbleColIndex > secondThimbleColIndex)
                                firstThimble.setOriginX(finalRightThimbleInitialOriginX);
                            else
                                secondThimble.setOriginX(finalRightThimbleInitialOriginX);
                            if (rollsCount % 2 != 0)
                                ThimbleActionHelper.swapBalls(firstThimble, secondThimble);
                            if (listener != null)
                                listener.onThimbleActionComplete();
                        }
                    })
                .build();
        secondThimble.setZIndex(firstThimble.getZIndex() - 1);
        secondThimble.addAction(secondThimbleAction.instance());
    }
}