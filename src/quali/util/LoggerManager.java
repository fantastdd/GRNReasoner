package quali.util;

import java.util.LinkedList;


public class LoggerManager {
	
	 private static LinkedList<Logger> loggers = new LinkedList<Logger>();
	 public static int mbrsSize = 0;
	 public final static int edgeValue = 100000;
	 public final static int threshold = 1000;// number of stable status a block hits during the s
	 public synchronized static Logger createLogger()
	 {
		 Logger logger = new Logger();
		 logger.createProfiles(mbrsSize);
		 loggers.add(logger);
		 return logger;
	 }
	 public static Logger merge()
	 {
	     	//System.out.println(" Merge the loggers ");
		 Logger mergeLogger = new Logger();
		 mergeLogger.createProfiles(mbrsSize);
		 for (Logger logger : loggers)
		 {
			 for (int mbr_index = 0 ; mbr_index < mbrsSize ; mbr_index ++)
			 {
				int[] unarys = logger.mbrs.get(mbr_index);
				for (int i = 0 ; i < unarys.length ; i++ )
				{
				    if(mergeLogger.mbrs.get(mbr_index)[i] < LoggerManager.edgeValue || unarys[i] < LoggerManager.edgeValue)
					mergeLogger.mbrs.get(mbr_index)[i] += unarys[i];
				}
			 }
		 }
		  System.out.println("  print the logger  " + mergeLogger);
		 return mergeLogger;
	 }
}
