package com.particle.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.main.Main;
import com.main.OldWorld;
import com.main.particle.Particle;
import com.main.particle.ParticleWorld;

public class DesktopLauncher {
	public static void main (String[] arg) {

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = "particle";
		config.width = (ParticleWorld.width) * Main.pixelSize + 310;
		config.height = (ParticleWorld.length) * Main.pixelSize;
		config.resizable = false; // Because particle spawning wont work otherwise

		new LwjglApplication(new Main(), config);

	}
}
