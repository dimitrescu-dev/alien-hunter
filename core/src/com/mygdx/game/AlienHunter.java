package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class AlienHunter extends ApplicationAdapter {
	SpriteBatch batch;
	AlienManager alienManager;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		alienManager = new AlienManager();
	}

	@Override
	public void render () {
		ScreenUtils.clear(176f/255f, 176f/255f, 176f/255f, 1);
		batch.begin();
		alienManager.render(batch);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
