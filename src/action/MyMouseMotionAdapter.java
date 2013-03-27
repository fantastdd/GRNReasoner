package action;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JFrame;



public class MyMouseMotionAdapter extends MouseMotionAdapter{
    private Graphics g;
    private MouseController mc;

    
	public MyMouseMotionAdapter(JFrame frame,MouseController mc)
	{
		this.mc = mc;
		g = frame.getGraphics();
	}

	
	@Override
	public void mouseMoved(MouseEvent e)
	{
		
	}

	@Override 
	public void mouseDragged(MouseEvent e)
	{
		mc.setDragged(true);
       /* frame.repaint();
		int startX = mc.getStartX();
		int startY = mc.getStartY();
	
		//if(startDrawing)
		{
			g.setColor(Color.red);	
		    int width = Math.abs(e.getX() - startX);
		    int height = Math.abs(e.getY() - startY);
	     
		    g.drawRect(startX, startY, width, height);  	
	
		}*/
	}
	
}
