package quali;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import quanti.QuantiShapeCalculator;

public class TestNode {

private HashMap<Integer,Configuration> confs ;
private int current_id= -1;

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
		
	}
	for (MBR edge : edges)
	{
		confs.get(edge).setEdge(true);
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
		
	for (int i = 0; i < confs.size() -1 ; i++)
	{
		Configuration conf = lookup(i);
		MBR mbr = conf.getMbr();
		for (int j = i + 1; j < confs.size(); j++)
		{			
			Configuration _conf = lookup(j);
			MBR mbr1 = _conf.getMbr();
			if(!QuantiShapeCalculator.isIntersected(mbr, mbr1 ).isEmpty())
			{
				
				conf.getOverlapping_mbrs().add(mbr1);
				conf.getContact_map().put(mbr1,new Contact() );
				
				Neighbor neighbor = new Neighbor(mbr1,(byte)0,0);
	             conf.getNeighbors().add(neighbor);
			    		
				
				Neighbor _neighbor = new Neighbor(mbr,(byte)0,0);			    
				_conf.getNeighbors().add(_neighbor);
			}
			else
			{
				Neighbor _neighbor  = createNeighbor(mbr,mbr1);
				if(	_neighbor != null	)
					conf.getNeighbors().add(_neighbor);
			}
		}
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
	
	if(r_my > y || r_y < my)
	{
		if ( r_x >= mx)
		
		{
			//in the view region 4.
	       neighbor = new Neighbor(rmbr,(byte)4, r_x - mx);				
		    
		}
		else
			if(r_mx <= x)
			{
				//in the view region 2
				 neighbor = new Neighbor(rmbr,(byte)2, r_mx - x);				
			
			}
		
	}
	else if (r_mx > x || r_x < mx)
	{
		if (r_y >= my)
			//in the view region 3
			 neighbor = new Neighbor(rmbr,(byte)3, r_y - my);				
		else
			if(r_my <= y)
				// in the view region 1
				neighbor = new Neighbor(rmbr,(byte)1, r_my - y);			
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
   
	return _node;
}
public void update(Configuration conf)
{
	   confs.put(conf.getMbr().getId(), conf);
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
