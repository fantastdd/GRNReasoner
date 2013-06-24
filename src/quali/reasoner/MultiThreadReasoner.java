package quali.reasoner;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import quali.Node;
import quali.util.LoggerManager;
import ab.WorldinVision;

public class MultiThreadReasoner extends Reasoner implements Observer {
    private int threads;
    private int attempts;
    private int time;
    private static ExecutorService executor = null;
    public static Node sol = null;
    private static volatile boolean sign = true;
    private boolean displayed = false;
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
	if(executor != null && !executor.isShutdown())
	{
	    //System.out.println(" shut down the executor ");
	    sol = _sol;
	    sign = false;
	       
	}
    }
    @Override
    public void run() {
	
	executor = Executors.newFixedThreadPool(threads);
	
   	for (int i = 0; i < threads; i++) 
   	{	  
   	      if(sign)
   	      {
   		  //System.out.println("start  " + i);
   		  SingleThreadReasoner singleReasoner = new SingleThreadReasoner(world,attempts,time);
   		  singleReasoner.addObserver(this);
   		  executor.execute(singleReasoner);		  
   	       }
   	      else
   		executor.shutdown(); 
   	}
   	try {   	    
	    executor.awaitTermination(attempts * time, TimeUnit.SECONDS);
	    if(!displayed)
		update(null,null);
   	} catch (InterruptedException e) {
	   
	    e.printStackTrace();
	}
   	
       }
    @Override
    public synchronized void update(Observable o, Object arg) {
	
	if(!displayed)
	    world.showWorldinVision(LoggerManager.merge(), root , sol);
	displayed = true;
	
    }
	
    }
 
