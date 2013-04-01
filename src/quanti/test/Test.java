package quanti.test;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;

import quanti.QuantiShapeCalculator;

import main.ScenarioPanelPoly;


public class Test {
	static int id = 0;
	public boolean test = false;

	public static int getId()
	{
		return id++;
	}
	public static void main(String args[])
	{
		
		/*MyPolygon pl = new MyPolygon(new Point(50,50),new Point(60,50),new Point(60,60),new Point(50,60));

		
		MyPolygon pl1 = new MyPolygon(new Point(60,50),new Point(70,50),new Point(70,60),new Point(60,60));
	
		
		MyPolygon pl2 = new MyPolygon(new Point(0,0),new Point(0,10),new Point(10,10),new Point(10,0));

		
		MyPolygon pl3 = new MyPolygon(new Point(0,10),new Point(10,10),new Point(10,20),new Point(0,20));
		
		 PolygonRegister.register(pl);
		 PolygonRegister.register(pl1);
		 PolygonRegister.register(pl2);
		 PolygonRegister.register(pl3);
		 PolygonRegister.verifySolidProperty();
		 PolygonRegister.verifyGravity();*/
		//System.out.println(getId());
              		
		//Test t1 = new Test();
		
		//t1.test = true;
        Polygon p1 = new Polygon();
        
        p1.addPoint(510, 530);
        p1.addPoint(586, 660);
        p1.addPoint(810, 530);
        p1.addPoint(734, 400);
       /* p1.xpoints[0] = 510;
        p1.xpoints[1] = 510;
        p1.xpoints[2] = 810;
        p1.xpoints[3] = 810;
        
        p1.ypoints[0] = 400;
        p1.ypoints[1] = 660;
        p1.ypoints[2] = 660;
        p1.ypoints[3] = 400;*/
        
    
/*        Rectangle rec = p1.getBounds();
        System.out.println(rec);
        System.out.println(p1.npoints);*/
   
/*        System.out.println(p1.contains(new Point(200, 670)));
        System.out.println(p1.contains(new Point(200, 660)));
        System.out.println(p1.contains(new Point(1200, 660)));
        System.out.println(p1.contains(new Point(1200, 670)));*/
        
        Polygon p2 = new Polygon();
        p2.addPoint(720, 480);
        p2.addPoint(760, 480);
        ScenarioPanelPoly scenario = new ScenarioPanelPoly();
        scenario.run(p1,p2);
        System.out.println(QuantiShapeCalculator.isIntersected(p1, p2, true ));
	}
}
