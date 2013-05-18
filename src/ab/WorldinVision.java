package ab;

import java.awt.Rectangle;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import main.ScenarioPanel;
import quali.Configuration;
import quali.MBR;
import quali.MBRReasoner;
import quali.MBRRegister;
import quali.TestNode;
import quali.util.Logger;

public class WorldinVision {
  public LinkedList<MBR> mbrs = new LinkedList<MBR>();
  public static int gap = 10;
  public TestNode node = null;
  public TestNode sol = null;
  public void buildWorld(List<Rectangle> objs)
  {
	   // hardcode the objects in this sList<MBR> mbrsr (Rectangle rec : objs)
      for (Rectangle rec : objs)
      { 
      	MBR mbr = new MBR(rec);     
      	
      	// filter the smaller recs
      	int area = mbr.height * mbr.width;
      	if(area > 30)
      		mbrs.add(mbr);
      }
   // the first mbr is the edge
      //MBRRegisterAdvance.batchRegister(mbrs, mbrs.get(0));
	  
	  MBRRegister.batchRegister(mbrs);
  	  

     

  }
  public void showWorldinVision()
  {
	  ScenarioPanel sp = new ScenarioPanel();
	//  sp.run(MBRRegister.getMbrs());
	  LinkedList<MBR> mbrs = new LinkedList<MBR>();
	 if(node  != null)
	  for (Configuration conf : node.conflist)
		  mbrs.add(conf.getMbr());
	 else
		 mbrs = MBRRegister.getMbrs();
	 
	 if(sol != null)
	  sp.run(mbrs , sol , true);
	 else
	 { 
	   if(node != null)
		 sp.run(mbrs , node , false);
	   else
		   sp.run(mbrs);
	 
	 }
	  
  }

  public void reason()
  {
	  node = MBRRegister.constructTestNode();
  	  MBRReasoner MBRR = new MBRReasoner(node);
  	 
  	  ExecutorService executor = Executors.newSingleThreadExecutor();
  	  
  	  try {
  		  
  		  executor.submit(MBRR).get(Logger.timeLimit, TimeUnit.SECONDS);
  	      
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ExecutionException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (TimeoutException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  	
  	
  	  //MBRR.search(node);
  	  sol = MBRR.sol;

  }
  
}
