package com.actors;

import com.ai.FoxAI;
import com.staff.interfaces.Fox;

public class FoxImpl extends AbstractAnimal implements Fox {
	private static final int FOX_MAX_ENERGY = 160;
	private static final int FOX_VIEW_RANGE = 5;
	private static final int FOX_BREED_LIMIT = FOX_MAX_ENERGY * 3 / 4;
	private static final int FOX_COOL_DOWN = 2;
	private static final int FOX_INITIAL_ENERGY = FOX_MAX_ENERGY * 1 / 2;
	
	//Empty constructor to create a Fox with FOX_INITIAL_ENERGY
	public FoxImpl() {
		this.myAI = new FoxAI();
		this.remainingEnergy = FOX_INITIAL_ENERGY;
	}
	
	//Constructor to create a Fox with specified energy
	public FoxImpl(int energy) {
		this.myAI = new FoxAI();
		this.remainingEnergy = energy;
	}
	
	@Override
	public int getMaxEnergy() {
		return FOX_MAX_ENERGY;
	}
	
	@Override
	public int getBreedLimit() {
		return FOX_BREED_LIMIT;
	}
	
	
	@Override
	public int getViewRange() {
		return FOX_VIEW_RANGE;
	}
	
	@Override
	public int getCoolDown() {
		return FOX_COOL_DOWN;
	}
	
	
	
}
