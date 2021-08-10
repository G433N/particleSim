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
import com.main.ui.PButton;
import com.main.ui.PCheckButton;
import com.main.ui.PLabel;

import java.util.Random;

import static java.lang.Math.floor;


// TODO : Better GUI
// TODO : Particle targeting
// TODO :
public class Main extends ApplicationAdapter {

	public static final int pixelSize = 4;

	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;

	private Stage stage;

	private World world;

	private final Float2 mousePos = new Float2();
	private final Int2 gridPos = new Int2();

	private boolean simulate = false;

	@Override
	public void create () {
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		stage = new Stage(new ScreenViewport(), batch);

		UI();

		world = new World();
	}

	private PLabel spawnIndexLabel;
	private PCheckButton pauseButton;

	private void UI() {

		Gdx.input.setInputProcessor(stage);

		int xMax = Gdx.graphics.getWidth();
		int yMax = Gdx.graphics.getHeight();

		spawnIndexLabel = new PLabel("Particle: " + spawnType, new Int2(10, 10));
		stage.addActor(spawnIndexLabel);

		pauseButton = new PCheckButton("Simulate", new Int2(10, 30), new Int2(70, 20), new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				simulate = !simulate;
			}
		});
		stage.addActor(pauseButton);


		for (int i = 0; i < Particle.TYPES.size(); i++) { // Adds all particle types buttons

			final Particle particle = Particle.DATA.get(Particle.TYPES.get(i));

			stage.addActor(new PButton(particle.type, new Int2(10, yMax - 30 - 30 * i), new Int2(50, 20), new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					spawnType = particle.type;
					spawnIndexLabel.setText("Particle: " + spawnType);
				}
			}));

		}
	}


	@Override
	public void render () {

		float deltaTime = Gdx.graphics.getDeltaTime();

		inputs();

		if (simulate) world.tick(deltaTime);

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
				if (0 <= gridPos.x && gridPos.x < World.width && 0 <= gridPos.y && gridPos.y < World.length) { // FIXME : MAKE TO METHOD

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

		stage.act();
		stage.draw();
	}

	@Override
	public void dispose () {
		batch.dispose();
		stage.dispose();
	}
}
