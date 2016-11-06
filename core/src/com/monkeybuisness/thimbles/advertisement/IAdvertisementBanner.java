package com.monkeybuisness.thimbles.advertisement;

import com.badlogic.gdx.math.Vector2;

public interface IAdvertisementBanner {
    Vector2 bannerSize();
    Vector2 bannerLocation();
    void setVisibility(boolean visible);
}