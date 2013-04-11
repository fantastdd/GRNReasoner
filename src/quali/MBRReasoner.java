package quali;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedList;

import ab.WorldinVision;


public class MBRReasoner {
	public int  global_counter = 0;
	public int formLocal_counter = 0;
	static
	{
		try {
			new PrintStream("test.txt");
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}  
	 
	}


    
    
    
	//search ----
	public boolean search(TestNode node)
	{
		if(checkSolution(node))
		{
		    //output the solutions	
			 System.out.println("solution is found:   \n" + node);
			 System.out.println(" the number of iterations : " + global_counter + "  filtered nodes:   " + formLocal_counter);
			 //System.out.println(StabilityConfigurationOutput.getStabilityReport(node));
			return true;
		}
		else
		{
		    LinkedList<TestNode> refinements = refine(node);
		   //  System.out.println(node + "    " + refinements.size());
		    // System.out.println(node.current_id + "   " + refinements.size());
		    
		    // if refinements should not be empty when a mbr has no neighbors.
		    if(!refinements.isEmpty())
		    {
		    	for(TestNode refinement: refinements)
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
	public LinkedList<TestNode> refine(final TestNode node)
	{   
		LinkedList<TestNode> refinements = new LinkedList<TestNode>();
        
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
								  TestNode _node = formLocalStabilityHeuristic(cconf,contactmap,node);// this node is a clone with the cconf updated
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
									
									//----- one unique configuration will have various contactmap..
								/*	if(cconf.getMbr().getId() == 12 && cconf.unary == 2 && node.lookup(5).unary == 0&& node.lookup(11).unary == 0)
									System.out.println(" get possible contacts ");*/
									LinkedList<HashMap<Integer,Contact>> lscontacts = ContactManager.getPossibleContacts(cconf,node,WorldinVision.gap);//get the possible contacts with the instantiated MBRs. TODO Note: if a mbr has no neighbors,should not return empty.
									
								/*	if (node.lookup(4).unary == 0 && node.lookup(5).unary == 0 && node.lookup(6).unary == 2 && node.lookup(7).unary == 1 && node.lookup(8).unary == 2
											&& node.lookup(9).unary == 1 && node.lookup(10).unary == 3 )
								 	 System.out.println(" lscontacts size " + cconf.toShortString()+"  "+ lscontacts.size());*/
									
						          //TODO  there are no valid contacts, even no valid non-touching rels, then the lscontacts will return empty map
	                             // then the node will be continued with an invalid conf. 
									if (lscontacts == null ) // will happen when this block is isolated or its neighbors are not initialized yet 
									{    
										TestNode _node = node.clone();
										//System.out.println("updates early " + cconf.toShortString());
										_node.update(cconf.clone());
										//System.out.println(_node);
										refinements.add(_node);
									}
									else
									    {

												for (HashMap<Integer,Contact> contactmap: lscontacts)
												{
													/*
													if(cconf.getMbr().getId() == 12)
													{
														System.out.println(cconf.toShortString());
														for (Integer key: contactmap.keySet())
													{
														 System.out.println(key + "   " + contactmap.get(key));
													}
													}
*/													
												   //---  initialize the cconf's neighbor's configuration that makes cconf local stable.
													//TestNode _node = formLocalStability(cconf,contactmap,node);
													TestNode _node = formLocalStabilityHeuristic(cconf,contactmap,node);// this node is a clone with the cconf updated
													/*if (node.lookup(4).unary == 0 && node.lookup(5).unary == 0 && node.lookup(6).unary == 2 && node.lookup(7).unary == 1 && node.lookup(8).unary == 2
															&& node.lookup(9).unary == 1 && node.lookup(10).unary == 3 && node.lookup(12).unary == -1 )
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
    public boolean checkSolution(TestNode node)
    {
		// System.out.println(" node current id : " + node.current_id);
  		//System.out.println(" node is incompleted, :  " + node);
/*		if (node.lookup(6).unary == 2 && node.lookup(7).unary == 0 && node.lookup(8).unary == 3 && node.lookup(9).unary == 2 && node.lookup(10).unary == 0
				&& node.lookup(11).unary == 3 && node.lookup(12).unary == 1 
				&& node.lookup(13).unary == 2	&& node.lookup(14).unary == 2 && node.lookup(15).unary == 1 && node.lookup(16).unary == 3
				&& node.lookup(17).unary == 3 && node.lookup(18).unary == 2 && node.lookup(19).unary == 1 && node.lookup(20).unary == 3)
			System.out.println(" node incompleted, check stability \n" + node + "  " + this.global_counter) ;*/
    	if(node.isCompleted())
    	{
  /*  		if (node.lookup(6).unary == 2 && node.lookup(7).unary == 0 && node.lookup(8).unary == 3 && node.lookup(9).unary == 2 && node.lookup(10).unary == 0
    				&& node.lookup(11).unary == 3 && node.lookup(12).unary == 1 
    				&& node.lookup(13).unary == 2	&& node.lookup(14).unary == 2 && node.lookup(15).unary == 1 && node.lookup(16).unary == 3
    				&& node.lookup(17).unary == 3 && node.lookup(18).unary == 2 && node.lookup(19).unary == 1 && node.lookup(20).unary == 3)
    			System.out.println(" node completed, check stability \n" + node);*/
//    		/System.out.println(" node completed, check stability \n" + node);
    		
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
    public boolean solidValidity(final Configuration cconf, final TestNode confs)
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
    
    public TestNode formLocalStabilityHeuristic (final Configuration newUpdatedConf , final HashMap<Integer,Contact> contactmap, final TestNode node)
    {
    	TestNode _node = null;
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
    	//Test the newUpdatedConf first
    	if( !_newUpdatedConf.isSupport() && _newUpdatedConf.lastValidNeighborId <= node.current_id)
    	{
    	
    		//formLocal_counter++;
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
    					}
    					else
    					{
    					    if(newUpdatedConf.getMbr().getId() >= _conf.lastValidNeighborId)
    					    {
    					    	//System.out.println(_newUpdatedConf.toShortString() + "  " + _conf.toShortString() + "   " + node + "   " + support);
    					    	//formLocal_counter++;
    					    	/*if (node.lookup(4).unary == 0 && node.lookup(5).unary == 0 && node.lookup(6).unary == 2 && node.lookup(7).unary == 1 && node.lookup(8).unary == 2
    									&& node.lookup(9).unary == 1 && node.lookup(10).unary == 4  )
    				    		System.out.println("   early end " + _newUpdatedConf.toShortString() + "   " + _conf.toShortString() + "   "  +  _newUpdatedConf.getContact_map().get(_conf.getMbr().getId())+ "   " + node);*/
    					    	return null;
    					    }
    					}
    		}
    		
    	}
    	
    	_node = node.clone();
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
