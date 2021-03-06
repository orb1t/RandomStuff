/**
 * Created by Enifs
 */
package antsimulator;

import java.awt.*;
import java.util.HashMap;


public class Ant
{
	// ----------------------------------------------------------------------------
	// Section: Constructors
	// ----------------------------------------------------------------------------


	public Ant(Hive hive, Point location)
	{
		this.hive = hive;
		this.location = new Point(location);
		this.id = IdFactory.getNextIntId();
		this.behaviour = Behaviour.SEARCH;
		this.direction = this.randomDirection();
	}

	// ----------------------------------------------------------------------------
	// Section: Getters and Setters
	// ----------------------------------------------------------------------------


	public Point getLocation()
	{
		return location;
	}


	public Behaviour getBehaviour()
	{
		return behaviour;
	}


	public void setBehaviour(Behaviour behaviour)
	{
		this.behaviour = behaviour;
	}


	public boolean isCarryingFood()
	{
		return this.carryingFood;
	}


	public void setCarryingFood(boolean carryingFood)
	{
		this.carryingFood = carryingFood;
	}


	public Hive getHive()
	{
		return this.hive;
	}

	// ----------------------------------------------------------------------------
	// Section: Movement related.
	// ----------------------------------------------------------------------------

	public void moveTo(Point point)
	{
		this.location.setLocation(point);
	}

	private Direction randomDirection()
	{
		Direction rd = Direction.UP;
		int x = RandomFactory.getRandomInteger() % 4;

		switch (x)
		{
			case 0:
				rd = Direction.DOWN;
				break;
			case 1:
				rd = Direction.LEFT;
				break;
			case 2:
				rd = Direction.RIGHT;
				break;
			case 3:
				rd = Direction.UP;
				break;
		}

		return rd;
	}


	public void moveByDelta(int deltax, int deltay)
	{
		int x = this.location.x;
		int y = this.location.y;

		x += deltax;
		y += deltay;

		this.location.setLocation(x, y);
	}


	public void moveForward()
	{
		switch (this.direction)
		{
			case UP:
				this.moveByDelta(0, -1);
				break;
			case DOWN:
				this.moveByDelta(0, 1);
				break;
			case LEFT:
				this.moveByDelta(-1, 0);
				break;
			case RIGHT:
				this.moveByDelta(1, 0);
				break;
		}
	}


	public void moveBackward()
	{
		switch (this.direction)
		{
			case UP:
				this.moveByDelta(0, 1);
				this.direction = Direction.DOWN;
				break;
			case DOWN:
				this.moveByDelta(0, -1);
				this.direction = Direction.UP;
				break;
			case LEFT:
				this.moveByDelta(1, 0);
				this.direction = Direction.RIGHT;
				break;
			case RIGHT:
				this.moveByDelta(-1, 0);
				this.direction = Direction.LEFT;
				break;
		}
	}


	public void moveLeft()
	{
		switch (this.direction)
		{
			case UP:
				this.moveByDelta(-1, 0);
				this.direction = Direction.LEFT;
				break;
			case DOWN:
				this.moveByDelta(1, 0);
				this.direction = Direction.RIGHT;
				break;
			case LEFT:
				this.moveByDelta(0, -1);
				this.direction = Direction.DOWN;
				break;
			case RIGHT:
				this.moveByDelta(0, 1);
				this.direction = Direction.UP;
				break;
		}
	}


	public void moveRight()
	{
		switch (this.direction)
		{
			case UP:
				this.moveByDelta(1, 0);
				this.direction = Direction.RIGHT;
				break;
			case DOWN:
				this.moveByDelta(-1, 0);
				this.direction = Direction.LEFT;
				break;
			case LEFT:
				this.moveByDelta(0, 1);
				this.direction = Direction.UP;
				break;
			case RIGHT:
				this.moveByDelta(0, -1);
				this.direction = Direction.DOWN;
				break;
		}
	}

	public void moveStaticUp()
	{
		this.moveByDelta(0, +1);
	}

	public void moveStaticRight()
	{
		this.moveByDelta(+1, 0);
	}

	public void moveStaticDown()
	{
		this.moveByDelta(0, -1);
	}

	public void moveStaticLeft()
	{
		this.moveByDelta(-1, 0);
	}

	public HashMap<RelativeDirection, Point> getMoveToCells()
	{
		HashMap<RelativeDirection, Point> map = new HashMap<RelativeDirection, Point>();
		// Ants are allowed to scan only left, right and the cell right in front of it.
		switch (this.direction)
		{
			case UP:
				map.put(RelativeDirection.FORWARD, new Point(this.location.x, this.location.y - 1));
				map.put(RelativeDirection.LEFT, new Point(this.location.x - 1, this.location.y));
				map.put(RelativeDirection.RIGHT, new Point(this.location.x + 1, this.location.y));
				break;
			case DOWN:
				map.put(RelativeDirection.FORWARD, new Point(this.location.x, this.location.y + 1));
				map.put(RelativeDirection.LEFT, new Point(this.location.x + 1, this.location.y));
				map.put(RelativeDirection.RIGHT, new Point(this.location.x - 1, this.location.y));
				break;
			case LEFT:
				map.put(RelativeDirection.FORWARD, new Point(this.location.x - 1, this.location.y));
				map.put(RelativeDirection.LEFT, new Point(this.location.x, this.location.y + 1));
				map.put(RelativeDirection.RIGHT, new Point(this.location.x, this.location.y - 1));
				break;
			case RIGHT:
				map.put(RelativeDirection.FORWARD, new Point(this.location.x + 1, this.location.y));
				map.put(RelativeDirection.LEFT, new Point(this.location.x, this.location.y - 1));
				map.put(RelativeDirection.RIGHT, new Point(this.location.x, this.location.y + 1));
				break;
		}

		return map;
	}


	// ----------------------------------------------------------------------------
	// Section: Fields
	// ----------------------------------------------------------------------------
	private Point location;
	private Direction direction;
	private boolean carryingFood = false;
	private Hive hive;

	private long id;

	private Behaviour behaviour;
}
