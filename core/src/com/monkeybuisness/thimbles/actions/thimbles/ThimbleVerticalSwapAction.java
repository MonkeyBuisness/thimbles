package com.monkeybuisness.thimbles.actions.thimbles;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SizeToAction;
import com.monkeybuisness.thimbles.Thimble;
import com.monkeybuisness.thimbles.actions.ActorAction;
import com.monkeybuisness.thimbles.actions.IActorActionListener;
import com.monkeybuisness.thimbles.scenes.GameFieldScene;
import com.monkeybuisness.thimbles.utils.RandomUtil;

import java.util.ArrayList;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class ThimbleVerticalSwapAction implements IThimbleAction {

    private static final float DEFAULT_SWAP_DURATION = 1.f;
    private int firstThimbleRowIndex = ThimbleActionHelper.RANDOM_VALUE;
    private int secondThimbleRowIndex = ThimbleActionHelper.RANDOM_VALUE;
    private int colIndex = ThimbleActionHelper.RANDOM_VALUE;
    private float swapDuration = DEFAULT_SWAP_DURATION;

    public void forThimblesInColumn(int colIndex, int firstThimbleRowIndex, int secondThimbleRowIndex) {
        this.colIndex = colIndex;
        this.firstThimbleRowIndex = firstThimbleRowIndex;
        this.secondThimbleRowIndex = secondThimbleRowIndex;
    }

    public void setSwapDuration(float duration) {
        swapDuration = duration;
    }

    @Override
    public void perform(Thimble[][] thimbles, GameFieldScene scene, final IActorActionListener listener) {
        int fieldRowsCount = thimbles.length;
        int fieldColumnsCount = thimbles[0].length;

        if (fieldRowsCount <= 1) {
            if (listener != null)
                listener.onThimbleActionComplete();
            return;
        }

        if (ThimbleActionHelper.isRandomValue(colIndex))
            colIndex = RandomUtil.nextInt(0, fieldColumnsCount - 1);

        if (ThimbleActionHelper.isRandomValue(firstThimbleRowIndex)) {
            if (ThimbleActionHelper.isRandomValue(secondThimbleRowIndex)) {
                ArrayList<Integer> inColThimblesIndexes = new ArrayList<Integer>();
                for (int i = 0; i < fieldRowsCount; ++i)
                    if (thimbles[i][colIndex] != null)
                        inColThimblesIndexes.add(i);
                if (inColThimblesIndexes.isEmpty()) {
                    if (listener != null)
                        listener.onThimbleActionComplete();
                    return;
                }
                firstThimbleRowIndex = inColThimblesIndexes.get(RandomUtil.nextInt(0, inColThimblesIndexes.size() - 1));
            }
            else {
                if (secondThimbleRowIndex == 0)
                    firstThimbleRowIndex = secondThimbleRowIndex + 1;
                else if (secondThimbleRowIndex == fieldRowsCount - 1)
                    firstThimbleRowIndex = secondThimbleRowIndex - 1;
                else
                    firstThimbleRowIndex = secondThimbleRowIndex + ThimbleActionHelper.offset(1);
            }
        }

        if (ThimbleActionHelper.isRandomValue(secondThimbleRowIndex)) {
            if (firstThimbleRowIndex == 0)
                secondThimbleRowIndex = firstThimbleRowIndex + 1;
            else if (firstThimbleRowIndex == fieldRowsCount - 1)
                secondThimbleRowIndex = firstThimbleRowIndex - 1;
            else
                secondThimbleRowIndex = firstThimbleRowIndex + ThimbleActionHelper.offset(1);
        }

        Thimble firstThimble = thimbles[firstThimbleRowIndex][colIndex];
        Thimble secondThimble = thimbles[secondThimbleRowIndex][colIndex];
        if (firstThimble == null && secondThimble == null) {
            if (listener != null)
                listener.onThimbleActionComplete();
            return;
        }
        float moveDuration = swapDuration / 2.f;
        Vector2 firstThimblePosition = scene.thimbles().thimblePosition(firstThimbleRowIndex, colIndex);
        Vector2 secondThimblePosition = scene.thimbles().thimblePosition(secondThimbleRowIndex, colIndex);
        Vector2 firstThimbleSize = scene.thimbles().thimbleSize(firstThimbleRowIndex);
        Vector2 secondThimbleSize = scene.thimbles().thimbleSize(secondThimbleRowIndex);

        if (firstThimble != null) {
            ActorAction firstThimbleAction = new ActorAction();
            MoveToAction moveRightAction = new MoveToAction();
            moveRightAction.setPosition(
                    firstThimblePosition.x + firstThimbleSize.x,
                    firstThimblePosition.y + (secondThimblePosition.y - firstThimblePosition.y) / 2.f);
            moveRightAction.setDuration(moveDuration);
            MoveToAction moveLeftAction = new MoveToAction();
            moveLeftAction.setPosition(
                    secondThimblePosition.x,
                    secondThimblePosition.y);
            moveLeftAction.setDuration(moveDuration);
            SizeToAction sizeToAction = new SizeToAction();
            sizeToAction.setSize(secondThimbleSize.x, secondThimbleSize.y);
            sizeToAction.setDuration(swapDuration);
            firstThimbleAction
                    .parallelActions()
                        .add(sizeToAction)
                        .add(sequence(moveRightAction, moveLeftAction))
                        .completeCallback(secondThimble == null ? listener : null)
                    .build();
            firstThimble.setZIndex(fieldRowsCount + fieldColumnsCount);
            firstThimble.addAction(firstThimbleAction.instance());
        }

        if (secondThimble != null) {
            ActorAction secondThimbleAction = new ActorAction();
            MoveToAction moveLeftAction = new MoveToAction();
            moveLeftAction.setPosition(
                    secondThimblePosition.x - secondThimbleSize.x,
                    secondThimblePosition.y + (firstThimblePosition.y - secondThimblePosition.y) / 2.f);
            moveLeftAction.setDuration(moveDuration);
            MoveToAction moveRightAction = new MoveToAction();
            moveRightAction.setPosition(
                    firstThimblePosition.x,
                    firstThimblePosition.y);
            moveRightAction.setDuration(moveDuration);
            SizeToAction sizeToAction = new SizeToAction();
            sizeToAction.setSize(firstThimbleSize.x, firstThimbleSize.y);
            sizeToAction.setDuration(swapDuration);
            secondThimbleAction
                    .parallelActions()
                        .add(sizeToAction)
                        .add(sequence(moveLeftAction, moveRightAction))
                        .completeCallback(listener)
                    .build();
            secondThimble.setZIndex(0);
            secondThimble.addAction(secondThimbleAction.instance());
        }

        ThimbleActionHelper.swapThimbles(thimbles, firstThimbleRowIndex, colIndex, secondThimbleRowIndex, colIndex);
    }
}