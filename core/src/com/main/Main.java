package com.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.particle.Particle;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.floor;

public class Main extends ApplicationAdapter {

	public static final int pixelSize = 4;

	private SpriteBatch batch;
	private HashMap<String, Texture> textureHashMap;
	private Stage stage;

	private World world;

	private Vector2 mousePos = new Vector2();
	private GridPoint2 gridPos = new GridPoint2();

	private boolean run = true;

	private int spawn = 0;
	private int spawnRate = 1;
	private int spawnIndex = 0;
	private String spawnType = Particle.particleTypes.get(spawnIndex);

	private PLabel spawnIndexLabel;

	@Override
	public void create () {
		batch = new SpriteBatch();
		textureHashMap = new HashMap<>();

		UI();

		ImageLoading();

		world = new World();
	}

	private void ImageLoading() {
		textureHashMap.put("air", new Texture("particle/air.png"));
		textureHashMap.put("sand", new Texture("particle/sand.png"));
		textureHashMap.put("water", new Texture("particle/water.png"));
	}

	private void UI() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		spawnIndexLabel = new PLabel("Particle: " + spawnType, new GridPoint2(50, 50));

		stage.addActor(spawnIndexLabel);

		stage.addActor(new PButton("Pause", new GridPoint2(50, 90), new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				run = !run;
			}
		}));

		stage.addActor(new PButton("Tick", new GridPoint2(50, 70), new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (!run) world.tick();
			}
		}));

		stage.addActor(new PButton("#", new GridPoint2(40, 50), new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				spawnIndex++;

				if (spawnIndex >= Particle.particleTypes.size()) spawnIndex = 0;

				spawnType = Particle.particleTypes.get(spawnIndex);

				spawnIndexLabel.setText("Particle: " + spawnType);
			}
		}));


	}

	@Override
	public void render () {

		inputs();

		if (run) world.tick();

		draw();

	}

	private void inputs() {


		// get mousePos and mousePos -> gridPos
		mousePos.set(Gdx.input.getX(), world.length*pixelSize - Gdx.input.getY());
		gridPos.set(
				(int) floor(mousePos.x/pixelSize),
				(int) floor(mousePos.y/pixelSize)
		);


		// spawn particles

		int brush = 2;

		if ( Gdx.input.isButtonPressed(Input.Buttons.LEFT) ) {
			if (spawn == 0) {
				if (0 <= gridPos.x && gridPos.x < world.width && 0 <= gridPos.y && gridPos.y < world.length) { // TODO : MAKE TO METHOD

					for (int x = Math.max(gridPos.x-brush, 0); x < Math.min(gridPos.x+brush, world.width-1); x++) {
						for (int y = Math.max(gridPos.y-brush, 0); y < Math.min(gridPos.y+brush, world.length-1); y++) {
							world.setParticle(x, y, new Particle(spawnType));
						}
					}

				}
				spawn = spawnRate;
			}
			else spawn--;
		}
		else spawn = 0;
	}

	private void draw() {

		ScreenUtils.clear(1, 1, 1, 1);

		stage.getBatch().begin();

		for (int x = 0; x < world.width; x++) {
			for (int y = 0; y < world.length; y++) {

				String name = world.getParticle(x, y).type;

				stage.getBatch().draw(textureHashMap.get(name), x * pixelSize, y * pixelSize, pixelSize, pixelSize);
			}
		}

		stage.getBatch().end();

		stage.act();
		stage.draw();
	}

	@Override
	public void dispose () {
		batch.dispose();
		stage.dispose();

		for (Map.Entry<String, Texture> t : textureHashMap.entrySet()) {
			t.getValue().dispose();
		}
	}
}
