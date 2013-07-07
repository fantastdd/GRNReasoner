package quali;

import quali.util.Logger;

/**
 * If no solution found during the backtracking, an approx solution will be returned
 * @author Gary, Jochen
 *
 */
public class ApproxSolution {
	private Node node;
	private MBR[] mbrs;
	private Logger logger;
	//private final int threshold = 1000;
	private final int coefficient = 2000;
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
		  
		   Configuration conf;
		   int[] unaryweights;
		   int[] fastApproxUnarys = new int[5];
		   unaryweights = logger.mbrs.get(mbr.uid);
		   conf = node.lookupByUID(mbr.uid);
		  // if(unaryweights[1] < threshold)
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
				    fastApproxUnarys[0] = 1;
				else
				    if(lean_to_left_blocked > lean_to_right_blocked)
					fastApproxUnarys[1] = 1;
				    else
					fastApproxUnarys[2] = 1;
				
				System.out.print("  area approx  " + conf.toShortString() + "  " + lean_to_left_blocked + "  " + lean_to_right_blocked);
				System.out.println(" ul " + ul + " ur " + ur + " bl " + bl + " br " + br);
		   
		   }  
		   //else
		   
		           int unary = 0;
		           int max = 0;
		           for (int i = 0; i < 5; i++) {
		            //since we assign the unary value during the backtracking in the order of 
		             //as the regular, slim lean to left etc, so we need to give more weights on the latter ones.
		            double orderWeights = ((i + 1.6)/2) * unaryweights[i]; 
		            int weights = (int)orderWeights   + fastApproxUnarys[i] * coefficient ; 
		   	    if ( weights > max) {
		   		max = weights ;
		   		unary = i;
		   	    }
		   	}
		           System.out.println(" final approx  weight: " + max + "  " + conf.mbr + "   " + unary);
	           
		   conf.unary = unary;
	   }
	}
}
