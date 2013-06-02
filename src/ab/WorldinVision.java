package ab;

import gui.ScenarioPanel;

import java.awt.Rectangle;
import java.util.List;

import quali.ApproxSolution;
import quali.MBR;
import quali.Node;
import quali.util.Logger;
import quali.util.LoggerManager;

/**
 * 
 * @author Gary , Jochen Get the blocks from the vision software. Display the
 *         blocks with/without configurations.
 * 
 */
public class WorldinVision {

    public MBR[] mbrs;
    public static int gap = 10; // The value of 'gap' indicates the upper-bound
				// of a gap between an arbitrary pair of blocks
				// which will be eliminated.
    public Node node = null;
    public Node sol = null;
    public int mbr_counter = 0;
    public Logger logger = new Logger();

    public void buildWorld(List<Rectangle> objs) {

	MBR[] buf_mbrs = new MBR[objs.size()];

	for (Rectangle rec : objs) {
	    MBR mbr = new MBR(rec);

	    // filter the smaller rectangles
	    int area = mbr.height * mbr.width;
	    if (area > 40)// In fully zoom out mode, change to 20, fully zoom
			  // in, change to 40
	    {
		mbr.uid = mbr_counter++;
		buf_mbrs[mbr.uid] = mbr;
	    }
	}
	mbrs = new MBR[mbr_counter];
	System.arraycopy(buf_mbrs, 0, mbrs, 0, mbr_counter);

	// MBRRegister.batchRegister(mbrs);
	// register the mbr for later analysis regarding the number of stable
	// states it hits during the backtracking
	LoggerManager.mbrsSize = mbrs.length;

    }

    public void showWorldinVision(Logger logger, Node node, Node sol) {

	ScenarioPanel sp = new ScenarioPanel();

	if (sol != null)
	    sp.run(mbrs, sol, true);
	else {
	    if (node != null) {
		// Use the Apporx solution
		ApproxSolution apps = new ApproxSolution(mbrs, node, logger);
		apps.getApporxSolution();
		sp.run(mbrs, node, false);
	    } else
		sp.run(mbrs);

	}

    }






}
