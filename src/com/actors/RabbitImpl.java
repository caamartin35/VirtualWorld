package com.actors;


import com.ai.RabbitAI;
import com.staff.interfaces.Rabbit;


public class RabbitImpl extends AbstractAnimal implements Rabbit {
	private static final int RABBIT_MAX_ENERGY = 20;
	private static final int RABBIT_VIEW_RANGE = 3;
	private static final int RABBIT_BREED_LIMIT = RABBIT_MAX_ENERGY * 3 / 4;
	private static final int RABBIT_ENERGY_VALUE = 20;
	private static final int RABBIT_COOL_DOWN = 4;
	private static final int RABBIT_INITIAL_ENERGY = RABBIT_MAX_ENERGY * 1 / 2;
	
	//empty constructor to create a rabbit with RABBIT_INITIAL_ENERGY
	public RabbitImpl() {
		this.myAI = new RabbitAI();
		this.remainingEnergy = RABBIT_INITIAL_ENERGY;
	}
	
	//constructor to create a rabbit with a specified energy
	public RabbitImpl(int energy) {
		this.myAI = new RabbitAI();
		this.remainingEnergy = energy;
	}
	
	@Override
	public int getMaxEnergy() {
		return RABBIT_MAX_ENERGY;
	}
	
	@Override
	public int getBreedLimit() {
		return RABBIT_BREED_LIMIT;
	}
	
	@Override
	public int getViewRange() {
		return RABBIT_VIEW_RANGE;
	}
	
	@Override
	public int getCoolDown() {
		return RABBIT_COOL_DOWN;
	}
	
	@Override
	public int getEnergyValue() {
		return RABBIT_ENERGY_VALUE;
	}
	
	
	
}
