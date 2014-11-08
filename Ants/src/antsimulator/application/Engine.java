/**
 * Created by Enifs.
 */

package antsimulator.application;

import antsimulator.*;
import java.awt.*;
import java.util.*;


public class Engine
{
	// ----------------------------------------------------------------------------
	// Section: Time control
	// ----------------------------------------------------------------------------

		public void advance()
		{
			assert this.isReady() : "Engine is not ready to work yet!";

			for (Hive hive : this.hives)
			{
				for (Ant ant : hive.getResidentAnts())
				{
					this.moveAnt(ant);
				}
			}
			// do stuff
			this.timePassed++;
		}


	private void moveAnt(Ant ant)
	{
		switch(ant.getBehaviour())
		{
			case SEARCH:
//				break;
			case GO_HOME:
//				break;
			case RANDOM:
				this.randomMovement(ant);
				break;
		}
	}

	private void searchMovement(Ant ant)
	{
		Map<Feramon, Point> map = new HashMap<Feramon, Point>(4);

		ArrayList<Feramon> feramons = new ArrayList<Feramon>();

		Point leftPoint = new Point(ant.getLocation().x - 1, ant.getLocation().y);
		Point rightPoint = new Point(ant.getLocation().x + 1, ant.getLocation().y);
		Point upPoint = new Point(ant.getLocation().x, ant.getLocation().y + 1);
		Point downPoint = new Point(ant.getLocation().x, ant.getLocation().y - 1);

		Feramon left = this.feramonManager.fetchFeramon(Feramon.Type.B, leftPoint);
		Feramon right = this.feramonManager.fetchFeramon(Feramon.Type.B, rightPoint);
		Feramon up = this.feramonManager.fetchFeramon(Feramon.Type.B, upPoint);
		Feramon down = this.feramonManager.fetchFeramon(Feramon.Type.B, downPoint);

		if (left != null)
		{
			map.put(left, leftPoint);
			feramons.add(left);
		}

		if (right != null)
		{
			map.put(right, rightPoint);
			feramons.add(right);
		}

		if (up != null)
		{
			map.put(up, upPoint);
			feramons.add(up);

		}

		if (down != null)
		{
			map.put(down, downPoint);
			feramons.add(down);
		}

		Collections.sort(feramons, new Comparator<Feramon>()
		{
			@Override
			public int compare(Feramon o1, Feramon o2)
			{
				return Integer.compare(o1.intensity(timePassed), o2.intensity(timePassed));
			}
		});

		switch (feramons.size())
		{
			case 0:
				this.randomMovement(ant);
			break;
			case 1:
				break;
			default:
		}
	}

	private void randomMovement(Ant ant)
	{
		int x = RandomFactory.getRandomInteger();
		x = x % 4;

		switch(x)
		{
			case 0:
				ant.moveStaticDown();

				if (!this.arena.contains(ant.getLocation()))
				{
					ant.moveStaticUp();
				}
				break;
			case 1:
				ant.moveStaticUp();

				if (!this.arena.contains(ant.getLocation()))
				{
					ant.moveStaticDown();
				}
				break;
			case 2:
				ant.moveStaticLeft();

				if (!this.arena.contains(ant.getLocation()))
				{
					ant.moveStaticRight();
				}
				break;
			case 3:
				ant.moveStaticRight();

				if (!this.arena.contains(ant.getLocation()))
				{
					ant.moveStaticLeft();
				}
				break;
		}

		if (ant.isCarryingFood())
		{
			if (ant.getHive().containsAnt(ant.getLocation()))
			{
				ant.getHive().storeFood();
				ant.setCarryingFood(false);
			}
		}
		else
		{
			for (Food f : this.food)
			{
				if (f.containsAnt(ant.getLocation()))
				{
					f.takeFoodBit();
					ant.setCarryingFood(true);
				}
			}
		}
	}


	public void advance(long time)
		{
			for (int i = 0; i < time; i++)
			{
				this.advance();
			}
		}

		public void advanceUntil(StopEvent stopEvent)
		{
			while (!stopEvent.stop())
			{
				advance();
			}
		}
	// ----------------------------------------------------------------------------
	// Section: Accessors
	// ----------------------------------------------------------------------------

	public boolean isReady()
	{
		return arena != null && !this.food.isEmpty() && !this.hives.isEmpty();
	}

	public void addHive(Hive hive)
	{
		this.hives.add(hive);
	}

	public void addFood(Food food)
	{
		this.food.add(food);
	}

	public void setArena(Arena arena)
	{
		this.arena = arena;
	}

	public Arena getArena()
	{
		return arena;
	}

	public ArrayList<Food> getFood()
	{
		return this.food;
	}

	public ArrayList<Hive> getHives()
	{
		return this.hives;
	}

	// ----------------------------------------------------------------------------
	// Section: Fields
	// ----------------------------------------------------------------------------

	private Arena arena;
	private ArrayList<Food> food = new ArrayList<Food>();
	private ArrayList<Hive> hives = new ArrayList<Hive>();

	private FeramonManager feramonManager = new FeramonManager();

	int timePassed = 0;

	// ----------------------------------------------------------------------------
	// Section: Inner classes
	// ----------------------------------------------------------------------------

	public interface StopEvent
	{
		public boolean stop();
	}
}
