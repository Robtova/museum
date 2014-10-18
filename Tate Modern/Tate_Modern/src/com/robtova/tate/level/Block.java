package com.robtova.tate.level;

import com.badlogic.gdx.graphics.Color;
import com.robtova.tate.gfx.Tesselator;
import com.robtova.tate.plaques.*;

public class Block {

	public static Block[] blocks = new Block[256];
	public static final int NORTH = 1, SOUTH = 3, EAST = 2, WEST = 4, TOP = 0, BOTTOM = 5;

	public static Block air = new Block(0, -1);
	public static Block floor = new Block(1, 0).setCanPass(false);
	public static Block wall = new Block(2, 1).setCanPass(false);
	public static Block GBCoCSD = new Block(3, 3).setCanPass(false);
	public static Block GBCoCSD_P = new BlockPlaque(4, 6, new PlaqueChurch()).setCanPass(false);
	public static Block FE = new Block(5, 4).setCanPass(false).setIcon(1, 4);
	public static Block Coffee_Mill = new Block(6, 7).setCanPass(false);
	public static Block Coffee_Mill_P = new BlockPlaque(7, 8, new PlaqueCoffeeMill()).setCanPass(false);
	public static Block Spiral_Movement = new Block(8, 9).setCanPass(false);
	public static Block Spiral_Movement_P = new BlockPlaque(9, 10, new PlaqueSpiralMovement()).setCanPass(false);
	
	public static Block SLwM_0 = new Block(10, 11).setCanPass(false);
	public static Block SLwM_1 = new Block(11, 12).setCanPass(false);
	public static Block SLwM_2 = new Block(12, 13).setCanPass(false);
	public static Block SLwM_3 = new Block(13, 14).setCanPass(false);
	public static Block SLwM_4 = new Block(14, 15).setCanPass(false);
	public static Block SLwM_5 = new Block(15, 16).setCanPass(false);
	
	public static Block SLwM_P = new BlockPlaque(16, 17, new PlaqueSpanishHills()).setCanPass(false);
	public static Block light = new Block(17, 5).setCanPass(false);
	
	public static Block IA_0 = new Block(18, 18).setCanPass(false);
	public static Block IA_1 = new Block(19, 19).setCanPass(false);
	public static Block IA_2 = new Block(20, 20).setCanPass(false);
	public static Block IA_3 = new Block(21, 26).setCanPass(false);
	public static Block IA_4 = new Block(22, 27).setCanPass(false);
	public static Block IA_5 = new Block(23, 28).setCanPass(false);
	public static Block IA_6 = new Block(24, 34).setCanPass(false);
	public static Block IA_7 = new Block(25, 35).setCanPass(false);
	public static Block IA_8 = new Block(26, 36).setCanPass(false);
	public static Block IA_P = new BlockPlaque(27, 21, new PlaqueInland_A()).setCanPass(false);
	
	public static Block QC_0 = new Block(28, 22).setCanPass(false).setIcon(22, 1, EAST);
	public static Block QC_1 = new Block(29, 23).setCanPass(false).setIcon(23, 1, EAST);
	public static Block QC_2 = new Block(30, 30).setCanPass(false).setIcon(30, 1, EAST);
	public static Block QC_3 = new Block(31, 31).setCanPass(false).setIcon(31, 1, EAST);
	public static Block QC_4 = new Block(32, 38).setCanPass(false).setIcon(38, 1, EAST);
	public static Block QC_5 = new Block(33, 39).setCanPass(false).setIcon(39, 1, EAST);
	public static Block QC_P = new BlockPlaque(34, 24, new PlaqueQC()).setCanPass(false).setIcon(24, 1, EAST);
	
