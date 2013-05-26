package ab;

import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;
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
  //public LinkedList<MBR> mbrs = new LinkedList<MBR>();
  public MBR[] mbrs;
  public static int gap = 10;
  public TestNode node = null;
  public TestNode sol = null;
  public int mbr_counter = 0;
  
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
      
		Logger.createProfiles(mbrs.length);

     

  }
  public void showWorldinVision()
  {
	  ScenarioPanel sp = new ScenarioPanel();
	//  sp.run(MBRRegister.getMbrs());
	 
	  /*	 MBR[] mbrs = new MBR[]; 
	   * if(node  != null)
	  	for (Configuration conf : node.conflist)
		  mbrs.add(conf.getMbr());
	 else
		 mbrs = this.mbrs;
	 else
		 mbrs = MBRRegister.getMbrs();
	 */
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
	  //node = MBRRegister.constructTestNode();
	  //System.out.println(" the length of mbrs " + mbrs.length);
  	  node = new TestNode(mbrs , null);
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
		/*e.printStackTrace();*/
		System.out.println(" time out, use the approx sol ");
	}
  	
     	  
  	
  	  //MBRR.search(node);
  	  sol = MBRR.sol;

  }
  public void reason(int times)
  {
	  ExecutorService executor = Executors.newSingleThreadExecutor();
    for (int i = 0; i < times ; i ++){
		 int[] order = Logger.generateOrder();
		 MBR[] _mbrs = new MBR[mbrs.length];
		 int counter = 0;
		 for (int uniqueID: order)
		 {
			_mbrs[counter] = mbrs[uniqueID];
			counter++;
		 }
		 node = new TestNode(_mbrs , null);
		 MBRReasoner MBRR = new MBRReasoner(node);
	  	 
	  	
	  	  
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
