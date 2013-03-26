package quali;

public class Neighbor {
    //neighbor MBR
	private MBR mbr;
    //neighbor type
	private byte neighborType;
	//the gap in between;
	private double gap;
	public Neighbor(MBR mbr, byte neighborType, double gap) {
		super();
		this.mbr = mbr;
		this.neighborType = neighborType;
		this.gap = gap;
	}
	public MBR getMbr() {
		return mbr;
	}
	public byte getNeighborType() {
		return neighborType;
	}
	public double getGap() {
		return gap;
	}
	
	
}
