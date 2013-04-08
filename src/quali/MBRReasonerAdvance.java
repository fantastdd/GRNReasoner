package quali;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedList;

import quali.util.StabilityConfigurationOutput;

import ab.WorldinVision;


public class MBRReasonerAdvance {
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
		    	//	// System.out.println(" refine the mbr conf : " +  node.current_id);
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
					if(solidValidity(cconf , node)){
					  
						//----- one unique configuration will have various contactmap..
						LinkedList<HashMap<Integer,Contact>> lscontacts = ContactManager.getPossibleContacts(cconf,node , WorldinVision.gap);//get the possible contacts with the instantiated MBRs.
						//System.out.println(" lscontacts size (edge) " + cconf.toShortString()+"  "+ lscontacts.size());
						if (lscontacts.isEmpty()) // will happen when this block is isolated or its neighbors are not initialized yet 
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
							   TestNode _node = formLocalStability(cconf,contactmap,node);// this node is a clone with the cconf updated
							   //System.out.println(_node);
							   // System.out.println(" after form local stability:  " + _node );
							   if (_node != null)
								   refinements.add(_node);
								
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
								
								//System.out.println(cconf .toShortString()+ "   \n  " + "node" +  node + "    " +  solidValidity(cconf , node));
								//test the solid properties
								if(solidValidity(cconf , node))
								{
									
									//----- one unique configuration will have various contactmap..
								/*	if(cconf.getMbr().getId() == 12 && cconf.unary == 2 && node.lookup(5).unary == 0&& node.lookup(11).unary == 0)
									System.out.println(" get possible contacts ");*/
									LinkedList<HashMap<Integer,Contact>> lscontacts = ContactManager.getPossibleContacts(cconf,node,WorldinVision.gap);//get the possible contacts with the instantiated MBRs. TODO Note: if a mbr has no neighbors,should not return empty.
									
								//	System.out.println(" lscontacts size " + cconf.toShortString()+"  "+ lscontacts.size());
									if (lscontacts.isEmpty()) // will happen when this block is isolated or its neighbors are not initialized yet 
									{    
										TestNode _node = node.clone();
										_node.update(cconf);
										refinements.add(_node.clone());
									}
									else
									    {

												for (HashMap<Integer,Contact> contactmap: lscontacts)
												{
													
												/*	if(cconf.getMbr().getId() == 12 && cconf.unary == 2 && node.lookup(5).unary == 0&& node.lookup(11).unary == 0)
													{
														for (Integer key: contactmap.keySet())
													{
														 System.out.println(key + "   " + contactmap.get(key));
													}
													}*/
													
												   //---  initialize the cconf's neighbor's configuration that makes cconf local stable.
												   TestNode _node = formLocalStability(cconf,contactmap,node);// this node is a clone with the cconf updated
												/*	if(cconf.getMbr().getId() == 12 && cconf.unary == 2)
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
    	if(node.isCompleted())
    	{
    		/*if (node.lookup(13).unary == 1 && node.lookup(14).unary == 1 && node.lookup(15).unary == 2 && node.lookup(16).unary == 4 && node.lookup(17).unary == 4
    				&& node.lookup(18).unary == 4 && node.lookup(19).unary == 4 && node.lookup(20).unary == 4)
    			System.out.println(" node completed, check stability \n" + node);*/
    		//System.out.println(" node completed, check stability \n" + node);
    		
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
    // This method will verify the stability of the each instantiated mbr in the tree. 
    public TestNode formLocalStability(Configuration newUpdatedConf , HashMap<Integer,Contact> contactmap, TestNode node)
    {
        // Test by enumerating all possible local configurations. there are so many tricks in the selection of the root. e.g. the root can be the one with the most neighbor and its immediately child could be the one with the least negihbor
    	//  n this version, we randomly pick up a mbr as the root.
    	
        // stability_id = the id of the mbr that waiting to be tested
    	//	current_id = the maximum id of the instantiated confs.
    /*	  TestNode  _node = node.clone();
    	  long time = System.currentTimeMillis();
    	
      
        Configuration   _newUpdatedConf = newUpdatedConf.clone();
        _newUpdatedConf.setContact_map(contactmap);
        
    outerloop:	for (int i = node.stability_id; i <= node.current_id; i++)
				{
						    		 // Get the current test node.
						             Configuration testConf;
						             if ( i == _newUpdatedConf.getMbr().getId())
						            	 testConf = _newUpdatedConf;
						             else
						            	  testConf = node.lookup(i);
						             
						    		 LinkedList<Neighbor> neighbors = testConf.getNeighbors();
						             // // System.out.println(testConf + "    " + neighbors.size());
						    		 // For those who do not have a neighbor, 1. If not supported (not an edge)  --> return null. 2. yes continue
						    		 // TODO last neighbor id == -1 implies neighbors.isEmpty()
						    	     if (neighbors.isEmpty() || testConf.lastValidNeighborId == -1)
						    	     {
						    	    	
						    	    	 if (testConf.isSupport())
						    	    	 {
						    	    		    _node = node.clone();
						                        _node.nextStabilityVerificationCandidate();
						                        
						                  }
						    	    	//System.out.println(" no neighbors");
						    	    	 
						    	     } 
						             else 
						             {
						            	 // test the testConf with all instantiated nodes
						             innerloop:	  for (Neighbor neighbor : neighbors)
						             	  {
                                               Configuration curConf;
                                               if (neighbor.getMbr().getId() == newUpdatedConf.getMbr().getId())
                                               	    curConf = _newUpdatedConf;
                                               	else
                                               		curConf = node.lookup(neighbor.getMbr());
                                               	int nid_curConf = neighbors.indexOf(neighbor);
                                               	if( nid_curConf > testConf.lastValidNeighborId)
                                               	{	
                                               	//   System.out.println("  entre this block ");
                                               		//_node = null;
                                               		break outerloop;
                                               	
                                               	}
                                               	else  if (testConf.lastTestNeighborId [nid_curConf] == 1 ||  curConf.getMbr().getId() > node.current_id)
                                               	{
                                                    System.out.println("  curConf  " + curConf.getMbr().getId() +"   " + curConf.toShortString() + "   node " + node.current_id);
                                               			//print last test id
                                               		for (int j = 0; j < testConf.lastTestNeighborId.length ; j ++)
                                               		System.out.println(" testConf.lastTestNeighborId  " + j + "   " + testConf.lastTestNeighborId[j]);
                                               		System.out.println( "   testConf   " + testConf.toShortString() +  "   curConf  " + curConf.toShortString());
                                               		   continue;
                                               	}
                                               	else
                                               	{
                                               		  boolean support = testConf.isNowSupport(curConf);
                                               		  testConf.lastTestNeighborId [nid_curConf] = 1;
                                               		  //System.out.println(" last test neighbor id:  " + nid_curConf );
                                               		  boolean completed = true;
                                               		  for (int index = 0 ; index < testConf.lastTestNeighborId.length; index ++)
                                               			  if(testConf.lastTestNeighborId[index] == 0)
                                               				  completed = false;
                                               		  if(!support &&completed)   
							                                 //all tested and no support
                                               		  {  
                                               		     _node = null;
                                               			  break outerloop;
                                               		  }
							                          else
					                                   {
					                            	        _node = node.clone();
					                                         //update profiles in the older ones. 
					                                         if(support)
					                                         {
					                                             _node.nextStabilityVerificationCandidate();
					                                        }   
					                                   } 

                                               	}    

						             	  }
				             }

             	}	
    		 

    		if(_node != null)
    		{
	            _node.update( _newUpdatedConf);
	        	//System.out.println("  updates the conf " + _newUpdatedConf +  "\n node description   "  +  _node);                      
    		}
    		else
    			formLocal_counter ++;*/
    	 // System.out.println(" form local stability consumption: "  +  (System.currentTimeMillis() - time ) );
    	
      //only return this when above codes commented
    	TestNode _node = node.clone();
      Configuration   _newUpdatedConf = newUpdatedConf.clone();
        _newUpdatedConf.setContact_map(contactmap);
        _node.update(_newUpdatedConf);
    		return _node;
    }


	
}
