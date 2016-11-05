package com.monkeybuisness.thimbles.actions;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.monkeybuisness.thimbles.InnerBuilder;
import com.monkeybuisness.thimbles.OuterBuilder;

import java.util.ArrayList;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class ActorAction implements OuterBuilder<ActorAction> {

    private SequenceAction sequenceAction = null;
    private Actions actions = null;
    private boolean isParallelSequence = true;

    public ActorAction() {
        sequenceAction = new SequenceAction();
        actions = new Actions();
    }

    protected void buildActions(Action[] actions, final IActorActionListener listener) {
        if (listener == null)
            sequenceAction.addAction(isParallelSequence ? parallel(actions) : sequence(actions));
        else {
            Runnable callbackRunnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        listener.onThimbleActionComplete();
                    } catch (Exception e) {}
                }
            };
            sequenceAction.addAction(
                    sequence((isParallelSequence ? parallel(actions) : sequence(actions)), run(callbackRunnable)));
        }
    }

    public SequenceAction instance() {
        return sequenceAction;
    }

    public Actions parallelActions() {
        isParallelSequence = true;
        return actions;
    }

    public Actions sequenceActions() {
        isParallelSequence = false;
        return actions;
    }

    @Override
    public ActorAction build() {
        return this;
    }

    public class Actions implements InnerBuilder<ActorAction> {

        private ArrayList<Action> actionsList = null;
        private IActorActionListener onActionListener = null;

        public Actions() {
            actionsList = new ArrayList<Action>();
        }

        public Actions add(Action action) {
            actionsList.add(action);
            return this;
        }

        public Actions completeCallback(IActorActionListener listener) {
            this.onActionListener = listener;
            return this;
        }

        @Override
        public ActorAction build() {
            Action[] actions = new Action[actionsList.size()];
            actionsList.toArray(actions);
            ActorAction.this.buildActions(actions, onActionListener);
            actionsList.clear();
            onActionListener = null;
            return ActorAction.this;
        }
    }
}