package quali;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedList;

import quali.util.Logger;
import quali.util.StabilityConfigurationOutput;
import ab.WorldinVision;



/**
 * Backtracking on the set of objects.
 * @author Gary
 *
 */
public class MBRReasoner implements Runnable{
	public int  global_counter = 0;
	public int formLocal_counter = 0;
	public int solution_counter = 0;
	public Node sol = null;
	public Node initialNode = null;
	public Logger logger;
	static
	{
		try {
			new PrintStream("test.txt");
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}  
	 
	}


	@Override
	public void run() {
		
		search(initialNode);
	}

    
    
    
	public MBRReasoner(Node node , Logger logger) {
		initialNode = node;
		this.logger = logger;
	}




	//search ----
	public boolean search(Node node)
	{
		if(checkSolution(node))
		{
		    //output the solutions	
			 solution_counter ++;
			 System.out.println("solution is found:   \n" + node);
			 System.out.println(" the number of iterations : " + global_counter + "  filtered nodes:   " + formLocal_counter);
			// System.out.println(StabilityConfigurationOutput.getStabilityReport(node));
			 System.out.println(StabilityConfigurationOutput.getStabilityShortReport(node));
			 sol = node;
			 if(solution_counter == 1)
			return true;
			 else
				 return false;
		}
		else
		{
		    LinkedList<Node> refinements = refine(node);
		   //  System.out.println(node + "    " + refinements.size());
		    // System.out.println(node.current_id + "   " + refinements.size());
		    
		    // if refinements should not be empty when a mbr has no neighbors.
		    if(!refinements.isEmpty())
		    {
		    	for(Node refinement: refinements)
		    	{	
		    	   //System.out.println(" refinement : " +  refinement);
		    		global_counter++;
		    		boolean result = search(refinement);
		    	
		     //System.out.println(" result  : " + refinement.current_id + refinement);
		    		if(result) 
		    			return true;
		    	}
		    		
		    }
		}
	
		return false;
		
	}
	//refine should return at least one TestNode if no properties have been violated
	public LinkedList<Node> refine(final Node node)
	{   
		LinkedList<Node> refinements = new LinkedList<Node>();
        
        // System.out.println(" go to the refine ");
	   	if(!node.isCompleted()){
	
				Configuration cconf = node.pop().clone(); 
			   if(cconf.isEdge())
			   {
				   //System.out.println(cconf + "\n" + node);
					if(solidValidity(cconf , node)){
					  
						//----- one unique configuration will have various contactmap..
						LinkedList<HashMap<Integer,Contact>> lscontacts = ContactManager.getPossibleContacts(cconf,node , WorldinVision.gap);//get the possible contacts with the instantiated MBRs.
						//System.out.println(" lscontacts size (edge) " + cconf.toShortString()+"  "+ lscontacts.size());
						if (lscontacts == null) // will happen when this block is isolated or its neighbors are not initialized yet 
						{
							refinements.add(node.clone());
						}
						else
						{	
							for (HashMap<Integer,Contact> contactmap: lscontacts)
							{
								
								// System.out.println("  contactmap of " + cconf.getMbr() + "\n");
								/*for (Integer key: contactmap.keySet())
								{
									 System.out.println(key + "   " + contactmap.get(key));
								}*/
						        
							   //---  initialize the cconf's neighbor's configuration that makes cconf local stable.
								  //TestNode _node = formLocalStability(cconf,contactmap,node);
								  Node _node = formLocalStabilityHeuristic(cconf,contactmap,node);// this node is a clone with the cconf updated
							   //System.out.println(_node);
							   // System.out.println(" after form local stability:  " + _node );
							   if (_node != null)
								   refinements.add(_node);
							   else
								   formLocal_counter++;
						   }
					   }
						
					}
				   
			   }
			   else {
							//there are three types, namely, regular, left, right
							while(!cconf.isCompleted())
							{
								//System.out.println(cconf.toShortString());
								cconf.nextInitialization();
							/*	if (node.lookup(4).unary == 0 && node.lookup(5).unary == 0 && node.lookup(6).unary == 2 && node.lookup(7).unary == 1 && node.lookup(8).unary == 2
										&& node.lookup(9).unary == 1 && node.lookup(10).unary == 3  )
								System.out.println(cconf .toShortString()+ "   \n  " + "node" +  node + "    " +  solidValidity(cconf , node));*/
								//test the solid properties
							 
								if(solidValidity(cconf , node))
								{
									
									// one unique configuration will have various contactmap..
									/*	if(cconf.getMbr().getId() == 12 && cconf.unary == 2 && node.lookup(5).unary == 0&& node.lookup(11).unary == 0)
											System.out.println(" get possible contacts ");
									*/
									
									LinkedList<HashMap<Integer,Contact>> lscontacts = ContactManager.getPossibleContacts(cconf,node,WorldinVision.gap);//get the possible contacts with the instantiated MBRs. TODO Note: if a mbr has no neighbors,should not return empty.
									
									/* if(cconf.getMbr().uid == 8 && cconf.unary == 1 )
								 	 System.out.println(" lscontacts size " + cconf.toShortString()+"  "+ lscontacts.size());*/
									
						          // if there are no valid contacts, even no valid non-touching rels, then the lscontacts will return empty map, otherwise the node will be continued with an invalid conf. 
									if (lscontacts == null ) // will happen when this block is isolated or its neighbors are not initialized yet 
									{    
										Node _node = node.clone();
										//System.out.println("updates early " + cconf.toShortString());
										_node.update(cconf.clone());
										//System.out.println(_node);
										refinements.add(_node);
									}
									else
									    {

												for (HashMap<Integer,Contact> contactmap: lscontacts)
												{
													
													/*if(cconf.getMbr().uid == 12 && cconf.unary == 1)
													{
														System.out.println(cconf.toShortString());
														for (Integer key: contactmap.keySet())
														{
															System.out.println(key + "   " + contactmap.get(key) + "  " + node.lookup(key).toShortString());
														}
													}*/
													
												   //---  initialize the cconf's neighbor's configuration that makes cconf local stable.
													
													Node _node = formLocalStabilityHeuristic(cconf,contactmap,node);// this node is a clone with the cconf updated
													/*if (node.lookup(0).unary == 0 && node.lookup(1).unary == 1 && node.lookup(2).unary == 1&& node.lookup(3).unary == 1 && node.lookup(4).unary == 1
																&& node.lookup(5).unary == 4&& node.lookup(6).unary == 0&& node.lookup(7).unary == 1&& node.lookup(12).unary == 1 && cconf.unary == 3 
																&&cconf.getMbr().getId() == 23)
    				                                       System.out.println(" to form stability     " + _node);*/
												 	if(_node != null)  
													 	refinements.add(_node);
												 	
											   }
										}
								   
							   
							}
							}
				
			   }
		}

		return refinements;
	}

