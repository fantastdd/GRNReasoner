package quali;

import quali.util.Logger;

/**
 * If no solution found during the backtracking, an approx solution will be returned
 * @author Gary, Jochen Renz
 *
 */
public class ApproxSolution {
	private Node node;
	private MBR[] mbrs;
	private Logger logger;
	
	public ApproxSolution(MBR[] mbrs, Node node , Logger logger) {
		super();
		this.node = node;
		this.mbrs = mbrs;
		this.logger = logger;
	}

	public void getApporxSolution()
	{
	   for (MBR mbr : mbrs)
	   {
		   int unary;
		   Configuration conf;
		   unary = logger.getMostLikelyUnary(mbr.uid);
		   conf = node.lookup(mbr);
		   if(unary == -1)
		   {
			  
			  int ul = 0; // the upper left block of the MBR
			  int ur = 0;
			  int bl = 0;
			  int br = 0;
			  //approximate using neighbors
				for (Neighbor neighbor : conf.getNeighbors())
				{
					MBR mbr1 = neighbor.getMbr();
					MBR ulmbr = new MBR(mbr.x , mbr.y , mbr.width/2 , mbr.height/2);
					MBR urmbr = new MBR(mbr.x + mbr.width/2 , mbr.y , mbr.width/2 , mbr.height/2);
					MBR blmbr = new MBR(mbr.x , mbr.y + mbr.height/2 , mbr.width/2 , mbr.height/2);
					MBR brmbr = new MBR(mbr.x + mbr.width/2 , mbr.y + mbr.height/2 , mbr.width/2 , mbr.height/2);
					
			
					  if(mbr1.intersects(ulmbr))	
						ul += (mbr1.intersection(ulmbr)).width * (mbr1.intersection(ulmbr)).height;
					  if(mbr1.intersects(urmbr))	
						ur += (mbr1.intersection(urmbr)).width * (mbr1.intersection(urmbr)).height;
					  if(mbr1.intersects(blmbr))	
						bl += (mbr1.intersection(blmbr)).width * (mbr1.intersection(blmbr)).height;
					  if(mbr1.intersects(brmbr))	
						br += (mbr1.intersection(brmbr)).width * (mbr1.intersection(brmbr)).height;
					
				}
				int lean_to_left_blocked = ul + br;
				int lean_to_right_blocked = ur + bl;
				
				if(lean_to_left_blocked == 0 && lean_to_right_blocked == 0)
					unary = 0;
				else
				if(lean_to_left_blocked > lean_to_right_blocked)
					unary = 1;
				else
					unary = 2;
				
				System.out.println("  Fast Approx  " + conf.toShortString() + "   " + unary + "  " + lean_to_left_blocked + "  " + lean_to_right_blocked);
				System.out.println(" ul " + ul + " ur " + ur + " bl " + bl + " br " + br);
		   }  
		   else
			   System.out.println(conf.toShortString() + "   " + unary);
		   conf.unary = unary;
	   }
	}
}
