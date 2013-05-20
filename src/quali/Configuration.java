package quali;

import java.awt.Point;
import java.util.HashMap;
import java.util.LinkedList;

import ab.WorldinVision;

import common.MyPolygon;

public class Configuration {
	
public int angular;
public HashMap<Integer,LinkedList<MBR>> blocked_regions = new HashMap<Integer,LinkedList<MBR>>();
public HashMap<Integer,Contact> contact_map = new HashMap<Integer,Contact>();
public MyPolygon core_left = new MyPolygon();
public MyPolygon core_left1 = new MyPolygon();
public MyPolygon core_left3 = new MyPolygon();
public MyPolygon core_right = new MyPolygon();
public MyPolygon core_right2 = new MyPolygon();
public MyPolygon core_right4 = new MyPolygon();

// approx_u3t3 is the approx maximum space of a fat right leaning rectangle. 
public MyPolygon approx_u3t3 = new MyPolygon();
public MyPolygon approx_u3t2 = new MyPolygon();
public MyPolygon approx_u3t1 = new MyPolygon();
public MyPolygon approx_u3t4 = new MyPolygon();

// approx_u3t3r is the remaining part of the approx_u3t3 region 
public MyPolygon approx_u3t3r = new MyPolygon();
public MyPolygon approx_u3t2r = new MyPolygon();
public MyPolygon approx_u3t1r = new MyPolygon();
public MyPolygon approx_u3t4r = new MyPolygon();

//approx_left is the approx maximum space of a fat left leaning rectangle. 
public MyPolygon approx_u4t3 = new MyPolygon();
public MyPolygon approx_u4t2 = new MyPolygon();
public MyPolygon approx_u4t1 = new MyPolygon();
public MyPolygon approx_u4t4 = new MyPolygon();

//approx_u4t3r is the remaining part of the approx_u4t3 region 
public MyPolygon approx_u4t3r = new MyPolygon();
public MyPolygon approx_u4t2r = new MyPolygon();
public MyPolygon approx_u4t1r = new MyPolygon();
public MyPolygon approx_u4t4r = new MyPolygon();


public MyPolygon diagonal_left = new MyPolygon();
public MyPolygon diagonal_right = new MyPolygon();

public MyPolygon fullRec = new MyPolygon();
public boolean edge = false;
public int height;
public int width;
public int hheight = height/2;
public int hwdith = width/2;
public int limit_horizontal;
public int limit_vertical;
public MBR mbr;
public LinkedList<Neighbor> neighbors = new LinkedList<Neighbor>();

public int[] permit_regions;

public LinkedList<Point> points1 = new LinkedList<Point>();
public LinkedList<Point> points11 = new LinkedList<Point>();
public LinkedList<Point> points2 = new LinkedList<Point>();
public LinkedList<Point> points21 = new LinkedList<Point>();


public LinkedList<Point> points3 = new LinkedList<Point>();
public LinkedList<Point> points31 = new LinkedList<Point>();
/* store all the points that potentially contact with the actual shape*/
public LinkedList<Point> points4 = new LinkedList<Point>();
/* store all the points that contact with the actual shape*/
public LinkedList<Point> points41 = new LinkedList<Point>();

public MyPolygon triangle1 = new MyPolygon();
public MyPolygon triangle11 = new MyPolygon();

public MyPolygon triangle2 = new MyPolygon();
public MyPolygon triangle22 = new MyPolygon();
public MyPolygon triangle3 = new MyPolygon();
public MyPolygon triangle33 = new MyPolygon();
public MyPolygon triangle4 =  new MyPolygon();
public MyPolygon triangle44 =  new MyPolygon();

public int unary = -1;//0 = regular, 1 = slim lean to right, 2 = slim lean to left, 3 = fat lean to right, 4 = fat lean to left ,   -1 = not initialized, -2 = completed.

public boolean v1 = false;
public boolean v2 = false;
public boolean v3 = false;
public boolean v4 = false;
public Point[] vertices = new Point[4];


public int x;
public int y;


public int lastTestNeighborId = -1;

public int lastValidNeighborId = -2; //id = -2, touches all others -1, no neighbors
//TODO write a isNowSupport method in configuration
public int left_support = 0;
public int right_support = 0;
boolean weaksupport = false;
//change now
public boolean isNowSupport(final Configuration tconf )
{
	
	boolean result = false;
   
  if(!tconf.getMbr().equals(mbr))
  { 
	
	Contact contact =  ContactManager.getPairContact(tconf, this, WorldinVision.gap);
	/*if(mbr.uid == 5 && unary == 1 && tconf.getMbr().uid == 10)
		System.out.println(" Is now support:  " + contact + "  " + tconf);*/
	contact_map.put(tconf.getMbr().getId(), contact);
	if(isEdge())
		result = true;
	else
	{
	
     	int tangential_area = contact.getTangential_area();
			//Debug.echo(this, this.contact_map.size(),contact,contact.getType());
			if(contact.getType() == 1)
			{
				if(unary != 0)
				{
					 if (tangential_area == 23 || tangential_area == 3)
					 {
					        left_support++;
					        weaksupport = true;
					        if (contact.strongEdgeSupport)
								left_support ++ ;
					 }
					 else
						 if(tangential_area == 4 || tangential_area == 34)
						 {
							 right_support++;
							 weaksupport = true;
							 if(contact.strongEdgeSupport)
								 right_support++;
						 } 
						 else
							 if(tangential_area == 12 )
							 {
								 if(unary > 2)
								 {
									 weaksupport = true;
									 left_support++;
								 }
							 } 
							 else
								 if (tangential_area == 14)
								 {
									 if(unary > 2)
									 {
										 weaksupport = true;
										 right_support++;
									 }
								 }
						 
				}
				else
				{
					if (tangential_area == 34)
					{
						left_support ++ ;
						right_support++;
					} 
					else
						if(tangential_area == 3 || tangential_area == 323)
						{
							left_support ++ ;
							if(tangential_area == 3)
							weaksupport = true;
						}
						else if (tangential_area == 4 || tangential_area == 414)
						{
							right_support ++ ;
							if(tangential_area == 4)
							weaksupport = true;
						}
							
					       
				}
			}
		
		result = ((left_support > 0 ) && ( right_support > 0));
       /* if(unary == 1 && mbr.getId() == 6 && tconf.getMbr().getId() == 7 && tconf.unary == 0)
        	System.out.println("  " + contact + "   " + left_support + "  " + right_support + "  ");*/
		if(unary == 0)
			result |= weaksupport;
		else
			if(unary == 1)
			{
				result |=  right_support > 1;
			}
			else 
				if(unary == 2)
					result |= left_support > 1;
					else
						result |= (right_support > 1)||(left_support > 1);
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
	   this.mbr = mbr;
       configureRegions();
       
       
        
}

public void configureRegions()
{
	
	  x = mbr.x;
      y = mbr.y;
      height = mbr.height;
      width = mbr.width;
	 getBlocked_regions().put(0, new LinkedList<MBR>());
     getBlocked_regions().put(1, new LinkedList<MBR>());
     getBlocked_regions().put(2, new LinkedList<MBR>());
     getBlocked_regions().put(3, new LinkedList<MBR>());

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
	triangle3.addPoint( x +  limit_horizontal, y +  height);
    
    
	triangle2.addPoint( x, y);
	triangle2.addPoint( x, y +  limit_vertical);
	triangle2.addPoint( x +  limit_horizontal, y);
    
	triangle1.addPoint( x +  width -  limit_horizontal, y);
	triangle1.addPoint( x +  width, y);
	triangle1.addPoint( x +  width, y +  limit_vertical);
	
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
	
	
/*	core_right2.addPoint( x,  y +  height -  limit_vertical);
	core_right2.addPoint(( x +  limit_horizontal+ x)/2, ( y +  height +  y +  height -  limit_vertical)/2);
	core_right2.addPoint(( x +  width +  x +  width -  limit_horizontal)/2, ( y +  limit_vertical+ y)/2);
	core_right2.addPoint( x +  width -  limit_horizontal,  y);
	
	
  	
	core_right4.addPoint(( x +  width +  x +  width -  limit_horizontal)/2, ( y +  limit_vertical+ y)/2);
	core_right4.addPoint( x +  limit_horizontal,  y +  height);
	core_right4.addPoint( x +  width,  y +  limit_vertical);
	core_right4.addPoint(( x +  width +  x +  width -  limit_horizontal)/2, ( y +  limit_vertical+ y)/2);*/
	
	core_right2.addPoint( x,  y +  height -  limit_vertical);
	core_right2.addPoint(x , y + height);
	core_right2.addPoint( x +  width , y);
	core_right2.addPoint( x +  width -  limit_horizontal,  y);
	
	
  	
	core_right4.addPoint( x , y + height);
	core_right4.addPoint( x +  limit_horizontal,  y +  height);
	core_right4.addPoint( x +  width,  y +  limit_vertical);
	core_right4.addPoint( x +  width, y);
	

	diagonal_right.addPoint( x,  y +  height);
	diagonal_right.addPoint( x +  width,  y);

	

	core_left.addPoint( x +  limit_horizontal,  y);
	core_left.addPoint( x,  y +  limit_vertical);
	core_left.addPoint( x +  width -  limit_horizontal,  y +  height);
	core_left.addPoint( x +  width,  y +  height -  limit_vertical);
	
/*	core_left1.addPoint( x +  limit_horizontal,  y);
	core_left1.addPoint(( x +  x +  limit_horizontal)/2, ( y +  limit_vertical +  y)/2);
	core_left1.addPoint(( x +  width -  limit_horizontal+ x +  width)/2, ( y +  height+ y +  height -  limit_vertical)/2);
	core_left1.addPoint( x +  width,  y +  height -  limit_vertical);
	
	core_left3.addPoint(( x +  x +  limit_horizontal)/2, ( y +  limit_vertical +  y)/2);
	core_left3.addPoint( x,  y +  limit_vertical);
	core_left3.addPoint( x +  width -  limit_horizontal,  y +  height);
	core_left3.addPoint(( x +  width -  limit_horizontal+ x +  width)/2, ( y +  height+ y +  height -  limit_vertical)/2);*/
	
	core_left1.addPoint( x +  limit_horizontal,  y);
	core_left1.addPoint( x , y);
	core_left1.addPoint( x + width , y + height);
	core_left1.addPoint( x +  width,  y +  height -  limit_vertical);
	
	
	core_left3.addPoint(x,y);
	core_left3.addPoint( x, y +  limit_vertical);
	core_left3.addPoint( x +  width -  limit_horizontal,  y +  height);
	core_left3.addPoint( x +  width,  y +  height);
	
	
	diagonal_left.addPoint( x +  width,  y +  height);
	diagonal_left.addPoint( x , y);
	
	
	approx_u3t3.addPoint(x, y);
	approx_u3t3.addPoint(x, y + height);
	approx_u3t3.addPoint(x + limit_horizontal , y + height);
	
	approx_u3t4.addPoint(x , y  + height);
	approx_u3t4.addPoint(x + width , y + height);
	approx_u3t4.addPoint(x + width , y + height - limit_vertical);
	
	approx_u3t1.addPoint(x + width ,  y + height);
	approx_u3t1.addPoint(x + width, y );
	approx_u3t1.addPoint(x + width - limit_horizontal , y);
	
	approx_u3t2.addPoint(x + width ,  y);
	approx_u3t2.addPoint(x , y );
	approx_u3t2.addPoint(x , y + limit_vertical);
	
	approx_u3t3r.addPoint(x, y);
	approx_u3t3r.addPoint(x + limit_horizontal , y + height);
	approx_u3t3r.addPoint(x + width, y + height);
	approx_u3t3r.addPoint(x + width , y);
	
	approx_u3t4r.addPoint(x , y  + height);
	approx_u3t4r.addPoint(x + width , y + height - limit_vertical);
	approx_u3t4r.addPoint(x + width , y);
	approx_u3t4r.addPoint(x , y);
	
	
	approx_u3t1r.addPoint(x + width ,  y + height);
	approx_u3t1r.addPoint(x + width - limit_horizontal , y);
	approx_u3t1r.addPoint(x , y);
	approx_u3t1r.addPoint(x, y + height);
	
	
	
	approx_u3t2r.addPoint(x + width ,  y);
	approx_u3t2r.addPoint(x , y + limit_vertical);
	approx_u3t2r.addPoint(x , y + height);
	approx_u3t2r.addPoint(x + width ,  y + height);
	
	
	
	approx_u4t3.addPoint(x, y + height - limit_vertical);
	approx_u4t3.addPoint(x, y + height);
	approx_u4t3.addPoint(x + width , y + height);
	
	approx_u4t4.addPoint(x + width - limit_horizontal , y  + height);
	approx_u4t4.addPoint(x + width , y + height);
	approx_u4t4.addPoint(x + width , y);
	
	approx_u4t1.addPoint(x + width ,  y + limit_vertical);
	approx_u4t1.addPoint(x + width, y );
	approx_u4t1.addPoint(x  , y);
	
	approx_u4t2.addPoint(x + limit_horizontal ,  y);
	approx_u4t2.addPoint(x , y );
	approx_u4t2.addPoint(x , y + height);
	
	
	approx_u4t3r.addPoint(x, y + height - limit_vertical);
	approx_u4t3r.addPoint(x + width , y + height);
	approx_u4t3r.addPoint(x + width , y);
	approx_u4t3r.addPoint(x , y );
	
	
	approx_u4t4r.addPoint(x + width - limit_horizontal , y  + height);
	approx_u4t4r.addPoint(x + width , y);
	approx_u4t4r.addPoint(x , y);
	approx_u4t4r.addPoint(x , y + height);
	
	
	
	approx_u4t1r.addPoint(x + width ,  y + limit_vertical);
	approx_u4t1r.addPoint(x  , y);
	approx_u4t1r.addPoint(x  , y + height);
	approx_u4t1r.addPoint(x + width , y + height);
	
	approx_u4t2r.addPoint(x + limit_horizontal ,  y);
	approx_u4t2r.addPoint(x , y + height);
	approx_u4t2r.addPoint(x + width , y + height);
	approx_u4t2r.addPoint(x + width, y );
	
	
	
	
	fullRec.addPoint(x, y);
	fullRec.addPoint(x, y + height);
	fullRec.addPoint(x + width, y + height);
	fullRec.addPoint(x + width, y);
	
	
	
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


// transform the conf to the tconf by eliminating the gap
public Configuration translate(Configuration tconf, int threshold)
{
	//find out the neighbor region
	MBR tmbr = tconf.getMbr();
	//System.out.println(tmbr.getId() + "  " + this.getMbr().getId());
	Neighbor neighbor = neighbors.get(neighbors.indexOf(tmbr));
	byte type = neighbor.getNeighborType();
	// only transform the configuration which are not overlapping/contained/containing
	int gap = (int)neighbor.getGap();
	if ( gap > threshold || type == 0)
		return this;
	
	
	MBR _mbr = new MBR();
	_mbr.x = mbr.x;
	_mbr.y = mbr.y;
	_mbr.height = mbr.height;
	_mbr.width = mbr.width;
	_mbr.setId(mbr.getId());
	_mbr.uid = mbr.uid;
	Configuration conf;
   	
	switch (type)
    {
    	case 1: 
    		{
    		  _mbr.translate(0,-gap);
    		  break;
    		  //System.out.println(" enter this " + mbr + "   " + mbr.getBounds() + "   " + _mbr.getBounds());
    		}
    	case 2:
    		{
    			_mbr.translate(-gap, 0);
    		    break;
    		}
    	case 3:
    		{
    			_mbr.translate(0, gap);
    		    break;
    		}
    	case 4:
    		{
    			_mbr.translate(gap, 0);
    		    break;
    		}
    
    }
	conf  = new Configuration(_mbr);
	conf.unary = this.unary;
	
	return conf;
  }

@Override
public Configuration clone()
{
	
  Configuration conf = new Configuration(mbr);
  
  
  conf.setAngular(angular);
  conf.permit_regions = permit_regions;
  
  
  conf.unary = unary;
  conf.lastTestNeighborId = lastTestNeighborId;
  conf.lastValidNeighborId = lastValidNeighborId;
  
  conf.left_support = left_support;
  conf.right_support = right_support;
  conf.weaksupport = weaksupport;
  
  for(Neighbor neighbor: neighbors)
  {
	  conf.getNeighbors().add(neighbor);
  }
  

  for(Integer key: blocked_regions.keySet())
  {
	  LinkedList<MBR> mbrs = blocked_regions.get(key);
	  
	  conf.blocked_regions.put(key, new LinkedList<MBR>());
      
	  for(MBR mbr: mbrs)
		  conf.blocked_regions.get(key).add(mbr);
  }
  
  for(Integer key:  contact_map.keySet())
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


public HashMap<Integer, Contact> getContact_map() {
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
	   result.addPoint( x, y + height);
	   result.addPoint( x +  width, y + height);
	   result.addPoint( x + width ,y);
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
	boolean result = (unary == 4)||isEdge();
     return result;	
}

public boolean isEdge() {
	return edge;
}

public boolean isSupport() {
	

	//weak support parameter included for the regular mbr
	//boolean weaksupport = false;
	boolean result = false;
	
	if(this.isEdge())
		result = true;
	else{
			//boolean left_support = false;
			//boolean right_support = false;
			for(Integer mbr:  contact_map.keySet())
			{
				Contact contact = contact_map.get(mbr);
				int tangential_area = contact.getTangential_area();
				//Debug.echo(this, this.contact_map.size(),contact,contact.getType());
				if(contact.getType() == 1)
				{
					if(unary != 0)
					{
					 if (tangential_area == 23 || tangential_area == 3)
					 {
					        left_support++;
					        weaksupport = true;
					        if (contact.strongEdgeSupport)
								left_support ++ ;
					 }
					 else
						 if(tangential_area == 4 || tangential_area == 34)
						 {
							 right_support++;
							 weaksupport = true;
							 if(contact.strongEdgeSupport)
								 right_support++;
						 } 
						 else
							 if(tangential_area == 12 )
							 {
								 if(unary > 2)
								 {
									 weaksupport = true;
									 left_support++;
								 }
							 } 
							 else
								 if (tangential_area == 14)
								 {
									 if(unary > 2)
									 {
										 weaksupport = true;
										 right_support++;
									 }
								 }
							 
					}
					else
					{
						if (tangential_area == 34)
						{
							left_support ++ ;
							right_support++;
						} 
						if(tangential_area == 3 || tangential_area == 323)
						{
							left_support ++ ;
							if(tangential_area == 3)
							weaksupport = true;
						}
						else if (tangential_area == 4 || tangential_area == 414)
						{
							right_support ++ ;
							if(tangential_area == 4)
							weaksupport = true;
						}
						       
					}
					
										
				}
			}
			result = (left_support > 0 ) && ( right_support > 0 );

			if(unary == 0)
				result |= weaksupport;
			else
				if(unary == 1)
				{
					result |= (right_support > 1);
				}
				else 
					if(unary == 2)
						result |= left_support > 1;
						else
							result |= (right_support > 1)||(left_support > 1);
	}
	return result;
}

public int nextInitialization()
{
	
    if(!isEdge())
    {  
    	unary = (unary + 1 > 4)?-2:++unary;
    	   if (unary == 1) // lean to right
    		   setPermit_regions(0,1,1,0);
    	   else if (unary == 2)
    		   setPermit_regions(0,1,0,1);
    	   else
    		   if(unary == 3)
    			   setPermit_regions(1,0,1,0);
    			   else
    				   if(unary == 4)
    					   setPermit_regions(1,0,0,1);
    				   else
    					   setPermit_regions(0,0,0,0);
	}
   //System.out.println("   unary " + unary);
    	
      return unary;
}

public void setAngular(int angular) {
	this.angular = angular;
}
public void setBlocked_regions(HashMap<Integer, LinkedList<MBR>> blocked_regions) {
	this.blocked_regions = blocked_regions;
}
public void setContact_map(HashMap<Integer, Contact> contact_map) {
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
public String toShortString()
{
  String result =  mbr + " ";
  if(unary == 0)
	  result += "( regular )";
  else
	  if( unary == 1)
	     result += "( SR)";
	  else
		  if(unary == 2)
			  result += "( SL )";
		  else if(unary == -2)
			  result += "( completed )";
		  else if(unary == 3)
			  result += " (FR )";
		  else if (unary == 4)
			  result += " ( FL )" ;
		  else
			  result += "( not initialized )";

  return result;
	  
  
}
@Override
public String toString()
{
  String result =  mbr + " unary: ";
  if(unary == 0)
	  result += " regular ";
  else
	  if( unary == 1)
	     result += " slim lean to right ";
	  else
		  if(unary == 2)
			  result += "slim lean to left";
		  else
			  if(unary == 3)
				  result += "fat lean to right";
			  else
				  if(unary == 4)
					  result += " fat lean to left";
		  else if(unary == -2)
			  result += " completed";
		  else
			  result += " not initialized ";
  
  for (Integer mbr: contact_map.keySet())
  {
	  result+= " contacted with [ MBR id: " + mbr + " at " +  contact_map.get(mbr) + " ] "; 
	  }
  return result;
	  
  
}
public Configuration enlarge(Configuration tconf , int i) {
	
	int type = -1;
	for (Neighbor neighbor : neighbors)
	{
		if(neighbor.getMbr().getId() == tconf.getMbr().getId())
			    type = neighbor.getNeighborType();
	}
	// only enlarge the bounding boxes when intersecting. 
	if(type == 0)
	{
		MBR _mbr = new MBR();
		_mbr.x = mbr.x - i;
		_mbr.y = mbr.y - i;
		_mbr.height = mbr.height + 2*i;
		_mbr.width = mbr.width + 2 * i;
		_mbr.setId(mbr.getId());
		this.mbr = _mbr;
	}
	
	
	return this;
}
}