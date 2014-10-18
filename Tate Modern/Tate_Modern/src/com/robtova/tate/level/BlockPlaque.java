package com.robtova.tate.level;

import com.robtova.tate.plaques.Plaque;

public class BlockPlaque extends Block {
	
	public Plaque plaque;

	public BlockPlaque(int id, int ico, Plaque plaque) {
		super(id, ico);
		this.plaque = plaque;
	}
}
