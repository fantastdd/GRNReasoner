package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;

import javax.swing.JFrame;

import quali.Configuration;
import quali.MBR;
import quali.Neighbor;
import quali.Node;
import quali.util.Logger;


public class ScenarioPanel extends JFrame {
	MBR[] mbrs;
	
	Node node = null;
	boolean painted = false;
	public ScenarioPanel()
	{
		this.setTitle(" Simulator for Angular Rectangle Representation");
	}
	public void run(MBR[] mbrs)
	{
		this.mbrs = mbrs;
		System.out.println( " Total " + mbrs.length + " blocks displayed");
		buildInitialCanvas();
		
	}
	
	
	
	
	public void run(MBR[] mbrs , Node node , boolean sol)
	{
	     	this.mbrs = mbrs;
	     	System.out.println(mbrs.length);
	     	 this.node = node;
	     if(sol)
	     {	
	    	
	         setTitle("First Sol");
	     }
	     else
	     {	 
	    	
	         setTitle("Approx Sol");
	     }
	     buildInitialCanvas();
		    
		
	}
	
	private void buildInitialCanvas()
	{
		this.setSize(1200,800);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	    //this.setResizable(false);
		this.setVisible(true);
	    super.paint(getGraphics());
		
	}
	@Override
	public void paint(Graphics g) {
	//if(!painted)
       for(MBR mbr:mbrs)
		{  
			
    	   if( node == null){
			g.drawRect(mbr.x,mbr.y,mbr.width,mbr.height);
			
		/*	g.drawLine(conf.getDiagonal_left().xpoints[0], conf.getDiagonal_left().ypoints[0], conf.getDiagonal_left().xpoints[1], conf.getDiagonal_left().ypoints[1]);
			g.drawLine(conf.getCore_left().xpoints[0], conf.getCore_left().ypoints[0],conf.getCore_left().xpoints[1], conf.getCore_left().ypoints[1]);
			g.drawLine(conf.getCore_left().xpoints[1], conf.getCore_left().ypoints[1],conf.getCore_left().xpoints[2], conf.getCore_left().ypoints[2]);
			g.drawLine(conf.getCore_left().xpoints[2], conf.getCore_left().ypoints[2],conf.getCore_left().xpoints[3], conf.getCore_left().ypoints[3]);
			g.drawLine(conf.getCore_left().xpoints[3], conf.getCore_left().ypoints[3],conf.getCore_left().xpoints[0], conf.getCore_left().ypoints[0]);*/
			g.drawString(mbr.uid +"", (int)mbr.getCenterX(), (int)mbr.getCenterY());
    	   }
    	   else
    	   {
    		   int unary;
    		   Configuration conf;
    		   conf = node.lookup(mbr);
    		   unary = conf.unary;
    	
    			
    			int x = mbr.x;
    			int y = mbr.y;
    			int width = mbr.width;
    			int height = mbr.height;
    			int hheight = mbr.height/2;
    			int hwidth = mbr.width/2;
    			if(unary == 0)
    			{
    				//draw solid blocks
    				System.out.println(" draw regular rec " + mbr.uid);
    			    g.setColor(Color.gray);
    				g.fillRect(mbr.x,mbr.y,mbr.width,mbr.height);
    				g.setColor(Color.black);
    	    		g.drawRect(mbr.x,mbr.y,mbr.width,mbr.height);
    	    		g.drawString(mbr.uid +" ", (int)mbr.getCenterX(), (int)mbr.getCenterY());
    	    		
    			} 
    			else if (unary == 1 || unary == 2)
    			{
    				//draw slim leans to right rectangles
    				 Polygon p = new Polygon();
    				 int new_limit_horizontal = 0;
    				 int new_limit_vertical = 0;
    				 //System.out.println(" " + conf.limit_vertical);
    				  if(mbr.width > mbr.height)
    				  {
    					
    					 new_limit_horizontal = conf.limit_horizontal/2;
    					 if(new_limit_horizontal == 0)
    						 new_limit_horizontal = conf.limit_horizontal;
    					 
    					 new_limit_vertical =
    							hheight - 
    							(int)Math.sqrt((hwidth - new_limit_horizontal)*(hwidth -  new_limit_horizontal) + 
    									hheight * hheight - hwidth * hwidth );
    					 //System.out.println(" xx " + new_limit_horizontal + "  " + new_limit_vertical);
    				  } 
    				  else
    				  {
    					   					 
     					 new_limit_vertical = conf.limit_vertical/2;
     					 if(new_limit_vertical == 0)
    						 new_limit_vertical = conf.limit_vertical;
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
     					 //Print ========================
     					 System.out.println(" draw poly "  + "  " + mbr.uid);
     					 for (int i = 0 ; i < p.npoints ; i ++){
     						 System.out.print("  " + p.xpoints[i] + "  " + p.xpoints[i] );
     					 }
     					 System.out.println();
     					 //Print End =====================
     					 g.setColor(Color.black);
     	    	    	 g.drawRect(mbr.x,mbr.y,mbr.width,mbr.height);
     	    	    	 g.drawString(mbr.uid +"", (int)mbr.getCenterX(), (int)mbr.getCenterY());
    				  
    				
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
   					 if(new_limit_horizontal == 0)
						 new_limit_horizontal = conf.limit_horizontal;
   					 //System.out.println(width + "  " + conf.limit_horizontal);
   					 new_limit_vertical =
   							hheight - 
   							(int)Math.sqrt((hwidth - new_limit_horizontal)*(hwidth -  new_limit_horizontal) + 
   									hheight * hheight - hwidth * hwidth );
   				  
   				 	} 
   				 	else
   				 	{
   					   					 
    					 new_limit_vertical = conf.limit_vertical/2;
    					 if(new_limit_vertical == 0)
    						 new_limit_vertical = conf.limit_vertical;
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
					 //Print ================================================================
 					 System.out.println(" draw poly "  + "  " + mbr.uid);
 					 for (int i = 0 ; i < p.npoints ; i ++){
 						 System.out.print("  " + p.xpoints[i] + "  " + p.xpoints[i] );
 					 }
 					 System.out.println();
 					 //Print End =====================
					 g.setColor(Color.black);
	    	    	 g.drawRect(mbr.x,mbr.y,mbr.width,mbr.height);
	    	    	 g.drawString(mbr.uid +"", (int)mbr.getCenterX(), (int)mbr.getCenterY());
    				
    			} 
    				  
    	   }
    	   
    	   }
	 
	//painted = true;
		
	}
	public static void main(String args[])
	{
	   Rectangle rec = new Rectangle (100,100,100,100);
	   Rectangle rec1 = new Rectangle (300,300,30,30);
	   System.out.println(rec.intersects(rec1));
	   System.out.println(rec1.intersection(rec).width * rec1.intersection(rec).height);
	}

}
