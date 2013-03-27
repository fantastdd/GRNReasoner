package quali;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.LinkedList;

public class MBRReasonerAdvance {
     
	static
	{
		try {
			new PrintStream("test.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	 
	}
    private LinkedList<Configuration> confs = new LinkedList<Configuration>();

    
    
    
	private LinkedList<Node> candidnates_solid = new LinkedList<Node>();
	private LinkedList<Node> candidnates_gravity = new LinkedList<Node>();
	private LinkedList<Node> candidnates_result = new LinkedList<Node>();
    
	//search ----
	public boolean search(TestNode node)
	{
		if(checkSolution(node))
		{
		    //output the solutions	
			System.out.println("solution is found:   " + node);
			return true;
		}
		else
		{
		    LinkedList<TestNode> refinements = refine(node);
		    if(!refinements.isEmpty())
		    {
		    	for(TestNode refinement: refinements)
		    		if(search(refinement)) {return true;};
		    }
		}
		return false;
		
	}
	public LinkedList<TestNode> refine(final TestNode node)
	{   
		
		LinkedList<TestNode> refinements = new LinkedList<TestNode>();
	   if(!node.isCompleted()){
				Configuration cconf = node.pop().clone(); 
				//there are three types, namely, regular, left, right
				while(!cconf.isCompleted())
				{
					cconf.nextInitialization();
					
					//--- first initialize the cconf's neighbor's configuration that makes cconf local stable.
					TestNode _node = formLocalStability(cconf,node);
					
					
		            if(solidValidity(cconf , node))
					{
		            	//TestNode _node = node.clone();
		            	_node.update(cconf.clone());
						confs.add(cconf.clone());
						refinements.add(_node);
					}
				}
		}
		return refinements;
	}

    //------ test ----
    public boolean checkSolution(TestNode node)
    {
    	return node.isCompleted();
    }

    //verify the solid properties among all the $expanded$ confs
    // only test the regular/angular case. do not do the block_region test (rely on precise data)
    public boolean solidValidity(final Configuration cconf, final TestNode confs)
    {
    	
    	/* test the regular/angular property*/
	   //System.out.println(cconf.getNeighbors().size());
    	for (Neighbor neighbor: cconf.getNeighbors())
    	{
             MBR mbr_neighbor = neighbor.getMbr();
             Configuration conf_neighbor =  confs.lookup(mbr_neighbor);
           //  System.out.println( cconf.getMbr() + "   " + cconf.unary + conf_neighbor.getMbr() + "   " + conf_neighbor.unary);
             if(neighbor.getNeighborType() == 0)
          {
            	
            	 if(cconf.unary == 0 && conf_neighbor.unary == 0)
            		 return false;
          }
             
    	}  
       return true;  
	
    	
    } 
   
    public TestNode formLocalStability(final Configuration conf , final TestNode node)
    {
        
    	
    	
    	
    	return node;
    }

	public boolean reason(Node initial)
	{

	    candidnates_solid.add(initial);
	 	while(!candidnates_solid.isEmpty())
		{
			Node node = candidnates_solid.pop();
			if(node.isCompleted())
			{
				node.reset();
				candidnates_gravity.add(node);
			}
			else{
				
				LinkedList<Node> nodes = expandOnSolidProperty(node);
			 
				if(!nodes.isEmpty())
				{	
					candidnates_solid.addAll(nodes);
			
				}
			 }
			
		}
	 
		System.out.println("gravity candidnate size: "+ candidnates_gravity.size());
		/*for(Node node: candidnates_gravity)
		{
			System.out.println(node);
		}*/
	 	
		while(!candidnates_gravity.isEmpty())
		{
			Node node = candidnates_gravity.pop();
			//Debug.echo(null,node);
			if(node.isCompleted())
			{
				node.reset();
				candidnates_result.add(node);
			}
			else
			/* only return valid (stable) results */
			{
				LinkedList<Node> nodes = MBRRegister.expandOnGravityProperty(node);
				//System.out.println(candidnates_gravity.size() + "  " +nodes.size());
			    if(!nodes.isEmpty())
			    {
			    	candidnates_gravity.addAll(nodes);
			    }
			}
			
			
			
		//	
			
		}
		
	System.out.println(" result candidnate size: " + candidnates_result.size()+"\n");
		
		for(Node node:candidnates_result)
		{
		    boolean stable = true;
			for(MBR mbr:node.mbrs)
			{
				if(!testGravity(mbr,node))
				{	
					//Debug.echo(this, node);
					//Debug.echo(this, node.lookupConf(mbr));
					//Debug.echo(this, " unstable block: ", mbr,node.lookupConf(mbr));
					//System.out.println("unstable block: " + mbr + " " + node.lookupConf(mbr));
					//System.out.println("unstable configurations: " + node);
					
					stable = false;
				
				}
			}
	 
		  if(stable)
		  {   System.out.println("valid configuration found");
			  System.out.println(node);
		   }
		  else
		  {
			  //  Debug.echo(this, "Invalid configuration found");
			  //  Debug.echo(this,node);
		  }
		}
		
		return true;
	}
	

	public LinkedList<Node> expandOnSolidProperty(Node node)
	{
	    LinkedList<Node> nodes = new LinkedList<Node>();
	    
	    /* set to regular */
	    MBR mbr = node.pop();
	  if(mbr != null){ 
		  if(node.lookupConf(mbr).isEdge())
		  {
			  Configuration conf1 = node.lookupConf(mbr).clone();
			  conf1.setAngular(0);
			    if(testSolid(mbr,conf1,node))
			    {
			    	Node _node = node.clone();
			    	_node.push(mbr);
			    	_node.updateRecord(mbr, conf1);
			    	nodes.add(_node);
			    }    
			   
		  }
		  else
		  {
			  
		    Configuration conf1 = node.lookupConf(mbr).clone();
		    conf1.setAngular(0);
	
		    if(testSolid(mbr,conf1,node))
		    {
		    	Node _node = node.clone();
		    	_node.push(mbr);
		    	_node.updateRecord(mbr, conf1);
		    	nodes.add(_node);
		    	//System.out.println(_node);
		    }    
		   
		    if(!this.earlyDetermination(mbr, node.lookupConf(mbr), node)){

		    Configuration conf4 = node.lookupConf(mbr).clone();
		    conf4.setAngular(1);
		    conf4.setPermit_regions(0,1,1,0);
		    if(testSolid(mbr,conf4,node))
		    {
		    	Node _node = node.clone();
		    	_node.push(mbr);
		    	_node.updateRecord(mbr, conf4);
		    	nodes.add(_node);
		    } 
		    
		    Configuration conf5 = node.lookupConf(mbr).clone();
		    conf5.setAngular(1);
		    conf5.setPermit_regions(0,1,0,1);
		    if(testSolid(mbr,conf5,node))
		    {
		    	Node _node = node.clone();
		    	_node.push(mbr);
		    	_node.updateRecord(mbr, conf5);
		    	nodes.add(_node);
		    } 
		    
		    }
		    else
		    {
		    	System.out.println("early determination: " + mbr);
		    }
		    
	    
	  }
	  }
	  else
		  nodes.add(node);
	  return nodes;
	  
		
	}
	// add on Jan.  if it touches the ground and has no bounding box surround, then must be regular.
	private boolean earlyDetermination(MBR mbr, Configuration conf, Node node)
	{
		boolean result = true;
		LinkedList<MBR> overlapping_mbrs = conf.getOverlapping_mbrs();
		for(MBR _mbr:overlapping_mbrs)
		{
			if(_mbr.getCenterY() + _mbr.getHeight()/2 > mbr.getCenterY() - mbr.getHeight()/2 && !node.lookupConf(_mbr).isEdge())
					result = false;
		}
		return result;
	}
	private boolean testSolid(MBR mbr, Configuration conf,Node node) {
		/* firstly, test the regular/angular property*/
		LinkedList<MBR> overlapping_mbrs = conf.getOverlapping_mbrs();
				
      if(conf.getAngular() == 0)
      {
		for(MBR ombr:overlapping_mbrs)
		{
		    Configuration oconf = node.lookupConf(ombr);
			if(oconf.getAngular() == 0)
			{
		      /* Using java Rectangle.Intersect(rec1,rec2) will return false when rec1 tocuhes rec2. However, using QuantiShapeCalculator.intersect(rec1,rec2) will return true when they touch
		       * Touched Rectangles are registered as overlapping, but here, we focus on the cases of real overlapping excluding touching
		       * */
				if(mbr.intersects(ombr))
				{ 
					
					return false;
				}
			}
		    else
		    	/* judge block regions */
		    	 for (Integer region_id:oconf.getBlocked_regions().keySet())
		  	   {
		  		   if(oconf.getPermit_regions()[region_id] == 1 && oconf.getBlocked_regions().get(region_id).contains(mbr))
		  		   { 
		  			   return false; 
		  		   }
		  	   }	
		}
		   
		}
	 
	else
	{

	   for (Integer region_id:conf.getBlocked_regions().keySet())
	   {
		   LinkedList<MBR> blockingMBRs = conf.getBlocked_regions().get(region_id);
		   if(conf.getPermit_regions()[region_id] == 1)
		   
		   {  
			   for(MBR bmbr : blockingMBRs)
			   { 
				   if(node.lookupConf(bmbr).getAngular() == 0)
				   {   
					 
					   return false;
					 }
			   }
			  
			   
			   }
	   }	
	}
     return true;  
	

	}
   private boolean testGravity(MBR mbr, Node node)
   {
	  Configuration conf = node.lookupConf(mbr);
	  if(conf.isSupport())
		  return true;  
     
	  return false;
	  
   }

	public static void main(String args[])
	{
	//	 RA Calcuate
	
	/*	Rectangle rec = new Rectangle(200,200,1000,10);
	
		MBRRegister.registerRectangle(rec,true);
	
		Rectangle rec1 = new Rectangle(200,100,100,100);
		MBRRegister.registerRectangle(rec1,false);
		
       //  System.out.println(rec.intersection(rec1).contains(200,200));
		

	//	Rectangle rec2 = new Rectangle(100,50,105,50);
	//	MBRRegister.registerRectangle(rec2,false);
		
		Rectangle rec2 = new Rectangle(500,30,300,170);
		MBRRegister.registerRectangle(rec2,false);
		
		
		Rectangle rec3 = new Rectangle(225,0,300,100);
		MBRRegister.registerRectangle(rec3,false);
		
		Node node = MBRRegister.constructNode();
	
		
		
		MBRReasoner MBRR = new MBRReasoner();
		
		MBRR.reason(node);*/

		
	}
	
}
