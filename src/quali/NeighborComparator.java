package quali;

import java.util.Comparator;

public class NeighborComparator implements Comparator<Neighbor> {

	@Override
	public int compare(Neighbor o1, Neighbor o2) {
		// TODO Auto-generated method stub
		return ((Double)o1.getGap()).compareTo((Double)o2.getGap());
	}



}
