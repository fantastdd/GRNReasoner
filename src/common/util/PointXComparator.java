package common.util;

import java.awt.Point;
import java.util.Comparator;

public class PointXComparator implements Comparator<Point> {

	@Override
	public int compare(Point p1, Point p2) {
	    
		if(p1.x > p2.x)
			return 1;
		else
			if(p1.x == p2.x)
		{
			return 0;
		}
			else
				return -1;
		
	}

}
