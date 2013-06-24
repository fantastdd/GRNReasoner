package quanti.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TestThread implements Runnable {

    volatile boolean sign = true;
    public void terminate()
    {
	sign = false;
    }
    @Override
    public void run() {
	
        System.out.println("Thread is running");
        while(sign);
        System.out.println(" Thread is end ");
    }
   public static void main(String args[])
   {
      /* for (int i = 0; i < 3; i++ )
       {
	 
	   TestThread tt = new TestThread();
	   TerminateTestThread ttt = new TerminateTestThread(tt);
	   Timer timer = new Timer();
	   timer.schedule(ttt, 3000);
	   Thread thread = new Thread(tt);
	   thread.start();
	   tt.run();
       }*/
       ExecutorService executor = Executors.newFixedThreadPool(10);
       int count = 10;
       while(count-- > 0)
	   executor.submit(new TestThread());
       try {
	   executor.awaitTermination(10, TimeUnit.SECONDS);
	   
    } catch (InterruptedException e) {
	
	e.printStackTrace();
    } 
       System.out.println("  start to wait ");
     
   }
}
