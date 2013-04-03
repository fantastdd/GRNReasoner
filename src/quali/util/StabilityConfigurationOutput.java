package quali.util;

import java.util.HashMap;

import quali.Configuration;
import quali.Contact;
import quali.TestNode;

public class StabilityConfigurationOutput {

  public static String getStabilityReport(TestNode node)
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
  public static String getStabilityReport(Configuration conf , TestNode node)
  {
	//weak support parameter included for the regular mbr;
	    String report = conf.toShortString();
		
		
		if(conf.isEdge())
			report += " is an edge or supported by edge or misconfigured";
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
  
  
  
	
	
	
}
