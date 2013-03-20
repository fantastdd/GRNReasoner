package action;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import javax.swing.JFrame;

import quali.ShapeCalculator;
import quanti.PolygonRegister;

import common.MyPolygon;



public class QualiMouseAdapter extends MouseAdapter {
    private JFrame frame;
    private int startX;
    private int startY;
    private Graphics2D g;
    private QualiMouseController mc;
	public QualiMouseAdapter(JFrame mainWindow, QualiMouseController mc)
	{
		frame = mainWindow;
		g = (Graphics2D) frame.getGraphics();
        this.mc = mc;
	}
    @Override
	public void mousePressed(MouseEvent e)
	{
		startX = e.getX();
		startY = e.getY();
	    mc.setStartX(startX);
	    mc.setStartY(startY);
	 
		
	}
    @Override
    public void mouseEntered(MouseEvent e){}
    @Override
    public void mouseExited(MouseEvent e){}
    @Override
    public void mouseClicked(MouseEvent e){}
    @Override
    public void mouseReleased(MouseEvent e)
    {
    	    g.setColor(Color.red);	
		    int width = Math.abs(e.getX() - startX);
		    int height = Math.abs(e.getY() - startY);
		    Rectangle rec = new Rectangle(startX, startY, width, height);
	        g.draw(rec);
	        
	      /*
	       * //Qualitative Representation 
	       * {
	        	EntityRegister.registerRectangle(rec);
	        	EntityRegister.constructCSP();
	        }
	        */
	        for ( Point p : mc.getEssentialPoints().getPoints(rec))
	        {
	        	
	        	g.fillRoundRect(p.x, p.y, 5,5,5,5);
	       
	        }
	        
	      
	        LinkedList<LinkedList<LinkedList<Point>>> res = ShapeCalculator.getIntersectedPoints(rec);
	        LinkedList<LinkedList<Point>> xpair = res.get(0);
	        LinkedList<LinkedList<Point>> ypair = res.get(1);
	     
	    
	        g.setColor(Color.magenta); 
	        MyPolygon pl1 = new MyPolygon(xpair.get(0).get(0),ypair.get(0).get(0),xpair.get(0).get(1),ypair.get(0).get(1)); 
            g.draw(pl1);
	        PolygonRegister.register(pl1);
	        PolygonRegister.verifySolidProperty();
	       
	       for (Point p : PolygonRegister.getPoints())
	       {  
	    	   
	    	   g.setColor(Color.black);
	      	   g.drawRoundRect(p.x, p.y, 6, 6, 6, 6);
	       }
	   /*     g.setColor(Color.yellow);
	      
	        MyPolygon pl2 = new MyPolygon(xpair.get(0).get(0),ypair.get(1).get(0),xpair.get(0).get(1),ypair.get(1).get(1)); 
	        g.draw(pl2);
	        g.setColor(Color.blue);
	      
	        MyPolygon pl3 = new MyPolygon(xpair.get(1).get(0),ypair.get(1).get(0),xpair.get(1).get(1),ypair.get(1).get(1)); 
	        g.draw(pl3);
	        g.setColor(Color.green);
	    
	        MyPolygon pl4 = new MyPolygon(xpair.get(1).get(0),ypair.get(0).get(0),xpair.get(1).get(1),ypair.get(0).get(1)); 
	        g.draw(pl4);*/
	     
    }
  
    public void drawLine(Point a, Point b)
    {
    	g.drawLine((int)a.getX(), (int)a.getY(), (int)b.getX(), (int)b.getY());
    }
    public void drawPoint(Point a)
    {
    	 g.setColor(Color.black);
    	 g.drawRoundRect(a.x, a.y, 6, 6, 6, 6);
    }
    public void drawPolygon(Point[] points)
    {
    	
    }

}
