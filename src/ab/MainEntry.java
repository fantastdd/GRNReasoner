package ab;

import io.ScenarioIO;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.LinkedList;

import quali.MBR;

public class MainEntry {

	public static void main(String argsp[])
	{
/*	  new ActionRobot();
	   BufferedImage screenshot = ActionRobot.doScreenShot();
	   Vision vision = new Vision(screenshot);
	   LinkedList<Rectangle> worldInVision = new LinkedList<Rectangle>();
	   worldInVision.addAll(vision.findStones());
	   worldInVision.addAll(vision.findWood());
	   worldInVision.addAll(vision.findIce());
	   WorldinVision wiv = new WorldinVision();
	   wiv.buildWorld(worldInVision);
	   wiv.showWorldinVision();*/
					
			ScenarioIO sio = new ScenarioIO("l4");

			LinkedList<LinkedList<MBR>> scenarios;
			try {
				scenarios = sio.load("l4");
				LinkedList<MBR> s1 = scenarios.get(0);
				LinkedList<Rectangle> worldInVision = new LinkedList<Rectangle>();
				for (MBR mbr : s1)
				{
					worldInVision.add(mbr);
				}
				WorldinVision wiv = new WorldinVision();
				wiv.buildWorld(worldInVision);
				 wiv.showWorldinVision();
			} catch (IOException e) {

				e.printStackTrace();
			}
			
		
	}
}
