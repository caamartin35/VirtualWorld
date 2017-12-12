package com.ai;

import java.util.ArrayList;

import com.commands.BreedCommand;
import com.commands.EatCommand;
import com.commands.MoveCommand;
import com.staff.interfaces.Actor;
import com.staff.interfaces.Command;
import com.staff.util.Direction;
import com.staff.util.Location;
import com.actors.Grass;
import com.ai.ThingSeen.Type;
import com.staff.interfaces.Rabbit;
import com.staff.interfaces.World;

public class RabbitAI extends AbstractAI{
	
	@Override
	public Command act(World world, Actor actor) {
		Rabbit rabbit = (Rabbit) actor;
		ArrayList<ThingSeen> thingsSeen;
		
		//initialize globals
		this.init();
		
		//get a list of all things currently seen by this rabbit
		thingsSeen = getThingsInSight(world, rabbit);
		
		//if nothing is seen, move randomly
		if (thingsSeen == null){
			return randomMove(world, rabbit);
		}
		
		//get optimal move if we were to move
		return getOptimalCommand(world, rabbit, thingsSeen);
		
		
	}
	
	//returns the direction of the optimal space that the rabbit should act on (whether it breeds,
	//eats, or moves) which is based on 
	private Command getOptimalCommand(World world, Rabbit rabbit, ArrayList<ThingSeen> thingsSeen){
		double[] foxCenterOfMass;
		double[] desiredDirection = new double[2];
		int[] desiredMove = new int[2];
		foxCenterOfMass = calculateFoxCenterOfMass(world, thingsSeen);
		Direction[] breedable, eatable;
		int dir;
		Command ret;
		Direction retDir;
		Direction desiredDir;
		Location desired;
		ThingSeen closestGrass;
		
		//rabbit can see no foxes
		if (foxCenterOfMass == null){
			//see if we should breed
			if (canBreed(world, rabbit)){
				breedable = findMoveableSpaces(world, rabbit);
				if (breedable != null){
					dir = (int) (Math.random()* breedable.length);
					retDir = breedable[dir];
					ret = new BreedCommand(breedable[dir]);
				}
				else if (canEat(world, rabbit)){
					eatable = findEatableSpaces(world, rabbit);
					dir = (int) (Math.random()* eatable.length);
					retDir = eatable[dir];
					ret = new EatCommand(eatable[dir]);
				}
				else{
					return randomMove(world, rabbit);
				}
			}
			
			//if we dont want to breed
			else if (canEat(world, rabbit)){
				eatable = findEatableSpaces(world, rabbit);
				dir = (int) (Math.random()* eatable.length);
				retDir = eatable[dir];
				ret = new EatCommand(eatable[dir]);
			}
			
			else if ((closestGrass = getClosestThing(thingsSeen, Type.GRASS)) != null){
				desiredDir = findDesiredDirection(closestGrass.getxDiff(), closestGrass.getyDiff());
				desired = new Location(world.getLocation(rabbit), desiredDir);
				if (world.isValidLocation(desired) && world.getThing(desired) != null){
					return randomMove(world, rabbit);
				}
				else{
					retDir = desiredDir;
					ret = new MoveCommand(desiredDir);
				}
			}
			else {
				return randomMove(world, rabbit);
			}
		}
		
		//rabbit can see a fox, move away from its center of mass
		else {
			desiredDirection[0] = -foxCenterOfMass[0];
			desiredDirection[1] = -foxCenterOfMass[1];
			desiredMove[0] = Math.round(10*(long)desiredDirection[0]);
			desiredMove[1] = Math.round(10*(long)desiredDirection[1]);
			
			desiredDir = findDesiredDirection(desiredMove[0], desiredMove[1]);
			
			if (desiredDir == null){
				return randomMove(world, rabbit);
			}
			
			desired = new Location(world.getLocation(rabbit), desiredDir);
			
			if (world.isValidLocation(desired)){
				if (world.getThing(desired) ==  null){
					retDir = desiredDir;
					ret = new MoveCommand(desiredDir);
				}
				else if (world.getThing(desired) instanceof Grass){
					retDir = desiredDir;
					ret = new EatCommand(desiredDir);
				}
				else
					return randomMove(world, rabbit);
			}
			else
				return randomMove(world, rabbit);
		}
		
		this.facing = retDir;
		return ret;
	}
	
}