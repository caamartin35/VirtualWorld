package com.commands;

import com.staff.interfaces.Actor;
import com.staff.interfaces.Animal;
import com.staff.interfaces.Command;
import com.staff.util.Direction;
import com.staff.interfaces.World;

/**
 * This command calls an {@link Animal} to move.
 */
public class MoveCommand implements Command {

	private Direction dir;

	/**
	 * Instantiates a move command to be executed in the given direction.
	 *
	 * @param dir
	 *            The direction in which the command will be performed.
	 *
	 * @throws NullPointerException
	 *             If direction is null.
	 */
	public MoveCommand(Direction dir) {
		if (dir == null) {
			throw new NullPointerException("Direction cannot be null.");
		}
		this.dir = dir;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalArgumentException
	 *             If actor is not an instance of Animal.
	 */
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
		((Animal) actor).move(world, dir);
	}
}
