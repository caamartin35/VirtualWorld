package com.ai;

import com.staff.interfaces.AI;
import com.staff.interfaces.Actor;
import com.staff.util.Direction;
import com.commands.MoveCommand;
import com.staff.interfaces.Command;
import com.staff.interfaces.World;

/**
 * The AI for a Gnat. This AI will pick a random direction and then return a
 * command which moves in that direction.
 *
 * This class serves as a simple example for how other AIs should be
 * implemented.
 */
public class GnatAI implements AI {

	/*
	 * Your AI implementation must provide a public default constructor so that
	 * the it can be initialized outside of the package.
	 */
	public GnatAI() {
	}

	/*
	 * GnatAI is dumb. It disregards its surroundings and simply tells the Gnat
	 * to move in a random direction.
	 */
	@Override
	public Command act(World world, Actor actor) {
		Direction[] allDirections = Direction.values();
		int dir = (int) (Math.random() * allDirections.length);
		return new MoveCommand(allDirections[dir]);
	}
}
