package quali;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import quanti.QuantiShapeCalculator;
import ab.WorldinVision;

import common.util.NeighborComparator;

public class TestNode {

private HashMap<Integer,Configuration> confs ;
//the pointer points to the next configuration (pop())
public int current_id= -1;

//the pointer points to the next stability verification conf
public int stability_id = 0;

// 0-i confs have been instantiated
public boolean instaniatedUntilIndex(int i)
{
		return i <= current_id;
}
public int nextStabilityVerificationCandidate()
{
   return stability_id++; 	
}
public boolean isStabilityVerificationCompleted()
{
	return stability_id >= confs.size() - 1;
}





public TestNode()
{
   confs = new HashMap<Integer,Configuration>();	
}
public TestNode(List<MBR> mbrs, List<MBR> edges)
{
	confs = new HashMap<Integer,Configuration>();
	for (MBR mbr : mbrs)
	{
		confs.put(mbr.getId(),  new Configuration(mbr));
		if(edges.contains(mbr))
			confs.get(mbr.getId()).setEdge(true);
	}
	
	 initialize();
	

}
public Configuration lookup(int id)
{
   return confs.get(id);	
}
public Configuration lookup(MBR mbr)
{
     return lookup(mbr.getId());	
}
public Configuration pop()
{
	if(++current_id < confs.size())
			return confs.get(current_id);
	else
			return null;
}
public boolean isCompleted()
{
     return current_id >= confs.size() - 1;	
}

//test the overlapping blocks
public void initialize()
{
		
	for (int i = 0; i < confs.size(); i++)
	{
		Configuration conf = lookup(i);
		MBR mbr = conf.getMbr();
		for (int j = 0; j < confs.size() ; j++)
		{
		 if (i != j) {
					Configuration _conf = lookup(j);
					MBR mbr1 = _conf.getMbr();
					//System.out.println(" test  " + mbr +  "   "+ mbr1   );
					if(QuantiShapeCalculator.isIntersected(mbr, mbr1, false))
					{
						
						conf.getOverlapping_mbrs().add(mbr1);
						conf.getContact_map().put(mbr1.getId(),new Contact() );
						
						Neighbor neighbor = new Neighbor(mbr1,(byte)0,0);
			             conf.getNeighbors().add(neighbor);
						
			           // System.out.println(" trigger the containing case" +  mbr +  "  " + mbr1);
					}
					else
					{
						Neighbor _neighbor  = createNeighbor(mbr,mbr1);
						//System.out.println(mbr + "  construct  " + mbr1 + "   " + _neighbor.getGap() + "   " + _neighbor.getNeighborType());
						if(	_neighbor != null	)
							conf.getNeighbors().add(_neighbor);
					}
		 }
		}
		
		// sort the neighbors according the gap in between in ascending order.
	    Collections.sort(conf.getNeighbors(), new NeighborComparator());
	    
	    for (Neighbor neighbor : conf.getNeighbors())
	    {
	    	if(neighbor.getGap() > WorldinVision.gap) 
	    	{	
	    		conf.lastValidNeighborId =  conf.getNeighbors().indexOf(neighbor) - 1; 
	    	    break;
	    	}
	    	else
	    	{
	    		//initialize the contact map
	    		conf.getContact_map().put(neighbor.getMbr().getId(), new Contact());
	    		//System.out.println(conf.getMbr() + "   " + neighbor.getMbr());
	    	}
	    }
	    // the mbr touches all others.
	    if(conf.lastValidNeighborId == -2)
	    	conf.lastValidNeighborId = conf.getNeighbors().size() - 1;

	    		
	      

	    
	    
	    //========================== Early Determination: Those who do not have neighbors from region 3 will be considered to be regular ======================
	    {
	          if(conf.lastValidNeighborId == -1 )
	        	  conf.setEdge(true);
	          else
	          {
	        	  int count = 0;
	        	  for (Neighbor neighbor : conf.getNeighbors())
	        	  {
	        		  if(neighbor.getNeighborType() == 3)
	        			  count++;
	        	  }
	        	  if(count == 0)
	        		  conf.setEdge(true);
	          }
	    }
	    
	    
	    //=====================DEBUG output the neighbor 
	    {
	    	System.out.println(conf.getMbr() +"  " + conf.getMbr().getBounds() + "  lastValid Neighbor id :  " + conf.lastValidNeighborId + "     edge:  " + conf.isEdge() + "  unary " + conf.unary);
	    	for (Neighbor neighbor: conf.getNeighbors())
	    	{
	    		
	    		System.out.println("    " + neighbor.getMbr() + "    " + neighbor.getGap() + "   index:  " + conf.getNeighbors().indexOf(neighbor.getMbr()) + " neighbor type:  " + neighbor.getNeighborType());
	    		
	    	}
	    	
	    }
	    //===========================
	    
	    //System.out.println("  conf " + conf.getMbr() + "   " +  " last valid id:  " + conf.lastValidNeighborId);
	     
	}
	


	
	
}

private Neighbor createNeighbor(MBR pmbr, MBR rmbr)
{


	double r_x = rmbr.getX();
	double r_y = rmbr.getY();
	double r_mx = rmbr.getX() + rmbr.getWidth();
	double r_my = rmbr.getY() + rmbr.getHeight();
	

	double x = pmbr.getX();
	double y = pmbr.getY();
	double mx = pmbr.getX() + pmbr.getWidth();
	double my = pmbr.getY() + pmbr.getHeight();
	
	Neighbor neighbor = null;
	//System.out.println("   go to here " + (r_my > y || r_y < my) );

		if ( r_x >= mx && r_my > y && r_y < my )
       {
			//in the view region 4.
	       neighbor = new Neighbor(rmbr,(byte)4, r_x - mx);						    
		}
		else
			if(r_mx <= x && r_my > y && r_y < my)
			{
				//in the view region 2
				 neighbor = new Neighbor(rmbr,(byte)2, x - r_mx);				
			
			}
			else 
				if (r_y >= my && r_x < mx && r_mx > x)
					//in the view region 3
						neighbor = new Neighbor(rmbr,(byte)3, r_y - my);				
			else
				if(r_my <= y  && r_x < mx && r_mx > x)
					// in the view region 1
					{
						neighbor = new Neighbor(rmbr,(byte)1, y - r_my);	
				   }
	

	
	return neighbor;
	
	
}
public HashMap<Integer, Configuration> getConfs() {
	return confs;
}
public TestNode clone()
{
	TestNode _node = new TestNode();
     //clone hash map
	for (Integer key: confs.keySet())
	{
		_node.getConfs().put(key, confs.get(key).clone());
	}
	_node.current_id = current_id;
	_node.stability_id = stability_id;
   
	return _node;
}
public void update(Configuration newlyUpdatedConf)
{

	   confs.put(newlyUpdatedConf.getMbr().getId(), newlyUpdatedConf);
	   //also update the contact map if necessary
	  for (Integer neighbor_mbrid  : newlyUpdatedConf.getContact_map().keySet())
	  {
		
		  Configuration neighbor_conf = lookup(neighbor_mbrid);
		  HashMap<Integer,Contact> neighbor_contactMap = neighbor_conf.getContact_map();
		  Integer mbr = newlyUpdatedConf.getMbr().getId();
	
		  if (neighbor_contactMap.containsKey(mbr))
		  {
	    		Contact contact =  ContactManager.getPairContact(newlyUpdatedConf,neighbor_conf,WorldinVision.gap);
	    		neighbor_contactMap.put(mbr, contact);	
	    	    /* System.out.println( neighbor_mbrid +  " updates   the contact map using  " + mbr + "    " + newlyUpdatedConf.getContact_map().get(neighbor_mbrid) + " on  " + contact
	    	    		 + contact.points[0]);*/
	    }	  
	    	
	
	  }
}
public String toString()
{
	String result = "================= The Configurations  ============\n";
	for (Integer id : confs.keySet())
	{
		result += confs.get(id) + "\n";
		
	}
	result += "================= End  =============\n";
	return result;
	
}
}
