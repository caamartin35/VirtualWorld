package com.actors;

import com.ai.GnatAI;
import com.staff.interfaces.Animal;
import com.staff.interfaces.Command;
import com.staff.interfaces.World;
import com.staff.util.Direction;
import com.staff.util.Location;

/**
 * This is a simple implementation of a Gnat. It never loses energy and moves in
 * random directions.
 */
public class Gnat implements Animal {

	private static final int MAX_ENERGY = 10;
	private static final int VIEW_RANGE = 1;
	private static final int BREED_LIMIT = 0;
	private static final int COOL_DOWN = 0;

	private GnatAI myAI;
	private int remainingEnergy;

	public Gnat(int energy) {
		this.myAI = new GnatAI();
		this.remainingEnergy = energy;
	}

	@Override
	public void act(World world) {
		Command c = myAI.act(world, this);
		if (c != null) {
			c.execute(world, this);
		}
	}

	@Override
	public int getViewRange() {
		return VIEW_RANGE;
	}

	@Override
	public int getEnergy() {
		return remainingEnergy;
	}

	@Override
	public int getMaxEnergy() {
		return MAX_ENERGY;
	}

	@Override
	public int getBreedLimit() {
		return BREED_LIMIT;
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
	public int getCoolDown() {
		return COOL_DOWN;
	}

	@Override
	public void breed(World w, Direction d) {
	}

	@Override
	public void eat(World w, Direction d) {
	}
}
