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
	public int hashcode()
	{
		 int hash  = 7;
		 hash = 31 *hash + mbr.hashcode();
		 return hash;
		

	}

	public boolean equals(Object object)
	{
		//  System.out.println(object instanceof Neighbor && getMbr().equals(((Neighbor)object).getMbr()) );
	   if(object instanceof Neighbor && getMbr().equals(((Neighbor)object).getMbr()))
	   {	   
		 
		   return true ;
	   
	   }
	   return false;
	   
	}
	
	
}
