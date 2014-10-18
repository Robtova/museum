package com.robtova.tate.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.robtova.tate.Main;
import com.robtova.tate.entity.Player;
import com.robtova.tate.gfx.Art;
import com.robtova.tate.gfx.Tesselator;
import com.robtova.tate.plaques.Plaque;
import com.robtova.tate.plaques.PlaqueChurch;

public class Level {
	public PerspectiveCamera camera;
	Player player = new Player();
	public Chunk[][][] chunks;
	private Tesselator tesselator;
	public static int chunk_size = 32;
	private int width, height, depth;
	
	Plaque plaque;
	
	SpriteBatch batch;
	
	public Level(int width, int height, int depth) {
		tesselator = new Tesselator();
		chunks = new Chunk[width][height][depth];
		this.width = width;
		this.height = height;
		this.depth = depth;
		
		for(int i = 0; i < width; i++) {
			for(int k = 0; k < depth; k++) {
				setBlock(Block.floor, i, 0, k);
				setBlock(Block.wall, i, 6, k);
				
				if(Art.map.getPixel(i, k) == 0xffffffff) {
					setBlock(Block.wall, i, 1, k);
					setBlock(Block.wall, i, 2, k);
					setBlock(Block.wall, i, 3, k);
					setBlock(Block.wall, i, 4, k);
					setBlock(Block.wall, i, 5, k);
				}
				
				if(Art.map.getPixel(i, k) == 0xE5E5E5ff) {
					setBlock(Block.wall, i, 4, k);
					setBlock(Block.wall, i, 5, k);
				}
				
				if(Art.map.getPixel(i, k) == 0xD6D6D6ff) {
					setBlock(Block.FE, i, 4, k);
					setBlock(Block.wall, i, 5, k);
				}
				
				if(Art.map.getPixel(i, k) == 0xffff00ff) {
					setBlock(Block.light, i, 6, k);
				}
			}
		}
		
		setBlock(Block.GBCoCSD, 3, 2, 0);
		setBlock(Block.GBCoCSD_P, 2, 2, 0);
		
		setBlock(Block.Coffee_Mill, 6, 2, 0);
		setBlock(Block.Coffee_Mill_P, 5, 2, 0);
		
		setBlock(Block.Spiral_Movement, 8, 2, 0);
		setBlock(Block.Spiral_Movement_P, 9, 2, 0);
		
		setBlock(Block.SLwM_0, 0, 3, 3);
		setBlock(Block.SLwM_1, 0, 3, 2);
		setBlock(Block.SLwM_2, 0, 2, 3);
		setBlock(Block.SLwM_3, 0, 2, 2);
		setBlock(Block.SLwM_4, 0, 1, 3);
		setBlock(Block.SLwM_5, 0, 1, 2);
		setBlock(Block.SLwM_P, 0, 2, 4);
		
		setBlock(Block.IA_0, 0, 3, 9);
		setBlock(Block.IA_1, 0, 3, 8);
		setBlock(Block.IA_2, 0, 3, 7);
		setBlock(Block.IA_3, 0, 2, 9);
		setBlock(Block.IA_4, 0, 2, 8);
		setBlock(Block.IA_5, 0, 2, 7);
		setBlock(Block.IA_6, 0, 1, 9);
		setBlock(Block.IA_7, 0, 1, 8);
		setBlock(Block.IA_8, 0, 1, 7);
		setBlock(Block.IA_P, 0, 2, 6);
		
		setBlock(Block.QC_0, 11, 3, 2);
		setBlock(Block.QC_1, 11, 3, 3);
		setBlock(Block.QC_2, 11, 2, 2);
		setBlock(Block.QC_3, 11, 2, 3);
		setBlock(Block.QC_4, 11, 1, 2);
		setBlock(Block.QC_5, 11, 1, 3);
		setBlock(Block.QC_P, 11, 2, 4);
		
		setBlock(Block.C_0, 11, 3, 7);
		setBlock(Block.C_1, 11, 3, 8);
		setBlock(Block.C_2, 11, 3, 9);
		setBlock(Block.C_3, 11, 2, 7);
		setBlock(Block.C_4, 11, 2, 8);
		setBlock(Block.C_5, 11, 2, 9);
		setBlock(Block.C_6, 11, 1, 7);
		setBlock(Block.C_7, 11, 1, 8);
		setBlock(Block.C_8, 11, 1, 9);
		setBlock(Block.C_P, 11, 2, 6);
		
		Gdx.input.setCatchBackKey(true);
	}
	
