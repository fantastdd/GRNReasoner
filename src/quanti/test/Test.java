package quanti.test;

import java.awt.Point;

import quanti.PolygonRegister;

import common.MyPolygon;

public class Test {
	public static void main(String args[])
	{
		
		MyPolygon pl = new MyPolygon(new Point(50,50),new Point(60,50),new Point(60,60),new Point(50,60));

		
		MyPolygon pl1 = new MyPolygon(new Point(60,50),new Point(70,50),new Point(70,60),new Point(60,60));
	
		
		MyPolygon pl2 = new MyPolygon(new Point(0,0),new Point(0,10),new Point(10,10),new Point(10,0));

		
		MyPolygon pl3 = new MyPolygon(new Point(0,10),new Point(10,10),new Point(10,20),new Point(0,20));
		
		 PolygonRegister.register(pl);
		 PolygonRegister.register(pl1);
		 PolygonRegister.register(pl2);
		 PolygonRegister.register(pl3);
		 PolygonRegister.verifySolidProperty();
		 PolygonRegister.verifyGravity();
		
              		
		
		
	}
}
