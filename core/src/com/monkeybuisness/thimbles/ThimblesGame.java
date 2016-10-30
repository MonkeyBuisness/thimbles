package com.monkeybuisness.thimbles;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.*;
import com.monkeybuisness.thimbles.scenes.GameFieldScene;

public class ThimblesGame extends ApplicationAdapter {

	private GameFieldScene gameFieldScene = null;

	@Override
	public void create () {
		gameFieldScene = new GameFieldScene(3, 3);

		Thimble thimble1 = new Thimble();
		thimble1
				.texture(createNiceTexture(Gdx.files.internal("thimble_skins/1.png")))
				.position(new Vector2(0, 0))
				.size(new Vector2(400, 300));

		Thimble thimble2 = new Thimble();
		thimble2
				.texture(createNiceTexture(Gdx.files.internal("thimble_skins/2.png")))
				.position(new Vector2(500, 100))
				.size(new Vector2(100, 100));

		gameFieldScene
				.thimbles()
					.add(thimble1, 0, 0)
					.add(thimble2, 0, 1)
				.build()
				//.backgroundSound(Gdx.audio.newSound(Gdx.files.internal("sounds/back.mp3")))
				.ready();
	}

	// TODO: delete this shit
	private Texture createNiceTexture(FileHandle fileHandle) {
		Texture texture = new Texture(fileHandle);
		texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		return texture;
	}

	public void update(float dt) {
		gameFieldScene.update(dt);
	}

	@Override
	public void render () {
		update(Gdx.graphics.getDeltaTime());
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		gameFieldScene.draw();
	}

	@Override
	public void dispose() {
		gameFieldScene.dispose();
	}

	@Override
	public void resize(int width, int height) {
		gameFieldScene.resize(width, height);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}