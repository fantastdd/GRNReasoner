package common.util;

import java.util.Comparator;

import quali.Configuration;
import quali.MBR;

public class MBRYComparator implements Comparator<Configuration> {

	@Override
	public int compare(Configuration c1, Configuration c2) {
	       
		MBR m1 = c1.getMbr();
		MBR m2 = c2.getMbr();
		Integer m1y = (int) (m1.getHeight() + m1.y);
		Integer m2y = (int) (m2.getHeight() + m2.y);
		
		m1y = (int) m1.getCenterY();
		m2y = (int) m2.getCenterY();
		
		return m2y.compareTo(m1y);
		
	}

}
