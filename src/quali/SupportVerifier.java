package quali;

import java.util.HashMap;
import java.util.LinkedList;

public class SupportVerifier {
private static LinkedList<Node> updatedList = new LinkedList<Node>();	
public static void enforceConsistency(Node node)
{
	  updatedList.add(node);
     
}
public static void supportExpand(Node node)
{
	  for (MBR mbr:node.mbrs)
	  {
		  verifySupport(mbr,node.lookupConf(mbr));
	  }
}
private static LinkedList<Configuration> verifySupport(MBR mbr, Configuration conf) {
	
	new LinkedList<Configuration>();
	
	return null;
	
	
}
public static boolean verifySupport(Configuration conf)
{
	  /* to vertex-support realation under an edge */
	 HashMap<MBR, Contact> cmap = conf.getContact_map();
	 for (MBR mbr: cmap.keySet())
	  {
		  cmap.get(mbr);
		  
		  
		  
	  }
      return false;
}
}
