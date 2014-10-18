package com.robtova.tate;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class MainRun {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Tate Modern by Robert Fraser";
		cfg.useGL20 = false;
		cfg.width = Main.WIDTH;
		cfg.height = Main.HEIGHT;

		new LwjglApplication(new Main(), cfg);
	}
}
