package quanti;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.Collections;
import java.util.LinkedList;

import quali.ShapeCalculator;

import common.Edge;
import common.MyPolygon;
import common.util.Debug;
import common.util.PointXComparator;
import common.util.PointYComparator;


public class QuantiShapeCalculator extends ShapeCalculator {
	
	
	public static LinkedList<Point> FindLineIntersection(Point start1, Point end1, Point start2, Point end2)
	{
	
		    LinkedList<Point> points =  new LinkedList<Point>();
	        float denom = ((end1.x - start1.x) * (end2.y - start2.y)) - ((end1.y - start1.y) * (end2.x - start2.x));
          
        	Line2D.Double ld1 = new Line2D.Double();
    		ld1.setLine(start1.x, start1.y, end1.x, end1.y);
    		
    		Line2D.Double ld2 = new Line2D.Double();
    		ld2.setLine(start2.x, start2.y, end2.x, end2.y);
	        //  AB & CD are parallel 
	        if (denom == 0)
	        {   
	        	  points.add(start1);
	              points.add(start2);
	              points.add(end1);
	              points.add(end2);
	        	if(ld1.intersectsLine(ld2))
	        	{
	        	 if(start1.y == end1.y && start1.y == end2.y)
	        		Collections.sort(points,new PointXComparator());
	        	 else
	        			Collections.sort(points,new PointYComparator());
	        	 
	        		 points.pollFirst();
	        		 points.pollLast();
	        	}
	        	else
	        		{
	        			points.clear();
	        		}
	        	
	        }    
	        else
	        {

		        float numer = ((start1.y - start2.y) * (end2.x - start2.x)) - ((start1.x - start2.x) * (end2.y - start2.y));
	
		        float r = numer / denom;
	
		        float numer2 = ((start1.y - start2.y) * (end1.x - start1.x)) - ((start1.x- start2.x) * (end1.y - start1.y));
	
		        float s = numer2 / denom;
	
		        if ((r < 0 || r > 1) || (s < 0 || s > 1))
		                points.clear();
		        else
	        // Find intersection point
		        { 
		        	Point result = new Point();
		        	result.x = (int) (start1.x + (r * (end1.x - start1.x)));
		        	result.y = (int) (start1.y + (r * (end1.y - start1.y)));
		        	points.add(result);
	             
		        }
	        }
	        //System.out.println(points.size());
            return points;
	 }
	
	 public static LinkedList<Point> isIntersected(Rectangle r1, Rectangle r2)
	 {
		 Polygon p1 = new Polygon();
		 p1.addPoint(r1.x, r1.y);
		 p1.addPoint(r1.x,r1.y + r1.height );
		 p1.addPoint(r1.x + r1.width, r1.y + r1.height);
		 p1.addPoint(r1.x + r1.width, r1.y);
		 
		 Polygon p2 = new Polygon();
		 p2.addPoint(r2.x, r2.y);
		 p2.addPoint(r2.x,r2.y + r2.height );
		 p2.addPoint(r2.x + r2.width, r2.y + r2.height);
		 p2.addPoint(r2.x + r2.width, r2.y);
		 
		 return isIntersected(p1,p2);
	 }
	 public static LinkedList<Point> isIntersected(Polygon p1, Polygon p2)
	 {
		 LinkedList<Point> points = new LinkedList<Point>();
		 for ( int i = 0; i < p1.npoints   ; i ++ )
				 for ( int j = 0; j < p2.npoints ; j++)
				 {
					 
						 LinkedList<Point> _points = FindLineIntersection(
								 new Point(p1.xpoints[i],p1.ypoints[i]),
								 new Point(p1.xpoints[(i+1>=p1.npoints)?0:i+1],p1.ypoints[(i+1>=p1.npoints)?0:i+1]),
								 new Point(p2.xpoints[j],p2.ypoints[j]),
								 new Point(p2.xpoints[(j+1>=p2.npoints)?0:j+1],p2.ypoints[(j+1>=p2.npoints)?0:j+1])
								 );
						 if( points != null)
						 {
							 for(Point p:_points)
							 {
								if(!points.contains(p))
								 points.add(p);
							 }
								 // Debug.echo(null ,p1," and ", p2, "\n Intersects at : ",p);
						 
						 }
						 
						
				 }
       if(points.size() == 0)
       {
    	   //   Debug.echo(null, p1 + " sepearte from " + p2);
       }
		
    	   return points;
	 }
	 
