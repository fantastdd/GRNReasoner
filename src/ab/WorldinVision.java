package ab;

import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;

import main.ScenarioPanel;
import quali.MBR;
import quali.MBRReasonerAdvance;
import quali.MBRRegister;
import quali.TestNode;

public class WorldinVision {
  private LinkedList<MBR> mbrs = new LinkedList<MBR>();
  public void buildWorld(List<Rectangle> objs)
  {
	   // hardcode the objects in this sList<MBR> mbrsr (Rectangle rec : objs)
      for (Rectangle rec : objs)
      { 
      	MBR mbr = new MBR(rec);     
      	mbrs.add(mbr);
      }
      refine(mbrs);
      reason();

  }
  public void showWorldinVision()
  {
	  ScenarioPanel sp = new ScenarioPanel();
	  sp.run(MBRRegister.getMbrs());
	  
	  
  }
  private void refine(List<MBR> mbrs)
  {
     
  }
  private void reason()
  {
      MBRRegister.batchRegister(mbrs);
  	  TestNode node = MBRRegister.constructTestNode();
  	  MBRReasonerAdvance MBRR = new MBRReasonerAdvance();
  	  MBRR.search(node);

  }
  
}
