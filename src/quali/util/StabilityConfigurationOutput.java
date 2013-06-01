package quali.util;

import java.util.HashMap;

import quali.Configuration;
import quali.Contact;
import quali.Node;

public class StabilityConfigurationOutput {

  public static String getStabilityReport(Node node)
  {
	String result = "============= Stability Report ===============" + "\n";
	HashMap<Integer , Configuration > confs = node.getConfs();
	for (Integer key : confs.keySet())
	{
		result += getStabilityReport(confs.get(key) , node) + "\n";
	}
	result +=" ================= End of Report ============== \n";  
	return result;
  }	
  
  //although there is a toString() in Configuration class, we need this method to provide a understandable content regarding the stability configuration.
  public static String getStabilityReport(Configuration conf , Node node)
  {
	//weak support parameter included for the regular mbr;
	    String report = conf.toShortString();
		
		
		if(conf.isEdge())
			report += " is an edge or supported by an edge or misconfigured";
		else{
				boolean left_support = false;
				boolean right_support = false;
			
				String weakSupportInfo = " is weakly supported by ";
				String strongSupportInfo = " is Strongly supported by ";
				
				for(Integer mbr: conf.getContact_map().keySet())
				{
					Contact contact = conf.getContact_map().get(mbr);
					String _conf_str = node.lookup(mbr).toShortString();
					if(contact.getType() == 1)
					{
						if(contact.getTangential_area() == 3)
						{
							  left_support = true;
							  weakSupportInfo += "  " + _conf_str + " at the left";
							  strongSupportInfo += "  " + _conf_str + " at the left";
							  
						}
						else
							if(contact.getTangential_area() == 4)
							 {
							   right_support = true;
							   weakSupportInfo += "  " + _conf_str + " at the right";
							   strongSupportInfo += "  " + _conf_str + " at the right";
							 }
							else
								if(contact.getTangential_area() == 34)
								{
									left_support = true;
									right_support = true;
									strongSupportInfo += "  " + _conf_str + " at the middle ";
								}
					}
				}
				if(left_support && right_support)
				    report += strongSupportInfo;
				else
					report += weakSupportInfo;
		}
	  return report;
  }
  
  
  
  public static String getStabilityShortReport(Node node)
  {
	String result = "\n============= Stability Short Report ===============" + "\n";
	HashMap<Integer , Configuration > confs = node.getConfs();
	int count = 0;
	for (Integer key : confs.keySet())
	{
		if(count++ % 3 == 0)
			result+="\n";
		result +=  confs.get(key) .toShortString() + "  ";
	}
	result +="\n ================= End of Short Report ============== \n";  
	return result;
  }	
  
  
	
	
}
