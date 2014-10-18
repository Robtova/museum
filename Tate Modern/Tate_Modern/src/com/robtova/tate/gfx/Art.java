package com.robtova.tate.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Art {
	
	public static Texture blocks, paper;
	public static Pixmap map;
	public static BitmapFont arial, arial_bold, arial_b_i;
	
	public Art() {
		blocks = new Texture(Gdx.files.internal("data/blocks.png"));
		map = new Pixmap(Gdx.files.internal("data/level.png"));
		
		arial = new BitmapFont(Gdx.files.internal("data/Arial.fnt"), Gdx.files.internal("data/Arial.png"), true);
		arial_bold = new BitmapFont(Gdx.files.internal("data/Arial_bold.fnt"), Gdx.files.internal("data/Arial_bold.png"), true);
		arial_b_i = new BitmapFont(Gdx.files.internal("data/Arial_B+I.fnt"), Gdx.files.internal("data/Arial_B+I.png"), true);
		
		paper = new Texture(Gdx.files.internal("data/paper.png"));
	}
}
