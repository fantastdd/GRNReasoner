package quali.util;

import java.util.HashMap;

import quali.MBR;

public class Logger {
 public static HashMap<Integer , int[]> mbrs = new HashMap<Integer , int[]>();
 public static int timeLimit = 20; // timeLimit in mins
 public static boolean inUse = true;
 public static void createProfiles(int size)
 {
	
	 for (int i = 0; i < size ; i++)
	 {
		 int[] unarys = new int[5];
		 mbrs.put(i, unarys);
	 }
 }
 public static void recordUnary(MBR mbr, int unary)
 {
	 assert(mbrs.containsKey(mbr.getId()));
	
     ++ mbrs.get(mbr.getId())[unary];
 }
 public static int getMostLikelyUnary(int key)
 {
	 assert(mbrs.containsKey(key));
	 int[] unarys = mbrs.get(key);
     int max = 0;
     int j = 0;
     System.out.println(" MBR " + key);
	 for (int i = 0 ; i < 5 ; i++)
     {
		 System.out.print("  " + unarys[i]);
    	 if(unarys[i] > max)
    	 {	 
    		 max = unarys[i];
    	     j = i;
    	 } 
     }
	 System.out.println();
     return j;
 }
}
