package quali.reasoner;

import java.util.Observable;

import quali.MBR;
import quali.Node;
import quali.util.Logger;
import quali.util.LoggerManager;
import ab.WorldinVision;

public abstract class Reasoner  extends Observable implements Runnable {
    protected WorldinVision world;
    protected Logger logger;
    public Node root = null;
   
    public MBR[] mbrs;
    public Reasoner(WorldinVision world) {
	this.world = world;
	logger = LoggerManager.createLogger();
	mbrs = new MBR[world.mbrs.length];
	for (int i = 0; i < mbrs.length; i ++)
	{
	    mbrs[i] = (world.mbrs[i]).clone();
	}
	root = new Node(mbrs,null);
    }

    @Override
    public abstract void run();
}
