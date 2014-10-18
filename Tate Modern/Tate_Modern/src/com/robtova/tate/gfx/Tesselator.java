package com.robtova.tate.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL11;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.robtova.tate.Main;
import com.robtova.tate.level.Block;
import com.robtova.tate.level.Level;

public class Tesselator {
	private Mesh mesh;
	ShaderProgram shader;
	private float[] verts;
	public int build_length;
	private Matrix4 trans_matrix = new Matrix4();
	private int size;
	
	public Tesselator() {
		this(120000);
	}
	
	public Tesselator(int size) {
		if (mesh == null) {
	        mesh = new Mesh(Mesh.VertexDataType.VertexArray, false, 4 * size, 6 * size, 
	                new VertexAttribute(Usage.Position, 3, "a_position"),
	                new VertexAttribute(Usage.ColorPacked, 4, "a_color"),
	                new VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoord0"));

	        verts = new float[size * 6];
	                              
	        short[] ind = new short[size * 6];
	        
	        int o = 0;
	        
	        for(int i = 0; i < ind.length; i+=6) {
	        	ind[i + 0] = (short) (o);
	        	ind[i + 1] = (short) (o + 1);
	        	ind[i + 2] = (short) (o + 2);
	        	ind[i + 3] = (short) (o + 2);
	        	ind[i + 4] = (short) (o + 3);
	        	ind[i + 5] = (short) (o);
	        	o += 4;
	        }
	        
	        mesh.setIndices(ind);
	    }
		
		this.size = size;
		
		if(Gdx.graphics.isGL20Available()) shader = new ShaderProgram(Gdx.files.internal("data/main.vert").readString(), Gdx.files.internal("data/main.frag").readString());

		if(!Gdx.graphics.isGL20Available()) {
			Gdx.gl10.glFogf(GL10.GL_FOG_MODE, GL10.GL_LINEAR);
			Gdx.gl10.glFogf(GL10.GL_FOG_DENSITY, 0.7f);
			Gdx.gl10.glFogf(GL10.GL_FOG_START, Main.render_front);
			Gdx.gl10.glFogf(GL10.GL_FOG_END, Main.render_dist - 5);
			Gdx.gl10.glEnable(GL10.GL_FOG);
			
			Gdx.gl10.glEnable(GL10.GL_COLOR_MATERIAL);

			Gdx.gl10.glMaterialfv(GL10.GL_FRONT, GL10.GL_AMBIENT_AND_DIFFUSE, new float[] {0.0f, 0.0f, 0.0f, 1.0f}, 0);
			Gdx.gl10.glMaterialfv(GL10.GL_FRONT, GL10.GL_SPECULAR, new float[] {0.0f, 0.0f, 0.0f, 1.0f}, 0);
			Gdx.gl10.glMaterialfv(GL10.GL_FRONT, GL10.GL_EMISSION, new float[] {0.0f, 0.0f, 0.0f, 1.0f}, 0);
			Gdx.gl10.glMaterialf(GL10.GL_FRONT, GL10.GL_SHININESS, 0);
			
			Gdx.gl10.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, new float[]{1.0f, 1.0f, 1.0f, 1.0f}, 0);
			Gdx.gl10.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, new float[]{0.0f, 0.0f, 0.0f, 1.0f}, 0);
			Gdx.gl10.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, new float[]{0.0f, 0.0f, 0.0f, 1.0f}, 0);
			Gdx.gl10.glLightfv(GL10.GL_LIGHT0, GL10.GL_EMISSION, new float[]{0.0f, 0.0f, 0.0f, 1.0f}, 0);

