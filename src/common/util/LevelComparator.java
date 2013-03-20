package common.util;

import java.awt.Polygon;
import java.util.Comparator;

public class LevelComparator implements Comparator<Polygon> {

	@Override
	public int compare(Polygon arg0, Polygon arg1) {
		if (arg0.getBounds().getY() + arg0.getBounds().getHeight() > arg1.getBounds().getY() + arg1.getBounds().getHeight())
		     return 1;
		else
			if(arg0.getBounds().getCenterY() == arg1.getBounds().getCenterY())
			  return 0;
			else
				return -1;
		
	}

}
