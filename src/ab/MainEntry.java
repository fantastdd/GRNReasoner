package ab;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;

import quali.util.Logger;
import quali.util.LoggersManager;
import ab.demo.other.ActionRobot;
import ab.vision.Vision;

public class MainEntry {

	public static void main(String args[]) throws IOException
	{
	   new ActionRobot();
	   
	   BufferedImage screenshot = ActionRobot.doScreenShot();
	   //BufferedImage screenshot = ImageIO.read(new File("F://AngryBirds//l13sd.png"));
	   Vision vision = new Vision(screenshot);
	   LinkedList<Rectangle> worldInVision = new LinkedList<Rectangle>();
	   worldInVision.addAll(vision.findStones());
	   worldInVision.addAll(vision.findWood());
	   worldInVision.addAll(vision.findIce());
	   WorldinVision wiv = new WorldinVision();
	   wiv.buildWorld(worldInVision);
	   wiv.showWorldinVision();
	   //wiv.reason(LoggersManager.attempts);
	   wiv.reasonMultiThread(5);
	   wiv.showWorldinVision();
	   
			/*long time = System.currentTimeMillis();
			ScenarioIO sio = new ScenarioIO("l41");

			LinkedList<LinkedList<MBR>> scenarios;
			try {
				scenarios = sio.load("l41");
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
