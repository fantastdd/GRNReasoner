package quali;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.LinkedList;


public class ShapeCalculator {
 public static LinkedList<LinkedList<LinkedList<Point>>> getIntersectedPoints(Rectangle rec)
 {
	 // get yInterval randomly
	 
		 double hwidth = rec.getWidth()/2;
		 double hheight = rec.getHeight()/2;
         if(hwidth > hheight)
        	 return getIntersectedPointsWhenWidthDominates(rec);
         else
        	 return getIntersectedPointsWhenHeightDominates(rec);
	
 }
 
 private static LinkedList<LinkedList<LinkedList<Point>>> getIntersectedPointsWhenWidthDominates(Rectangle rec)
 {
	 
	 

	 double hwidth = rec.getWidth()/2;
	 double hheight = rec.getHeight()/2;
	 double yInterval = 0.0;
	 do
	    yInterval = Math.random() * hheight;
     while ( yInterval == 0);
	 
	
    	 
    double xInterval = xIntervalLength(hwidth,hheight,yInterval);
    LinkedList<Point> xpoints1 = new LinkedList<Point>();
    LinkedList<Point> xpoints2 = new LinkedList<Point>();
    LinkedList<Point> ypoints1 = new LinkedList<Point>();
    LinkedList<Point> ypoints2 = new LinkedList<Point>();
    
    xpoints1.add(new Point(((int)(rec.getLocation().x + xInterval)),rec.getLocation().y));
    xpoints1.add(new Point(((int)(rec.getLocation().x + rec.getWidth() - xInterval)),(int) (rec.getLocation().y + rec.getHeight())));
    
    xpoints2.add(new Point(((int)(rec.getLocation().x + xInterval)),(int) (rec.getLocation().y + rec.getHeight())));
    xpoints2.add(new Point(((int)(rec.getLocation().x + rec.getWidth() - xInterval)),rec.getLocation().y));
    
    
    ypoints1.add(
    				new Point(
    								rec.getLocation().x,
    								(int) (rec.getLocation().y + yInterval)
    						 )
    			);
    
    ypoints1.add(
    				new Point(
    								(int) (rec.getLocation().x + rec.getWidth()),
    								(int) (rec.getLocation().y + rec.getHeight() - yInterval)
    						 )
    			);
    
    
    ypoints2.add(
			new Point(
							rec.getLocation().x,
							(int) (rec.getLocation().y + rec.getHeight() - yInterval)
					 )
		);

    ypoints2.add(
						new Point(
									(int) (rec.getLocation().x + rec.getWidth()),
									(int) (rec.getLocation().y +  yInterval)
									
				));


	LinkedList<LinkedList<Point>> xpairs = new LinkedList<LinkedList<Point>>();
	LinkedList<LinkedList<Point>> ypairs = new LinkedList<LinkedList<Point>>();
	
	xpairs.add(xpoints1);
	xpairs.add(xpoints2);
	
	ypairs.add(ypoints1);
	ypairs.add(ypoints2);
	
	LinkedList<LinkedList<LinkedList<Point>>> res = new  LinkedList<LinkedList<LinkedList<Point>>>();
    res.add(xpairs);
    res.add(ypairs);
 return res;
	 
	 
 }
 
 
 
 private static LinkedList<LinkedList<LinkedList<Point>>> getIntersectedPointsWhenHeightDominates(Rectangle rec)
 {
	 
	 

	 double hwidth = rec.getWidth()/2;
	 double hheight = rec.getHeight()/2;
	 double xInterval = 0.0;
	 do
	    xInterval = Math.random() * hheight;
     while ( xInterval == 0);
	 
	
    	 
    double yInterval = yIntervalLength(hwidth,hheight,xInterval);
    
    LinkedList<Point> xpoints1 = new LinkedList<Point>();
    LinkedList<Point> xpoints2 = new LinkedList<Point>();
    LinkedList<Point> ypoints1 = new LinkedList<Point>();
    LinkedList<Point> ypoints2 = new LinkedList<Point>();
    
    xpoints1.add(new Point(((int)(rec.getLocation().x + xInterval)),rec.getLocation().y));
    xpoints1.add(new Point(((int)(rec.getLocation().x + rec.getWidth() - xInterval)),(int) (rec.getLocation().y + rec.getHeight())));
    
    xpoints2.add(new Point(((int)(rec.getLocation().x + xInterval)),(int) (rec.getLocation().y + rec.getHeight())));
    xpoints2.add(new Point(((int)(rec.getLocation().x + rec.getWidth() - xInterval)),rec.getLocation().y));
    
    
    ypoints1.add(
    				new Point(
    								rec.getLocation().x,
    								(int) (rec.getLocation().y + yInterval)
    						 )
    			);
    
    ypoints1.add(
    				new Point(
    								(int) (rec.getLocation().x + rec.getWidth()),
    								(int) (rec.getLocation().y + rec.getHeight() - yInterval)
    						 )
    			);
    
    
    ypoints2.add(
			new Point(
							rec.getLocation().x,
							(int) (rec.getLocation().y + rec.getHeight() - yInterval)
					 )
		);

    ypoints2.add(
						new Point(
									(int) (rec.getLocation().x + rec.getWidth()),
									(int) (rec.getLocation().y +  yInterval)
									
				));


	LinkedList<LinkedList<Point>> xpairs = new LinkedList<LinkedList<Point>>();
	LinkedList<LinkedList<Point>> ypairs = new LinkedList<LinkedList<Point>>();
	
	xpairs.add(xpoints1);
	xpairs.add(xpoints2);
	
	ypairs.add(ypoints1);
	ypairs.add(ypoints2);
	
	LinkedList<LinkedList<LinkedList<Point>>> res = new  LinkedList<LinkedList<LinkedList<Point>>>();
    res.add(xpairs);
    res.add(ypairs);
 return res;
	 
	 
 }
 private static double xIntervalLength(double hwidth, double hheight, double yInterval)
 {

	
		 double radius = Math.sqrt(  hwidth*hwidth + Math.pow(hheight - yInterval,2));
		 double x = Math.sqrt(radius * radius - hheight * hheight); 
		 double trInterval = hwidth - x;
		 return trInterval;
	 
 }
 
 private static double yIntervalLength(double hwidth, double hheight, double xInterval)
 {

	
		 double radius = Math.sqrt(  hheight*hheight + Math.pow(hwidth - xInterval,2));
		 double y = Math.sqrt(radius * radius - hwidth * hwidth); 
		 double yInterval = hheight - y;
		 return yInterval;
	 
 }
 
}
