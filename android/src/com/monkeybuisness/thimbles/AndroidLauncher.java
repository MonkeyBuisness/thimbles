package com.monkeybuisness.thimbles;

import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.monkeybuisness.thimbles.ThimblesGame;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initializeAndroidApplicationComponents();
	}

	private void initializeAndroidApplicationComponents() {
		RelativeLayout gameLayout = new RelativeLayout(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		AdvertisementBanner advertisementBanner = new AdvertisementBanner(this);
		advertisementBanner.setVisibility(false);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		View gameView = initializeForView(new ThimblesGame(advertisementBanner), config);
		gameLayout.addView(gameView);
		RelativeLayout.LayoutParams adParams =
				new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
		adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		adParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		gameLayout.addView(advertisementBanner.getAdvertisementView(), adParams);
		setContentView(gameLayout);
	}
}