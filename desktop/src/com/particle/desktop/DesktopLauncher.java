package com.particle.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.main.Main;
import com.main.World;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = "particle";
		config.width = (World.width) * Main.pixelSize + 310;
		config.height = (World.length) * Main.pixelSize;
		config.resizable = false; // Because particle spawning wont work otherwise

		new LwjglApplication(new Main(), config);

	}
}
