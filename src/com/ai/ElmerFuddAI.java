package com.ai;

import com.actors.ElmerFudd;
import com.staff.interfaces.Actor;
import com.staff.interfaces.Command;
import com.staff.interfaces.Fox;
import com.staff.util.Direction;
import com.staff.util.Location;
import com.commands.ShootCommand;
import com.staff.interfaces.Rabbit;
import com.staff.interfaces.World;

public class ElmerFuddAI extends AbstractAI{

	
	public ElmerFuddAI(){
		this.facing = Direction.NORTH;
	}
	
	@Override
	public Command act(World world, Actor actor) {
		ElmerFudd elmer = (ElmerFudd) actor;
		Location shootable;
		
		//if elmer can shoot something, do it, otherwise move randomly
		if ((shootable = findShootableAnimal(world, elmer)) != null){
			return new ShootCommand(shootable);
		}
		else if (canMove(world, elmer)){
			return randomMove(world, elmer);
		}
		else
			return null;
	}
	
	//returns the location of a shootable animal in the world,
	//or null if there aren't any.
	private Location findShootableAnimal(World world, ElmerFudd elmer){
		Direction facing = this.facing;
		Location gameLoc = new Location(world.getLocation(elmer), facing);
		
		while (world.isValidLocation(gameLoc)){
			if (world.getThing(gameLoc) != null){
				if (world.getThing(gameLoc) instanceof Rabbit || world.getThing(gameLoc) instanceof Fox){
					return gameLoc;
				}
			}
			
			gameLoc = new Location(gameLoc, facing);
			
		}
		
		return null;
	}
	
	
}