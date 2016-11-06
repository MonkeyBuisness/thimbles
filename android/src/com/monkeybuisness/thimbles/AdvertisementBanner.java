package com.monkeybuisness.thimbles;

import android.content.Context;
import android.view.View;
import com.badlogic.gdx.math.Vector2;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.monkeybuisness.thimbles.advertisement.IAdvertisementBanner;

public class AdvertisementBanner implements IAdvertisementBanner {

    private Context context = null;
    private AdView adView = null;

    public AdvertisementBanner(Context context) {
        this.context = context;
        adView = new AdView(context);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(context.getResources().getString(R.string.admob_banner_id));
        AdRequest.Builder adRequestBuilder = new AdRequest.Builder();
        adView.loadAd(adRequestBuilder.build());
    }

    public View getAdvertisementView() {
        return adView;
    }

    @Override
    public Vector2 bannerSize() {
        return new Vector2(adView.getWidth(), adView.getHeight());
    }

    @Override
    public Vector2 bannerLocation() {
        return new Vector2(adView.getX(), adView.getY());
    }

    @Override
    public void setVisibility(final boolean visible) {
        AndroidLauncher androidLauncher = (AndroidLauncher)context;
        androidLauncher.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adView.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
            }
        });
    }
}