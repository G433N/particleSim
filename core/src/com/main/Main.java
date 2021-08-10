package com.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.main.math.Float2;
import com.main.math.Int2;
import com.main.ui.PButton;
import com.main.ui.PLabel;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static java.lang.Math.floor;

public class Main extends ApplicationAdapter {

	public static final int pixelSize = 4;

	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;

	private Stage stage;

	private World world;

	private final Float2 mousePos = new Float2();
	private final Int2 gridPos = new Int2();

	private boolean run = true;

	@Override
	public void create () {
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		stage = new Stage(new ScreenViewport(), batch);

		UI();

		world = new World();
	}

	private PLabel spawnIndexLabel;

	private void UI() {

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
				if (!run) world.tick(Gdx.graphics.getDeltaTime());
			}
		}));

		stage.addActor(new PButton("#", new GridPoint2(40, 50), new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				spawnIndex++;

				if (spawnIndex >= Particle.TYPES.size()) spawnIndex = 0;

				spawnType = Particle.TYPES.get(spawnIndex);

				spawnIndexLabel.setText("Particle: " + spawnType);
			}
		}));


	}

	@Override
	public void render () {

		float deltaTime = Gdx.graphics.getDeltaTime();

		inputs();

		if (run) world.tick(deltaTime);

		draw();

	}

	// Input handling

	private int spawn = 0;
	private final int spawnRate = 1;
	private final int spawnChance = 64;
	private int spawnIndex = 0;
	private String spawnType = Particle.TYPES.get(spawnIndex);
	private int brush = 16;



	private final Random random = new Random();

	private void inputs() {


		// get mousePos and mousePos -> gridPos
		mousePos.set(Gdx.input.getX(), World.length * pixelSize - Gdx.input.getY());
		gridPos.set(
				(int) floor(mousePos.x / pixelSize),
				(int) floor(mousePos.y / pixelSize)
		);


		// spawn particles

		if ( Gdx.input.isButtonPressed(Input.Buttons.LEFT) ) {
			if (spawn == 0) {
				if (0 <= gridPos.x && gridPos.x < World.width && 0 <= gridPos.y && gridPos.y < World.length) { // TODO : MAKE TO METHOD

					for (int x = Math.max(gridPos.x-brush, 0); x < Math.min(gridPos.x+brush, World.width -1); x++) {
						for (int y = Math.max(gridPos.y-brush, 0); y < Math.min(gridPos.y+brush, World.length -1); y++) {

							if(random.nextInt(spawnChance) == 0) {
								world.setParticle(x, y, new Particle(spawnType));
							}

						}
					}
				} spawn = spawnRate;
			} else spawn--;
		} else spawn = 0;
	}

	private void draw() {

		ScreenUtils.clear(0, 1, 1, .5f);

		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

		for (int x = 0; x < World.width; x++) {
			for (int y = 0; y < World.length; y++) {
				String color = world.getParticle(x, y).color;
				shapeRenderer.setColor(Particle.COLOR.get(color));
				shapeRenderer.rect(x * pixelSize, y * pixelSize, pixelSize, pixelSize);
			}
		}
		shapeRenderer.end();


		stage.draw();
	}

	@Override
	public void dispose () {
		batch.dispose();
		stage.dispose();
	}
}
