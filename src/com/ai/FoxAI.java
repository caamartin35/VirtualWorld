package com.ai;

import java.util.ArrayList;

import com.commands.BreedCommand;
import com.commands.EatCommand;
import com.staff.interfaces.Actor;
import com.staff.interfaces.Command;
import com.staff.interfaces.Fox;
import com.staff.util.Direction;
import com.ai.ThingSeen.Type;
import com.commands.MoveCommand;
import com.staff.interfaces.World;
import com.staff.util.Location;

public class FoxAI extends AbstractAI{

	private static final double DISTANCE_THRESHOLD = 7;
	private boolean leftTaken;
	
	public FoxAI(){
		this.facing = Direction.NORTH;
		leftTaken = false;
	}
	
	@Override
	public Command act(World world, Actor actor) {
		Fox fox = (Fox) actor;
		ArrayList<ThingSeen> thingsSeen;
		Command ret;
		
		//initialize globals
		this.init();
		
		//get a list of all things currently seen by this rabbit
		thingsSeen = getThingsInSight(world, fox);
		
		//if nothing is seen, move randomly
		if (thingsSeen == null){
			return randomMove(world, fox);
		}
		
		//get the optimal action for the rabbit at this time
		ret = getOptimalCommand(world, fox, thingsSeen);
		
		return ret;
	}
	
	//returns the direction of the optimal space that the fox should act on (whether it breeds,
	//eats, or moves) which is based on the rabbits that the fox sees.
	private Command getOptimalCommand(World world, Fox fox, ArrayList<ThingSeen> thingsSeen){
		ThingSeen closestRabbit; 
		Direction[] eatable, breedable, moveable;
		Direction desired;
		Location newLoc;
		Command ret = null;
		Direction retDir = null;
		int dir;
		closestRabbit = getClosestThing(thingsSeen, Type.RABBIT);
		
		//try to breed
		if (canBreed(world, fox)){
			breedable = findMoveableSpaces(world, fox);
			if (breedable != null){
				dir = (int) (Math.random()*breedable.length);
				retDir = breedable[dir];
				ret =  new BreedCommand(breedable[dir]);
			}
			else {
				return  randomMove(world, fox);
			}
		}
		
		//if the fox sees a rabbit
		else if (closestRabbit != null){
			//here is where the fox will go after the rabbit
			if (closestRabbit.getDistanceFromActor() < DISTANCE_THRESHOLD){
				
				//if we can eat, let us eat
				if (canEat(world, fox)){
					eatable = findEatableSpaces(world, fox);
					dir = (int) (Math.random()*eatable.length);
					retDir = eatable[dir];
					ret =  new EatCommand(eatable[dir]);
				}
				
				//if we cant eat yet, move toward the rabbit if we can.
				else if (canMove(world, fox)){
					
					moveable = findMoveableSpaces(world, fox);
					if (moveable != null && moveable.length <= 2)
						return getOutOfMaze(world, fox);
					else{
						desired = findDesiredDirection(closestRabbit.getxDiff(), closestRabbit.getyDiff());
						if (desired == null) return randomMove(world, fox);
						newLoc = new Location(world.getLocation(fox), desired);
						if (!world.isValidLocation(newLoc) || world.getThing(newLoc) != null){
							return  randomMove(world, fox);
						}
						else{
							retDir = desired;
							ret = new MoveCommand(desired);
						}
					}
					
				}
			}
			else {
				moveable = findMoveableSpaces(world, fox);
				if (moveable != null && moveable.length == 4){
					return new MoveCommand(this.facing);
				}
				else
					return getOutOfMaze(world, fox);
			}
		}
		
		//if the fox does not see a rabbit
		else {
			moveable = findMoveableSpaces(world, fox);
			if (moveable != null && moveable.length == 4){
				return new MoveCommand(this.facing);
			}
			else
				return getOutOfMaze(world, fox);
		}
		
		//if we didnt do a randomMove, we set the facing to the direction we are moving in,
		//and return our command.
		if (retDir != null)
			this.facing = retDir;
		
		return ret;
	}
	
	//we use this function when a fox is inside a maze of grass.
	//this works much better than just moving randomly. we essentially
	//follow the left wall.
	private Command getOutOfMaze(World world, Fox fox){
		turnLeft();
		Location newLoc = new Location(world.getLocation(fox), this.facing);
		
		if (world.isValidLocation(newLoc) && world.getThing(newLoc) == null && !leftTaken){
			leftTaken = true;
			return new MoveCommand(this.facing);
		}
		
		else {
			leftTaken = false;
			turnRight();
			newLoc = new Location(world.getLocation(fox), this.facing);
			if (world.isValidLocation(newLoc) && world.getThing(newLoc) == null){
				return new MoveCommand(this.facing);
			}
			else {
				turnRight();
				newLoc = new Location(world.getLocation(fox), this.facing);
				if (world.isValidLocation(newLoc) && world.getThing(newLoc) == null){
					return new MoveCommand(this.facing);
				}
				else{
					turnRight();
					return new MoveCommand(this.facing);
				}
			}
			
		}
	}
	
	//changes facing so that it is as if the animal turned right.
	private void turnRight(){
		switch (this.facing){
		case NORTH:
			this.facing = Direction.EAST;
			break;
		case EAST:
			this.facing = Direction.SOUTH;
			break;
		case SOUTH:
			this.facing = Direction.WEST;
			break;
		case WEST:
			this.facing = Direction.NORTH;
			break;
		}
	}
	
	//changes facing so that it is as if the animal turned right.
	private void turnLeft(){
		switch (this.facing){
		case NORTH:
			this.facing = Direction.WEST;
			break;
		case EAST:
			this.facing = Direction.NORTH;
			break;
		case SOUTH:
			this.facing = Direction.EAST;
			break;
		case WEST:
			this.facing = Direction.SOUTH;
			break;
		}
	}
}