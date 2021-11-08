package com.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
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
import com.main.particle.World;
import com.main.ui.PButton;
import com.main.ui.PCheckButton;
import com.main.ui.PLabel;
import com.main.ui.PSelectBox;

import static java.lang.Math.*;


// TODO
// Wood color Done
// Iron color Done
// Empty color Done
// Fire spread tweaking Done
// Fire burns upwards Done
// Write fire rules Done
// Oil Done
// Interactions???
// Steam???
// O2???
// Gunpowder
// Gas???
// More modifiers for liquids like flow speed and shit

// TODO
// Perma fire source
// Perma water source
// Perma empty source
// Detect if is in body or is in edge of body
// Particle updates?
// Lerp color between particle of same type
// Friction through liquids and shit
// pressure

// TODO SOME DAY
// Pressure
//	With particle mass and shit
// Ice

// Chances are nearly always better than timers
// Diagonals takes time don't do that
public class Main extends ApplicationAdapter {

	public static final int pixelSize = 2;

	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;

	private Stage stage;

	private World world;

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

		world = new World();
	}

	private PLabel brushRadiusLabel;
	private PLabel brushChanceLabel;
	private PLabel spawnRateLabel;
	private PSelectBox spawnSelect;


	private void UI() {

		Gdx.input.setInputProcessor(stage);

		int yMax = Gdx.graphics.getHeight();



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
				for (int x = 0; x < World.width; x++) {
					for (int y = 0; y < World.length; y++) {
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

		// Spawn type select
		spawnSelect = new PSelectBox(Particle.TYPES, new Int2(10, 10), new Int2(100, 20), new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				spawnType = (String) spawnSelect.getSelected();
			}
		});
		stage.addActor(spawnSelect);

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
	private String spawnType = "empty";
	private int brushRadius = 5;

	private boolean brush = false;
	private boolean brushRandom = false;

	private void inputs() {
		final boolean inputMouseLeft = Gdx.input.isButtonPressed(Input.Keys.LEFT);

		// get mousePos and mousePos -> gridPos
		mousePos.set(Gdx.input.getX(), World.length * pixelSize - Gdx.input.getY());
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

		ScreenUtils.clear(175/255f, 238/255f, 238/255f, .5f);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

		for (int x = 0; x < World.width; x++) { // TODO : Paint pixmap
			for (int y = 0; y < World.length; y++) {


				shapeRenderer.setColor(world.getParticle(x, y).getColor());
				shapeRenderer.rect(x * pixelSize, y * pixelSize, pixelSize, pixelSize);
			}
		}

		shapeRenderer.setColor(Color.RED);

		shapeRenderer.end();

		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		//shapeRenderer.rect(world.minUpdate.x*pixelSize, world.minUpdate.y*pixelSize, (world.maxUpdate.x-world.minUpdate.x)*pixelSize, (world.maxUpdate.y-world.minUpdate.y)*pixelSize);
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
