package quanti.test;

import java.util.TimerTask;

import quali.Backtracking;
/**
 * This class is a timer that will terminate a backtracking task when reaching time limits.
 * @author Gary , Jochen
 *
 */
public class TerminateTestThread extends TimerTask {

    private TestThread bt;

    public TerminateTestThread(TestThread bt)
    {
	this.bt = bt;
	
	
    }
    
    @Override
    public void run() {
	System.out.println("terminate ######### ");
	bt.terminate();
    }
    
    

}
