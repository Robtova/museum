package com.robtova.tate.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.robtova.tate.Main;
import com.robtova.tate.Maths;
import com.robtova.tate.level.Block;
import com.robtova.tate.level.BlockPlaque;
import com.robtova.tate.level.Level;

public class Player implements InputProcessor {
	private int walk_time = 0;
	public float bob;
	private boolean moving;
	public float oldx, oldy;
	public float rot = 0, roty = 0;
	public Vector3 position = new Vector3(0, 0, 0);
	public float xa, ya, za;
	private float width = 0.9f, depth = 0.9f, height = 1.7f, above_head = 0.3f;
	
	public Player() {
		oldy = Gdx.input.getY();
    	oldx = Gdx.input.getX();

    	position.y += 1 + height;
    	position.x = 4;
    	position.z = 4;
    	
    	Gdx.input.setInputProcessor(this);
	}
	
	private boolean bool1, bool2;
	private int bx1, by1, bx2, by2, p1 = -10, p2 = -10;
	
	public void tick() {
		if(Main.desktop()) moving = false;
		
		xa *= 0.6f;
		ya *= 0.6f;
		za *= 0.6f;
		
		 if(Gdx.input.isKeyPressed(Keys.W)) {
			float speed = 0.02f;
		    float z = (float) (speed * Math.cos(Math.toRadians(rot)));
		    float x = (float) (Math.sin(Math.toRadians(rot)) * speed);
		    xa += -x;
		    za += -z;
		    moving = true;
		 }
		 if(Gdx.input.isKeyPressed(Keys.S)) {
		    float speed = 0.02f;
		    float z = (float) (speed * Math.cos(Math.toRadians(rot + 180)));
		    float x = (float) (Math.sin(Math.toRadians(rot + 180)) * speed);
		    xa += -x;
		    za += -z;
		    moving = true;
		 }
		 if(Gdx.input.isKeyPressed(Keys.A)) {
		    float speed = 0.02f;
		    float z = (float) (speed * Math.cos(Math.toRadians(rot + 90)));
		    float x = (float) (Math.sin(Math.toRadians(rot + 90)) * speed);
		    xa += -x;
		    za += -z;
		    moving = true;
		 }
		 if(Gdx.input.isKeyPressed(Keys.D)) {
		    float speed = 0.02f;
		    float z = (float) (speed * Math.cos(Math.toRadians(rot - 90)));
		    float x = (float) (Math.sin(Math.toRadians(rot - 90)) * speed);
		    xa += -x;
		    za += -z;
		    moving = true;
		 }
		 
		 if(moving) {
			 walk_time += 1;
		 }
		 
		 bob = (float) Math.cos(walk_time * 0.2f) * 0.1f;

		 if(Gdx.input.isButtonPressed(0)) {
		    Gdx.input.setCursorCatched(true);
		    oldy = Gdx.input.getY();
		    oldx = Gdx.input.getX();
		 }

		 if(Main.desktop()) {
			if(oldx != Gdx.input.getX() && Gdx.input.isCursorCatched()) {
			   float x = Gdx.input.getX();
			   rot -= (x - oldx) * 0.3f;
			   oldx = x;
			}
			    
			if(oldy != Gdx.input.getY() && Gdx.input.isCursorCatched()) {
			   float y = Gdx.input.getY();
			   float h = (y - oldy);
			   h *= 0.3f;
			    
			   if(roty < 79 && h < 0) roty -= h;
			   if(roty > -79 && h > 0) roty -= h;
			   oldy = y;
			   if(roty < -79) roty = -79;
			   if(roty > 79) roty = 79;
			}
			 
			if(Gdx.input.isButtonPressed(Buttons.LEFT) && Gdx.input.isCursorCatched()) {
				float x = position.x, y = position.y, z = position.z;
				float za = 0.5f * (float) Math.cos(Math.toRadians(rot)) * (float) Math.cos(Math.toRadians(roty));
		        float xa =  0.5f * (float) (Math.sin(Math.toRadians(rot))) * (float) Math.cos(Math.toRadians(roty));
		        float ya =  0.5f * (float) Math.sin(Math.toRadians(roty));
				for(float i = 0; i < 2.5; i += 0.5) {
					x += -xa;
					y += ya;
					z += -za;
					
					Block block = Main.level.getBlock((int) x, (int) y, (int) z);
					if(block instanceof BlockPlaque) {
						Main.level.setPlaque(((BlockPlaque) block).plaque);
					}
				}
			}
		 } else {
			 if(bool1 && Gdx.input.isTouched(p1)) {
				 float x = Gdx.input.getX(p1);
				 rot -= (x - bx1) * 0.005f;
						 
				 float y = Gdx.input.getY(p1);
						
				 float h = (y - by1) * 0.005f;

				 if(roty < 79 && h < 0) roty -= h;
				 if(roty > -79 && h > 0) roty -= h;

				 if(roty < -79) roty = -79;
				 if(roty > 79) roty = 79;
			 }
				
			 if(bool2 && Gdx.input.isTouched(p2)) {
				 int h = Gdx.input.getX(p2) - bx2;
				 int g = by2 - Gdx.input.getY(p2);
					
				 float rot = (float) Maths.atanDegrees(h, g);

				 if(rot < 0) rot += 360;
					
				 rot = 360 - rot;
					
				 float speed = 0.02f;
				 float z = (float) (speed * Math.cos(Math.toRadians(this.rot + rot)));
				 float x = (float) (Math.sin(Math.toRadians(this.rot + rot)) * speed);
				 xa += -x;
				 za += -z;
				 moving = true;
			 }
		 }
		 
		 move(Main.level, xa, 0, 0);
		 move(Main.level, 0, 0, za);
	}
	
