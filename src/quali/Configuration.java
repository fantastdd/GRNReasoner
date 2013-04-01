package quali;

import java.awt.Point;
import java.util.HashMap;
import java.util.LinkedList;

import common.MyPolygon;

public class Configuration {
	
private int angular;
private HashMap<Integer,LinkedList<MBR>> blocked_regions = new HashMap<Integer,LinkedList<MBR>>();
private HashMap<MBR,Contact> contact_map = new HashMap<MBR,Contact>();
private MyPolygon core_left = new MyPolygon();

private MyPolygon core_left1 = new MyPolygon();
private MyPolygon core_left3 = new MyPolygon();
private MyPolygon core_right = new MyPolygon();
private MyPolygon core_right2 = new MyPolygon();
private MyPolygon core_right4 = new MyPolygon();

private MyPolygon diagonal_left = new MyPolygon();
private MyPolygon diagonal_right = new MyPolygon();
private boolean edge = false;
public int height;
public int width;
public int hheight = height/2;
public int hwdith = width/2;
public int limit_horizontal;
public int limit_vertical;
private MBR mbr;
private LinkedList<Neighbor> neighbors = new LinkedList<Neighbor>();
private LinkedList<MBR> overlapping_mbrs = new  LinkedList<MBR>();
private int[] permit_regions;

private LinkedList<Point> points1 = new LinkedList<Point>();
private LinkedList<Point> points11 = new LinkedList<Point>();
private LinkedList<Point> points2 = new LinkedList<Point>();
private LinkedList<Point> points21 = new LinkedList<Point>();


private LinkedList<Point> points3 = new LinkedList<Point>();
private LinkedList<Point> points31 = new LinkedList<Point>();
/* store all the points that potentially contact with the actual shape*/
private LinkedList<Point> points4 = new LinkedList<Point>();
/* store all the points that contact with the actual shape*/
private LinkedList<Point> points41 = new LinkedList<Point>();

private MyPolygon triangle1 = new MyPolygon();
private MyPolygon triangle11 = new MyPolygon();

private MyPolygon triangle2 = new MyPolygon();
private MyPolygon triangle22 = new MyPolygon();
private MyPolygon triangle3 = new MyPolygon();
private MyPolygon triangle33 = new MyPolygon();
private MyPolygon triangle4 =  new MyPolygon();
private MyPolygon triangle44 =  new MyPolygon();

public int unary = -1;//0 = regular, 1 = lean to right, 2 = lean to left, -1 = not initialized, -2 = completed.

public boolean v1 = false;
public boolean v2 = false;
public boolean v3 = false;
public boolean v4 = false;
public Point[] vertices = new Point[4];


public int x;
public int y;


public int lastTestNeighborId = -1;
public boolean left_support = false;
public boolean right_support = false;
public int lastValidNeighborId = -1; //id = -1, no valid neighbors
//TODO write a isNowSupport method in configuration

public boolean isNowSupport(final Configuration tconf )
{
	boolean result = false;

  if(!tconf.getMbr().equals(mbr))
  { 
	//  System.out.println(tconf.getMbr() + "  " + mbr);
	Contact contact =  MBRRegisterAdvance.getPairContact(tconf, this);
	
	if(this.isEdge())
		result = true;
	else{
				//Debug.echo(this, this.contact_map.size(),contact,contact.getType());
				if(contact.getType() == 1)
				{
					if(contact.getTangential_area() == 3)
						left_support = true;
					else
						if(contact.getTangential_area() == 4)
							right_support = true;
						else
							if(contact.getTangential_area() == 34)
							{
								left_support = true;
								right_support = true;
							}
				}
			
		result = left_support && right_support;
	    return result;
	}
  }
	return result;
}
public Configuration(MBR mbr)
{
	
	   permit_regions = new int[4];
	   permit_regions[0] = 1;
	   permit_regions[1] = 1;
	   permit_regions[2] = 1;
	   permit_regions[3] = 1;
	   angular = 2;
	    getBlocked_regions().put(0, new LinkedList<MBR>());
	    getBlocked_regions().put(1, new LinkedList<MBR>());
	    getBlocked_regions().put(2, new LinkedList<MBR>());
	    getBlocked_regions().put(3, new LinkedList<MBR>());
        this.mbr = mbr;
        x = mbr.x;
        y = mbr.y;
        height = mbr.height;
        width = mbr.width;
       
       if( height > width)
       {
       	 limit_horizontal = width/2;
       	 limit_vertical = height/2 - (int) Math.sqrt( (height/2) * (height/2) - (width/2)*(width/2) );
       }
       else
       {
       	
       	 limit_vertical = height/2;
       	 limit_horizontal = width/2 - (int) Math.sqrt(  (width/2)*(width/2) - (height/2) * (height/2) );
       }
  
    triangle4.addPoint( x +  width -  limit_horizontal, y +  height);
   	triangle4.addPoint( x +  width, y +  height);
   	triangle4.addPoint( x +  width, y +  height -  limit_vertical);
       
   	triangle3.addPoint( x, y +  height -  limit_vertical);
   	triangle3.addPoint( x, y +  height);
   	triangle3.addPoint( x +  limit_vertical, y +  height);
       
       
   	triangle2.addPoint( x, y);
   	triangle2.addPoint( x, y +  limit_vertical);
   	triangle2.addPoint( x +  limit_horizontal, y);
       
   	triangle1.addPoint( x +  width -  limit_horizontal, y);
   	triangle1.addPoint( x +  width, y);
   	triangle1.addPoint( x +  width, y +  limit_horizontal);
   	
   	triangle44.addPoint( x, y +  height);
   	triangle44.addPoint( x +  width, y +  height);
   	triangle44.addPoint( x +  width, y);
       
   	triangle33.addPoint( x, y);
   	triangle33.addPoint( x, y +  height);
   	triangle33.addPoint( x +  width, y +  height);
       
       
   	triangle22.addPoint( x, y);
   	triangle22.addPoint( x, y +  height);
   	triangle22.addPoint( x +  width, y);
       
   	triangle11.addPoint( x, y);
   	triangle11.addPoint( x +  width, y +  height);
   	triangle11.addPoint( x +  width, y);
   	
   	core_right.addPoint( x,  y +  height -  limit_vertical);
   	core_right.addPoint( x +  limit_horizontal,  y +  height);
   	core_right.addPoint( x +  width,  y +  limit_vertical);
   	core_right.addPoint( x +  width -  limit_horizontal,  y);
   	
   	
 	  core_right2.addPoint( x,  y +  height -  limit_vertical);
   	core_right2.addPoint(( x +  limit_horizontal+ x)/2, ( y +  height +  y +  height -  limit_vertical)/2);
   	core_right2.addPoint(( x +  width +  x +  width -  limit_horizontal)/2, ( y +  limit_vertical+ y)/2);
   	core_right2.addPoint( x +  width -  limit_horizontal,  y);
   	
   	
     	
   	core_right4.addPoint(( x +  width +  x +  width -  limit_horizontal)/2, ( y +  limit_vertical+ y)/2);
    core_right4.addPoint( x +  limit_horizontal,  y +  height);
  	core_right4.addPoint( x +  width,  y +  limit_vertical);
   	core_right4.addPoint(( x +  width +  x +  width -  limit_horizontal)/2, ( y +  limit_vertical+ y)/2);
 	
 	

   	diagonal_right.addPoint( x,  y +  height);
   	diagonal_right.addPoint( x +  width,  y);

   	

   	core_left.addPoint( x +  limit_horizontal,  y);
   	core_left.addPoint( x,  y +  limit_vertical);
   	core_left.addPoint( x +  width -  limit_horizontal,  y +  height);
   	core_left.addPoint( x +  width,  y +  height -  limit_vertical);
   	
   	core_left1.addPoint( x +  limit_horizontal,  y);
   	core_left1.addPoint(( x +  x +  limit_horizontal)/2, ( y +  limit_vertical +  y)/2);
   	core_left1.addPoint(( x +  width -  limit_horizontal+ x +  width)/2, ( y +  height+ y +  height -  limit_vertical)/2);
   	core_left1.addPoint( x +  width,  y +  height -  limit_vertical);
   	
   	core_left3.addPoint(( x +  x +  limit_horizontal)/2, ( y +  limit_vertical +  y)/2);
   	core_left3.addPoint( x,  y +  limit_vertical);
  	core_left3.addPoint( x +  width -  limit_horizontal,  y +  height);
  	core_left3.addPoint(( x +  width -  limit_horizontal+ x +  width)/2, ( y +  height+ y +  height -  limit_vertical)/2);
   	
   	diagonal_left.addPoint( x +  width,  y +  height);
   	diagonal_left.addPoint( x , y);
   	
   	vertices[0] = new Point( x +  width,  y);
   	vertices[1]  = new Point( x, y);
   	vertices[2]  = new Point( x,  y +  height);
   	vertices[3]  = new Point( x +  width, y +  height);
        
}

public boolean addRestrictedPoints(Point point, int region,boolean contacted)
{
	boolean result = true;
   	if(region == 2)
   	{
   		for(Point p : this.points21)
   		{
   			if(p.x > point.x)
   			{
   				continue;
   			}
   			else
   				if(p.x == point.x)
   					result = false;
   				else if(p.x < point.x)
   				{
   					if(p.y <= point.y)
   						result = false;
   					else
   						continue;
   				}
   		}
   		
   		if(result && !contacted)
	   			points2.add(point);
	  
	        if(result && contacted)
	        {
	        	for(Point p: this.points2)
	        	{
	   	   			if(p.x > point.x)
	   	   			{
	   	   				if(p.y >= point.y)
	   	   					result = false;
	   	   				else
	   	   					continue;
	   	   			}
	   	   			else
	   	   				if(p.x == point.x)
	   	   					result = false;
	   	   				else if(p.x < point.x)
	   	   				{
	   	   						continue;
	   	   				}
	   	   		}
	        }
	    	if(result && contacted)
	   	 	{	
	   	 		points2.add(point);
	   	 		points21.add(point);
	   	 	
	   	 	}
   	}
   	else
   		if(region == 3)
   	   	{
   			/* first examine whether there are some shorter blocks supporting the object at left */
   	   		for(Point p : this.points31)
   	   		{
   	   			if(p.x > point.x)
   	   			{
   	   				continue;
   	   			}
   	   			else
   	   				if(p.x == point.x)
   	   					result = false;
   	   				else if(p.x < point.x)
   	   				{
   	   					if(p.y >= point.y)
   	   						result = false;
   	   					else
   	   						continue;
   	   				}
   	   		}
   	   		
   	   		if(result && !contacted)
   	   			points3.add(point);
   	   		/* then examine whether there are some higher blocks supporting the object at right*/
   	        if(result && contacted)
   	        {
   	        	for(Point p: this.points3)
   	        	{
   	   	   			if(p.x > point.x)
   	   	   			{
   	   	   				if(p.y <= point.y)
   	   	   					result = false;
   	   	   				else
   	   	   					continue;
   	   	   			}
   	   	   			else
   	   	   				if(p.x == point.x)
   	   	   					result = false;
   	   	   				else if(p.x < point.x)
   	   	   				{
   	   	   						continue;
   	   	   				}
   	   	   		}
   	        }
   	 	if(result && contacted)
   	 	{	
   	 		points3.add(point);
   	 		points31.add(point);
   	 	
   	 	}
   			
   	 	//Debug.echo(this, this.getMbr(),this.points3,result);
   	   	}
   		else
   	   		if(region == 4)
   	   	   	{
   	   	   		for(Point p : this.points41)
   	   	   		{
   	   	   			if(p.x < point.x)
   	   	   			{
   	   	   				continue;
   	   	   			}
   	   	   			else
   	   	   				if(p.x == point.x)
   	   	   					result = false;
   	   	   				else if(p.x > point.x)
   	   	   				{
   	   	   					if(p.y >= point.y)
   	   	   						result = false;
   	   	   					else
   	   	   						continue;
   	   	   				}
   	   	   		}
   	   	  if(result && !contacted)
 	   			points4.add(point);
 	   		
 	        if(result && contacted)
 	        {
 	        	for(Point p: this.points4)
 	        	{
 	   	   			if(p.x < point.x)
 	   	   			{
 	   	   				if(p.y <= point.y)
 	   	   					result = false;
 	   	   				else
 	   	   					continue;
 	   	   			}
 	   	   			else
 	   	   				if(p.x == point.x)
 	   	   					result = false;
 	   	   				else if(p.x > point.x)
 	   	   				{
 	   	   						continue;
 	   	   				}
 	   	   		}
 	        }
 	   	if(result && contacted)
   	 	{	
   	 		points4.add(point);
   	 		points41.add(point);
   	 	
   	 	}
 	 	
 	 	
   	   	   	}else
   	   	   		if(region == 1)
   	   	   	   	{
   	   	   	   		for(Point p : points11)
   	   	   	   		{
   	   	   	   			if(p.x < point.x)
   	   	   	   			{
   	   	   	   				continue;
   	   	   	   			}
   	   	   	   			else
   	   	   	   				if(p.x == point.x)
   	   	   	   					result = false;
   	   	   	   				else if(p.x > point.x)
   	   	   	   				{
   	   	   	   					if(p.y <= point.y)
   	   	   	   						result = false;
   	   	   	   					else
   	   	   	   						continue;
   	   	   	   				}
   	   	   	   		}
   	   	   	   	
   	   	   	if(result && !contacted)
   	   			points1.add(point);
   	   	
   	        if(result && contacted)
   	        {
   	        	for(Point p: this.points1)
   	        	{
   	   	   			if(p.x < point.x)
   	   	   			{
   	   	   				if(p.y >= point.y)
   	   	   					result = false;
   	   	   				else
   	   	   					continue;
   	   	   			}
   	   	   			else
   	   	   				if(p.x == point.x)
   	   	   					result = false;
   	   	   				else if(p.x > point.x)
   	   	   				{
   	   	   						continue;
   	   	   				}
   	   	   		}
   	        }
   	 	if(result && contacted)
   	 	{	
   	 		points1.add(point);
   	 		points11.add(point);
   	 	
   	 	}
   	   	   	   	}

   return result;


}


@Override
public Configuration clone()
{
	
  Configuration conf = new Configuration(mbr);
  
  
  conf.setAngular(angular);
  conf.permit_regions = permit_regions;
  
  
  conf.unary = unary;
  conf.lastTestNeighborId = lastTestNeighborId;
  conf.right_support = right_support;
  conf.left_support =  left_support;
  conf.lastValidNeighborId = lastValidNeighborId;
  
  
  
  for(Neighbor neighbor: neighbors)
  {
	  conf.getNeighbors().add(neighbor);
  }
  
  for(MBR _mbr:overlapping_mbrs)
  {
	  conf.getOverlapping_mbrs().add(_mbr);
  }
  
  for(Integer key: blocked_regions.keySet())
  {
	  LinkedList<MBR> mbrs = blocked_regions.get(key);
	  
	  conf.blocked_regions.put(key, new LinkedList<MBR>());
      
	  for(MBR mbr: mbrs)
		  conf.blocked_regions.get(key).add(mbr);
  }
  
  for(MBR key:  contact_map.keySet())
  {
	  conf.contact_map.put(key, contact_map.get(key).clone());
  }

  conf.setEdge(edge);
  



		  
  return conf;


}

public int getAngular() {
	if(unary == 0)
		angular = 0;
	else
		angular = 1;
	return angular;
}




public HashMap<Integer, LinkedList<MBR>> getBlocked_regions() {
	return blocked_regions;
}


public HashMap<MBR, Contact> getContact_map() {
	return contact_map;
}

public MyPolygon getCore_left() {
	

	
	return core_left;
}

public MyPolygon getCore_left1() {
	return core_left1;
}

public MyPolygon getCore_left3() {
	return core_left3;
}

public MyPolygon getCore_right() {

	
	
	return core_right;
}



public MyPolygon getCore_right2() {
	return core_right2;
}

public MyPolygon getCore_right4() {
	return core_right4;
}


public MyPolygon getDiagonal_left() {
	
	return diagonal_left;
}
public MyPolygon getDiagonal_right() {
	
	return diagonal_right;
}
public int getHeight() {
	return height;
}

public MBR getMbr() {
	return mbr;
}


public LinkedList<Neighbor> getNeighbors() {
	return neighbors;
}


public LinkedList<MBR> getOverlapping_mbrs() {
	return overlapping_mbrs;
}

public int[] getPermit_regions() {
	return permit_regions;
}

public MyPolygon getRegion(int region)
{
   MyPolygon result = new MyPolygon();
   
   if(region == 1)
	   result = getTriangle1();
   else if(region == 2)
	   result = getTriangle2();
   
   else if(region == 3)
	   result = getTriangle3();
   
   else if(region == 4)
	   result = getTriangle4();

   else if(region == 5)
   {
	   result.addPoint( x,  y);
	   result.addPoint( x, ( y +  getHeight()));
	   result.addPoint(( getX() +  getWidth()),( getY() +  getHeight()));
	   result.addPoint(( getX() +  getWidth()),getY());
   }
   
   return result;


}

public MyPolygon getRegionLarge(int region)
{
   MyPolygon result = new MyPolygon();
   
   if(region == 1)
	   result = getTriangle11();
   else if(region == 2)
	   result = getTriangle22();
   
   else if(region == 3)
	   result = getTriangle33();
   
   else if(region == 4)
	   result = getTriangle44();

   else if(region == 5)
   {
	   result.addPoint( x,  y);
	   result.addPoint( x,  y +  height);
	   result.addPoint( x +  width, y +  height);
	   result.addPoint( x +  width, y);
   }
   
   return result;


}

public MyPolygon getRegionLine(int region)
{
   MyPolygon result = new MyPolygon();
   
   if(region == 1)
   { 
	 if( unary == 0)
	 { 
	   result.addPoint(x + width, y);
	   result.addPoint(x + width/2, y);
	 }
	 else
	 {
		 result.addPoint(x + width, y);
		 result.addPoint(x + width -  limit_horizontal, y);
	 }
   }
   else if(region == 2)
   {  
	   if( unary == 0)
	   {
	     result.addPoint(( getX()), ( getY()));
		 result.addPoint(( getX() +  getWidth()/2), ( getY()));
	   }
	   else
	   {
		   result.addPoint(x, y);
		    result.addPoint(x +  limit_horizontal, y);
	   }
   }
   else if(region == 3)
   {
	   if(unary == 0)
	   {
	    result.addPoint(( getX()), ( getY() +  getHeight()));
		 result.addPoint(( getX() +  getWidth()/2), ( getY() +  getHeight()));
       }
	   else
	   {
		   result.addPoint(x, y + height);
		    result.addPoint(x +  limit_horizontal, y + height);
	   }
   }
   else if(region == 4)
   {
	   if( unary == 0)
	   {
		  result.addPoint(( getX() +  getWidth()/2), ( getY() +  getHeight()));
		  result.addPoint(( getX() +  getWidth()), ( getY() +  getHeight()));
	   }
	   else
	   {
		    result.addPoint(x + width -  limit_horizontal, y + height);
		    result.addPoint(x + width, y + height);
	   }
   }
   else if(region == 5)
   {
	   result.addPoint( x,  y);
	   result.addPoint( x, ( y +  getHeight()));
	   result.addPoint(( getX() +  getWidth()),( getY() +  getHeight()));
	   result.addPoint(( getX() +  getWidth()),getY());
   }
   else
	   if(region == 34)
	   {
		   assert( getAngular() != 1);
		   result.addPoint(( getX()), ( getY() +  getHeight()));
		   result.addPoint(( getX() +  getWidth()), ( getY() +  getHeight()));
	   }
	   else
		   if(region == 12)
		   {
			   assert( getAngular() != 1);
			   result.addPoint(( getX()), ( getY()));
			   result.addPoint(( getX() +  getWidth()), ( getY()));
		   }
		   else
			   if(region == 23)
			{
				   assert( getAngular() != 1);
				   result.addPoint(( getX()), ( getY())); 
			       result.addPoint(( getX()), ( getY() +  getHeight()));
			}
			   else
				   if(region == 14)
				   {
					   assert( getAngular() != 1);
					   result.addPoint(( getX() +  getWidth()), ( getY()));
					   result.addPoint(( getX()+  getWidth()), ( getY() +  getHeight()));
				   }
				   else if(region == 230)
					{
					   assert( getAngular() == 1);
					   result.addPoint(x,y); 
				       result.addPoint(x,y +  limit_vertical);
				}
				   else
					   if(region == 231)
					   {
						   assert( getAngular() == 1);
						   result.addPoint(x,y + height); 
					       result.addPoint(x,y + height -  limit_vertical);
					   }
					   else if(region == 140)
						{
						   assert( getAngular() == 1);
						   result.addPoint(x + width,y); 
					       result.addPoint(x + width,y +  limit_vertical);
					}
					   else
						   if(region == 141)
						   {
							   assert( getAngular() == 1);
							   result.addPoint(x + width,y + height); 
						       result.addPoint(x + width,y + height -  limit_vertical);
						   }
                   
   return result;


}



public MyPolygon getTriangle1() {


	
	return triangle1;
}

public MyPolygon getTriangle11() {
	return triangle11;
}

public MyPolygon getTriangle2() {


	
	return triangle2;
}

public MyPolygon getTriangle22() {
	return triangle22;
}

public MyPolygon getTriangle3() {
	

	
	return triangle3;
}

public MyPolygon getTriangle33() {
	return triangle33;
}

public MyPolygon getTriangle4() {
	
	
	return triangle4;
}

public MyPolygon getTriangle44() {
	return triangle44;
}

public int getWidth() {
	return width;
}

public int getX() {
	return x;
}

public int getY() {
	return y;
}

public boolean isCompleted()
{
	// the next is -2...
	boolean result = (unary == 2)||isEdge();
     return result;	
}

public boolean isEdge() {
	return edge;
}

public boolean isSupport() {
	

	boolean result = false;
	
	if(this.isEdge())
		result = true;
	else{
			boolean left_support = false;
			boolean right_support = false;
			for(MBR mbr: this.contact_map.keySet())
			{
				Contact contact = this.contact_map.get(mbr);
				//Debug.echo(this, this.contact_map.size(),contact,contact.getType());
				if(contact.getType() == 1)
				{
					if(contact.getTangential_area() == 3)
						left_support = true;
					else
						if(contact.getTangential_area() == 4)
							right_support = true;
						else
							if(contact.getTangential_area() == 34)
							{
								left_support = true;
								right_support = true;
							}
				}
			}
			result = left_support && right_support;
	}
	return result;
}

public int nextInitialization()
{
	
    if(!isEdge())
    {  
    	unary = (unary + 1 > 3)?-2:++unary;
    	   if (unary == 1)
    		   setPermit_regions(0,1,1,0);
    	   else if (unary == 2)
    		   setPermit_regions(0,1,0,1);
    	   else
    		   setPermit_regions(0,0,0,0);
	}
 
    	
      return unary;
}

public void setAngular(int angular) {
	this.angular = angular;
}
public void setBlocked_regions(HashMap<Integer, LinkedList<MBR>> blocked_regions) {
	this.blocked_regions = blocked_regions;
}
public void setContact_map(HashMap<MBR, Contact> contact_map) {
	this.contact_map = contact_map;
}
public void setEdge(boolean edge) {
	if(edge)
		unary = 0;
	this.edge = edge;
}
public void setHeight(int height) {
	this.height = height;
}
public void setMbr(MBR mbr) {
	this.mbr = mbr;
}
public void setOverlapping_mbrs(LinkedList<MBR> overlapping_mbrs) {
	this.overlapping_mbrs = overlapping_mbrs;
}
public void setPermit_regions(int... permit_regions) {
	this.permit_regions = permit_regions;
}
public void setWidth(int width) {
	this.width = width;
}
public void setX(int x) {
	this.x = x;
}
public void setY(int y) {
	this.y = y;
}
@Override
public String toString()
{
  String result =  mbr + "  ";
  if(unary == 0)
	  result += " regular ";
  else
	  if( unary == 1)
	     result += " lean to right ";
	  else
		  if(unary == 2)
			  result += "lean to left";
		  else if(unary == -2)
			  result += " completed";
		  else
			  result += " not initialized ";

  for (MBR mbr: contact_map.keySet())
  {
	  result+= " contacted with [ " + mbr + " at " +  contact_map.get(mbr) + " ] "; 
	  }
  return result;
	  
  
}
}