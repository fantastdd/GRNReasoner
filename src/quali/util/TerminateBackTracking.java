package quali.util;

import java.util.TimerTask;

import quali.Backtracking;
/**
 * This class is a timer that will terminate a backtracking task when reaching time limits.
 * @author Gary , Jochen
 *
 */
public class TerminateBackTracking extends TimerTask {

    private Backtracking bt;

    public TerminateBackTracking(Backtracking bt)
    {
	this.bt = bt;
	
	
    }
    
    @Override
    public void run() {
	bt.terminate();
    }
    
    

}