	Ray ray;
	
	public void move(Level level, float xa, float ya, float za) {
		 if(xa == 0 && ya == 0 && za == 0) return;
		 
    	 float x = position.x;
    	 float z = position.z;
    	
    	 float xd = (x + xa);
    	 float zd = (z + za);
    	
    	 float xn = xd - width * 0.5f;
    	 float zn = zd - depth * 0.5f;
    	 float xn1 = xd + width * 0.5f;
    	 float zn1 = zd + depth * 0.5f;
    	 
    	 /*float xn2 = x - width * 0.5f;
    	 float zn2 = z - depth * 0.5f;
    	 float xn3 = x + width * 0.5f;
    	 float zn3 = z + depth * 0.5f;*/

    	 boolean done = false;
    	 for(float xt = xn; xt < xn1; xt+=0.2f) {
    		 for(float yt = 1; yt < 3; yt+=0.2f) {
    			 for(float zt = zn; zt < zn1; zt+=0.2f) {
    				 //if(xt > xn2 && zt > zn2 && xt < xn3 && zt < zn3) continue;
    				 int xt2 = (int) xt, zt2 = (int) zt, yt2 = (int) yt;
    				 if(xt < 0) xt2--;
    				 if(zt < 0) zt2--;
    				 if(yt < 0) yt2--;
    				 Block block = level.getBlock(xt2, yt2, zt2);
    				 if(!block.canPass() && block.intersects(xt - xt2, zt - zt2, xt2, yt2, zt2)) {
    					 done = true;
    					 return;
    				 } 
    			 }
    		 }
    	 }
    	 if(done) return;
    	 position.y += ya;
    	 position.x += xa;
    	 position.z += za;
    }
	
	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if(Main.desktop()) return false;
		
		if(!bool1 && !bool2) {
			float x = position.x, y = position.y, z = position.z;
			float za = 0.5f * (float) Math.cos(Math.toRadians(rot)) * (float) Math.cos(Math.toRadians(roty));
	        float xa =  0.5f * (float) (Math.sin(Math.toRadians(rot))) * (float) Math.cos(Math.toRadians(roty));
	        float ya =  0.5f * (float) Math.sin(Math.toRadians(roty));
			for(float i = 0; i < 2.5; i += 0.5) {
				x += -xa;
				y += ya;
				z += -za;
				
				Block block = Main.level.getBlock((int) x, (int) y, (int) z);
				if(block instanceof BlockPlaque) {
					Main.level.setPlaque(((BlockPlaque) block).plaque);
				}
			}
		}
		
		if(!bool1 && screenX >= Main.WIDTH / 2) {
			bool1 = true;
			bx1 = screenX;
			by1 = screenY;
			p1 = pointer;
		} else if(bool1 && pointer == p1) {
			float x = screenX;
			rot -= (x - bx1) * 0.005f;
				 
			float y = screenY;
				
			float h = (y - by1) * 0.005f;

			if(roty < 79 && h < 0) roty -= h;
			if(roty > -79 && h > 0) roty -= h;

			if(roty < -79) roty = -79;
			if(roty > 79) roty = 79;
		}
		
		if(!bool2 && screenX < Main.WIDTH / 2) {
			bool2 = true;
			bx2 = screenX;
			by2 = screenY;
			p2 = pointer;
		} else if(bool2 && pointer == p2) {
			int h = screenX - bx2;
			int g = by2 - screenY;
			
			float rot = (float) Maths.atanDegrees(h, g);

			if(rot < 0) rot += 360;
			
			rot = 360 - rot;
			
			float speed = 0.02f;
		    float z = (float) (speed * Math.cos(Math.toRadians(this.rot + rot)));
		    float x = (float) (Math.sin(Math.toRadians(this.rot + rot)) * speed);
		    xa += -x;
		    za += -z;
		    moving = true;
		}
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(Main.desktop()) return false;
		if(pointer == p1) {
			bool1 = false;
			p1 = -10;
		}
		if(pointer == p2) {
			bool2 = false;
			moving = false;
			p2 = -10;
		}
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if(Main.desktop()) return false;
		if(bool1 && pointer == p1) {
			float x = screenX;
			rot -= (x - bx1) * 0.005f;
				 
			float y = screenY;
				
			float h = (y - by1) * 0.005f;

			if(roty < 79 && h < 0) roty -= h;
			if(roty > -79 && h > 0) roty -= h;

			if(roty < -79) roty = -79;
			if(roty > 79) roty = 79;
		}
		
		if(bool2 && pointer == p2) {
			int h = screenX - bx2;
			int g = by2 - screenY;
			
			float rot = (float) Maths.atanDegrees(h, g);

			if(rot < 0) rot += 360;
			
			rot = 360 - rot;
			
			float speed = 0.02f;
		    float z = (float) (speed * Math.cos(Math.toRadians(this.rot + rot)));
		    float x = (float) (Math.sin(Math.toRadians(this.rot + rot)) * speed);
		    xa += -x;
		    za += -z;
		    moving = true;
		}
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
