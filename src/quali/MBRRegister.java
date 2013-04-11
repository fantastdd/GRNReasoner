package quali;

import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;

public class MBRRegister {
	private static LinkedList<MBR> mbrs = new LinkedList<MBR>();
	private static LinkedList<MBR> edge = new LinkedList<MBR>();

	public static LinkedList<MBR> getMbrs() {
		return mbrs;
	}

	public static void setMbrs(LinkedList<MBR> mbrs) {
		MBRRegister.mbrs = mbrs;
	}

	private static int count = 0;

	public static void registerRectangle(Rectangle rec, boolean edge) {
		MBR mbr = new MBR(rec);
		if (edge)
			MBRRegister.edge.add(mbr);
		if (!mbrs.contains(mbr)) {
			mbr.setId(count++);
			mbrs.add(mbr);
		}
	}

	public static void registerMBR(MBR mbr, boolean edge) {

		if (edge)
			MBRRegister.edge.add(mbr);
		if (!mbrs.contains(mbr)) {
			mbr.setId(count++);
			mbrs.add(mbr);
		}
	}

	public static void batchRegister(List<MBR> mbrs, MBR... edge) {

		for (MBR mbr : mbrs) {
			boolean isEdge = false;
			for (MBR _edge : edge) {
				if (_edge.equals(mbr))
					isEdge = true;

			}
			registerMBR(mbr, isEdge);
		}

	}

	public static TestNode constructTestNode() {

		TestNode node = new TestNode(mbrs, edge);

		return node;

	}

	
}
