package com.commands;

import com.actors.ElmerFudd;
import com.staff.interfaces.Actor;
import com.staff.interfaces.Animal;
import com.staff.interfaces.Command;
import com.staff.interfaces.World;
import com.staff.util.Location;


//allows elmer fudd to shoot a rabbit or fox at 
//a specified location.
public class ShootCommand implements Command {
	
	private Location loc;
	
	public ShootCommand(Location loc) {
		if (loc == null) {
			throw new NullPointerException("Location cannot be null.");
		}
		this.loc = loc;
	}
	
	@Override
	public void execute(World world, Actor actor) {
		if (actor == null) {
			throw new NullPointerException("Actor cannot be null");
		} else if (world == null) {
			throw new NullPointerException("World cannot be null");
		} else if (!(actor instanceof Animal)) {
			throw new IllegalArgumentException(
					"actor must be an instance of Animal.");
		}
		((ElmerFudd) actor).shoot(world, loc);
	}
}