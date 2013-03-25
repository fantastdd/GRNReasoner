package ab;

import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;

import main.ScenarioPanel;

import quali.MBR;
import quali.MBRRegisterWithFuzzyShape;

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
	  sp.run(MBRRegisterWithFuzzyShape.getMbrs());
	  
	  
  }
  private void refine(List<MBR> mbrs)
  {
     
  }
  private void reason()
  {
      MBRRegisterWithFuzzyShape.batchRegister(mbrs);

  }
  
}