	public static Block C_0 = new Block(35, 42).setCanPass(false).setIcon(42, 1, EAST);
	public static Block C_1 = new Block(36, 43).setCanPass(false).setIcon(43, 1, EAST);
	public static Block C_2 = new Block(37, 44).setCanPass(false).setIcon(44, 1, EAST);
	public static Block C_3 = new Block(38, 50).setCanPass(false).setIcon(50, 1, EAST);
	public static Block C_4 = new Block(39, 51).setCanPass(false).setIcon(51, 1, EAST);
	public static Block C_5 = new Block(40, 52).setCanPass(false).setIcon(52, 1, EAST);
	public static Block C_6 = new Block(41, 58).setCanPass(false).setIcon(58, 1, EAST);
	public static Block C_7 = new Block(42, 59).setCanPass(false).setIcon(59, 1, EAST);
	public static Block C_8 = new Block(43, 60).setCanPass(false).setIcon(60, 1, EAST);
	public static Block C_P = new BlockPlaque(44, 25, new PlaqueC()).setCanPass(false).setIcon(25, 1, EAST);
	
	public short id;
	public int[] icon, ix, iy;
	
	public static float size = 1f;
	private float c = 0.125f;
	private int a = 8;

	private float[] inset = {0f, 0f, 1f, 1f};
	
	private boolean can_pass = true; 
	
	float[] col = {Color.toFloatBits(255, 255, 255, 255), Color.toFloatBits(205, 205, 205, 255), 
				   Color.toFloatBits(195, 195, 195, 255), Color.toFloatBits(205, 205, 205, 255), 
				   Color.toFloatBits(195, 195, 195, 255), Color.toFloatBits(255, 255, 255, 255)};
	
	public Block(int id, int ico) {
		this.id = (short) id;
		if(blocks[id] != null) throw new RuntimeException("Duplicate block ids.");
		blocks[id] = this;
		setIcon(ico);
	}
	
	public Block setIcon(int ico) {
		this.icon = new int[] {ico, ico, ico, ico, ico, ico};
		iy = new int[] {icon[0] / a, icon[1] / a, icon[2] / a, icon[3] / a, icon[4] / a, icon[5] / a};
		ix = new int[] {icon[0] - (iy[0] * a), icon[1] - (iy[1] * a), icon[2] - (iy[2] * a), icon[3] - (iy[3] * a), icon[4] - (iy[4] * a), icon[5] - (iy[5] * a)};
		return this;
	}
	
	public Block setIcon(int ico, int ico2) {
		this.icon = new int[] {ico, ico2, ico2, ico2, ico2, ico};
		iy = new int[] {icon[0] / a, icon[1] / a, icon[2] / a, icon[3] / a, icon[4] / a, icon[5] / a};
		ix = new int[] {icon[0] - (iy[0] * a), icon[1] - (iy[1] * a), icon[2] - (iy[2] * a), icon[3] - (iy[3] * a), icon[4] - (iy[4] * a), icon[5] - (iy[5] * a)};
		return this;
	}
	
	public Block setIcon(int ico, int ico2, int b) {
		this.icon = new int[] {ico2, ico2, ico2, ico2, ico2, ico2};
		this.icon[b] = ico;
		iy = new int[] {icon[0] / a, icon[1] / a, icon[2] / a, icon[3] / a, icon[4] / a, icon[5] / a};
		ix = new int[] {icon[0] - (iy[0] * a), icon[1] - (iy[1] * a), icon[2] - (iy[2] * a), icon[3] - (iy[3] * a), icon[4] - (iy[4] * a), icon[5] - (iy[5] * a)};
		return this;
	}
	
	public Block setIcon(int ico, int ico2, int ico3, int ico4, int ico5, int ico6) {
		this.icon = new int[] {ico, ico2, ico3, ico4, ico5, ico6};
		iy = new int[] {icon[0] / a, icon[1] / a, icon[2] / a, icon[3] / a, icon[4] / a, icon[5] / a};
		ix = new int[] {icon[0] - (iy[0] * a), icon[1] - (iy[1] * a), icon[2] - (iy[2] * a), icon[3] - (iy[3] * a), icon[4] - (iy[4] * a), icon[5] - (iy[5] * a)};
		return this;
	}
	
	public boolean canPass() {
		return can_pass;
	}
	
	public Block setCanPass(boolean can_pass) {
		this.can_pass = can_pass;
		return this;
	}
	
	public float getInset(int i, int x, int y, int z) {
		return getInsets(x, y, z)[i];
	}
	
	public float[] getInsets(int i, int j, int k) {
		return inset;
	}
	
