package com.virginia.cs.SlingshotSam.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.virginia.cs.SlingshotSam.Game;
import com.virginia.cs.SlingshotSam.TileTest;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		//Use initialize Game for running the actual game, use initialize TileTest for the view of practice map
		initialize(new Game(), config);
		//initialize(new TileTest(), config);

	}
}
