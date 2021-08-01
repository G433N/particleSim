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
import com.particle.Particles;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.floor;

// TODO : Add editor tools, like brush size, fast selecet, spawn speed


public class Main extends ApplicationAdapter {

	public static final int pixelSize = 4;

	private SpriteBatch batch;
	private HashMap<String, Texture> textureHashMap;
	private Stage stage;

	private ParticleGrid particleGrid;

	private Vector2 mousePos = new Vector2();
	private GridPoint2 gridPos = new GridPoint2(); //IntVector2;

	private boolean run = true;

	private int spawn = 0;
	private int spawnRate = 1;
	private int spawnIndex = 2;
	private Particles spawnType = Particles.getParticle(spawnIndex);

	private PLabel spawnIndexLabel;

	@Override
	public void create () {
		batch = new SpriteBatch();
		textureHashMap = new HashMap<>();

		UI();

		ImageLoading();

		particleGrid = new ParticleGrid();
	}

	private void ImageLoading() { // TODO : Add automated loading
		textureHashMap.put("air", new Texture("particle/air.png"));
		textureHashMap.put("sand", new Texture("particle/sand.png"));
		textureHashMap.put("water", new Texture("particle/water.png"));
		textureHashMap.put("dirt", new Texture("particle/dirt.png"));
		textureHashMap.put("metal", new Texture("particle/metal.png"));
		textureHashMap.put("mud", new Texture("particle/mud.png"));
		textureHashMap.put("grass", new Texture("particle/grass.png"));
	}

	private void UI() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		spawnIndexLabel = new PLabel("Particle: " + spawnType.name().toLowerCase(), new GridPoint2(50, 50));

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
				if (!run) particleGrid.update();
			}
		}));

		stage.addActor(new PButton("#", new GridPoint2(40, 50), new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				spawnIndex++;

				if (spawnIndex >= Particles.getAmount()) spawnIndex = 1;

				spawnType = Particles.getParticle(spawnIndex);

				spawnIndexLabel.setText("Particle: " + spawnType.name().toLowerCase());
			}
		}));


	}



	@Override
	public void render () {

		inputs();

		if(run) particleGrid.update();

		//slowMode();

		draw();

	}


	//slowMode();

	private int i = 0;
	private int max = ParticleGrid.width * ParticleGrid.length;

	private void slowMode() {
		if (this.i >= this.max) {
			this.i = 0;
		}

		int y = this.i % ParticleGrid.length;
		int x = Math.floorDiv(this.i, ParticleGrid.width);

		particleGrid.updateParticle(x, y);

		System.out.println(i);

		i++;
	}

	private void inputs() {
		mousePos.set(Gdx.input.getX(), ParticleGrid.length*pixelSize - Gdx.input.getY());
		gridPos.set(
				(int) floor(mousePos.x/pixelSize),
				(int) floor(mousePos.y/pixelSize)
		);

		if ( Gdx.input.isButtonPressed(Input.Buttons.LEFT) ) {
			if (spawn == 0) {
				if (0 <= gridPos.x && gridPos.x < ParticleGrid.width && 0 <= gridPos.y && gridPos.y < ParticleGrid.length) { // TODO : MAKE TO METHOD
					particleGrid.setParticle(gridPos, new Particle(spawnType));
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

		for (int y = 0; y < particleGrid.length; y++) {
			for (int x = 0; x < particleGrid.width; x++) {

				String name = particleGrid.getParticle(x, y).getType().name().toLowerCase();

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

	public static int clamp(int v, int min, int max) {

		return Math.max(min, Math.min(max, v));

	}
}
