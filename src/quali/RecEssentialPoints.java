package quali;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.LinkedList;


public class RecEssentialPoints {

	private int num;
	public RecEssentialPoints(int num){
		this.num = num;
	}
	public LinkedList<Point> getPoints(Rectangle rec)
	{
		LinkedList<Point> points = new LinkedList<Point>();
		for ( int i = 0; i < num ;  i++)
		{ 
			int step_width = (int) ( rec.getWidth()/( num + 1) * ( i + 1));
			int step_height = (int) ( rec.getHeight()/( num + 1) * ( i + 1));
			Point pt = new Point((int)(rec.getLocation().x + step_width) ,rec.getLocation().y);
			Point pb = new Point((int)(rec.getLocation().x + step_width) ,(int)(rec.getLocation().y + rec.getHeight()));
			Point pl = new Point(rec.getLocation().x,(int)(rec.getLocation().y + step_height));
			Point pr = new Point((int)(rec.getLocation().x + rec.getWidth()),(int)(rec.getLocation().y + step_height));
			points.add(pt);
			points.add(pb);
			points.add(pl);
			points.add(pr);
		}
		
		return points;
	}
	
}
