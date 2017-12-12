package com.actors;


import com.ai.ElmerFuddAI;
import com.ai.FoxAI;
import com.ai.RabbitAI;
import com.staff.interfaces.Animal;
import com.staff.interfaces.Command;
import com.staff.interfaces.Edible;
import com.staff.interfaces.Fox;
import com.staff.interfaces.Rabbit;
import com.staff.interfaces.World;
import com.staff.util.Direction;
import com.staff.util.Location;

abstract class AbstractAnimal implements Animal{
	
	protected Object myAI;
	protected int remainingEnergy;
	
	@Override
	public int getEnergy() {
		return this.remainingEnergy;
	}
	
	@Override
	public void move(World world, Direction dir) {
		if (world == null) {
			throw new NullPointerException("World must not be null.");
		} else if (dir == null) {
			throw new NullPointerException("Direction must not be null.");
		}

		Location currLoc = world.getLocation(this);
		Location nextLoc = new Location(currLoc, dir);

		if (world.isValidLocation(nextLoc) && world.getThing(nextLoc) == null) {
			// then move to the new location
			world.remove(this);
			world.add(this, nextLoc);
		}
	}
	
	@Override
	public void act(World world) {
		Command c = null;
		if (this instanceof Fox){
			FoxAI foxAI = (FoxAI) myAI;
			c = foxAI.act(world, this);
		}
		else if (this instanceof Rabbit){
			RabbitAI rabbitAI = (RabbitAI) myAI;
			c = rabbitAI.act(world, this);
		}
		else if (this instanceof ElmerFudd){
			ElmerFuddAI elmerAI = (ElmerFuddAI) myAI;
			c = elmerAI.act(world,  this);
		}
		
		if (c != null) {
			c.execute(world, this);
		}
		this.remainingEnergy--;
		if (this.remainingEnergy == 0){
			world.remove(this);
		}
	}
	
	@Override
	public void eat(World world, Direction dir) {
		if (world == null) {
			throw new NullPointerException("World must not be null.");
		} else if (dir == null) {
			throw new NullPointerException("Direction must not be null.");
		}
		
		Location currLoc = world.getLocation(this);
		Location foodLoc = new Location(currLoc, dir);
		Edible edible;
		
		//if we are eating at a valid location and there is actually something where we are eating.
		if (world.isValidLocation(foodLoc) && world.getThing(foodLoc) != null){
			if (world.getThing(foodLoc) instanceof Edible){
				if (this instanceof Fox && world.getThing(foodLoc) instanceof Grass) {
					System.out.println("Foxes cannot eat grass!");
					return;
				}
				if (this instanceof Rabbit && world.getThing(foodLoc) instanceof RabbitImpl){
					System.out.println("Rabbits cannot eat rabbits!");
				}
				
				edible = (Edible) world.getThing(foodLoc);
				
				if (this.remainingEnergy + edible.getEnergyValue() > this.getMaxEnergy()){
					this.remainingEnergy = this.getMaxEnergy();
				}
				else this.remainingEnergy += edible.getEnergyValue();
					
				world.remove(world.getThing(foodLoc));
			}
			else System.out.println("Trying to eat inedible thing!");
		}
		
		//if there is nothing where we are trying to eat
		else if (world.getThing(foodLoc) == null) System.out.println("Nothing to eat at that location!");
		
		//if we are trying to eat at an invalid location
		else System.out.println("Trying to eat at invalid location!");
	}
	
	
	@Override
	public void breed(World world, Direction dir) {
		if (this.getBreedLimit() > this.remainingEnergy){
			System.out.println("Not enough energy to Breed!");
		}
		else {
			Location breedLoc = new Location(world.getLocation(this), dir);
			if (world.getThing(breedLoc) != null) {
				System.out.println("Cannot Breed on another thing!");
				return;
			}
			
			//if everything is good, let us breed
			else if (world.isValidLocation(breedLoc)){
				if (this instanceof Rabbit)
					world.add(new RabbitImpl(this.remainingEnergy/2), breedLoc);
				else if (this instanceof Fox)
					world.add(new FoxImpl(this.remainingEnergy/2), breedLoc);
				
				this.remainingEnergy = this.remainingEnergy/2;
				
			}
		}
		
	}
	
}