	 private static boolean testCornerPoint(Polygon p, int x, int y)
	 {
      		 for (int i = 0; i < p.npoints; i ++)
      		 {
      			 if(p.xpoints[i] == x && p.ypoints[i] == y)
      				 return true;
      		 }
      		 return false;
		 
	 }
	/* including touch or not.   touched/intersected, including "contain" relationship*/
	 public static boolean isIntersected(Polygon p1, Polygon p2,boolean touch)
	 {
		 boolean result = false;
		 boolean containing = true;
			boolean contained = true;
		 if(touch)
		 {
			
			result = /* overlapping */!isIntersected(p1,p2).isEmpty(); 
			
		
		 }
		 
		 else
		 {
			
			 LinkedList<Point> points = isIntersected(p1,p2);
			 //Debug.echo(null,points);
			 if(!points.isEmpty())
			 {
				 result = false;
				 for(Point p:points)
				 {
				      if(! (testCornerPoint(p1,p.x,p.y) || testCornerPoint(p2,p.x,p.y)) 
				    		  
				        /* predicular */ )
				    	  result = true;
				    	  
				 }
			 }
			 else
				 result = false;
			 
			 
		 
		 
		 
		 
		 
		 }
			/* contained relationship*/ 
			for(int i  = 0; i < p1.npoints;i++)
			{
				if(!p2.contains(new Point(p1.xpoints[i],p1.ypoints[i])))
				    contained = false;
				    
					
			}
			/* containing relationship*/
			for(int i  = 0; i < p2.npoints;i++)
			{
				if(!p1.contains(new Point(p2.xpoints[i],p2.ypoints[i])))
				    containing = false;
				  
					
			}
		
			result |= contained || containing;
          		 
		 return result;

	 }
	
	 public static LinkedList<Point> isIntersectedWithReasoning(Polygon p1, Polygon p2, double gap)
	 {
		 LinkedList<Point> points = new LinkedList<Point>();
		 for ( int i = 0; i < p1.npoints   ; i ++ )
				 for ( int j = 0; j < p2.npoints ; j++)
				 {
					 
					 LinkedList<Point> _points = FindLineIntersection(
								 new Point(p1.xpoints[i],p1.ypoints[i]),
								 new Point(p1.xpoints[(i+1>=p1.npoints)?0:i+1],p1.ypoints[(i+1>=p1.npoints)?0:i+1]),
								 new Point(p2.xpoints[j],p2.ypoints[j]),
								 new Point(p2.xpoints[(j+1>=p2.npoints)?0:j+1],p2.ypoints[(j+1>=p2.npoints)?0:j+1])
								 );
						 
						 if( _points != null)
							 
						 {
							for(Point p:_points)
							{
								if(!points.contains(p)) 
								{
									for (int k = 0; k < p1.npoints ; k++)
								
									{
										Point testPoint = new Point(p1.xpoints[k],p1.ypoints[k]);
										if(similarPoint(testPoint,p,gap))
											p = testPoint;
						    	 
									}
							 		points.add(p);
							 		 Debug.echo(null ,p1," and ", p2, "\n Intersects ( with reasoning )at : ",p);
								}
							}
							
						 
						 }
						 
						
				 }
       if(points.size() == 0)
    	   Debug.echo(null, p1 + " sepearte from " + p2);
		 return points;
	 }
	 private static boolean similarPoint(Point p1, Point p2,double gap)
	 {
		  double distance =  Math.sqrt((p1.getX() - p2.getX()) * (p1.getX() - p2.getX()) +   (p1.getY() - p2.getY()) *  (p1.getY() - p2.getY()));
		//  System.out.println(p1 + "  "+ distance);
		  if(distance <= gap)
			  return true;
		  return false;
	   
	 }
	 
	 
	 
