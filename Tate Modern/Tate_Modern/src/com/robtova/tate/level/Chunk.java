package com.robtova.tate.level;

import com.robtova.tate.gfx.Tesselator;

public class Chunk {
	private short[][][] blocks = new short[Level.chunk_size][Level.chunk_size][Level.chunk_size];
	
	public Block getBlock(int x, int y, int z) {
		return Block.blocks[blocks[x][y][z]];
	}
	
	public void setBlock(Block block, int x, int y, int z) {
		blocks[x][y][z] = block.id;
	}
	
	public void render(Tesselator tesselator, int x, int y, int z, Level level) {
		for(int i = 0; i < Level.chunk_size; i++) {
			for(int j = 0; j < Level.chunk_size; j++) {
				for(int k = 0; k < Level.chunk_size; k++) {
					getBlock(i, j, k).render(tesselator, level, x + i, y + j, z + k);
				}
			}
		}
	}
}
