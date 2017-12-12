package com.ai;

import java.util.ArrayList;

import com.actors.Gardener;
import com.staff.interfaces.AI;
import com.staff.interfaces.Fox;
import com.staff.util.Direction;
import com.actors.Gnat;
import com.actors.Grass;
import com.ai.ThingSeen.Type;
import com.commands.MoveCommand;
import com.staff.interfaces.Animal;
import com.staff.interfaces.Rabbit;
import com.staff.interfaces.World;
import com.staff.util.Location;

abstract class AbstractAI implements AI {
	protected static final int NUM_DIRECTIONS = 4;
	protected int numRabbits, numFoxes, numGrass;
	protected Direction facing;
	
	//returns a list of things seen in the animal's view range
	protected ArrayList<ThingSeen> getThingsInSight(World world, Animal animal){
		ArrayList<ThingSeen> thingsSeen = new ArrayList<ThingSeen>();
		Location animalLoc = world.getLocation(animal);
		Location curLoc;
		ThingSeen curThingSeen;
		Type curType = null;
		
		//iterate through all of the spaces in animal's viewRange
		for (int i= -animal.getViewRange(); i<=animal.getViewRange(); i++){
			for (int j= -animal.getViewRange(); j<=animal.getViewRange(); j++){
				
				//if both iterators are 0, that is the current rabbit, so do nothing
				if (i == 0 && j == 0) continue;

				curLoc = new Location(animalLoc.getX() + i, animalLoc.getY() + j);
				if (world.isValidLocation(curLoc) && world.getThing(curLoc) != null){
					if (world.getThing(curLoc) instanceof Rabbit){
						curType = Type.RABBIT;
						this.numRabbits++;
					}
					else if (world.getThing(curLoc) instanceof Fox){
						curType = Type.FOX;
						this.numFoxes++;
					}
					else if (world.getThing(curLoc) instanceof Grass){
						curType = Type.GRASS;
						this.numGrass++;
					}
					else if (world.getThing(curLoc) instanceof Gardener)
						curType = Type.GARDENER;
					else if (world.getThing(curLoc) instanceof Gnat)
						curType = Type.GNAT;
					
					curThingSeen = new ThingSeen(world, animal, curLoc, curType);
					thingsSeen.add(curThingSeen);
				}
			}
		}
		/*for (int i=0; i< thingsSeen.size(); i++){
			System.out.println("Things Seen: " + thingsSeen.get(i).getType() + " Xdiff: "
					+ thingsSeen.get(i).getxDiff() + " Ydiff: " + thingsSeen.get(i).getyDiff() 
					+ " Distance: " + thingsSeen.get(i).getDistanceFromActor());
		}*/
		return thingsSeen;
	}
	
	//returns the average distance for thing of type type seen, returns -1 if nothing of 
	//type type is found.
	protected double getAvgThingDistance(ArrayList<ThingSeen> thingsSeen, Type type){
		int i=0;
		double sum;
		sum=0;
		boolean typeFound = false;
		
		while (i<thingsSeen.size()){
			if (thingsSeen.get(i).getType() == type){
				sum += thingsSeen.get(i).getDistanceFromActor();
				typeFound = true;
			}
			
			i++;
		}
		if (typeFound)
			return sum/thingsSeen.size();
		else
			return -1;
	}
	
	//returns distance of closest thing of type type. if nothing is found of type type,
	//return -1
	protected ThingSeen getClosestThing(ArrayList<ThingSeen> thingsSeen, Type type){
		int i=1;
		
		if (thingsSeen.size() == 0) return null;
		boolean typeFound = false;
		
		ThingSeen curThing = thingsSeen.get(0);
		double minDistance = thingsSeen.get(0).getDistanceFromActor();
		
		while (i<thingsSeen.size()){
			if (thingsSeen.get(i).getType() == type){
				typeFound = true;
				if (thingsSeen.get(i).getDistanceFromActor() < minDistance){
					minDistance = thingsSeen.get(i).getDistanceFromActor();
					curThing = thingsSeen.get(i);
				}
			}
			i++;
		}
		if (typeFound)
			return curThing;
		else
			return null;
	}
	
	//initializes the globals each time act is run.
	protected void init(){
		this.numFoxes = 0;
		this.numGrass = 0;
		this.numRabbits = 0;
	}
	
	//returns true if the animal can breed at all, false if not.
	protected boolean canBreed(World world, Animal animal){
		
		if (animal.getEnergy() < animal.getBreedLimit()) return false;
		
		return canMove(world, animal);
	}
	
	//returns true if the animal is able to eat, false if it cannot eat
	protected boolean canEat(World world, Animal animal){
		Location curLoc = world.getLocation(animal);
		Location dirLoc = null;
		boolean isPossible = false;
		
		if (animal.getEnergy() == animal.getMaxEnergy()) return false;
		
		for (int i=0; i<NUM_DIRECTIONS; i++){
			switch (i){
				case 0:
					dirLoc = new Location(curLoc, Direction.NORTH);
					break;
				case 1:
					dirLoc = new Location(curLoc, Direction.WEST);
					break;
				case 2:
					dirLoc = new Location(curLoc, Direction.SOUTH);
					break;
				case 3:
					dirLoc = new Location(curLoc, Direction.EAST);
					break;
			}
			if (world.isValidLocation(dirLoc)){
				if (animal instanceof Rabbit && world.getThing(dirLoc) instanceof Grass) isPossible = true;
				else if (animal instanceof Fox && world.getThing(dirLoc) instanceof Rabbit) isPossible = true;
			}
		}
		
		return isPossible;
	}
	
