package ab;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;

import quali.reasoner.MultiThreadReasoner;
import quali.util.Logger;
import quali.util.LoggerManager;
import ab.demo.other.ActionRobot;
import ab.vision.Vision;

public class MainEntry {

    public static void main(String args[]) throws IOException {
	
	  new ActionRobot();
	  
	  BufferedImage screenshot = ActionRobot.doScreenShot();
	  //BufferedImage screenshot = ImageIO.read(newFile("F://AngryBirds//l13sd.png")); 
	  Vision vision = new Vision(screenshot); 
	  LinkedList<Rectangle> worldInVision = new LinkedList<Rectangle>(); 
	  worldInVision.addAll(vision.findStones());
	  worldInVision.addAll(vision.findWood());
	  worldInVision.addAll(vision.findIce());
	  worldInVision.addAll(vision.findPigs());
	  
	  WorldinVision wiv = new WorldinVision();
	  wiv.buildWorld(worldInVision); 
	  wiv.showWorldinVision(null , null ,null);
	  
	   MultiThreadReasoner multiReasoner = new MultiThreadReasoner(wiv, 30 ,5, 4); 
	   multiReasoner.run();
	  
	  //display 
	 // Logger logger = LoggerManager.merge();
	 // System.out.println("  print the logger  " + logger);
	  
	 // wiv.showWorldinVision(logger, multiReasoner.root , MultiThreadReasoner.sol);
	 

	/*long time = System.currentTimeMillis();
	ScenarioIO sio = new ScenarioIO("l4");

	LinkedList<LinkedList<MBR>> scenarios;
	try {
	    scenarios = sio.load("l4");
	    LinkedList<MBR> s1 = scenarios.get(0);
	    LinkedList<Rectangle> worldInVision = new LinkedList<Rectangle>();
	    for (MBR mbr : s1) {
		worldInVision.add(mbr);
	    }
	    WorldinVision wiv = new WorldinVision();
	    wiv.buildWorld(worldInVision);
	    wiv.showWorldinVision(null, null, null);
	    MultiThreadReasoner multiReasoner = new MultiThreadReasoner(wiv, 20,
		    4, 5);
	    multiReasoner.run();
	    //Logger logger = LoggerManager.merge();
	    //System.out.println("  print the logger  " + logger);

	    //wiv.showWorldinVision(logger, multiReasoner.root, MultiThreadReasoner.sol);
	} catch (IOException e) {

	    e.printStackTrace();
	}

	System.out.println(" Time Consumption:  "
		+ (System.currentTimeMillis() - time));*/

    }
}
