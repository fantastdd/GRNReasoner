package ab;

import gui.ScenarioPanel;

import java.awt.Rectangle;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import quali.ApproxSolution;
import quali.Configuration;
import quali.MBR;
import quali.MBRReasoner;
import quali.Node;
import quali.util.Logger;
import quali.util.LoggersManager;

public class WorldinVision {
  //public LinkedList<MBR> mbrs = new LinkedList<MBR>();
  public MBR[] mbrs;
  public static int gap = 10;
  public Node node = null;
  public Node sol = null;
  public int mbr_counter = 0;
  public Logger logger = new Logger();
  public void buildWorld(List<Rectangle> objs)
  {
	 
	  MBR[] buf_mbrs = new MBR[objs.size()];
	  
      for (Rectangle rec : objs)
      { 
      	MBR mbr = new MBR(rec);     
      	
      	// filter the smaller recs
      	int area = mbr.height * mbr.width;
      	if(area > 40)//In fully zoom out mode, change to 20, fully zoom in, change to 40
      	{	
      		mbr.uid = mbr_counter++;
      		buf_mbrs[mbr.uid] = mbr;
      	}
      }
       mbrs = new MBR[mbr_counter];
       System.arraycopy(buf_mbrs, 0, mbrs, 0, mbr_counter);
        
	  
      	//MBRRegister.batchRegister(mbrs);
		// register the mbr for later analysis regarding the number of stable states it hits during the backtracking
        LoggersManager.mbrsSize = mbrs.length;
	

     

  }
  public void showWorldinVision()
  {
	  ScenarioPanel sp = new ScenarioPanel();

	 if(sol != null)
	  sp.run(mbrs , sol , true);
	 else
	 { 
	   if(node != null)
	   { 
		   //  Use the Apporx solution
		   ApproxSolution apps = new ApproxSolution(mbrs, node, logger);
		   apps.getApporxSolution();
		   sp.run(mbrs , node , false);
	   }
	   else
		   sp.run(mbrs);
	 
	 }
	  
  }

  public void reason()
  {
	  //node = MBRRegister.constructTestNode();
	  //System.out.println(" the length of mbrs " + mbrs.length);
	  logger.createProfiles(mbrs.length);
  	  node = new Node(mbrs , null); // initialize the root node
  	  for (Configuration conf : node.conflist)
  		  if(conf.edge)
  			  logger.recordAsEdge(conf.mbr);
  	  
	  MBRReasoner MBRR = new MBRReasoner(node , logger);
  	 
  	  ExecutorService executor = Executors.newSingleThreadExecutor();
  	  
  try {
  		  
  		  executor.submit(MBRR).get(LoggersManager.timeLimit, TimeUnit.SECONDS);
  	      
	} catch (InterruptedException e) {
		
		e.printStackTrace();
	} catch (ExecutionException e) {
		
		e.printStackTrace();
	} catch (TimeoutException e) {
	
		System.out.println(" time out, use the approx sol ");
	}
  	
     	  
  	
  	  //MBRR.search(node);
  	  sol = MBRR.sol;

  }
  public void reasonMultiThread(int times)
  {
	  ExecutorService multiThread = Executors.newFixedThreadPool(5);
	  
	
	  for (int i = 0; i < 5; i ++)
	  {
		  Logger logger = LoggersManager.createLogger();
		  int[] order = logger.generateOrder();
		  MBR[] _mbrs = new MBR[mbrs.length];
		
			 int counter = 0;
			 for (int uniqueID: order)
			 {
				
				_mbrs[counter] = mbrs[uniqueID];
				
				counter++;
			 }
			 
			 node = new Node(_mbrs , null);
			 for (Configuration conf : node.conflist)
		  		  if(conf.edge)
		  			  logger.recordAsEdge(conf.mbr);
			 MBRReasoner MBRR = new MBRReasoner(node , logger);
			 
			 multiThread.execute(MBRR);
	  }

	   try {
		   
		multiThread.awaitTermination(5, TimeUnit.SECONDS);
	} catch (InterruptedException e) {
		
		e.printStackTrace();
	}
	   logger = LoggersManager.merge();
	   
	  
	
  }
  public void reason(int times)
  {
	   ExecutorService executor = Executors.newSingleThreadExecutor();
		logger.createProfiles(mbrs.length);
	  for (int i = 0; i < times ; i ++){
		 int[] order = logger.generateOrder();
		 MBR[] _mbrs = new MBR[mbrs.length];
		 int counter = 0;
		 for (int uniqueID: order)
		 {
			_mbrs[counter] = mbrs[uniqueID];
			counter++;
		 }
		 node = new Node(_mbrs , null);
		  for (Configuration conf : node.conflist)
	  		  if(conf.edge)
	  			  logger.recordAsEdge(conf.mbr);
		 MBRReasoner MBRR = new MBRReasoner(node , logger);
	  	 
	  try {
	  		
		   executor.submit(MBRR).get(LoggersManager.timeLimit, TimeUnit.SECONDS);
	  	      
	  	  } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	  	  } catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	  	  } catch (TimeoutException e) {
			// TODO Auto-generated catch block
			/*e.printStackTrace();*/
	  		  
			System.out.println(" time out, use the approx sol ");
		}
	  	
	     	  
	  	
	  	  //MBRR.search(node);
	  	  sol = MBRR.sol;
	  	  if(sol!= null)
	  		  break;
    }  
	  
  }
  
}