	//returns true of the animal can move at all, false if it cannot.
	protected boolean canMove(World world, Animal animal){
		Location curLoc = world.getLocation(animal);
		Location dirLoc = null;
		boolean isPossible = false;
		
		for (int i=0; i<NUM_DIRECTIONS; i++){
			switch (i){
				case 0:
					dirLoc = new Location(curLoc, Direction.NORTH);
					break;
				case 1:
					dirLoc = new Location(curLoc, Direction.WEST);
					break;
				case 2:
					dirLoc = new Location(curLoc, Direction.SOUTH);
					break;
				case 3:
					dirLoc = new Location(curLoc, Direction.EAST);
					break;
			}
			if (world.isValidLocation(dirLoc) && world.getThing(dirLoc) == null) isPossible = true;
		}
		
		return isPossible;
	}
	
	//calculates the center of mass of all the foxes in the animals view range relative to
	//the rabbit. first element in array is the difference in the x direction, the second
	//element is the difference in the y direction.
	protected double[] calculateFoxCenterOfMass(World world, ArrayList<ThingSeen> thingsSeen){
		double[] ret = new double[2];
		double xAvg, yAvg;
		double xSum = 0;
		double ySum = 0;
		int i=0;
		boolean foxSeen = false;
		
		while (i < thingsSeen.size()){
			if (thingsSeen.get(i).getType() == Type.FOX){
				xSum += thingsSeen.get(i).getxDiff();
				ySum += thingsSeen.get(i).getyDiff();
				foxSeen = true;
			}
			
			i++;
		}
		
		xAvg = xSum/thingsSeen.size();
		yAvg = ySum/thingsSeen.size();
		ret[0] = xAvg;
		ret[1] = yAvg;
		
		if (foxSeen)
			return ret;
		else
			return null;
	}
	
	//finds all moveable/breedable spaces next to animal
	protected Direction[] findMoveableSpaces(World world, Animal animal){
		Location loc = world.getLocation(animal);
		Location dirLoc = null;
		ArrayList<Direction> directions = new ArrayList<Direction>();
		Direction[] allDirections = Direction.values();
		Direction[] ret;
		
		for (int i=0; i<NUM_DIRECTIONS; i++){
			dirLoc = new Location(loc, allDirections[i]);
			if (world.isValidLocation(dirLoc) && world.getThing(dirLoc) == null){
				directions.add(allDirections[i]);
			}
		}
		
		if (directions.size() == 0)
			return null;
		
		ret = new Direction[directions.size()];
		
		for (int i=0; i<directions.size(); i++){
			ret[i] = directions.get(i);
		}
		
		return ret;
	}
	
	
	//finds all eatable spaces next to animal
	protected Direction[] findEatableSpaces(World world, Animal animal){
		Location loc = world.getLocation(animal);
		Location dirLoc = null;
		ArrayList<Direction> directions = new ArrayList<Direction>();
		Direction[] allDirections = Direction.values();
		Direction[] ret;
		
		for (int i=0; i<NUM_DIRECTIONS; i++){
			dirLoc = new Location(loc, allDirections[i]);
			if (world.isValidLocation(dirLoc)){
				if ((animal instanceof Fox && world.getThing(dirLoc) instanceof Rabbit) ||
						(animal instanceof Rabbit && world. getThing(dirLoc) instanceof Grass)){
					directions.add(allDirections[i]);
				}
			}
		}
		
		if (directions.size() == 0)
			return null;
		
		ret = new Direction[directions.size()];
		
		for (int i=0; i<directions.size(); i++){
			ret[i] = directions.get(i);
		}
		
		return ret;
	}
	
	
	//takes coordinates of a vector and finds the NORTH, SOUTH, EAST
	//WEST value that it most closely resembles
	protected Direction findDesiredDirection(int x, int y){
		Direction desired = null;
		
		if (x == 0 && y == 0)
			return null;
		
		else if (x == 0){
			if (y > 0)
				desired = Direction.SOUTH;
			else
				desired = Direction.NORTH;
		}
		
		else if (y == 0){
			if (x > 0)
				desired = Direction.EAST;
			else
				desired = Direction.WEST;
		}
		
		else if (x > 0 && y > 0){
			if (Math.abs(x) > Math.abs(y))
				desired = Direction.EAST;
			else
				desired = Direction.SOUTH;
		}
		
		else if (x < 0 && y < 0){
			if (Math.abs(x) > Math.abs(y))
				desired = Direction.WEST;
			else
				desired = Direction.NORTH;
		}
		
		else if (x > 0 && y < 0){
			if (Math.abs(x) > Math.abs(y))
				desired = Direction.EAST;
			else
				desired = Direction.NORTH;
		}
		
		else if (x < 0 && y > 0){
			if (Math.abs(x) > Math.abs(y))
				desired = Direction.WEST;
			else
				desired = Direction.SOUTH;
		}
		
		return desired;
	}
	
	protected MoveCommand randomMove(World world, Animal animal){
		Direction[] allDirections = Direction.values();
		int dir = (int) (Math.random() * allDirections.length);
		Location newLoc = new Location(world.getLocation(animal), allDirections[dir]);
		
		if (canMove(world, animal)){
			while (!world.isValidLocation(newLoc) || world.getThing(newLoc) != null){
				dir = (int) (Math.random() * allDirections.length);
				newLoc = new Location(world.getLocation(animal), allDirections[dir]);
			}
			this.facing = allDirections[dir];
			return new MoveCommand(allDirections[dir]);
		}
		else
			return null;
	}
	
}