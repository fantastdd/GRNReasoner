package quali.reasoner;

import java.util.Date;
import java.util.Timer;

import quali.Backtracking;
import quali.Configuration;
import quali.MBR;
import quali.Node;
import quali.util.TerminateBackTracking;
import ab.WorldinVision;

public class SingleThreadReasoner extends Reasoner {
    private int attempts = 0;
    private int time = 0;
    public Node sol = null;
    public SingleThreadReasoner(WorldinVision world) {
	super(world);
	attempts = 5;
	time = 10;
	
    }

    /**
     * 
     * @param world
     *            : stores a set of blocks detected by the vision software
     * @param attempts
     *            : the number of restart times.
     * @param time
     *            : the time (in seconds) allocated to each backtracking
     * 
     */
    public SingleThreadReasoner(WorldinVision world, int attempts, int time) {
	super(world);
	this.attempts = attempts;
	this.time = time;

    }

    @Override
    public void run() {
	
	//System.out.println(" Run a Single Thread Reasoner");
	//ExecutorService executor = Executors.newSingleThreadExecutor();
        
	for (int i = 0; i < attempts; i++) {
	   
            //TODO write it to LoggerManager 
	    int[] order = logger.generateOrder();
	    MBR[] _mbrs = new MBR[world.mbrs.length];
	    int counter = 0;
	    for (int uniqueID : order) {
               
		_mbrs[counter] = mbrs[uniqueID];
		counter++;
	    }
	    // automatically generate an root node according the ordering;
	    Node node = new Node(_mbrs, null);
	    for (Configuration conf : node.conflist)
		if (conf.edge)
		    logger.recordAsEdge(conf.mbr);

	    Backtracking MBRR = new Backtracking(node, logger);
	    TerminateBackTracking tbttask = new TerminateBackTracking(MBRR);
	    Timer timer = new Timer();
	    timer.schedule(tbttask, new Date(System.currentTimeMillis() + time * 1000));	   
	    MBRR.run();
	    sol = MBRR.sol;
	    if (sol != null)
	    {	
		MultiThreadReasoner.terminate(sol);
		this.setChanged();
		this.notifyObservers();
		break;
	    }
	}

    }

}
