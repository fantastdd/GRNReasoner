package ab;

import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;

import main.ScenarioPanel;
import quali.MBR;
import quali.MBRReasoner;
import quali.MBRRegister;
import quali.TestNode;

public class WorldinVision {
  public LinkedList<MBR> mbrs = new LinkedList<MBR>();
  public static int gap = 0;
  public void buildWorld(List<Rectangle> objs)
  {
	   // hardcode the objects in this sList<MBR> mbrsr (Rectangle rec : objs)
      for (Rectangle rec : objs)
      { 
      	MBR mbr = new MBR(rec);     
      	mbrs.add(mbr);
      }
   // the first mbr is the edge
      //MBRRegisterAdvance.batchRegister(mbrs, mbrs.get(0));
	  
	  MBRRegister.batchRegister(mbrs);
  	  

     

  }
  public void showWorldinVision()
  {
	  ScenarioPanel sp = new ScenarioPanel();
	  sp.run(MBRRegister.getMbrs());
	  
	  
  }

  public void reason()
  {
	  TestNode node = MBRRegister.constructTestNode();
  	  MBRReasoner MBRR = new MBRReasoner();
  	  MBRR.search(node);

  }
  
}
