package com.monkeybuisness.thimbles.actions.thimbles;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.monkeybuisness.thimbles.actors.Thimble;
import com.monkeybuisness.thimbles.actions.ActorAction;
import com.monkeybuisness.thimbles.actions.IActorActionListener;
import com.monkeybuisness.thimbles.scenes.GameFieldScene;
import com.monkeybuisness.thimbles.utils.RandomUtil;

import java.util.ArrayList;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class ThimbleHorizontalSwapAction implements IThimbleAction {

    private static final float SCALE_INITIAL_VALUE = 1.f;
    private static final float SCALE_UP_VALUE = 1.12f;
    private static final float SCALE_DOWN_VALUE = 0.86f;
    private static final float DEFAULT_SWAP_DURATION = 1.f;
    private int rowIndex = ThimbleActionHelper.RANDOM_VALUE;
    private int firstThimbleColIndex = ThimbleActionHelper.RANDOM_VALUE;
    private int secondThimbleColIndex = ThimbleActionHelper.RANDOM_VALUE;
    private float swapDuration = DEFAULT_SWAP_DURATION;

    public void forThimblesInRow(int rowIndex, int firstThimbleColIndex, int secondThimbleColIndex) {
        this.rowIndex = rowIndex;
        this.firstThimbleColIndex = firstThimbleColIndex;
        this.secondThimbleColIndex = secondThimbleColIndex;
    }

    public void setSwapDuration(float duration) {
        swapDuration = duration;
    }

    @Override
    public void perform(Thimble[][] thimbles, GameFieldScene scene, final IActorActionListener listener) {
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
                    if (thimbles[rowIndex][i] != null)
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

        Thimble firstThimble = thimbles[rowIndex][firstThimbleColIndex];
        Thimble secondThimble = thimbles[rowIndex][secondThimbleColIndex];
        if (firstThimble == null && secondThimble == null) {
            if (listener != null)
                listener.onThimbleActionComplete();
            return;
        }
        float scaleDuration = swapDuration / 2.f;
        float moveDuration = scaleDuration;
        Vector2 firstThimblePosition = scene.thimbles().thimblePosition(rowIndex, firstThimbleColIndex);
        Vector2 secondThimblePosition = scene.thimbles().thimblePosition(rowIndex, secondThimbleColIndex);

        if (firstThimble != null) {
            ActorAction firstThimbleAction = new ActorAction();
            MoveToAction moveFrontAction = new MoveToAction();
            moveFrontAction.setPosition(
                    firstThimblePosition.x + (secondThimblePosition.x - firstThimblePosition.x) / 2.f,
                    firstThimblePosition.y - firstThimble.getHeight() * (SCALE_UP_VALUE - SCALE_INITIAL_VALUE));
            moveFrontAction.setDuration(moveDuration);
            MoveToAction moveBackAction = new MoveToAction();
            moveBackAction.setPosition(
                    secondThimblePosition.x,
                    secondThimblePosition.y);
            moveBackAction.setDuration(moveDuration);
            ScaleToAction scaleUpAction = new ScaleToAction();
            scaleUpAction.setScale(SCALE_UP_VALUE);
            scaleUpAction.setDuration(scaleDuration);
            ScaleToAction scaleDownAction = new ScaleToAction();
            scaleDownAction.setScale(SCALE_INITIAL_VALUE);
            scaleDownAction.setDuration(scaleDuration);
            firstThimbleAction
                    .parallelActions()
                        .add(sequence(moveFrontAction, moveBackAction))
                        .add(sequence(scaleUpAction, scaleDownAction))
                        .completeCallback(secondThimble == null ? listener : null)
                    .build();
            firstThimble.setZIndex(fieldRowsCount + fieldColumnsCount);
            firstThimble.addAction(firstThimbleAction.instance());
        }

        if (secondThimble != null) {
            ActorAction secondThimbleAction = new ActorAction();
            MoveToAction moveBackAction = new MoveToAction();
            moveBackAction.setPosition(
                    secondThimblePosition.x + (firstThimblePosition.x - secondThimblePosition.x) / 2.f,
                    secondThimblePosition.y + secondThimble.getHeight() * (SCALE_INITIAL_VALUE - SCALE_DOWN_VALUE));
            moveBackAction.setDuration(moveDuration);
            MoveToAction moveFrontAction = new MoveToAction();
            moveFrontAction.setPosition(
                    firstThimblePosition.x,
                    firstThimblePosition.y);
            moveFrontAction.setDuration(moveDuration);
            ScaleToAction scaleDownAction = new ScaleToAction();
            scaleDownAction.setScale(SCALE_DOWN_VALUE);
            scaleDownAction.setDuration(scaleDuration);
            ScaleToAction scaleUpAction = new ScaleToAction();
            scaleUpAction.setScale(SCALE_INITIAL_VALUE);
            scaleUpAction.setDuration(scaleDuration);
            secondThimbleAction
                    .parallelActions()
                        .add(sequence(moveBackAction, moveFrontAction))
                        .add(sequence(scaleDownAction, scaleUpAction))
                        .completeCallback(listener)
                    .build();
            secondThimble.setZIndex(0);
            secondThimble.addAction(secondThimbleAction.instance());
        }

        ThimbleActionHelper.swapThimbles(thimbles, rowIndex, firstThimbleColIndex, rowIndex, secondThimbleColIndex);
    }
}