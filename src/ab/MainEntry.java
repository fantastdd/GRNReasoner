package ab;

import io.ScenarioIO;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;

import quali.MBR;
import quali.reasoner.MultiThreadReasoner;
import quali.util.Logger;
import quali.util.LoggerManager;
import ab.demo.other.ActionRobot;
import ab.vision.Vision;

public class MainEntry {
    public static int gap = 10;
    public static int threads = 1;
    public static int time = 4;
    public static int attempt = 5;
    
    public static String[] extract(String[] commands)
    {
	if(commands.length < 2)
	    return null;
	else
	{
	    String _option = commands[0];
	    String _value = commands[1];
	    int value = Integer.parseInt(_value);
	    if (_option.equalsIgnoreCase("-gap"))
	    {
		gap = value;
	    }else
		if(_option.equalsIgnoreCase("t"))
		{
		    threads = value;
		}else
		    if(_option.equalsIgnoreCase("-attempt"))
		    {
			attempt = value;
		    }else
			if(_option.equalsIgnoreCase("-time"))
			{
			    time = value;
			}
	    String[] _commands = new String[commands.length - 2];
	    System.arraycopy(commands, 2, _commands, 0, _commands.length);
	    extract(_commands);
	}
	return null;
	
    }
    public static void main(String args[]) throws IOException {
	
	assert(args.length > 0);
	String type = args[args.length - 1];
	extract(args);
	if(type.equalsIgnoreCase("vision")){
	 
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
	  
	  MultiThreadReasoner multiReasoner = new MultiThreadReasoner(wiv, threads ,attempt, time); 
	  multiReasoner.run();
	  
	  //display 
	   //Logger logger = LoggerManager.merge();
	 // System.out.println("  print the logger  " + logger);
	  
	  //wiv.showWorldinVision(logger, multiReasoner.root , MultiThreadReasoner.sol);
	 
	}
	else{
        	//long time = System.currentTimeMillis();
	        
        	ScenarioIO sio = new ScenarioIO(type);
        
        	LinkedList<LinkedList<MBR>> scenarios;
        	try {
        	    scenarios = sio.load(type);
        	    LinkedList<MBR> s1 = scenarios.get(0);
        	    LinkedList<Rectangle> worldInVision = new LinkedList<Rectangle>();
        	    for (MBR mbr : s1) {
        		worldInVision.add(mbr);
        	    }
        	    WorldinVision wiv = new WorldinVision();
        	    wiv.buildWorld(worldInVision);
        	    wiv.showWorldinVision(null, null, null);
        	    MultiThreadReasoner multiReasoner = new MultiThreadReasoner(wiv, threads ,attempt, time);
        	    multiReasoner.run();
        	    //Logger logger = LoggerManager.merge();
        	    //System.out.println("  print the logger  " + logger);
        
        	   //wiv.showWorldinVision(logger, multiReasoner.root, MultiThreadReasoner.sol);
        	} catch (IOException e) {
        
        	    e.printStackTrace();
        	}
        
        	System.out.println(" Time Consumption:  "
        		+ (System.currentTimeMillis() - time));
        
            }
    }
}
