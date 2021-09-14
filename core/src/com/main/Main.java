package com.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.main.math.Float2;
import com.main.math.Int2;
import com.main.particle.Particle;
import com.main.particle.ParticleWorld;
import com.main.ui.PButton;
import com.main.ui.PCheckButton;
import com.main.ui.PLabel;

import static java.lang.Math.*;


// TODO : Particle targeting
// TODO : Fire and Wood
public class Main extends ApplicationAdapter {

	public static final int pixelSize = 16;

	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;

	private Stage stage;

	private ParticleWorld world;

	private final Float2 mousePos = new Float2();
	private final Int2 gridPos = new Int2();
	private boolean dataNext = false;
	private boolean dataStop = false;

	@Override
	public void create () {
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		stage = new Stage(new ScreenViewport(), batch);

		UI();

		world = new ParticleWorld();
	}

	private PLabel spawnIndexLabel;
	private PLabel brushRadiusLabel;
	private PLabel brushChanceLabel;
	private PLabel spawnRateLabel;


	private void UI() {

		Gdx.input.setInputProcessor(stage);

		int xMax = Gdx.graphics.getWidth();
		int yMax = Gdx.graphics.getHeight();

		spawnIndexLabel = new PLabel("Particle: " + spawnType, new Int2(10, 10));
		stage.addActor(spawnIndexLabel);

		brushRadiusLabel = new PLabel("" + brushRadius, new Int2(172, yMax - 30));
		stage.addActor(brushRadiusLabel);

		brushChanceLabel = new PLabel("" + (spawnChance), new Int2(172, yMax - 50));
		stage.addActor(brushChanceLabel);

		spawnRateLabel = new PLabel("" + (spawnRate), new Int2(172, yMax - 70));
		stage.addActor(spawnRateLabel);

		stage.addActor(new PLabel("%", new Int2(216, yMax - 50)));
		stage.addActor(new PLabel("Spawn rate:", new Int2(70, yMax - 70)));
		stage.addActor(new PLabel("every n frame", new Int2(216, yMax - 70)));

		// Simulate button
		stage.addActor(new PCheckButton("Simulate", new Int2(10, 30), new Int2(70, 20), new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				world.simulate = !world.simulate;
			}
		}));

		// Reset button
		stage.addActor(new PButton("Reset", new Int2(10, 50), new Int2(50, 20), new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				for (int x = 0; x < ParticleWorld.width; x++) {
					for (int y = 0; y < ParticleWorld.length; y++) {
						world.setParticle(x, y, "empty");
					}
				}
			}
		}));

		// Data button
		stage.addActor(new PButton("Data", new Int2(10, 80), new Int2(50, 20), new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				dataNext = true;
			}
		}));

		// Brush button
		stage.addActor(new PCheckButton("Brush", new Int2(56, yMax - 30), new Int2(80, 20), new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				brush = !brush;
			}
		}));

		// Brush radius minus
		stage.addActor(new PButton("-", new Int2(150, yMax - 30), new Int2(20, 20), new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				brushRadius = max(1, brushRadius - 1);
				brushRadiusLabel.setText(brushRadius);
			}
		}));

		// Brush radius plus
		stage.addActor(new PButton("+", new Int2(196, yMax - 30), new Int2(20, 20), new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				brushRadius = min(101, brushRadius + 1);
				brushRadiusLabel.setText(brushRadius);
			}
		}));

		// Random brush button
		stage.addActor(new PCheckButton("Random", new Int2(63, yMax - 50), new Int2(80, 20), new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				brushRandom = !brushRandom;
			}
		}));

		// Random chance minus
		stage.addActor(new PButton("-", new Int2(150, yMax - 50), new Int2(20, 20), new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				spawnChance = max(1, spawnChance - 1);
				brushChanceLabel.setText(spawnChance);
			}
		}));

		// Random chance plus
		stage.addActor(new PButton("+", new Int2(196, yMax - 50), new Int2(20, 20), new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				spawnChance = min(100, spawnChance + 1);
				brushChanceLabel.setText(spawnChance);
			}
		}));

		// Spawn rate minus
		stage.addActor(new PButton("-", new Int2(150, yMax - 70), new Int2(20, 20), new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				spawnRate = max(1, spawnRate - 1);
				spawnRateLabel.setText(spawnRate);
			}
		}));

		// Spawn rate plus
		stage.addActor(new PButton("+", new Int2(196, yMax - 70), new Int2(20, 20), new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				spawnRate = min(10, spawnRate + 1);
				spawnRateLabel.setText(spawnRate);
			}
		}));

		// Adds all particle types buttons
		for (int i = 0; i < Particle.TYPES.length; i++) {

			final String type = Particle.TYPES[i];

			stage.addActor(new PButton(type, new Int2(10, yMax - 30 - 30 * i), new Int2(50, 20), new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					spawnType = type;
					spawnIndexLabel.setText("Particle: " + type);
				}
			}));
		}
	}

	@Override
	public void render () {

		float deltaTime = Gdx.graphics.getDeltaTime();

		inputs();

		world.tick(deltaTime);

		draw();
	}

	// Input handling

	private int spawn = 0;
	private int spawnRate = 1;
	private int spawnChance = 5;
	private String spawnType = "sand";
	private int brushRadius = 5;

	private boolean brush = false;
	private boolean brushRandom = false;

	private void inputs() {
		final boolean inputMouseLeft = Gdx.input.isButtonPressed(Input.Keys.LEFT);

		// get mousePos and mousePos -> gridPos
		mousePos.set(Gdx.input.getX(), ParticleWorld.length * pixelSize - Gdx.input.getY());
		gridPos.set(
				(int) floor(mousePos.x / pixelSize),
				(int) floor(mousePos.y / pixelSize)
		);

		if(dataNext && dataStop && !inputMouseLeft) {
			dataNext = false;
			dataStop = false;
		}
		else if (dataNext && !dataStop && inputMouseLeft) {

			Particle p = this.world.getParticle(gridPos);
			p.printData();
			System.out.println("---------------------------");
			dataStop = true;
		}
		else if (!dataStop && inputMouseLeft) {
			if (spawn >= spawnRate) {
				world.spawn(gridPos, brushRadius, spawnType, brush, brushRandom, spawnChance);
				spawn = 0;
			}
			else spawn++;
		}
	}

	private void draw() {

		ScreenUtils.clear(0, 1, 1, .5f);

		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

		for (int x = 0; x < ParticleWorld.width; x++) {
			for (int y = 0; y < ParticleWorld.length; y++) {

				//if (!world.getParticle(x, y).liquid) continue;

				shapeRenderer.setColor(world.getParticle(x, y).getColor());
				shapeRenderer.rect(x * pixelSize, y * pixelSize, pixelSize, pixelSize);
			}
		}
		shapeRenderer.end();

		stage.act();
		stage.draw();
	}

	@Override
	public void dispose () {
		batch.dispose();
		stage.dispose();
	}
}