    //------ test ----
    public boolean checkSolution(Node node)
    {

  		
/*		if (node.lookup(6).unary == 2 && node.lookup(7).unary == 0 && node.lookup(8).unary == 3 && node.lookup(9).unary == 2 && node.lookup(10).unary == 0
				&& node.lookup(11).unary == 3 && node.lookup(12).unary == 1 
				&& node.lookup(13).unary == 2	&& node.lookup(14).unary == 2 && node.lookup(15).unary == 1 && node.lookup(16).unary == 3
				&& node.lookup(17).unary == 3 && node.lookup(18).unary == 2 && node.lookup(19).unary == 1 && node.lookup(20).unary == 3)
			System.out.println(" node incompleted, check stability \n" + node + "  " + this.global_counter) ;*/
    	//System.out.println(" node incompleted, check stability \n" + node ) ;
    	if(node.isCompleted())
    	{
  /*  		if (node.lookup(6).unary == 2 && node.lookup(7).unary == 0 && node.lookup(8).unary == 3 && node.lookup(9).unary == 2 && node.lookup(10).unary == 0
    				&& node.lookup(11).unary == 3 && node.lookup(12).unary == 1 
    				&& node.lookup(13).unary == 2	&& node.lookup(14).unary == 2 && node.lookup(15).unary == 1 && node.lookup(16).unary == 3
    				&& node.lookup(17).unary == 3 && node.lookup(18).unary == 2 && node.lookup(19).unary == 1 && node.lookup(20).unary == 3)
    			System.out.println(" node completed, check stability \n" + node);*/
    //    System.out.println(" node completed, check stability \n" + node);
    		
    		 for (Integer id : node.getConfs().keySet())
    		 {
    			 if (!node.getConfs().get(id).isSupport())
    				 return false;
    		 }
    		 return true;
    		
    	}
    	else
    		return false;
    }

    //verify the solid properties among all the $expanded$ confs
    // only test the regular/angular case. do not do the block_region test (rely on precise data)
    public boolean solidValidity(final Configuration cconf, final Node confs)
    {
    	
    	/* test the regular/angular property*/
	   // System.out.println(cconf.getNeighbors().size());

    	for (Neighbor neighbor: cconf.getNeighbors())
    	{
             MBR mbr_neighbor = neighbor.getMbr();
             Configuration conf_neighbor =  confs.lookup(mbr_neighbor);
           //System.out.println( cconf.getMbr() + "   " + cconf.unary + conf_neighbor.getMbr() + "   " + conf_neighbor.unary +  "  the neighbor type is   " + neighbor.getNeighborType());

             if(neighbor.getNeighborType() == 0)
             {
            	if(cconf.unary == 0 && conf_neighbor.unary == 0)
            		return false;
             }
          
             
    	}  
       return true;  
	
    	
    } 
    
