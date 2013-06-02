package quanti.test;

import java.util.Timer;

public class TestThread implements Runnable {

    volatile boolean sign = true;
    public void terminate()
    {
	sign = false;
    }
    @Override
    public void run() {
	// TODO Auto-generated method stub
        System.out.println("Thread is running");
        while(sign);
        System.out.println(" Thread is end ");
    }
   public static void main(String args[])
   {
       for (int i = 0; i < 3; i++ )
       {
	 
	   TestThread tt = new TestThread();
	   TerminateTestThread ttt = new TerminateTestThread(tt);
	   Timer timer = new Timer();
	   timer.schedule(ttt, 3000);
	   /*Thread thread = new Thread(tt);
	   thread.start();*/
	   tt.run();
       }
   }
}
