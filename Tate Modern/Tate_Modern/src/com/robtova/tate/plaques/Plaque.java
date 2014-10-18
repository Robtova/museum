package com.robtova.tate.plaques;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.robtova.tate.Main;
import com.robtova.tate.gfx.Art;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class Plaque {
	public String artist, born, title, year, medium, script;
	public int w, h;
	
	public void render(SpriteBatch batch) {
		batch.begin();
		
		batch.setColor(0.5f, 0.5f, 0.5f, 0.5f);
		batch.draw(Art.paper, -700, -700, 1400, 1400);
		batch.setColor(Color.WHITE);
		
		batch.draw(Art.paper, -w / 2, -h / 2, w, h);
		
		Art.arial.setScale(0.3125f);
		Art.arial_bold.setScale(0.35f);
		Art.arial_b_i.setScale(0.35f);
		Art.arial.setColor(Color.BLACK);
		Art.arial_bold.setColor(Color.BLACK);
		Art.arial_b_i.setColor(Color.BLACK);

		Art.arial_bold.draw(batch, artist, -w / 2 + 30, -h / 2 + 50);
		Art.arial.draw(batch, born, -w / 2 + 30, -h / 2 + 85);
		
		batch.setColor(Color.DARK_GRAY);
		batch.draw(Art.paper, -w / 2 + 30, -h / 2 + 120, w - 60, 1.5f);
		batch.draw(Art.paper, -w / 2 + 30, -h / 2 + 245, w - 60, 1.5f);
		batch.setColor(Color.WHITE);
		
		Art.arial_b_i.drawWrapped(batch, title, -w / 2 + 30, -h / 2 + 140, w - 60);
		Art.arial.draw(batch, year, -w / 2 + 30, -h / 2 + 175);
		Art.arial.draw(batch, medium, -w / 2 + 30, -h / 2 + 210);
		
		Art.arial.drawWrapped(batch, script, -w / 2 + 30, -h / 2 + 265, w - 60);

		Art.arial.setScale(1);
		Art.arial_bold.setScale(1);
		Art.arial_b_i.setScale(1);
		Art.arial.setColor(Color.WHITE);
		Art.arial_bold.setColor(Color.WHITE);
		Art.arial_b_i.setColor(Color.WHITE);

		batch.end();
	}
	
	public void speechText() {
		VoiceManager voiceManager = VoiceManager.getInstance();

	    Voice helloVoice = voiceManager.getVoice("kevin16");

	    helloVoice.allocate();

	    helloVoice.speak(title + " by " + artist + ". " + new PlaqueChurch().script);

	    helloVoice.deallocate();
	}
}
