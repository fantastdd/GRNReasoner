package quali;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedList;

public class MBRReasonerAdvance {
     
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
			return true;
		}
		else
		{
		    LinkedList<TestNode> refinements = refine(node);
		    // System.out.println(node + "    " + refinements.size());
		    // System.out.println(node.current_id + "   " + refinements.size());
		    
		    // if refinements should not be empty when a mbr has no neighbors.
		    if(!refinements.isEmpty())
		    {
		    	for(TestNode refinement: refinements)
		    	{	
		    	//	// System.out.println(" refine the mbr conf : " +  node.current_id);
		    		boolean result = search(refinement);
		    	//	// System.out.println(" result  : " + result + "   id: " + counter);
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
						LinkedList<HashMap<MBR,Contact>> lscontacts = MBRRegisterAdvance.getPossibleContacts(cconf,node);//get the possible contacts with the instantiated MBRs.
						// System.out.println("*********:  " + lscontacts.size());
						if (lscontacts.isEmpty()) // will happen when this block is isolated or its neighbors are not initialized yet 
						{
							refinements.add(node.clone());
						}
						else
						{	
							for (HashMap<MBR,Contact> contactmap: lscontacts)
							{
								//test the solid properties
						        
							   //---  initialize the cconf's neighbor's configuration that makes cconf local stable.
							   TestNode _node = formLocalStability(cconf,contactmap,node);// this node is a clone with the cconf updated
							   // // System.out.println(_node);
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
								cconf.nextInitialization();
								
								// System.out.println(cconf .toShortString()+ "   \n  " + node + "    " +  solidValidity(cconf , node));
								//test the solid properties
								if(solidValidity(cconf , node))
								{
									
									//----- one unique configuration will have various contactmap..
									LinkedList<HashMap<MBR,Contact>> lscontacts = MBRRegisterAdvance.getPossibleContacts(cconf,node);//get the possible contacts with the instantiated MBRs. TODO Note: if a mbr has no neighbors,should not return empty.
									// System.out.println(" after solid check " + cconf.getMbr()+"  "+ lscontacts.size());
									if (lscontacts.isEmpty()) // will happen when this block is isolated or its neighbors are not initialized yet 
									{    
										TestNode _node = node.clone();
										_node.update(cconf);
										refinements.add(_node.clone());
									}
									else
									    {

												for (HashMap<MBR,Contact> contactmap: lscontacts)
												{
													
											/*		// System.out.println("  contactmap of " + cconf.getMbr() + "\n");
													for (MBR key: contactmap.keySet())
													{
														// System.out.println(key + "   " + contactmap.get(key));
													}
													*/
												   //---  initialize the cconf's neighbor's configuration that makes cconf local stable.
												   TestNode _node = formLocalStability(cconf,contactmap,node);// this node is a clone with the cconf updated
												 
												   // System.out.println(" to form stability     " + _node);
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
  		// System.out.println(" node is incompleted, :  " + node);
    	if(node.isCompleted())
    	{
    		 // System.out.println(" node completed, check stability \n" + node);
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
           	//  // System.out.println( cconf.getMbr() + "   " + cconf.unary + conf_neighbor.getMbr() + "   " + conf_neighbor.unary);
         	//   // System.out.println( cconf +  "  the neighbor type is   " + neighbor.getNeighborType());
             if(neighbor.getNeighborType() == 0)
             {
            	
            	 if(cconf.unary == 0 && conf_neighbor.unary == 0)
            		 return false;
             }
             else
            	 break;// since we sort the gap ascendingly and the neighbor type 0 indicates a zero gap. thus, if the type is not 0, it means the type should be more than 0.
             
    	}  
       return true;  
	
    	
    } 
    // This method will verify the stability of the each instantiated mbr in the tree. 
    public TestNode formLocalStability(Configuration newUpdatedConf , HashMap<MBR,Contact> contactmap, TestNode node)
    {
        // Test by enumerating all possible local configurations. there are so many tricks in the selection of the root. e.g. the root can be the one with the most neighbor and its immediately child could be the one with the least negihbor
    	//  n this version, we randomly pick up a mbr as the root.
    	
        // stability_id = the id of the mbr that waiting to be tested
    	//	current_id = the maximum id of the instantiated confs.
        TestNode _node = null;    		 
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
						    	     if (neighbors.isEmpty())
						    	     {
						    	    	
						    	    	 if (testConf.isSupport())
						    	    	 {
						    	    		    _node = node.clone();
						                        _node.nextStabilityVerificationCandidate();
						                        
						                  }
						    	    	// // System.out.println(" no neighbors");
						    	    	 
						    	     } 
						             else 
						             {
						                 // System.out.println( " go to here ");
						            	 // test the testConf with all instantiated nodes
						             	  for (Neighbor neighbor : neighbors)
						             	  {
                                               Configuration curConf;
                                               if (neighbor.getMbr().getId() == newUpdatedConf.getMbr().getId())
                                               	    curConf = _newUpdatedConf;
                                               	else
                                               		curConf = node.lookup(neighbor.getMbr());
                                               	int nid_curConf = neighbors.indexOf(neighbor);
                                               	if( nid_curConf > testConf.lastValidNeighborId)
                                               		break outerloop;
                                               	else 
                                               	{
                                               		  boolean support = testConf.isNowSupport(curConf);
                                               		  if(!support && nid_curConf == testConf.lastValidNeighborId)   
							                                 //all tested and no support
							                                   break outerloop;
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
	        	// System.out.println("  updates the conf " + _newUpdatedConf +  "\n node description   "  +  _node);                      
    		}
	 
    		return _node;
    }


	
}
