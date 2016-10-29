package com.monkeybuisness.thimbles;

public interface InnerBuilder<T extends OuterBuilder> {
    T build();
}