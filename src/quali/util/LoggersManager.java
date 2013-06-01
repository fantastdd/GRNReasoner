package quali.util;

import java.util.LinkedList;


public class LoggersManager {
	 public static int timeLimit = 6; // timeLimit in seconds
	 public static int attempts = 5; // number of attempts
	 private static LinkedList<Logger> loggers = new LinkedList<Logger>();
	 public static int mbrsSize = 0;
	 public static Logger createLogger()
	 {
		 Logger logger = new Logger();
		 logger.createProfiles(mbrsSize);
		 loggers.add(logger);
		 return logger;
	 }
	 public static Logger merge()
	 {
		 Logger mergeLogger = new Logger();
		 mergeLogger.createProfiles(mbrsSize);
		 for (Logger logger : loggers)
		 {
			 for (int mbr_index = 0 ; mbr_index < mbrsSize ; mbr_index ++)
			 {
				int[] unarys = logger.mbrs.get(mbr_index);
				for (int i = 0 ; i < unarys.length ; i++ )
				{
					mergeLogger.mbrs.get(mbr_index)[i] += unarys[i];
				}
			 }
		 }
		 return mergeLogger;
	 }
}
