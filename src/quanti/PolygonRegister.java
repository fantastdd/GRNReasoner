package quanti;
import java.awt.Point;
import java.awt.Polygon;
import java.util.Collections;
import java.util.LinkedList;

import common.CompositeStructure;
import common.Edge;
import common.MyPolygon;
import common.util.LevelComparator;



public class PolygonRegister {
private static  LinkedList<MyPolygon> polys = new LinkedList<MyPolygon>();
private static LinkedList<Point> points = new LinkedList<Point>();
private static CompositeStructure cs = new CompositeStructure();
//private static   CSPConstructor cspc = new   CSPConstructor();
public static void register(MyPolygon poly)
{
	
	if(!polys.contains(poly))
     	polys.add(poly);

}
public static void verifySolidProperty()
{
System.out.println("-------------------  Verification Starts ------------------------");
    for (int i = 0; i < polys.size() - 1; i ++)
    	for( int j = i + 1; j < polys.size(); j++)
    {
    	    LinkedList<Point> _points = new LinkedList<Point>();
    	  //  _points = QuantiShapeCalculator.isIntersected(polys.get(i), polys.get(j));
    	    _points = QuantiShapeCalculator.isIntersectedWithReasoning(polys.get(i), polys.get(j),5.0);
    	    
    	    polys.get(i).addTouch(_points, polys.get(j));
    	    
    	    points.addAll(_points);
    	    
    	    _points = QuantiShapeCalculator.isIntersectedWithReasoning(polys.get(j), polys.get(i),5.0);
    	    
    	    polys.get(j).addTouch(_points, polys.get(i));
    	    
    	    points.addAll(_points);
    
    }
 System.out.println(" ------------------- End of Verification -------------------------");
}
public static void verifyGravity()
{
	
   Collections.sort(polys,new LevelComparator());
 
   /* verify local support */
  /* for (MyPolygon poly : polys)
   {
	  
	   if(!poly.isLocalStable())
	   {   
		  stability = false;
		  System.out.println("#### local unstable:  " + poly);
	   }
   }*/
   
 
  
   
   /*  verify the global gravity by iterating the reamining polygons
    *  however, not all the polygons are assoicated with the target composite structure, in the futer
    *  should only reterive the core support polygons from sub-polygons of compoisite structure*/
   
   LinkedList<MyPolygon> _polys = new LinkedList<MyPolygon>();
   _polys.addAll(polys);
   for (MyPolygon pl: polys)
   {
	   cs.addPolygon(pl);
	   cs.verifyStability(_polys);
	  // verifyGlobalGravity(cs,_polys); 
	   
   }

}


public static LinkedList<Point> getPoints() {
	return points;
}


}