package ab;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import ab.demo.other.ActionRobot;
import ab.vision.Vision;

public class MainEntry {

	public static void main(String argsp[])
	{
	   ActionRobot ar = new ActionRobot();
	   BufferedImage screenshot = ar.doScreenShot();
	   Vision vision = new Vision(screenshot);
	   LinkedList<Rectangle> worldInVision = new LinkedList<Rectangle>();
	   worldInVision.addAll(vision.findStones());
	   worldInVision.addAll(vision.findWood());
	   worldInVision.addAll(vision.findIce());
	   WorldinVision wiv = new WorldinVision();
	   wiv.buildWorld(worldInVision);
	   wiv.showWorldinVision();
	}
}
