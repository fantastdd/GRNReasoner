package main;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;

import javax.swing.JFrame;

import quali.Configuration;
import quali.MBR;


public class ScenarioPanel extends JFrame {
	LinkedList<MBR> mbrs = new LinkedList<MBR>();
	public ScenarioPanel()
	{
		
	}
	public void run(LinkedList<MBR> mbrs)
	{
		this.mbrs = mbrs;
		
		this.buildInitialCanvas();
		
		
	}
	private void buildInitialCanvas()
	{
		this.setSize(1500,1000);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle(" Simulator for Angular Rectangle Representation");
		this.setVisible(true);
	    super.paint(this.getGraphics());
	}
	public void paint(Graphics g) {
       //super.paint(g);
		int count = 0;
		for(MBR mbr:mbrs)
		{  
			count++;
			/*if(count%4 == 0)
				g.setColor(Color.magenta);
			else if (count%4 == 1)
				g.setColor(Color.blue);
			else if(count%4 == 2)
				g.setColor(Color.black);
			else if(count%4 == 3)
				g.setColor(Color.cyan);*/
			g.drawRect(mbr.x,mbr.y,mbr.width,mbr.height);
			Configuration conf = new Configuration(mbr);
		/*	g.drawLine(conf.getDiagonal_left().xpoints[0], conf.getDiagonal_left().ypoints[0], conf.getDiagonal_left().xpoints[1], conf.getDiagonal_left().ypoints[1]);
			g.drawLine(conf.getCore_left().xpoints[0], conf.getCore_left().ypoints[0],conf.getCore_left().xpoints[1], conf.getCore_left().ypoints[1]);
			g.drawLine(conf.getCore_left().xpoints[1], conf.getCore_left().ypoints[1],conf.getCore_left().xpoints[2], conf.getCore_left().ypoints[2]);
			g.drawLine(conf.getCore_left().xpoints[2], conf.getCore_left().ypoints[2],conf.getCore_left().xpoints[3], conf.getCore_left().ypoints[3]);
			g.drawLine(conf.getCore_left().xpoints[3], conf.getCore_left().ypoints[3],conf.getCore_left().xpoints[0], conf.getCore_left().ypoints[0]);*/
			//g.drawString(mbr.getId()+"", (int)mbr.getCenterX(), (int)mbr.getCenterY());
		}
		
	}
	public static void main(String args[])
	{
	   ScenarioPanel gui = new ScenarioPanel();
	   //gui.run();
	}
}
