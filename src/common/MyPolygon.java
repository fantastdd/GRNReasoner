package common;
import java.awt.Point;
import java.awt.Polygon;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeMap;

import quanti.QuantiShapeCalculator;




public class MyPolygon extends Polygon {

private static final long serialVersionUID = 8036073011840303349L;

private HashMap<Polygon,LinkedList<Point>> touchedPolygon = new HashMap<Polygon,LinkedList<Point>>();

private TreeMap<Double,LinkedList<MyPolygon>> coreSupport = new TreeMap<Double,LinkedList<MyPolygon>>();

private LinkedList<Edge> supportEdges = new LinkedList<Edge>(); 
private int[] bottomPointIndex = {-1,-1};
private boolean angular = true;




/* Information here reflects local stability*/
private boolean left_support = false;
private boolean right_support = false;
private static int count = 0;
private int id;
public MyPolygon(Point... points)
{
    	super();
    	this.id = count++;
    	int bt_y = 0;
    	for ( int i = 0; i < points.length; i++)
    	{
            if ( points[i].y > bt_y)
            {	
            	bottomPointIndex[0] = i; 
                bt_y = points[i].y;	
            }
        	this.addPoint(points[i].x, points[i].y);
    	}
    	
    	/* determine if angular*/
    	{
	    	int count = 0;
	      	for ( int i = 0; i < points.length; i++)
	     	{
	      		if(points[i].y == bt_y && i != bottomPointIndex[0])
	      			{
	      			   bottomPointIndex[1] = i;
	      			   count ++;
	      			}
	     	}
	      	if(count == 1 )
	      		angular = false;
    	}
        
    
}


public void addTouch(LinkedList<Point> points, MyPolygon pl)
{
	touchedPolygon.put(pl, points);
	for (Edge edge : this.getSupportEdges())
	{
		
			if(verifySupport(points,edge))
			{
				if(!coreSupport.containsKey(1.0))
				{
					coreSupport.put(1.0, new LinkedList<MyPolygon>());
				}
					coreSupport.get(1.0).add(pl);
					
					
			}
		
	}
	
}

public boolean isLocalStable()
{
     return left_support && right_support;	

}

public double getArea()
{
	double area = 0.0;
	for(int i = 0; i < this.npoints; i++)
	{
			area += (xpoints[i]*ypoints[(i+1==this.npoints)?0:i+1])-(xpoints[(i+1==this.npoints)?0:i+1]*ypoints[i]);
			//double temp = xpoints[i]*ypoints[i+1]- xpoints[i+1]*ypoints[i];
			//System.out.println(xpoints[i] + " * " + ypoints[i+1] + " - " + xpoints[i+1] +" * "+ ypoints[i] + " = " + temp);
	}
	area /= 2;
	
	/*if they enter points counterclockwise the area will be negative but correct.*/
	if(area < 0)
		area *= -1;
	return area;
}

public LinkedList<Edge> getUnSupportEdges(LinkedList<MyPolygon> polygons)
{
	LinkedList<Edge> unsupportedEdges = new LinkedList<Edge>();
    unsupportedEdges.addAll(this.getSupportEdges());
	LinkedList<Edge> supportedEdges = new LinkedList<Edge>();
	for(MyPolygon mypolygon: polygons)
	{
		if(mypolygon.equals(this)){
			for (Edge edge: this.getSupportEdges())
			{
			     if ( 
			    		 verifySupport(QuantiShapeCalculator.isIntersectedWCA(mypolygon, this),edge)
			        )
			     {
			    	 supportedEdges.add(edge);
			     }
			}
		}
	}
	unsupportedEdges.removeAll(supportedEdges);
	return unsupportedEdges;
	
	

}
private boolean verifySupport(LinkedList<Point> points, Edge edge)
{
	/* as long as a intersected point at support area and the centorid should be during */
	    for (Point pt: points)
	    {
	    
	    	
	    		if(edge.left)
	    		{
	    			if(testSupport(edge.getStartPoint(),edge.getEndPoint(),pt) && pt.getX() < this.getBounds().getCenterX())
	    			{
	    			
	    				left_support = true;
	    				//System.out.println(this+"  left: "+edge.getStartPoint() + "  "+edge.getEndPoint() + "  " + pt + " "+left_support);
	    				return true;
	    			}   
	    		}
	    		else
	    		{
	    			//System.out.println(this+"  start to evaluate right "+edge.getStartPoint() + "  "+edge.getEndPoint() + "   support point: " + pt);
	    				if(testSupport(edge.getStartPoint(),edge.getEndPoint(),pt) && pt.getX() > this.getBounds().getCenterX())
	    				{
	    					right_support = true;
	    					System.out.println(this+"  right: "+edge.getStartPoint() + "  "+edge.getEndPoint() + "  " + pt + " "+right_support);
	    					return true;
	    				}   
        	   
	    		}
         }
	   
	    return false;
	    
	    
	
}
/* Edges required to be supported 
 * 
 * if return size 2, then it is an angular polygon
 * */
public LinkedList<Edge> getSupportEdges()
{
if(this.supportEdges.isEmpty()) 
{
	LinkedList<Edge> edges = new LinkedList<Edge>();
	Point pt = new Point(this.xpoints[bottomPointIndex[0]],this.ypoints[bottomPointIndex[0]]); 
	if(angular)
	{   
		if (bottomPointIndex[0] == 0 )
		{
			Point pt1 = new Point(this.xpoints[bottomPointIndex[0] + 1],this.ypoints[bottomPointIndex[0] + 1]); 
			Point pt2 = new Point(this.xpoints[this.npoints - 1],this.ypoints[this.npoints - 1]); 
			if(pt1.getX() < pt2.getX())
			{
					edges.add(new Edge(pt,pt1,true));
					edges.add(new Edge(pt,pt2,false));
				
			}
			else
			{
				edges.add(new Edge(pt,pt1,false));
				edges.add(new Edge(pt,pt2,true));
			}
				}
		else
			if(bottomPointIndex[0] == this.npoints - 1)
			{

				Point pt1 = new Point(this.xpoints[0],this.ypoints[0]); 
				Point pt2 = new Point(this.xpoints[bottomPointIndex[0] - 1],this.ypoints[bottomPointIndex[0] - 1]); 
				
				if(pt1.getX() < pt2.getX())
				{
						edges.add(new Edge(pt,pt1,true));
						edges.add(new Edge(pt,pt2,false));
					
				}
				else
				{
					edges.add(new Edge(pt,pt1,false));
					edges.add(new Edge(pt,pt2,true));
				}
			}
			else
			{

				Point pt1 = new Point(this.xpoints[bottomPointIndex[0] + 1],this.ypoints[bottomPointIndex[0] + 1]); 
				Point pt2 = new Point(this.xpoints[bottomPointIndex[0] - 1],this.ypoints[bottomPointIndex[0] - 1]); 
				if(pt1.getX() < pt2.getX())
				{
						edges.add(new Edge(pt,pt1,true));
						edges.add(new Edge(pt,pt2,false));
					
				}
				else
				{
					edges.add(new Edge(pt,pt1,false));
					edges.add(new Edge(pt,pt2,true));
				}
			}
	
		
	}
	else
	{
		Point pt1 = new Point(this.xpoints[bottomPointIndex[1]],this.ypoints[bottomPointIndex[1]]);  
		System.out.println(" add support edge "+this + "  "+pt1 +  " "+pt);
		//edges.add(new Edge(pt,pt1));
		edges.add(new Edge(pt,pt1,true));
        edges.add(new Edge(pt,pt1,false));
	}
if(!this.supportEdges.containsAll(edges))
	this.supportEdges.addAll(edges);
}
	
	return this.supportEdges;

}
private boolean testSupport(Point l1, Point l2, Point p)
{
	/* test if the point p is along the line l*/
	if (!(p.getX() > Math.max(l1.getX(), l2.getX())) && !(p.getX() < Math.min(l1.getX(), l2.getX()))
			
			&&
			!(p.getY() > Math.max(l1.getY(), l2.getY())) && !(p.getY() < Math.min(l1.getY(), l2.getY()))
		)
		{
		    return true;
		}
	return false;

}
@Override
public String toString()
{
   String result = "Polygon: " + this.id + " ";
   for(int i = 0; i < this.npoints; i++ )
	   result+= " ( " + this.xpoints[i] + "," + this.ypoints[i] + "),";
   return result;	
}
}
