package quali.reasoner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import quali.Node;
import ab.WorldinVision;

public class MultiThreadReasoner extends Reasoner {
    private int threads;
    private int attempts;
    private int time;
    private static ExecutorService executor = null;
    public static Node sol = null;
    public MultiThreadReasoner(WorldinVision world) {
	super(world);
    }
    public MultiThreadReasoner(WorldinVision world , int threads , int attempts , int time)
    {
	super(world);
	this.threads = threads;
	this.attempts = attempts;
	this.time = time;
	
    }
    public synchronized static void terminate(Node _sol)
    {
	if(executor != null)
	{
	    sol = _sol;
	    executor.shutdown();    
	}
    }
    @Override
    public void run() {
	
	executor = Executors.newFixedThreadPool(threads);
	
   	for (int i = 0; i < threads; i++) 
   	{	  
   	   executor.execute(new SingleThreadReasoner(world,attempts,time));
   	  
   	}
   	try {
   	    
	    executor.awaitTermination(attempts * time, TimeUnit.SECONDS);
	
   	} catch (InterruptedException e) {
	   
	    e.printStackTrace();
	}
   	
       }
	
    }
 
