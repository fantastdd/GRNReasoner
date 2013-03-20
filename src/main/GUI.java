package main;

import java.awt.Graphics;

import javax.swing.JFrame;

import action.MouseController;


public class GUI extends JFrame {

	public GUI()
	{
		
	}
	public void run()
	{
		this.buildInitialCanvas();
		MouseController mc = new MouseController(this);
		this.addMouseMotionListener(mc.getMma());
		//this.addMouseListener(mc.getMa());
		this.addMouseListener(mc.getMasp());
	}
	private void buildInitialCanvas()
	{
		this.setSize(800,600);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle(" Simulator for Angular Rectangle Representation");
		this.setVisible(true);
	    super.paint(this.getGraphics());
	}
	public void paint(Graphics g) {
       //super.paint(g);
	}
	public static void main(String args[])
	{
	   GUI gui = new GUI();
	   gui.run();
	}
}