    public Node formLocalStabilityHeuristic (final Configuration newUpdatedConf , final HashMap<Integer,Contact> contactmap, final Node node)
    {
    	Node _node = null;
    	int[] stability_id = new int[node.getConfs().size()];
    	int[] lastTestId = new int[newUpdatedConf.getNeighbors().size() ];
    	LinkedList<Configuration> effectedConfs = new LinkedList<Configuration>();
    	for (int i = 0 ; i < lastTestId.length ; i ++)
    		lastTestId[i] = -1;
    	Configuration _newUpdatedConf = newUpdatedConf.clone();
    	_newUpdatedConf.setContact_map(contactmap);
    	// Test the neighbors stability
    
       int newUpdatedConfLastTestId = 0;
       int newUpdatedConfId = _newUpdatedConf.getMbr().getId();
       boolean _newUpdatedConfSupport = _newUpdatedConf.isSupport();
    	//Test the newUpdatedConf first , if the conf cannot be stable, then return null
       if(_newUpdatedConfSupport)
       {
    	   logger.recordUnary(newUpdatedConf.getMbr(), newUpdatedConf.unary);
       }
    	if( !_newUpdatedConfSupport && _newUpdatedConf.lastValidNeighborId <= node.current_id)
    	{
    	
    		//formLocal_counter++;
    		/*	if (node.lookup(0).unary == 0 && node.lookup(1).unary == 1 && node.lookup(2).unary == 1&& node.lookup(3).unary == 1 && node.lookup(4).unary == 1
					&& node.lookup(5).unary == 4&& node.lookup(6).unary == 0&& node.lookup(7).unary == 1&& node.lookup(12).unary == 1 && newUpdatedConf.unary == 1 
					&&newUpdatedConf.getMbr().getId() == 23)
    						System.out.println(_newUpdatedConf);*/
    		return null;
    	}
    	else 
    		for (Neighbor neighbor : _newUpdatedConf.getNeighbors())
    		{
    			Configuration testConf = node.lookup(neighbor.getMbr());
    			int mbr_id = testConf.getMbr().getId();
    			if (mbr_id > node.current_id)
    				continue;
    			else 
    				if (node.stability_id[mbr_id] == 1)
    			{
    				newUpdatedConfLastTestId = mbr_id;
    				continue;
    			}
    			else
    				{
        	   			newUpdatedConfLastTestId = mbr_id;
        	   			Configuration _conf = testConf.clone();
        	   			
        	            boolean support = _conf.isNowSupport(_newUpdatedConf);
        	            //int lastId = testConf.lastTestNeighborId;
        	            _conf.lastTestNeighborId = newUpdatedConfId;
        	            effectedConfs.add(_conf);
        	            //System.out.println(_newUpdatedConf.toShortString() + "  " + _conf.toShortString() + "   " + node + "   " + support);
    					if(support)
    					{
    						stability_id[mbr_id] = 1;
    						
    						//PRINT =========================================
    						/*if(_conf.unary == 1 && _conf.getMbr().uid == 5)
    							System.out.println(_conf + "  " );*/
    						//PRINT END =====================================
    						
    						logger.recordUnary(_conf.getMbr(), _conf.unary);
    					}
    					else
    					{
    					    if(newUpdatedConf.getMbr().getId() >= _conf.lastValidNeighborId)
    					    {
    					    	//System.out.println(_newUpdatedConf.toShortString() + "  " + _conf.toShortString() + "   " + node + "   " + support);
    					    	//formLocal_counter++;
    					   /* 	if (node.lookup(0).unary == 0 && node.lookup(1).unary == 1 && node.lookup(2).unary == 1&& node.lookup(3).unary == 1 && node.lookup(4).unary == 1
    									&& node.lookup(5).unary == 4&& node.lookup(6).unary == 0&& node.lookup(7).unary == 1&& node.lookup(12).unary == 1 && newUpdatedConf.unary == 1 
    									&&newUpdatedConf.getMbr().getId() == 23)
    				    						System.out.println(_newUpdatedConf);*/
    					    	return null;
    					    }
    					}
    		}
    		
    	}
    	
    	_node = node.clone();
    	// _node.stability_id = stability_id; not used
    	for (Configuration conf : effectedConfs)
    	{
    	  _node.getConfs().put(conf.getMbr().getId(), conf);
    	}
    	_newUpdatedConf.lastTestNeighborId = newUpdatedConfLastTestId ;
    	_node.updateConf(_newUpdatedConf);
    	//System.out.println(" updates in formLocalStability  " + _node);
    	return _node;
    }
	
}
