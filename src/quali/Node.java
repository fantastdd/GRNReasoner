package quali;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.LinkedList;

import quanti.QuantiShapeCalculator;

public class Node {
public LinkedList<MBR> mbrs;
public LinkedList<MBR> completed_mbrs; 
public boolean completed;
public HashMap<MBR,Configuration> confmaps = new HashMap<MBR,Configuration>();

public Configuration lookupConf(MBR mbr)
{
   return confmaps.get(mbr);	
}

public void updateRecord(MBR mbr,Configuration conf)
{
   this.confmaps.put(mbr, conf);	

}

public boolean isCompleted() {
	return mbrs.isEmpty();
}
public void reset()
{
     this.mbrs = completed_mbrs;
     this.completed_mbrs = new LinkedList<MBR>();

}
public LinkedList<MBR> getMbrs() {
	return mbrs;
}

public void setMbrs(LinkedList<MBR> mbrs) {
	this.mbrs = mbrs;
}

public LinkedList<MBR> getCompleted_mbrs() {
	return completed_mbrs;
}

public void setUpdated_mbrs(LinkedList<MBR> updated_mbrs) {
	this.completed_mbrs = updated_mbrs;
}

public MBR pop()
{
    MBR mbr = mbrs.pop();
    return mbr;
}
public void push(MBR mbr)
{
	completed_mbrs.push(mbr);
}
public Node clone()
{
	LinkedList<MBR> _mbrs = new LinkedList<MBR>();
	for(MBR mbr:this.mbrs)
	{
		_mbrs.add(mbr);
	}
	
	LinkedList<MBR> _updated_mbrs = new LinkedList<MBR>(); 
	for(MBR umbr:this.completed_mbrs)
	{
		_updated_mbrs.add(umbr);
	}
	
	Node node =  new Node(_mbrs,_updated_mbrs);
	node.completed = this.completed;
	for(MBR key: this.confmaps.keySet())
	{
		Configuration conf = this.confmaps.get(key).clone();
		node.confmaps.put(key, conf);
	}
	return node;
			


}

public Node(LinkedList<MBR> mbrs, LinkedList<MBR> updated_mbrs) {
	super();
	this.mbrs = mbrs;
	this.completed_mbrs = updated_mbrs;
   
}
public Node(LinkedList<MBR> mbrs, LinkedList<MBR> updated_mbrs, LinkedList<MBR> edge) {
	super();
	this.mbrs = mbrs;
	this.completed_mbrs = updated_mbrs;
    initialize(edge);

}
// test the overlapping blocks
public void initialize(LinkedList<MBR> edge)
{
	
	for(MBR mbr: mbrs)
	{
		Configuration configuration = new Configuration(mbr);
	   
		if(edge.contains(mbr))
		{	
			//Debug.echo(this, mbr);
			configuration.setEdge(true);
			
		}
		confmaps.put(mbr, configuration);
	}
	
	
	for (int i = 0; i < mbrs.size() - 1; i++)
	{
		MBR mbr = mbrs.get(i);
		Configuration configuration = lookupConf(mbr);
		for (int j = i + 1; j < mbrs.size(); j++)
		{
			MBR mbr1 = mbrs.get(j);
			
			if(!QuantiShapeCalculator.isIntersected(mbr, mbr1).isEmpty())
			{
				
				configuration.getOverlapping_mbrs().add(mbr1);
				configuration.getContact_map().put(mbr1,new Contact() );
			    Short[] id = new Short[3];
			    id[0] = 0;
			    id[1] = 0;
			    id[2] = (short)mbr1.getId();
			    configuration.getNeighbors().add(id);
			    		
				
				
				Configuration configuration1 = lookupConf(mbr1);
				configuration1.getOverlapping_mbrs().add(mbr);
				configuration1.getContact_map().put(mbr,new Contact() );
			    Short[] _id = new Short[3];
			    _id[0] = 0;
			    _id[1] = 0;
			    _id[2] = (short)mbr.getId();
			    configuration.getNeighbors().add(_id);
			    
				detectBlockedRegion(configuration,configuration1);
				detectBlockedRegion(configuration1,configuration);
				
				
			}
			else
			{
				Short[] id = createTreeId(mbr,mbr1);
				configuration.getNeighbors().add(id);
				
			}
		}
	}

}
// use the tree structure 
private Short[] createTreeId(MBR pmbr, MBR rmbr)
{


	double r_x = rmbr.getX();
	double r_y = rmbr.getY();
	double r_mx = rmbr.getX() + rmbr.getWidth();
	double r_my = rmbr.getY() + rmbr.getHeight();
	

	double x = pmbr.getX();
	double y = pmbr.getY();
	double mx = pmbr.getX() + pmbr.getWidth();
	double my = pmbr.getY() + pmbr.getHeight();
	
	Short[] id = new Short[3];
	
	if(r_my > y || r_y < my)
	{
		if ( r_x > mx)
		
		{
			//in the view region 4.
			id[0] = 4;
			id[1] = (short)(r_x - mx);
			
		    
		}
		else
			if(r_mx < x)
			{
				//in the view region 2
				id[0] = 2;
				id[1] = (short)(x - r_mx);
			
			}
		
	}
	else if (r_mx > x || r_x < mx)
	{
		if (r_y > my)
			//in the view region 3
		{
			id[0] = 3;
			id[1] = (short)(r_y - my);
			
		}
		else
			if(r_my < y)
				// in the view region 1
				{id[0] = 1;
				id[1] = (short)(y - r_my);
				}
	}

	id[2] = (short)rmbr.getId();
	
	return id;
	
	
}


private void detectBlockedRegion(Configuration conf,Configuration conf1)
{
	Rectangle intersectedRec = conf1.getMbr().intersection(conf.getMbr());
	if((intersectedRec.contains(conf.x,conf.y) && intersectedRec.contains(conf.x + conf.limit_horizontal,conf.y))
			||
	   (intersectedRec.contains(conf.x + conf.width - conf.limit_horizontal,conf.y + conf.height) && intersectedRec.contains(conf.x + conf.width,conf.y+conf.height))
			
			)
		conf.getBlocked_regions().get(3).add(conf1.getMbr());
	
	if((intersectedRec.contains(conf.x,conf.y) && intersectedRec.contains(conf.x,conf.y + conf.limit_vertical))
		||
		(intersectedRec.contains(conf.x + conf.width,conf.y + conf.height) && intersectedRec.contains(conf.x + conf.width,conf.y + conf.height - conf.limit_vertical))
			)
		conf.getBlocked_regions().get(0).add(conf1.getMbr());
	if((intersectedRec.contains(conf.x,conf.y + conf.height - conf.limit_vertical) && intersectedRec.contains(conf.x,conf.y + conf.limit_vertical))
		||
		(intersectedRec.contains(conf.x + conf.width,conf.y) && intersectedRec.contains(conf.x + conf.width, conf.y + conf.limit_vertical))
			)
		conf.getBlocked_regions().get(1).add(conf1.getMbr());
	
	if((intersectedRec.contains(conf.x,conf.y + conf.height) && intersectedRec.contains(conf.x + conf.limit_horizontal,conf.y + conf.height))
			||
	  (intersectedRec.contains(conf.x + conf.width - conf.limit_horizontal,conf.y) && intersectedRec.contains(conf.x + conf.width,conf.y))	
			)
		conf.getBlocked_regions().get(2).add(conf1.getMbr());
	
	
	}



public String toString()
{
	String result = " node details \n";
	
    for(MBR mbr: this.completed_mbrs)
    {
    	
    	result+= "completed mbrs:  " + mbr + "  " + this.lookupConf(mbr) + "\n";
    }
    
    for(MBR mbr: this.mbrs)
    {
    	
    	//result+= "Non completed mbrs:  " + mbr + "  " + this.lookupConf(mbr) + "\n";
    	result+= "  " + mbr + "  " + this.lookupConf(mbr) + "\n";
    }
    
    
    return result;
}


}
