package com.monkeybuisness.thimbles.actions.thimbles;

import com.monkeybuisness.thimbles.actors.Thimble;
import com.monkeybuisness.thimbles.actions.IActorActionListener;
import com.monkeybuisness.thimbles.scenes.GameFieldScene;

public interface IThimbleAction {
    void perform(Thimble[][] thimbles, GameFieldScene scene, IActorActionListener listener);
}