	public boolean intersects(float x, float z, int i, int j, int k) {
		float[] inset = getInsets(i, j, k);
		return x > inset[0] && x < inset[2] && z > inset[1] && z < inset[3];
	}
	
	public void render(Tesselator tess, Level level, int x, int y, int z) {
		if(icon[0] < 0) return;

		boolean xm = level.getBlock(x - 1, y, z) == Block.air, xp = level.getBlock(x + 1, y, z) == Block.air;
		boolean ym = level.getBlock(x, y - 1, z) == Block.air, yp = level.getBlock(x, y + 1, z) == Block.air;
		boolean zm = level.getBlock(x, y, z - 1) == Block.air, zp = level.getBlock(x, y, z + 1) == Block.air;

		if(zm && level.player.position.z < z) {
			/*South*/
			tess.addVert(x * size + size, y * size, z * size, col[3], c * (ix[3]), c * (iy[3] + 1));
			tess.addVert(x * size, y * size, z * size, col[3], c * (ix[3] + 1), c * (iy[3] + 1));
			tess.addVert(x * size, y * size + size, z * size, col[3], c * (ix[3] + 1), c * (iy[3]));
			tess.addVert(x * size + size, y * size + size, z * size, col[3], c * (ix[3]), c * (iy[3]));
		}
			
		if(xp && level.player.position.x > x + 1) {
			/*West*/
			tess.addVert(x * size + size, y * size, z * size, col[4], c * (ix[4] + 1), c * (iy[4] + 1));
			tess.addVert(x * size + size, y * size + size, z * size, col[4], c * (ix[4] + 1), c * (iy[4]));
			tess.addVert(x * size + size, y * size + size, z * size + size, col[4], c * (ix[4]), c * (iy[4]));
			tess.addVert(x * size + size, y * size, z * size + size, col[4], c * (ix[4]), c * (iy[4] + 1));
		}
		
		if(xm && level.player.position.x < x) {
			/*East*/
			tess.addVert(x * size, y * size + size, z * size, col[2], c * (ix[2]), c * (iy[2]));
			tess.addVert(x * size, y * size, z * size, col[2], c * (ix[2]), c * (iy[2] + 1));
			tess.addVert(x * size, y * size, z * size + size, col[2], c * (ix[2] + 1), c * (iy[2] + 1));
			tess.addVert(x * size, y * size + size, z * size + size, col[2], c * (ix[2] + 1), c * (iy[2]));
		}
		
		if(zp && level.player.position.z > z + 1) {
			/*North*/
			tess.addVert(x * size, y * size, z * size + size, col[1], c * (ix[1]), c * (iy[1] + 1));
			tess.addVert(x * size + size, y * size, z * size + size, col[1], c * (ix[1] + 1), c * (iy[1] + 1));
			tess.addVert(x * size + size, y * size + size, z * size + size, col[1], c * (ix[1] + 1), c * (iy[1]));
			tess.addVert(x * size, y * size + size, z * size + size, col[1], c * (ix[1]), c * (iy[1]));
		}
		
		if(yp && level.player.position.y > y + 1) {
			/*Top*/
			tess.addVert(x * size + size, y * size + size, z * size + size, col[0], c * (ix[0] + 1), c * (iy[0] + 1));
			tess.addVert(x * size + size, y * size + size, z * size, col[0], c * (ix[0] + 1), c * (iy[0]));
			tess.addVert(x * size, y * size + size, z * size, col[0], c * (ix[0]), c * (iy[0]));
			tess.addVert(x * size, y * size + size, z * size + size, col[0], c * (ix[0]), c * (iy[0] + 1));
		}
		
		if(ym && level.player.position.y < y) {
			/*Bottom*/
			tess.addVert(x * size + size, y * size, z * size, col[5], c * (ix[5]), c * (iy[5] + 1));
			tess.addVert(x * size + size, y * size, z * size + size, col[5], c * (ix[5]), c * (iy[5]));
			tess.addVert(x * size, y * size, z * size + size, col[5], c * (ix[5] + 1), c * (iy[5]));
			tess.addVert(x * size, y * size, z * size, col[5], c * (ix[5] + 1), c * (iy[5] + 1));
		}
	}
}
