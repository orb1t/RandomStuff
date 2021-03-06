package antsimulator;

import antsimulator.application.Engine;
import com.sun.org.apache.xpath.internal.operations.Mod;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

import static java.util.Map.Entry;

/**
 * Created by Anna on 06/11/2014.
 */
public class Visualiser extends JFrame
{

	public Visualiser(Engine engine, int width, int heigth, Mode mode) throws HeadlessException
	{
		this.setTitle(mode.toString());
		this.engine = engine;
		double scaleX;
		double scaleY;
		if (width < engine.getArena().width || heigth < engine.getArena().heigth)
		{
			this.setSize(engine.getArena().width, engine.getArena().heigth + 19);
		}
		else
		{
			scaleX = width / engine.getArena().width;
			scaleY = heigth / engine.getArena().heigth;
			if (scaleX > scaleY)
			{
				this.setScale((int) scaleY);
				this.setSize((int) (engine.getArena().width * scaleY), heigth + 19);
			}
			else
			{
				this.setScale((int) scaleX);
				this.setSize(width, (int) (engine.getArena().heigth * scaleX) + 19);
			}
		}

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		switch (mode)
		{
			case Feramons:
				FeramonGround feramonGround = new FeramonGround();
				feramonGround.setBackground(new Color(172, 225, 175));
				this.setContentPane(feramonGround);
				break;
			case Ants:
				AntGround antGround = new AntGround();
				antGround.setBackground(new Color(172, 225, 175));
				this.setContentPane(antGround);
				break;
		}

		this.setVisible(true);
	}


	private class AntGround extends JPanel
	{
		@Override
		public void paintComponent(Graphics g)
		{
			for (Hive hive : engine.getHives())
			{
				g.setColor(Color.GREEN);
				g.fillRect((int) (hive.getTopLeftPoint().getX() * scale), (int) (hive.getTopLeftPoint()
								.getY() * scale),
						((int) (hive.getWidth() * scale)),
						((int) (hive.getHeight() * scale)));
				for (Ant ant : hive.getResidentAnts())
				{
					g.setColor(Color.RED);
					g.fillOval((int) (ant.getLocation().getX() * scale), (int) (ant.getLocation().getY() *
									scale), (int) scale,
							(int) scale);
				}
			}

			for (Food food : engine.getFood())
			{
				g.setColor(Color.YELLOW);
				g.fillRect((int) (food.getTopLeftPoint().getX() * scale), (int) (food.getTopLeftPoint()
								.getY
										() * scale),
						((int) (food.getWidth() * scale)),
						((int) (food.getHeight() * scale)));
			}
			g.setColor(Color.BLACK);
			g.drawRect(0, 0, ((int) (engine.getArena().width * scale)), ((int) (engine.getArena().heigth
					* scale)));
		}
	}

	private class FeramonGround extends JPanel
	{
		@Override
		public void paintComponent(Graphics g)
		{
			g.setColor(Color.GREEN);
			for (Hive hive : engine.getHives())
			{

				g.fillRect((int) (hive.getTopLeftPoint().getX() * scale), (int) (hive.getTopLeftPoint()
								.getY() * scale),
						((int) (hive.getWidth() * scale)),
						((int) (hive.getHeight() * scale)));
			}

			g.setColor(Color.YELLOW);
			for (Food food : engine.getFood())
			{
				g.fillRect((int) (food.getTopLeftPoint().getX() * scale), (int) (food.getTopLeftPoint()
								.getY
										() * scale),
						((int) (food.getWidth() * scale)),
						((int) (food.getHeight() * scale)));
			}

			for (Object o : Feramon.Type.values())
			{
				Map map = engine.getFeramonManager().map.get(o);
				// Find min/max intensity
				int max = 255;
				for (Object o1 : map.values())
				{
					Feramon feramon = (Feramon) o1;
					max = Math.max(max, feramon.intensity(engine.getTimePassed()));
				}

				for (Object o1 : map.keySet())
				{
					Feramon colorifer = (Feramon) map.get(o1);
					double part = colorifer.intensity(engine.getTimePassed()) / max;
					int blo;
					if (max > 255)
					{
						blo = (int) (255 * part);
						blo = 255 - blo;
					}
					else
					{
						blo = 255 - colorifer.intensity(engine.getTimePassed());
					}
					g.setColor(new Color(255, blo, blo));

					FeramonManager.CustomPoint customPoint = (FeramonManager.CustomPoint) o1;
					g.fillOval((int) (customPoint.x * scale), (int) (customPoint.y *
									scale), (int) scale,
							(int) scale);

				}
			}

			g.setColor(Color.BLACK);
			g.drawRect(0, 0, ((int) (engine.getArena().width * scale)),
					((int) (engine.getArena().heigth
							* scale)));
		}

	}

	public void setScale(int scale)
	{
		this.scale = scale;
	}

	private Engine engine;

	private double scale;

	public enum Mode
	{
		Feramons, Ants
	}
}
