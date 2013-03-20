package common;

import java.awt.Point;
import java.util.LinkedList;

import quanti.QuantiShapeCalculator;

public class CompositeStructure {
     private LinkedList<MyPolygon> addedpolys = new LinkedList<MyPolygon>();
     private LinkedList<Edge> unSupportedEdges = new LinkedList<Edge>();
     private Point centroid; 
     public CompositeStructure()
     {
     }
     public void addPolygon(MyPolygon polygon)
     {
    	 addedpolys.add(polygon);
    	 unSupportedEdges.clear();
         for (MyPolygon pl: addedpolys)
         {
        	 
           unSupportedEdges.addAll(pl.getUnSupportEdges(this.addedpolys));
         }
    
     }
	public Point getCentriod() {
		
		centroid = QuantiShapeCalculator.calCentroid((MyPolygon[]) addedpolys.toArray());
		
		return centroid;
	}
	public void setCentroid(Point centroid) {
		this.centroid = centroid;
	}
	
	public boolean verifyStability(LinkedList<MyPolygon> polys)
	{
		for (Edge edge : unSupportedEdges)
		{
          for (MyPolygon pl: polys)
			{
        	  verifyEdgeSupport(pl,edge);
			}
		}
		return false;
	}
	
	private boolean verifyEdgeSupport(MyPolygon pl, Edge edge)
	{
		
		
		LinkedList<Point> points = new LinkedList<Point>(); 
		points = QuantiShapeCalculator.isIntersected(edge, pl);
		/* as long as a intersected point at support area and the centorid should be during */
		    for (Point pt: points)
		    {
		    
		    	
		    	/*	if(edge.left)
		    		{
		    			if(testSupport(edge.getStartPoint(),edge.getEndPoint(),pt) && pt.getX() < this.getBounds().getCenterX())
		    			{
		    				left_support = true;
		    				return true;
		    			}   
		    		}
		    		else
		    		{
		    				if(testSupport(edge.getStartPoint(),edge.getEndPoint(),pt) && pt.getX() > this.getBounds().getCenterX())
		    				{
		    					return true;
		    				}   
	        	   
		    		}*/
	         }
		   
		    return false;
		    
		    
		
	}
    
}


