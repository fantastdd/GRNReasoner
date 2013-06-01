package common.util;

import java.util.Comparator;

import quali.Neighbor;

public class NeighborComparatorByMBRID implements Comparator<Neighbor> {

	@Override
	public int compare(Neighbor o1, Neighbor o2) {
		
		return ((Integer)o1.getMbr().getId()).compareTo(o2.getMbr().getId());
	}



}
