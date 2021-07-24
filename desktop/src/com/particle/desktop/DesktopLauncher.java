package com.particle.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.main.Main;
import com.main.ParticleGrid;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = "particle";
		config.width = (ParticleGrid.width) * Main.pixelSize + 250;
		config.height = (ParticleGrid.length) * Main.pixelSize;

		new LwjglApplication(new Main(), config);
	}
}
