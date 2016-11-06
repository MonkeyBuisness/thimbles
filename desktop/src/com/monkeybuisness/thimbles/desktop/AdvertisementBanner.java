package com.monkeybuisness.thimbles.desktop;

import com.badlogic.gdx.math.Vector2;
import com.monkeybuisness.thimbles.advertisement.IAdvertisementBanner;

public class AdvertisementBanner implements IAdvertisementBanner {
    @Override
    public Vector2 bannerSize() {
        return new Vector2(0.f, 0.f);
    }

    @Override
    public Vector2 bannerLocation() {
        return new Vector2(0.f, 0.f);
    }

    @Override
    public void setVisibility(boolean visible) {}
}