package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.LinkedList;

import javax.swing.JFrame;

import quali.Configuration;
import quali.MBR;
import quali.TestNode;
import quali.util.Logger;


public class ScenarioPanel extends JFrame {
	LinkedList<MBR> mbrs = new LinkedList<MBR>();
	TestNode sol = null;
	TestNode node = null;
	public ScenarioPanel()
	{
		this.setTitle(" Simulator for Angular Rectangle Representation");
	}
	public void run(LinkedList<MBR> mbrs)
	{
		this.mbrs = mbrs;
		System.out.println(mbrs.size());
		buildInitialCanvas();
		
	}
	
	
	
	
	public void run(LinkedList<MBR> mbrs , TestNode node , boolean sol)
	{
	     	this.mbrs = mbrs;
	     	System.out.println(mbrs.size());
	     if(sol)
	     {	
	    	 this.sol = node;
	         setTitle("First Sol");
	     }
	     else
	     {	 
	    	 this.node = node;
	         setTitle("Approx Sol");
	     }
	     buildInitialCanvas();
		    
		
	}
	
	private void buildInitialCanvas()
	{
		this.setSize(1200,800);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	
		this.setVisible(true);
	    super.paint(getGraphics());
	}
	public void paint(Graphics g) {
       for(MBR mbr:mbrs)
		{  
			/*if(count%4 == 0)
				g.setColor(Color.magenta);
			else if (count%4 == 1)
				g.setColor(Color.blue);
			else if(count%4 == 2)
				g.setColor(Color.black);
			else if(count%4 == 3)
				g.setColor(Color.cyan);*/
    	   if(sol == null && node == null){
			g.drawRect(mbr.x,mbr.y,mbr.width,mbr.height);
			
		/*	g.drawLine(conf.getDiagonal_left().xpoints[0], conf.getDiagonal_left().ypoints[0], conf.getDiagonal_left().xpoints[1], conf.getDiagonal_left().ypoints[1]);
			g.drawLine(conf.getCore_left().xpoints[0], conf.getCore_left().ypoints[0],conf.getCore_left().xpoints[1], conf.getCore_left().ypoints[1]);
			g.drawLine(conf.getCore_left().xpoints[1], conf.getCore_left().ypoints[1],conf.getCore_left().xpoints[2], conf.getCore_left().ypoints[2]);
			g.drawLine(conf.getCore_left().xpoints[2], conf.getCore_left().ypoints[2],conf.getCore_left().xpoints[3], conf.getCore_left().ypoints[3]);
			g.drawLine(conf.getCore_left().xpoints[3], conf.getCore_left().ypoints[3],conf.getCore_left().xpoints[0], conf.getCore_left().ypoints[0]);*/
			g.drawString(mbr.getId()+"", (int)mbr.getCenterX(), (int)mbr.getCenterY());
    	   }
    	   else
    	   {
    		   int unary;
    		   Configuration conf;
    		   if(sol != null)
    		   {
    			   conf = sol.lookup(mbr);
    			   unary = conf.unary;
    		   }
    		   else 
    		   {
    			   unary = Logger.getMostLikelyUnary(mbr.getId());
    			   conf = node.lookup(mbr);
    			   System.out.println(conf.toShortString() + "   " + unary);
    		   }
    			
    			int x = mbr.x;
    			int y = mbr.y;
    			int width = mbr.width;
    			int height = mbr.height;
    			int hheight = mbr.height/2;
    			int hwidth = mbr.width/2;
    			if(unary == 0)
    			{
    				//draw solid blocks
    			    g.setColor(Color.gray);
    				g.fillRect(mbr.x,mbr.y,mbr.width,mbr.height);
    				g.setColor(Color.black);
    	    		g.drawRect(mbr.x,mbr.y,mbr.width,mbr.height);
    	    		g.drawString(mbr.getId()+"", (int)mbr.getCenterX(), (int)mbr.getCenterY());
    			} 
    			else if (unary == 1 || unary == 2)
    			{
    				//draw slim leans to right rectangles
    				 Polygon p = new Polygon();
    				 int new_limit_horizontal = 0;
    				 int new_limit_vertical = 0;
    				
    				  if(mbr.width > mbr.height)
    				  {
    					
    					 new_limit_horizontal = conf.limit_horizontal/2;
    					 //System.out.println(width + "  " + conf.limit_horizontal);
    					 new_limit_vertical =
    							hheight - 
    							(int)Math.sqrt((hwidth - new_limit_horizontal)*(hwidth -  new_limit_horizontal) + 
    									hheight * hheight - hwidth * hwidth );
    				  
    				  } 
    				  else
    				  {
    					   					 
     					 new_limit_vertical = conf.limit_vertical/2;
     					 //System.out.println(width + "  " + conf.limit_horizontal);
     					 new_limit_horizontal =
     							hwidth - 
     							(int)Math.sqrt((hheight- new_limit_vertical)*(hheight -  new_limit_vertical) 
     									+ hwidth * hwidth - hheight * hheight );
     					// System.out.println(new_limit_vertical);
     			
    				  }
    				  if(unary == 1)
    				  {
    					 p.addPoint(x + new_limit_horizontal, y + height);
     					 p.addPoint(x + width , y + new_limit_vertical);
     					 p.addPoint(x + width - new_limit_horizontal, y);
     					 p.addPoint(x, y + height - new_limit_vertical);
     				  }
    				  else
    					  if(unary == 2)
    					  {
    						  p.addPoint(x , y + new_limit_vertical);
    	     				  p.addPoint(x + width - new_limit_horizontal , y + height);
    	     				  p.addPoint(x + width , y + height - new_limit_vertical);
    	     				  p.addPoint(x + new_limit_horizontal, y );
    					  }
     					 g.setColor(Color.gray);
     					 g.fillPolygon(p);
     					 g.setColor(Color.black);
     	    	    	 g.drawRect(mbr.x,mbr.y,mbr.width,mbr.height);
     	    	    	g.drawString(mbr.getId()+"", (int)mbr.getCenterX(), (int)mbr.getCenterY());
    				  
    				
    				}
    			else if (unary == 3 || unary == 4)
    			{
    				//draw slim leans to right rectangles
    				Polygon p = new Polygon();
    				int new_limit_horizontal = 0;
   				 	int new_limit_vertical = 0;
   				
   				 	if(mbr.width > mbr.height)
   				 	{
   					
   					 new_limit_horizontal = conf.limit_horizontal/2;
   					 //System.out.println(width + "  " + conf.limit_horizontal);
   					 new_limit_vertical =
   							hheight - 
   							(int)Math.sqrt((hwidth - new_limit_horizontal)*(hwidth -  new_limit_horizontal) + 
   									hheight * hheight - hwidth * hwidth );
   				  
   				 	} 
   				 	else
   				 	{
   					   					 
    					 new_limit_vertical = conf.limit_vertical/2;
    					 //System.out.println(width + "  " + conf.limit_horizontal);
    					 new_limit_horizontal =
    							hwidth - 
    							(int)Math.sqrt((hheight- new_limit_vertical)*(hheight -  new_limit_vertical) 
    									+ hwidth * hwidth - hheight * hheight );
    					// System.out.println(new_limit_vertical);
    			
   				 	}
    				
   				 	if(unary == 3)
   				 	{
	   					 p.addPoint(x, y + new_limit_vertical);
						 p.addPoint(x + new_limit_horizontal, y + height);
						 p.addPoint(x + width , y + height - new_limit_vertical);
						 p.addPoint(x + width - new_limit_horizontal, y);
				
   				 	}
				  else
					  if(unary == 4)
					  {
						  p.addPoint(x , y + height - new_limit_vertical);
	     				  p.addPoint(x + width - new_limit_horizontal , y + height);
	     				  p.addPoint(x + width , y  + new_limit_vertical);
	     				  p.addPoint(x + new_limit_horizontal, y );
					  }
					 g.setColor(Color.gray);
					 g.fillPolygon(p);
					 g.setColor(Color.black);
	    	    	 g.drawRect(mbr.x,mbr.y,mbr.width,mbr.height);
	    	    	 g.drawString(mbr.getId()+"", (int)mbr.getCenterX(), (int)mbr.getCenterY());
    				
    			} 
    				  
    	   }
    	   
    	   }
		
	}
	public static void main(String args[])
	{
	   new ScenarioPanel();
	}

}
