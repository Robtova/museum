package com.robtova.tate;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Input.Keys;
import com.robtova.tate.gfx.Art;
import com.robtova.tate.level.Level;
import com.robtova.tate.plaques.PlaqueChurch;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class Main implements ApplicationListener {
	
	public static int WIDTH = 1024, HEIGHT = 600;
	private float accum = 0;
	private int fps, collective_fps, ticks;
	private long now;
	public static Level level;
	public static int render_dist = 32, render_front = 12;
    
	@Override
	public void create() {	
		new Art();
		Gdx.input.setCursorCatched(true);
		level = new Level(32, 16, 32);
		if(device()) {
			render_dist = 15;
			render_front = 5;
		}
	}

	@Override
	public void dispose() {
	}

	@Override
	public void render() {	
		accum += Gdx.graphics.getDeltaTime();
		while(accum > 1.0f / 60.0f) {
			tick();
			accum -= 1.0f / 60.0f;
			ticks++;
		}
		
		level.render();
		
	    fps++;
	    
	    if(System.currentTimeMillis() - now > 1000) {
			now = System.currentTimeMillis();
			collective_fps = fps;
			fps = 0;
			print("FPS: " + collective_fps + ", ticks: " + ticks);
			ticks = 0;
		}
	}
	
	private int old_w, old_h, p;
	private boolean fullscreen;
	
	public void tick() {
		if(Gdx.input.isKeyPressed(Keys.F11) && p == 0) {
			if(!fullscreen) {
				old_w = Gdx.graphics.getWidth();
				old_h = Gdx.graphics.getHeight();
				Gdx.graphics.setDisplayMode(Gdx.graphics.getDesktopDisplayMode().width, Gdx.graphics.getDesktopDisplayMode().height, true);
				fullscreen = true;
				p = 15;
			} else {
				Gdx.graphics.setDisplayMode(old_w, old_h, false);
				fullscreen = false;
				p = 15;
			}
		}
		if(p > 0) p--;
		level.tick();
	}
	
	@Override
	public void resize(int width, int height) {
		WIDTH = width;
		HEIGHT = height;
		level.resize(width, height);
	}

	public static boolean desktop() {
		return Gdx.app.getType() == ApplicationType.Desktop || Gdx.app.getType() == ApplicationType.Applet || Gdx.app.getType() == ApplicationType.WebGL;
	}
	
	public static boolean device() {
		return Gdx.app.getType() == ApplicationType.Android || Gdx.app.getType() == ApplicationType.iOS;
	}
	
	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	public static void print(Object o) {
		Gdx.app.log("Tate Modern", o + "");
	}
}
