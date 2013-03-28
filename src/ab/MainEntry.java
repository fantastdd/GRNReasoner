package ab;

import io.ScenarioIO;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;

import main.ScenarioPanel;

import common.util.Debug;

import quali.MBR;
import quali.MBRReasoner;
import quali.MBRRegister;
import quali.Node;

import ab.demo.other.ActionRobot;
import ab.vision.Vision;

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
					
			ScenarioIO sio = new ScenarioIO("s6");

			LinkedList<LinkedList<MBR>> scenarios;
			try {
				scenarios = sio.load("s6");
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
	}
}