			Gdx.gl10.glEnable(GL10.GL_LIGHTING);
			Gdx.gl10.glEnable(GL10.GL_LIGHT0);
		}
	}
	
	public void render(Camera camera, float[] fogColor) {
		mesh.setVertices(verts);
		
		Gdx.gl.glEnable(GL20.GL_TEXTURE_2D);   
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);   

		if(Gdx.graphics.isGL20Available()) {
			shader.begin();
			
			Art.blocks.bind();

			shader.setUniformMatrix("u_projTrans", camera.combined);
			shader.setUniformf("u_fog_color", fogColor[0], fogColor[1], fogColor[2], fogColor[3]);
			shader.setUniformf("u_fog_start", Main.render_dist - 25);
			shader.setUniformf("u_fog_end", Main.render_dist - 5);
			shader.setUniformf("u_ambient", 1.0f, 1.0f, 1.0f, 1.0f);

			mesh.render(shader, GL20.GL_TRIANGLES, 0, build_length);
			    
			shader.end();
		} else {
			Art.blocks.bind();
			
			Gdx.gl10.glFogfv(GL10.GL_FOG_COLOR, fogColor, 0);
			Gdx.gl10.glFogf(GL10.GL_FOG_START, Main.render_front);
			Gdx.gl10.glFogf(GL10.GL_FOG_END, Main.render_dist - 5);
			Gdx.gl10.glAlphaFunc(GL10.GL_GREATER, 0.2f);
			Gdx.gl10.glEnable(GL10.GL_ALPHA_TEST);
			
			camera.apply(Gdx.gl10);
			
			mesh.render(GL20.GL_TRIANGLES, 0, build_length);
		}
	}
	
	public void clear() {
		for(int i = 0; i < size * 6; i++) {
			verts[i] = 0f;
		}
		build_length = 0;
	}
	
	public void translate(float x, float y, float z) {
		trans_matrix.translate(x, y, z);
	}
	
	public void rotate(float rot, float x, float y, float z) {
		trans_matrix.rotate(x, y, z, rot);
	}
	
	public void drawLowSphere() {
		/*addVert(-1.0f, -0.5f, -1.0f, th, 0, 1);
    	addVert(-0.5f, -0.5f, -1.0f, th, 1, 1);
    	addVert(-0.5f,  0.0f, -1.0f, t, 1, 0);
    	addVert(-1.0f,  0.0f, -1.0f, t, 0, 0);
    	
    	addVert(-1.0f,  0.0f, -1.0f,  t, 0, 1);
    	addVert(-0.5f,  0.0f, -1.0f,  t, 1, 1);
    	addVert(-0.5f, 0.25f,-1.25f,  o, 1, 0);
    	addVert(-1.0f, 0.25f,-1.25f,  o, 0, 0);
    	
    	addVert(-0.5f, -0.5f, -1.0f, th, 0, 1);
    	addVert(-0.25f,-0.5f, -1.25f, th, 1, 1);
    	addVert(-0.25f, 0.0f, -1.25f, t, 1, 0);
    	addVert(-0.5f,  0.0f, -1.0f, t, 0, 0);
    	
    	addVert(-0.25f, 0.0f, -1.25f, t, 1, 0);
    	addVert(-0.25f, 0.0f, -1.25f, t, 1, 0);
    	addVert(-0.5f, 0.25f,-1.25f,  o, 1, 0);
    	addVert(-0.5f,  0.0f, -1.0f,  t, 0, 0);
    	
    	addVert(-1.25f,-0.5f, -1.25f, th, 1, 1);
    	addVert(-1.0f, -0.5f, -1.0f, th, 0, 1);
    	addVert(-1.0f,  0.0f, -1.0f, t, 0, 0);
    	addVert(-1.25f, 0.0f, -1.25f, t, 1, 0);
    	
    	addVert(-1.25f, 0.0f, -1.25f, t, 1, 0);
    	addVert(-1.25f, 0.0f, -1.25f, t, 1, 0);
    	addVert(-1.0f,  0.0f, -1.0f,  t, 0, 0);
    	addVert(-1.0f, 0.25f,-1.25f,  o, 1, 0);*/
	}
	
	float[] col = {Color.toFloatBits(255, 255, 255, 255), Color.toFloatBits(205, 205, 205, 255), 
			   Color.toFloatBits(195, 195, 195, 255), Color.toFloatBits(205, 205, 205, 255), 
			   Color.toFloatBits(195, 195, 195, 255), Color.toFloatBits(200, 200, 200, 255)};
	
	/*public void drawCube(float x, float y, float z, float w, float h, float d, float px, float py, float seg) {
		float pw = w * seg;
		float ph = h * seg;
		float pd = d * seg;
		/*South*
		addVert(x + w, y, z, 			col[3], px, c * (iy[3] + 1));
		addVert(x, y, z, 				col[3], c * (ix[3] + 1), c * (iy[3] + 1));
		addVert(x, y + h, z, 			col[3], c * (ix[3] + 1), c * (iy[3]));
		addVert(x + w, y + h, z, 		col[3], px, c * (iy[3]));
		
		/*West*
		addVert(x + w, y, z, 			col[4], c * (ix[4] + 1), c * (iy[4] + 1));
		addVert(x + w, y + h, z, 		col[4], c * (ix[4] + 1), c * (iy[4]));
		addVert(x + w, y + h, z + d, 	col[4], px, c * (iy[4]));
		addVert(x + w, y, z + d, 		col[4], px, c * (iy[4] + 1));

		/*East*
		addVert(x, y + h, z, 			col[2], px, c * (iy[2]));
		addVert(x, y, z, 				col[2], px, c * (iy[2] + 1));
		addVert(x, y, z + d, 			col[2], c * (ix[2] + 1), c * (iy[2] + 1));
		addVert(x, y + h, z + d, 		col[2], c * (ix[2] + 1), c * (iy[2]));

		/*North*
		addVert(x, y, z + d,			col[1], px + ph, py);
		addVert(x + w, y, z + d, 		col[1], px + ph + pw, py);
		addVert(x + w, y + h, z + d, 	col[1], px + ph + pw, py + ph);
		addVert(x, y + h, z + d, 		col[1], px + ph, py + ph);

		/*Top*
		addVert(x + w, y + h, z + d, 	col[0], c * (ix[0] + 1), c * (iy[0] + 1));
		addVert(x + w, y + h, z, 		col[0], c * (ix[0] + 1), c * (iy[0]));
		addVert(x, y + h, z, 			col[0], px, c * (iy[0]));
		addVert(x, y + h, z + d, 		col[0], px, c * (iy[0] + 1));

		/*Bottom*
		addVert(x + w, y, z, 			col[5], px, c * (iy[5] + 1));
		addVert(x + w, y, z + d, 		col[5], px, c * (iy[5]));
		addVert(x, y, z + d,	 		col[5], c * (ix[5] + 1), c * (iy[5]));
		addVert(x, y, z, 				col[5], c * (ix[5] + 1), c * (iy[5] + 1));
	}*/
	
	public void addVert(float xi, float yi, float zi, float u, float v) {
		vec.set(xi, yi, zi);
		vec.mul(trans_matrix);
		verts[build_length + 0] = vec.x;
		verts[build_length + 1] = vec.y;
		verts[build_length + 2] = vec.z;
		verts[build_length + 3] = white;
		verts[build_length + 4] = u;
		verts[build_length + 5] = v;
		build_length += 6;
	}
	
	private float white = Color.WHITE.toFloatBits();
	Vector3 vec = new Vector3(0, 0, 0);
	
	public void addVert(float xi, float yi, float zi, float col, float u, float v) {
		vec.set(xi, yi, zi);
		vec.mul(trans_matrix);
		verts[build_length + 0] = vec.x;
		verts[build_length + 1] = vec.y;
		verts[build_length + 2] = vec.z;
		verts[build_length + 3] = col;
		verts[build_length + 4] = u;
		verts[build_length + 5] = v;
		build_length += 6;
	}
}
