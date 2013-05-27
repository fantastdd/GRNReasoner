package quanti.test;

import java.awt.Graphics;
import java.awt.Polygon;
import javax.swing.JFrame;


@SuppressWarnings("serial")
public class ScenarioPanelPoly extends JFrame {
	Polygon[]  polygons ;
	public ScenarioPanelPoly()
	{
		
	}
	public void run(Polygon...polygons)
	{
		
		this.polygons = polygons;
		buildInitialCanvas();
		
		
	}
	private void buildInitialCanvas()
	{
		this.setSize(1500,1000);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle(" Simulator for Angular Rectangle Representation");
		this.setVisible(true);
	    super.paint(getGraphics());
	}
	public void paint(Graphics g) {
       for(Polygon polygon : polygons)
		{  
			/*if(count%4 == 0)
				g.setColor(Color.magenta);
			else if (count%4 == 1)
				g.setColor(Color.blue);
			else if(count%4 == 2)
				g.setColor(Color.black);
			else if(count%4 == 3)
				g.setColor(Color.cyan);*/
    	   //g.drawLine(100, 200, 200, 200);
			  g.drawPolygon(polygon);
		}
		
	}
	public static void main(String args[])
	{
	   new ScenarioPanelPoly();
	}

}
