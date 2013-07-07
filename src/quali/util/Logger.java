package quali.util;

import java.util.HashMap;
import java.util.Random;

import quali.MBR;

public class Logger {
    public HashMap<Integer, int[]> mbrs = new HashMap<Integer, int[]>();
   


    public int uncheckedBlocks = 0;

    public void createProfiles(int size) {

	for (int i = 0; i < size; i++) {
	    int[] unarys = new int[5];
	    mbrs.put(i, unarys);
	}
    }

    public synchronized void recordUnary(MBR mbr, int unary) {
	assert (mbrs.containsKey(mbr.uid));

	++mbrs.get(mbr.uid)[unary];
    }

    public void recordAsEdge(MBR mbr) {
	mbrs.get(mbr.uid)[0] = LoggerManager.edgeValue;
    }

    public int getMostLikelyUnary(int key) {
	assert (mbrs.containsKey(key));
	int[] unarys = mbrs.get(key);
	int max = 0;
	int j = 0;
	System.out.println(" MBR uid " + key);
	for (int i = 0; i < 5; i++) {
	    System.out.print("  " + unarys[i]);
	    if (unarys[i] > max) {
		max = unarys[i];
		j = i;
	    }
	}
	System.out.println();
	if (max < LoggerManager.threshold) {
	    j = -1;
	}
	return j;
    }

    public int[] generateOrder() {

	uncheckedBlocks = 0;
	for (Integer key : mbrs.keySet()) {
	    int total = 0;
	    int[] unarys = mbrs.get(key);
	    for (int val : unarys)
		total += val;
	    if (total < LoggerManager.threshold)
		uncheckedBlocks++;
	}

	System.out.println(" number of un-checked blocks:  " + uncheckedBlocks);
	int[] priority = new int[uncheckedBlocks];
	int[] completed = new int[mbrs.size() - uncheckedBlocks];
	int[] all = new int[mbrs.size()];

	uncheckedBlocks = 0;
	int _counter = 0;
	for (Integer key : mbrs.keySet()) {
	    int total = 0;
	    int[] unarys = mbrs.get(key);
	    for (int val : unarys)
		total += val;
	    if (total < LoggerManager.threshold) {
		priority[uncheckedBlocks++] = key;
	    } else {
		completed[_counter++] = key;
	    }
	}

	// Randomly sort the priority array
	Random random = new Random();
	for (int i = priority.length - 1; i > 1; i--) {
	    int randIndex = random.nextInt(i);
	    int temp = priority[i];
	    priority[i] = priority[randIndex];
	    priority[randIndex] = temp;
	}
	System.arraycopy(priority, 0, all, 0, priority.length);

	System.arraycopy(completed, 0, all, priority.length, completed.length);

	
	return all;
    }
    
    public String toString()
    {
	
	String result = "";
	for (int key: mbrs.keySet())
	{
	    int[] unary = mbrs.get(key);
	    result += " MBR uid " + key + "  unary";
	    for (int i : unary)
	    {
		result +=  "  " + i; 
	    }
	    result += "\n";
	}
	return result;
	
    }

}
