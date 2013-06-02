package quanti.test;
import java.awt.Point;

import common.MyPolygon;


public class Test {
	static int id = 0;
	public boolean test = false;

	public static int getId()
	{
		return id++;
	}
	public static void main(String args[])
	{
		
      int[] a = new int[3];
	  a[0]  = 1;
	  for (int i : a)
	  System.out.println(i);
		
/*		int a = 1;
		int b = 2;
		if(a == 1 ||++b == 3)
		{}
		System.out.println(b);*/
		/*MyPolygon pl1 = new MyPolygon(new Point(50,50),new Point(60,50),new Point(60,60),new Point(50,60));
		MyPolygon pl2 = new MyPolygon();
	    System.out.println(QuantiShapeCalculator.isIntersected(pl1, pl2, true ));*/
		/*		MyPolygon pl = new MyPolygon(new Point(50,50),new Point(60,50),new Point(60,60),new Point(50,60));

		
		MyPolygon pl1 = new MyPolygon(new Point(60,50),new Point(70,50),new Point(70,60),new Point(60,60));
	
		
		MyPolygon pl2 = new MyPolygon(new Point(0,0),new Point(0,10),new Point(10,10),new Point(10,0));

		
		MyPolygon pl3 = new MyPolygon(new Point(0,10),new Point(10,10),new Point(10,20),new Point(0,20));
		
		 PolygonRegister.register(pl);
		 PolygonRegister.register(pl1);
		 PolygonRegister.register(pl2);
		 PolygonRegister.register(pl3);
		 PolygonRegister.verifySolidProperty();
		 PolygonRegister.verifyGravity();
		//System.out.println(getId());
              		
		//Test t1 = new Test();
		
		//t1.test = true;
        Polygon p1 = new Polygon();
        
        p1.addPoint(510, 530);
        p1.addPoint(586, 660);
        p1.addPoint(810, 530);
        p1.addPoint(734, 400);
        p1.xpoints[0] = 510;
        p1.xpoints[1] = 510;
        p1.xpoints[2] = 810;
        p1.xpoints[3] = 810;
        
        p1.ypoints[0] = 400;
        p1.ypoints[1] = 660;
        p1.ypoints[2] = 660;
        p1.ypoints[3] = 400;
        
    
        Rectangle rec = p1.getBounds();
        System.out.println(rec);
        System.out.println(p1.npoints);
   
        System.out.println(p1.contains(new Point(200, 670)));
        System.out.println(p1.contains(new Point(200, 660)));
        System.out.println(p1.contains(new Point(1200, 660)));
        System.out.println(p1.contains(new Point(1200, 670)));
        
        Polygon p2 = new Polygon();
        p2.addPoint(720, 480);
        p2.addPoint(760, 480);
        ScenarioPanelPoly scenario = new ScenarioPanelPoly();
        scenario.run(p1,p2);
        System.out.println(QuantiShapeCalculator.isIntersected(p1, p2, true ));*/
	  

	  // extreme case 1 ====================
/*	     MyPolygon pl1 = new MyPolygon(new Point( 578,451),new Point( 567,473),new Point( 615,496),new Point ( 626,474));
		MyPolygon pl2 = new MyPolygon(new Point( 599,490),new Point( 599,490));*/
	//extreme case 1 end ================================================
	  
	  //extreme case 2 ===================
	    /* MyPolygon pl1 = new MyPolygon(new Point( 711,472),new Point( 711,476),new Point( 756,476));
			MyPolygon pl2 = new MyPolygon(new Point( 623,575),new Point( 713,468));*/
	//extreme case 2 end ===================================
			
/*	  MyPolygon pl1 = new MyPolygon(new Point( 711,472),new Point( 711,476),new Point( 756,476));
		MyPolygon pl2 = new MyPolygon(new Point( 623,575),new Point( 717,468));
		System.out.println(QuantiShapeCalculator.isIntersected(pl1, pl2, true));
		 ScenarioPanelPoly scenario = new ScenarioPanelPoly();
	        scenario.run(pl1,pl2);*/

	  
    MyPolygon pl1 = new MyPolygon(new Point ( 675,473),new Point ( 697,477),new Point ( 719,352) , new Point ( 697,348));
		MyPolygon pl2 = new MyPolygon(new Point  ( 611,489),new Point( 623,515) , new Point( 683,489), new Point( 671,463));
		
	  
	 /* MyPolygon pl1 = new MyPolygon(new Point 	  ( 611,515), new Point ( 683,463));
		MyPolygon pl2 = new MyPolygon(new Point ( 675,473) , new Point( 675,477) , new Point( 697,477));*/

		//System.out.println(QuantiShapeCalculator.isIntersected(pl1, pl2, false));
		 ScenarioPanelPoly scenario = new ScenarioPanelPoly();
	        scenario.run(pl1,pl2);
		
/*		ScenarioIO sio = new ScenarioIO("l6c");

		LinkedList<LinkedList<MBR>> scenarios;
		try {
			scenarios = sio.load("l6c");
			LinkedList<MBR> s1 = scenarios.get(0);
			LinkedList<Rectangle> worldInVision = new LinkedList<Rectangle>();
			for (MBR mbr : s1)
			{
				worldInVision.add(mbr);
			}
			WorldinVision wiv = new WorldinVision();
			wiv.buildWorld(worldInVision);
			Configuration conf = new Configuration(wiv.mbrs.get(0));
			conf.unary = 3;
			Configuration tconf = new Configuration(wiv.mbrs.get(1));
			tconf.unary = 3;
			LinkedList<Contact> contacts = ContactManager.getContact(tconf, conf);
            for(Contact contact : contacts )
            {
            	System.out.println(conf.toShortString() + "   " + tconf.toShortString() + "   " + contact);
            	
            } 
			
			wiv.showWorldinVision();
		} catch (IOException e) {

			e.printStackTrace();
		}
		
	}*/
}
}