	 public static LinkedList<Point> isIntersected(Edge edge, Polygon p)
	{
		LinkedList<Point> points = new LinkedList<Point>();
		for (int i = 0; i < p.npoints; i ++)
		{
			 LinkedList<Point> pt = FindLineIntersection(
					 new Point(p.xpoints[i],p.ypoints[i]),
					 new Point(p.xpoints[(i+1>=p.npoints)?0:i+1],p.ypoints[(i+1>=p.npoints)?0:i+1]),
					 edge.getStartPoint(),
					 edge.getEndPoint()
					 );
			 if( pt != null)
			 {
				
				 for(Point _p: pt){
					if(!points.contains(_p))
					{	
						points.add(_p);
						System.out.println(p + " and " + edge + "\n Intersects at : "+ pt);
					}
					
					
				}
			 }
		}
		return points;
	}
	public static Point calCentroid(MyPolygon... polygons)
	{
		/* now only works for the regular polygons, eg.rectangles */
		//Point ref_point = new Point(0,0);
		double total_x = 0.0;
		double total_y = 0.0;
		double total_area = 0.0;
		for ( MyPolygon polygon : polygons)
		{
			Point local_centroid = new Point((int)polygon.getBounds().getCenterX(), (int)polygon.getBounds().getCenterY());
			//System.out.println(local_centroid);
			double area = polygon.getArea();
            total_x += area * local_centroid.getX();
            //System.out.println(area);
            total_y += area * local_centroid.getY();
            total_area += area; 
		}
		double x = total_x / total_area;
		double y = total_y / total_area;
		Point p = new Point();
		p.setLocation(x, y);
		return p;
	}
	public static void main(String args[])
	{
/*		MyPolygon mp1 = new MyPolygon();
		mp1.addPoint(10, 10);
		mp1.addPoint(10,12);
		mp1.addPoint(12,12);
		mp1.addPoint(12,10);
		
		
		MyPolygon mp2 = new MyPolygon();
		mp2.addPoint(12, 10);
		mp2.addPoint(12,14);
		mp2.addPoint(13,14);
		mp2.addPoint(13,10);
	
		MyPolygon mp3 = new MyPolygon();
		mp3.addPoint(12, 10);
		mp3.addPoint(12,7);
	    System.out.println(QuantiShapeCalculator.isIntersected(mp3, mp2));*/
		
		MyPolygon m1 = new MyPolygon();
		m1.addPoint(200, 150);
		m1.addPoint(250, 200);
		m1.addPoint(300, 150);
		m1.addPoint(250, 100);
		
	
		MyPolygon m2 = new MyPolygon();
		m2.addPoint(200, 200);
		m2.addPoint(200, 210);
		m2.addPoint(1200, 210);
		m2.addPoint(1200, 200);
		
		MyPolygon m3 = new MyPolygon();
		m3.addPoint(300, 201);
		m3.addPoint(500, 201);
		
		
		MyPolygon m4 = new MyPolygon();
		m4.addPoint ( 286,400);
		m4.addPoint( 210,530);
		m4.addPoint ( 434,660);
		m4.addPoint( 510,530);
		
		
		MyPolygon m5 = new MyPolygon();
		m5.addPoint(300, 480);
		m5.addPoint(260, 480);
		
		MyPolygon m6 = new MyPolygon();
		m6.addPoint ( 286,400);
		m6.addPoint ( 286,400);

	
	/*	MyPolygon m3 = new MyPolygon();
		m2.addPoint(200, 100);
		m2.addPoint(200, 125);
		
		Line2D.Double ld1 = new Line2D.Double();
		ld1.setLine(200, 100, 200, 150);
		
		Line2D.Double ld2 = new Line2D.Double();
		ld2.setLine(200, 100, 300, 100);

		System.out.println(QuantiShapeCalculator.FindLineIntersection(new Point(200,100), new Point(200,150), new Point(200,100), new Point(200,130)));*/
		//System.out.println(QuantiShapeCalculator.isIntersected(m4, m5));
		System.out.println(QuantiShapeCalculator.isIntersected(m4,m6));
	     System.out.println(m1.contains(new Point(300,480)));
	/*	LinkedList<Integer> test1 = new LinkedList<Integer>();
		test1.add(1);
		
		LinkedList<Integer> test2 = new LinkedList<Integer>();
		test2.addAll(test1);
		test1.clear();
		System.out.println(test2);*/
	
	}
	

}