	Vector3 camdir = new Vector3(0, 0, 0), localCamAxisX = new Vector3(0, 0, 0);
	private float[] fogColor = new float[] { (215f / 255f), (245f / 255f), (255f / 255f), 0.5f};
	
	public void render() {
		Gdx.gl.glClearColor(fogColor[0], fogColor[1], fogColor[2], fogColor[3]);
		Gdx.gl.glEnable(GL10.GL_CULL_FACE);
		Gdx.gl.glCullFace(GL10.GL_BACK);
		Gdx.gl.glDepthFunc(GL10.GL_LESS);
	    Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));

		camdir.set(camera.direction);
	    
	    if((camdir.nor().y>-0.9) && (player.roty<80) || (camdir.nor().y<-0.3) && (player.roty>-80)) {
	    	localCamAxisX.set(camera.direction);
	    	localCamAxisX.crs(camera.up.tmp()).nor();
	    	camera.rotate(player.roty, localCamAxisX.x, localCamAxisX.y, localCamAxisX.z);
	    	camera.up.nor();
	    }
	    
	    camera.translate(player.position.x, player.position.y - player.bob, player.position.z);
	    camera.rotate(player.rot, 0, 1, 0);
	    camera.update();

	    /*for(int i = (int) (player.position.x / Block.size) - Main.render_dist; i < (int) (player.position.x / Block.size); i++) {
	    	for(int j = (int) (player.position.y / Block.size) - Main.render_dist; j < (int) (player.position.y / Block.size); j++) {
	    		for(int k = (int) (player.position.z / Block.size) - Main.render_dist; k < (int) (player.position.z / Block.size); k++) {
	    	    	getBlock(i, j, k).render(tesselator, this, i, j, k);
	    	    }
	    		
	    		for(int k = (int) (player.position.z / Block.size); k <= (int) (player.position.z / Block.size) + Main.render_dist; k++) {
	    	    	getBlock(i, j, k).render(tesselator, this, i, j, k);
	    	    }
		    }
	    	
	    	for(int j = (int) (player.position.y / Block.size); j <= (int) (player.position.y / Block.size) + Main.render_dist; j++) {
	    		for(int k = (int) (player.position.z / Block.size) - Main.render_dist; k < (int) (player.position.z / Block.size); k++) {
	    	    	getBlock(i, j, k).render(tesselator, this, i, j, k);
	    	    }
	    		
	    		for(int k = (int) (player.position.z / Block.size); k <= (int) (player.position.z / Block.size) + Main.render_dist; k++) {
	    	    	getBlock(i, j, k).render(tesselator, this, i, j, k);
	    	    }
		    }
	    }
	    
	    for(int i = (int) (player.position.x / Block.size); i <= (int) (player.position.x / Block.size) + Main.render_dist; i++) {
	    	for(int j = (int) (player.position.y / Block.size) - Main.render_dist; j < (int) (player.position.y / Block.size); j++) {
	    		for(int k = (int) (player.position.z / Block.size) - Main.render_dist; k < (int) (player.position.z / Block.size); k++) {
	    	    	getBlock(i, j, k).render(tesselator, this, i, j, k);
	    	    }
	    		
	    		for(int k = (int) (player.position.z / Block.size); k <= (int) (player.position.z / Block.size) + Main.render_dist; k++) {
	    	    	getBlock(i, j, k).render(tesselator, this, i, j, k);
	    	    }
		    }
	    	
	    	for(int j = (int) (player.position.y / Block.size); j <= (int) (player.position.y / Block.size) + Main.render_dist; j++) {
	    		for(int k = (int) (player.position.z / Block.size) - Main.render_dist; k < (int) (player.position.z / Block.size); k++) {
	    	    	getBlock(i, j, k).render(tesselator, this, i, j, k);
	    	    }
	    		
	    		for(int k = (int) (player.position.z / Block.size); k <= (int) (player.position.z / Block.size) + Main.render_dist; k++) {
	    	    	getBlock(i, j, k).render(tesselator, this, i, j, k);
	    	    }
		    }
	    }*/
	    
	    for(int i = (int) ((float) (player.position.x - Main.render_dist) / chunk_size); i < (int) ((float) (player.position.x + Main.render_dist) / chunk_size); i++) {
	    	for(int j = (int) ((float) (player.position.y - Main.render_dist) / chunk_size); j < (int) ((float) (player.position.y + Main.render_dist) / chunk_size); j++) {
	    		for(int k = (int) ((float) (player.position.z - Main.render_dist) / chunk_size); k < (int) ((float) (player.position.z + Main.render_dist) / chunk_size); k++) {
	    	    	Chunk c = getChunk(i, j, k);
	    	    	if(c != null) c.render(tesselator, i * chunk_size, j * chunk_size, k * chunk_size, this);
	    	    }
		    }
	    }
	    
	    tesselator.addVert(player.position.x + Block.size * 0.5f, 1.01f, player.position.z + Block.size / 2, Color.WHITE.toFloatBits(), 0.125f * (2 + 1), 0.125f * (0 + 1));
		tesselator.addVert(player.position.x + Block.size * 0.5f, 1.01f, player.position.z - Block.size / 2, Color.WHITE.toFloatBits(), 0.125f * (2 + 1), 0.125f * (0));
		tesselator.addVert(player.position.x - Block.size * 0.5f, 1.01f, player.position.z - Block.size / 2, Color.WHITE.toFloatBits(), 0.125f * (2), 0.125f * (0));
		tesselator.addVert(player.position.x - Block.size * 0.5f, 1.01f, player.position.z + Block.size / 2, Color.WHITE.toFloatBits(), 0.125f * (2), 0.125f * (0 + 1));
	    
	    tesselator.render(camera, fogColor);
	    
	    tesselator.clear();
		
		camera.rotate(-player.rot, 0, 1, 0);
		
	    if((camdir.nor().y > -0.9) && (player.roty < 80) || (camdir.nor().y < -0.3) && (player.roty > -80)) {
	    	localCamAxisX.set(camera.direction);
	    	localCamAxisX.crs(camera.up.tmp()).nor();
	    	camera.rotate(-player.roty, localCamAxisX.x, localCamAxisX.y, localCamAxisX.z);
	    	camera.up.nor();
	    }
	    camera.translate(-player.position.x, -player.position.y + player.bob, -player.position.z);
	    camera.update();
	    
	    if(plaque != null) {
	    	plaque.render(batch);
	    }
	}
	
	public Block getBlock(int x, int y, int z) {
		if(x < 0 || x >= width || z < 0 || z >= depth || y < 0 || y >= height) return Block.air;
		int xc = x / chunk_size;
		int yc = y / chunk_size;
		int zc = z / chunk_size;
		if(chunks[xc][yc][zc] == null) return Block.air;
		return chunks[xc][yc][zc].getBlock(x - (xc * chunk_size), y - (yc * chunk_size), z - (zc * chunk_size));
	}
	
	public Chunk getChunk(int x, int y, int z) {
		if(x < 0 || x >= width || z < 0 || z >= depth || y < 0 || y >= height) return null;
		int xc = x / chunk_size;
		int yc = y / chunk_size;
		int zc = z / chunk_size;
		if(chunks[xc][yc][zc] == null) return null;
		return chunks[xc][yc][zc];
	}

	public void setBlock(Block block, int x, int y, int z) {
		if(x < 0 || x >= width || z < 0 || z >= depth || y < 0 || y >= height) return;
		int xc = x / chunk_size;
		int yc = y / chunk_size;
		int zc = z / chunk_size;
		if(chunks[xc][yc][zc] == null) chunks[xc][yc][zc] = new Chunk();
		chunks[xc][yc][zc].setBlock(block, x - (xc * chunk_size), y - (yc * chunk_size), z - (zc * chunk_size));
	}

	private int p;
	
	public void tick() {
		if(plaque == null) player.tick();
		
		if(Gdx.input.isKeyPressed(Keys.ESCAPE) && p == 0) {
			if(plaque != null) {
				plaque = null;
				player.oldx = Gdx.input.getX();
				player.oldy = Gdx.input.getY();
			}
			else Gdx.input.setCursorCatched(false);
			p = 20;
		}
		
		if(Gdx.input.isKeyPressed(Keys.BACK) && p == 0) {
			if(plaque != null) {
				plaque = null;
				player.oldx = Gdx.input.getX();
				player.oldy = Gdx.input.getY();
			}
			p = 20;
		}
		
		if(p > 0) p--;
	}

	public void resize(int width, int height) {
		float ratio = (float) height / (float) width;
		camera = new PerspectiveCamera(68, 1f, ratio);
        camera.near = 0.1f;
        camera.far = 50f;
        camera.update();

        Matrix4 projection = new Matrix4();
        float as = (float) height / (float) width * 700f;
		projection.setToOrtho(-700, 700, as, -as, -1, 1);

		batch = new SpriteBatch();
		batch.setProjectionMatrix(projection);
	}

	public void setPlaque(Plaque plaque) {
		this.plaque = plaque;
	}
}
