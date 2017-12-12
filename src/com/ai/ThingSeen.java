package com.ai;

import com.staff.interfaces.Actor;
import com.staff.interfaces.World;
import com.staff.util.Location;

//simple class that represents the relationship between some thing in the world
//that our actor can see and the actor itself.
class ThingSeen{
	private double distanceFromActor;
	private int xDiff, yDiff;
	private Type type;
	
	public enum Type {
		GRASS, FOX, RABBIT, GNAT, GARDENER;
	}
	
	public ThingSeen(World world, Actor actor, Location thingLoc, Type type){
		this.setDistanceAndAngle(world, actor, thingLoc);
		this.type = type;
	}
	
	private void setDistanceAndAngle(World world, Actor actor, Location thingLoc){
		Location actorLoc = world.getLocation(actor);
		this.xDiff = thingLoc.getX() - actorLoc.getX();
		this.yDiff = thingLoc.getY() - actorLoc.getY();
		distanceFromActor = Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
		
	}
	
	public Type getType(){
		return this.type;
	}
	
	public int getxDiff(){
		return this.xDiff;
	}
	
	public int getyDiff(){
		return this.yDiff;
	}
	public double getDistanceFromActor(){
		return this.distanceFromActor;
	}
}