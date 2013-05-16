package ab;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import ab.demo.other.ActionRobot;
import ab.vision.Vision;

public class MainEntry {

	public static void main(String args[])
	{
	   new ActionRobot();
	   
	   BufferedImage screenshot = ActionRobot.doScreenShot();
	   Vision vision = new Vision(screenshot);
	   LinkedList<Rectangle> worldInVision = new LinkedList<Rectangle>();
	   worldInVision.addAll(vision.findStones());
	   worldInVision.addAll(vision.findWood());
	   worldInVision.addAll(vision.findIce());
	   WorldinVision wiv = new WorldinVision();
	   wiv.buildWorld(worldInVision);
	   wiv.reason();
	   wiv.showWorldinVision();
	   /*
			long time = System.currentTimeMillis();
			ScenarioIO sio = new ScenarioIO("l11c");ghjm,

			LinkedList<LinkedList<MBR>> scenarios;
			try {
				scenarios = sio.load("l16c");
				LinkedList<MBR> s1 = scenarios.get(0);
				LinkedList<Rectangle> worldInVision = new LinkedList<Rectangle>();
				for (MBR mbr : s1)
				{
					worldInVision.add(mbr);
				}
				WorldinVision wiv = new WorldinVision();
				wiv.buildWorld(worldInVision);
				wiv.reason();
				 wiv.showWorldinVision(); 
			} catch (IOException e) {

				e.printStackTrace();
			}
			
		System.out.println(" TIme Consumption:  " + (System.currentTimeMillis() - time ));*/
		
	}
}
