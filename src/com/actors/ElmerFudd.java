package com.actors;

import com.ai.ElmerFuddAI;
import com.staff.interfaces.World;
import com.staff.util.Location;


//Elmer fudd is my third animal. He can see as far as the world goes in the direction that
//he is facing. If he sees a rabbit or a fox in that line of sight, he automatically
//shoots it.When elmer shoots a rabbit or fox, a new elmer spawns in that rabbits/foxes location with
//ELMER_INITIAL_ENERGY
public class ElmerFudd extends AbstractAnimal{
	private static final int ELMER_COOL_DOWN = 2;
	private static final int ELMER_INITIAL_ENERGY = 100;
	
	public ElmerFudd(){
		this.myAI = new ElmerFuddAI();
		this.remainingEnergy = ELMER_INITIAL_ENERGY;
	}
	
	@Override
	public int getMaxEnergy() {
		return 0;
	}

	@Override
	public int getBreedLimit() {
		return 0;
	}

	@Override
	public int getViewRange() {
		return 0;
	}

	@Override
	public int getCoolDown() {
		return ELMER_COOL_DOWN;
	}
	
	public void shoot(World world, Location location){
		world.remove(world.getThing(location));
		world.add(new ElmerFudd(), location);
	}
	
}