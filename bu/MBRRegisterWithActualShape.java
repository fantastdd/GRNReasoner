package quali;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.LinkedList;

import quanti.QuantiShapeCalculator;

import common.MyPolygon;
import common.util.Debug;

import exception.SolidOverlapping;

public class MBRRegisterWithActualShape {
private static LinkedList<MBR> mbrs = new LinkedList<MBR>();
private static LinkedList<MBR> edge = new LinkedList<MBR>();
public static LinkedList<MBR> getMbrs() {
	return mbrs;
}
public static void setMbrs(LinkedList<MBR> mbrs) {
	MBRRegisterWithActualShape.mbrs = mbrs;
}

private static int count = 0;

public static void registerRectangle(Rectangle rec,boolean edge)
{
	MBR mbr = new MBR(rec);
	if(edge)
		MBRRegisterWithActualShape.edge.add(mbr);
	if(!mbrs.contains(mbr))
	{	
		mbr.setId(count++);
		mbrs.add(mbr);
	}
}
public static void registerMBR(MBR mbr,boolean edge)
{
	
	if(edge)
		MBRRegisterWithActualShape.edge.add(mbr);
	if(!mbrs.contains(mbr))
	{	
		mbr.setId(count++);
		mbrs.add(mbr);
	}
}
public static Node constructNode()
{
   Node node = new Node(mbrs, new LinkedList<MBR>(),edge);
   return node;

}
public static LinkedList<Node>  expandOnGravityProperty(Node node)
{
	 LinkedList<Node> nodes = new LinkedList<Node>();
	 MBR mbr = node.pop();
	  if(mbr != null){ 
		  
		  LinkedList<Configuration> insConfs = new LinkedList<Configuration>();
		  insConfs =  instantiateConf(mbr,node);
	    //  System.out.println(insMBRs.size());
		  //Debug.echo(null, mbr,insMBRs.size());
		  
		  for(Configuration _conf:insConfs)
		  {
		      //Debug.echo(null, mbr,_conf);
			  Node _node = node.clone();
			  _node.push(mbr);
			  _node.updateRecord(mbr, _conf);
			  nodes.add(_node);
			 // Debug.echo(null, _node);
			  
		  }
	   
	    
	  }
	  else
		  nodes.add(node);
     
 return nodes;  

}	
private static LinkedList<Configuration> instantiateConf(final MBR mbr,Node node)
{
	LinkedList<Configuration> confs = new LinkedList<Configuration>();
	Configuration conf = node.lookupConf(mbr);
    HashMap<MBR,Contact> cmap = conf.getContact_map();
 
    for (MBR ombr:conf.getOverlapping_mbrs())
    {
    
    	
    	Contact contact = cmap.get(ombr);
    	
    	if(!contact.isInitialized())
    	{
    	
    	  Configuration tconf = node.lookupConf(ombr);
    	  Contact _contact = tconf.getContact_map().get(mbr);
    	  // Debug.echo(null, mbr,_contact,_contact.isInitialized(),"  con1 : ",conf," oconf:  ",oconf);
    	  /* if relation {mbr, po} has already be instantiated in po's contact list */
          if(_contact.isInitialized())
	      {
	         if(conf.getAngular() == 1 && tconf.getAngular() == 1)
	         {
        	  	if(_contact.getTangential_area() == 1)
	              {
	        		  contact.setTangential_area(3);
	        		  contact.setType(1);
	        	 }
	        	  else  if(_contact.getTangential_area() == 2)
	              {
	        		  contact.setTangential_area(4);
	        		  contact.setType(1);
	        	 }
	        	  else  if(_contact.getTangential_area() == 3)
	              {
	        		  contact.setTangential_area(1);
	        		  contact.setType(-1);
	        	 }
	        	  else  if(_contact.getTangential_area() == 4)
	              {
	        		  contact.setTangential_area(2);
	        		  contact.setType(-1);
	        	 } else if(_contact.getTangential_area() == 0)
	        	 {
	        		 contact.setTangential_area(0);
	        		 contact.setType(0);
	        	 }
	         } 
	         else
	         	 
	        	 /* ombr regular supported by regular mbr */
	        	if(conf.getAngular() == 0 && tconf.getAngular() == 0)	
	        	{ 	 
	        		if(_contact.getTangential_area() == 34 || _contact.getTangential_area() == 3 || _contact.getTangential_area() == 4)
	        		  {
	        			
	        			  MyPolygon regionline1 = conf.getRegionLine(1);
	        			  MyPolygon regionline2 = conf.getRegionLine(2);
	        			  boolean region1Contact = QuantiShapeCalculator.isIntersected(regionline1, tconf.getRegionLine(34), true);
	        			  boolean region2Contact = QuantiShapeCalculator.isIntersected(regionline2, tconf.getRegionLine(34), true);
	        			  if(region1Contact && region2Contact)
	        			  {
	        				  contact.setTangential_area(12);
	        			      contact.setType(-1);
	        			  }
	        			  else
	        				  if(region1Contact)
	        				  {
	        					  contact.setTangential_area(1);
	        					  contact.setType(-1);
	        				  }
	        				  else
	        					  if(region2Contact)
	        					  {
	        						  contact.setTangential_area(2);
	        						  contact.setType(-1);
	        					  }
	        		  }
	        		  else
	        			  if(_contact.getTangential_area() == 12 || _contact.getTangential_area() == 1 || _contact.getTangential_area() == 2)
		        		  {
		        			  MyPolygon regionline3 = conf.getRegionLine(3);
		        			  MyPolygon regionline4 = conf.getRegionLine(4);
		        			  boolean region3Contact = QuantiShapeCalculator.isIntersected(regionline3, tconf.getRegionLine(12), true);
		        			  boolean region4Contact = QuantiShapeCalculator.isIntersected(regionline4, tconf.getRegionLine(12), true);
		        			  if(region3Contact && region4Contact)
		        			  {
		        				  contact.setTangential_area(34);
		        			      contact.setType(1);
		        			  }
		        			  else
		        				  if(region3Contact)
		        				  {
		        					  contact.setTangential_area(3);
		        					  contact.setType(1);
		        				  }
		        				  else
		        					  if(region4Contact)
		        					  {
		        						  contact.setTangential_area(4);
		        						  contact.setType(1);
		        					  }
		        		  }
	        			  else
		        			  if(_contact.getTangential_area() == 23)
			        		  {
			        			 
			        					  contact.setTangential_area(14);
			        					  contact.setType(0);
			        		  }
		        			  else
			        			  if(_contact.getTangential_area() == 14)
				        		  {
				        			 
				        					  contact.setTangential_area(23);
				        					  contact.setType(0);
				        		  }
			        			  else if(_contact.getTangential_area() == 0)
			     	        	 {
			     	        		 contact.setTangential_area(0);
			     	        		 contact.setType(0);
			     	        	 }
	        		
	        		
	        	 
	         }	 
	        	else
	        		if(conf.getAngular() == 1 && tconf.getAngular() == 0)
	        		{
	        			if(_contact.getTangential_area() == 1 || _contact.getTangential_area() == 2)
		        		  {
		        			
	        				
	        				      if(conf.getPermit_regions()[2] == 1)
	        				      {	  
	        				    	  contact.setTangential_area(3);
		        					  contact.setType(1);
	        				      }
	        				      else
	        				    	  if(conf.getPermit_regions()[3]==1)
	        				    	  {
	        				    		  contact.setTangential_area(2);
			        					  contact.setType(1);
	        				    	  }
		        				
		        		  }
		        	  else
		        			if(_contact.getTangential_area() == 23)
			        		  {

	        				      if(conf.getPermit_regions()[2] == 1)
	        				      {	  
	        				    	  contact.setTangential_area(4);
		        					  contact.setType(1);
	        				      }
	        				      else
	        				    	  if(conf.getPermit_regions()[3]==1)
	        				    	  {
	        				    		  contact.setTangential_area(1);
			        					  contact.setType(0);
	        				    	  }
			        		  }
		        			  else 
			        				  if (_contact.getTangential_area() == 14 )
			        				  {
				        				    if(conf.getPermit_regions()[2] == 1)
				        				      {	  
				        				    	  contact.setTangential_area(2);
					        					  contact.setType(0);
				        				      }
				        				      else
				        				    	  if(conf.getPermit_regions()[3]==1)
				        				    	  {
				        				    		  contact.setTangential_area(3);
						        					  contact.setType(1);
				        				    	  }
		        				  
		        				  
			        				  }
			        				  else if(_contact.getTangential_area() == 3 || _contact.getTangential_area() == 4)
			        				  {
				  		        			
				  	        				/* no need to distinguish between region 1 or 2*/
				        				      if(conf.getPermit_regions()[2] == 1)
				        				      {	  
				        				    	  contact.setTangential_area(1);
					        					  contact.setType(-1);
				        				      }
				        				      else
				        				    	  if(conf.getPermit_regions()[3]==1)
				        				    	  {
				        				    		  contact.setTangential_area(1);
						        					  contact.setType(-1);
				        				    	  }
					        				
						        	} 
			        				  		else  if (_contact.getTangential_area() == 114 )
		        				  			{
					        				    	if(conf.getPermit_regions()[2] == 1)
					        				    	{	  
					        				    	  contact.setTangential_area(3);
						        					  contact.setType(1);
					        				    	}
					        				    	else
					        				    		if(conf.getPermit_regions()[3]==1)
					        				    		{
					        				    		  contact.setTangential_area(3);
							        					  contact.setType(1);
					        				    		}
		        				  
		        				  
		        				  			}
			        				  			else  if (_contact.getTangential_area() == 223 )
				        				  		{
					        				    	if(conf.getPermit_regions()[2] == 1)
					        				    	{	  
					        				    	  contact.setTangential_area(4);
						        					  contact.setType(1);
					        				    	}
					        				    	else
					        				    		if(conf.getPermit_regions()[3]==1)
					        				    		{
					        				    		  contact.setTangential_area(4);
							        					  contact.setType(1);
					        				    		}
				        				  
				        				  
				        				  		}
				        				  else  if (_contact.getTangential_area() == 233 )
			        				  		{
				        				    	if(conf.getPermit_regions()[2] == 1)
				        				    	{	  
				        				    	  contact.setTangential_area(1);
					        					  contact.setType(-1);
				        				    	}
				        				    	else
				        				    		if(conf.getPermit_regions()[3]==1)
				        				    		{
				        				    		  contact.setTangential_area(1);
						        					  contact.setType(-1);
				        				    		}
			        				  
			        				  
			        				  		}
				        				  else  if (_contact.getTangential_area() == 414 )
			        				  		{
				        				    	if(conf.getPermit_regions()[2] == 1)
				        				    	{	  
				        				    	  contact.setTangential_area(2);
					        					  contact.setType(-1);
				        				    	}
				        				    	else
				        				    		if(conf.getPermit_regions()[3]==1)
				        				    		{
				        				    		  contact.setTangential_area(2);
						        					  contact.setType(-1);
				        				    		}
			        				  
			        				  
			        				  		}
				        				     else if(_contact.getTangential_area() == 0)
				        		        	 {
				        		        		 contact.setTangential_area(0);
				        		        		 contact.setType(0);
				        		        	 }
			        			
	        			
	        				}
	                  /* mbr is regular, ombr is angular
	                   * 
	                   * */
				     	if(conf.getAngular() == 0 && tconf.getAngular() == 1)
			    		{
				     		boolean tr2 = testRegionR_A(conf,tconf,2);
				     		boolean tr1 = testRegionR_A(conf,tconf,1);
		    		
		    				
		    				boolean tr3 = testRegionR_A(conf,tconf,3);
				     		boolean tr4 = testRegionR_A(conf,tconf,4);
		    		
		    				
		    				 boolean vertex_1 = (testVertexR_A13(conf,tconf,1) ==  1 ) &&(testVertexR_A13(conf,tconf,14) ==  1 ) ; // right-top corner
		        			 boolean vertex_3 = (testVertexR_A13(conf,tconf,3) ==  1 ) &&(testVertexR_A13(conf,tconf,23) ==  1 ) ;
		        			 boolean vertex_2 = (testVertexR_A24(conf,tconf,2) ==  1 ) &&(testVertexR_A24(conf,tconf,23) ==  1 ) ;
		        			 boolean vertex_4 = (testVertexR_A24(conf,tconf,4) ==  1 ) &&(testVertexR_A24(conf,tconf,14) ==  1 ) ;
		        			 
			    			if(_contact.getTangential_area() == 4)
			    			{
			    			
			    				if(vertex_2)
			    				{
			    					  contact.setTangential_area(223);
		        					  contact.setType(-1);
		        					 // Debug.echo(null, mbr,conf,ombr,oconf);
			    				}
			    				else
			    					if(tr1)
				    				{
				    					  contact.setTangential_area(1);
			        					  contact.setType(-1);
				    				}
			    					else
			    					if(tr2)
			    					{
			    						 contact.setTangential_area(2);
			        					  contact.setType(-1);
			        					  
			    					}
			    				
			    			}else 
			    			if(_contact.getTangential_area() == 1)
			    			{
			    				if(vertex_3)
			    				{
			    					  contact.setTangential_area(233);
		        					  contact.setType(1);
			    				}
			    				else
			    					if(tr3)
			    					{
			    						 contact.setTangential_area(3);
			        					  contact.setType(1);
			    					}
			    					else if(tr4)
			    					{
			    						 contact.setTangential_area(4);
			        					  contact.setType(1);
			    					}
			    				
			    			}else 
				    			if(_contact.getTangential_area() == 2)
				    			{
				    				if(vertex_4)
				    				{
				    					  contact.setTangential_area(414);
			        					  contact.setType(1);
				    				}
				    				else
				    					if(tr4)
				    					{
				    						 contact.setTangential_area(4);
				        					  contact.setType(1);
				    					}
				    				
				    					else if(tr3)
				    					{
				    						 contact.setTangential_area(3);
				        					  contact.setType(1);
				    					}
				    				
				    			}else 
					    			if(_contact.getTangential_area() == 3)
					    			{
					    				if(vertex_1)
					    				{
					    					  contact.setTangential_area(114);
				        					  contact.setType(-1);
					    				}
					    				else
					    					if(tr2)
						    				{
						    					  contact.setTangential_area(2);
					        					  contact.setType(-1);
						    				}else
					    					if(tr1)
					    					{
					    						 contact.setTangential_area(1);
					        					  contact.setType(-1);
					    					}
					    				
					    				
					    			}
					    			else if(_contact.getTangential_area() == 14)
					    			{
					    				 contact.setTangential_area(23);
			        					  contact.setType(0);
					    			}
					    			else if(_contact.getTangential_area() ==23)
					    			{
					    				 contact.setTangential_area(14);
			        					  contact.setType(0);
					    			}
					    			else if(_contact.getTangential_area() == 0)
						        	 {
						        		 contact.setTangential_area(0);
						        		 contact.setType(0);
						        	 }
			    			
			    			
				        			
			    			
			    				}
				         
	         
			       conf.getContact_map().put(ombr, contact);
	     }
	         
          
        }
    }
    	
    	LinkedList<Configuration> tree = new LinkedList<Configuration>();
    	LinkedList<Configuration> candidnate = new LinkedList<Configuration>();
        LinkedList<Configuration> cache = new LinkedList<Configuration>();
    	cache.add(conf);
    
   
    	for(MBR ombr: conf.getOverlapping_mbrs())
    	{
    		 candidnate.clear();
    		 candidnate.addAll(cache);
    	     Configuration oconf = node.lookupConf(ombr);
    	     cache.clear();
    		while(!candidnate.isEmpty())
    		{
    			 Configuration _conf = candidnate.pop();
    			 LinkedList<Configuration>  _confs = initializeContact(_conf,oconf);
    		     cache.addAll(_confs);
    		}
    }
    	//Debug.echo(null, " tree out: ", tree);

        tree.addAll(cache);
    	confs.addAll(tree);
     	//Debug.echo(null, " out intialize contacts: ", confs);
    	if(confs.isEmpty())
    	{
    	   Debug.echo(null, " Unexpected Error :  tree is empty after iteration","  instantiateConf ");
    	}
   
    return confs;
}
/*private static Configuration confirmTouching(final Configuration conf,Contact contact) throws SolidOverlapping
{
	 Configuration _conf = conf.clone();
	 if(_conf.isActualConfiguration())
	 {
		  test solid overlapping by this contact 
		 for (int i = 0; i < contact.points.length; i ++)
		 {
			 if(contact.points[0] != null && contact.points[1] != null)
			 {
				 
			 }
			 else
				 if(contact.points[0]!= null)
				 {
					  test whether this point is contained in that solid object
					 if(_conf.getActual_object().contains(contact.points[0]))
						   throw new SolidOverlapping();
					 
				 }
			 
		 }
	     
	}
	 else
	 {
	   if(contact.points[0]!=null && contact.points[1]!=null)
	   {
		   
	   }
	   else
	   {
		   _conf.addRestrictedPoints(contact.points[0], contact.getTangential_area());
	   }
	   
	 }
	 
	 
	 
	 
	 
	 return _conf;

}*/
/* if only one intersected point, then it is a two regular mbr vertex touch, can be ignored*/
private static boolean testRegularRegion34(final Configuration conf, final Configuration tconf, int region)
{
	MyPolygon rline = conf.getRegionLine(region);
	
	
	MyPolygon trline = tconf.getRegionLine(12);
	//Debug.echo(null,mbr,rline, tmbr,trline,QuantiShapeCalculator.isIntersected(rline, trline).size() > 1);
	//System.out.println(QuantiShapeCalculator.isIntersected(rline, trline));
	return QuantiShapeCalculator.isIntersected(rline, trline).size() > 1;	
	
}
private static boolean testRegularRegion12(final Configuration conf, final Configuration tconf, int region)
{	

	MyPolygon rline = conf.getRegionLine(region);
	
	MyPolygon trline = tconf.getRegionLine(34);
	//Debug.echo(null, " test ",rline,trline,QuantiShapeCalculator.isIntersected(rline, trline));
	return QuantiShapeCalculator.isIntersected(rline, trline).size() > 1;	
	

}

private static boolean testRegularRegion23(final Configuration conf, final Configuration tconf)
{
	/* region 23 refers to the left edge of the rectangle*/
	MyPolygon rline = conf.getRegionLine(23);
	
	
	MyPolygon trline = tconf.getRegionLine(14);
	
	return QuantiShapeCalculator.isIntersected(rline, trline).size() > 1;	
	
}

private static boolean testRegularRegion14(final Configuration conf, final Configuration tconf)
{
	/* region 23 refers to the right edge of the rectangle*/
	MyPolygon rline = conf.getRegionLine(14);
	
	
	MyPolygon trline = tconf.getRegionLine(23);
	
	return QuantiShapeCalculator.isIntersected(rline, trline).size() > 1;	
	
}

	
private static LinkedList<Configuration> initializeContact(final Configuration conf,final Configuration tconf)
{
	  LinkedList<Configuration> confs = new LinkedList<Configuration>();
	  //Debug.echo(null, mbr,conf,tmbr,oconf);
	  /* if both are regular */		 
     if(conf.getAngular() == 0 && tconf.getAngular() == 0)
     {
    	  boolean tr2 = testRegularRegion12(conf,tconf,2);
    	  boolean tr1 = testRegularRegion12(conf,tconf,1);
    	  boolean tr3 = testRegularRegion34(conf,tconf,3);
    	  boolean tr4 = testRegularRegion34(conf,tconf,4);
    	  boolean tr23 = testRegularRegion23(conf,tconf);
    	  boolean tr14 = testRegularRegion14(conf,tconf);
    	
          if( tr2 && tr1)
          {
    			  Contact _contact = new Contact();
	    		  _contact.setTangential_area(12);
	    		  _contact.setType(1);
	    		  
	    		  Configuration _conf = conf.clone();
	    		  _conf.getContact_map().put(tconf.getMbr(), _contact);
	        	  confs.add(_conf);
	        	
    	  }else if( tr3 && tr4)
          {
    			  Contact _contact = new Contact();
	    		  _contact.setTangential_area(34);
	    		  _contact.setType(1);
	    		  
	    		  Configuration _conf = conf.clone();
	    		  _conf.getContact_map().put(tconf.getMbr(), _contact);
	    		  confs.add(_conf);
	    		
    	  }else if( tr1)
          {
    			  Contact _contact = new Contact();
	    		  _contact.setTangential_area(1);
	    		  _contact.setType(1);
	    		  
	    		  Configuration _conf = conf.clone();
	    		  _conf.getContact_map().put(tconf.getMbr(), _contact);
	    		  confs.add(_conf);
	    		  
    	  }else if( tr2)
          {
    			  Contact _contact = new Contact();
	    		  _contact.setTangential_area(2);
	    		  _contact.setType(1);
	    		  
	    		  Configuration _conf = conf.clone();
	    		  _conf.getContact_map().put(tconf.getMbr(), _contact);
	    		  confs.add(_conf);
	    		  
    	  }else if( tr3)
          {
    			  Contact _contact = new Contact();
	    		  _contact.setTangential_area(3);
	    		  _contact.setType(1);
	    		  
	    		  Configuration _conf = conf.clone();
	    		  _conf.getContact_map().put(tconf.getMbr(), _contact);
	    		  confs.add(_conf);
	    		  
    	  }else if(tr4)
          {
    			  Contact _contact = new Contact();
	    		  _contact.setTangential_area(4);
	    		  _contact.setType(1);


	    		  Configuration _conf = conf.clone();
	    		  _conf.getContact_map().put(tconf.getMbr(), _contact);
	    		  confs.add(_conf);
	    		  
    	  }else if(tr23)
          {
    			  Contact _contact = new Contact();
	    		  _contact.setTangential_area(23);
	    		  _contact.setType(1);
	    		 
	    		  Configuration _conf = conf.clone();
	    		  _conf.getContact_map().put(tconf.getMbr(), _contact);
	    		  confs.add(_conf);
	    		  
    	  }else if(tr14)
          {
    			  Contact _contact = new Contact();
	    		  _contact.setTangential_area(14);
	    		  _contact.setType(1);
	    		  
	    		  Configuration _conf = conf.clone();
	    		  _conf.getContact_map().put(tconf.getMbr(), _contact);
	    		  confs.add(_conf);
          }	 
          
          
         }
    	
     else
    	 if(conf.getAngular() == 1 && tconf.getAngular() == 0)
    	 {
			 /* no needs to distiguish between edge 12,23,34,14*/
			 boolean tr12 = testRegionR_A(tconf,conf,12);
			 // boolean tr2 = testRegionR_A(tmbr,mbr,oconf,2);
			 boolean tr34 = testRegionR_A(tconf,conf,34);
			 //boolean tr4 = testRegionR_A(tmbr,mbr,oconf,4);
			
			 boolean tr23 = testRegularRegion23(tconf,conf);
			 boolean tr14 = testRegularRegion14(tconf,conf);
			 //Debug.echo(null, tr12);
			 boolean vertex_1 = (testVertexR_A13(tconf,conf,1) ==  2 ) &&(testVertexR_A13(tconf,conf,14) ==  2 ) ; // right-top corner
			 boolean vertex_3 = (testVertexR_A13(tconf,conf,3) ==  2 ) &&(testVertexR_A13(tconf,conf,23) ==  2 ) ;
			 boolean vertex_2 = (testVertexR_A24(tconf,conf,2) ==  2 ) &&(testVertexR_A24(tconf,conf,23) ==  2 ) ;
			 boolean vertex_4 = (testVertexR_A24(tconf,conf,4) ==  2 ) &&(testVertexR_A24(tconf,conf,14) ==  2 ) ;
			 
			 boolean non_touching = false;
			 if(vertex_1)
			 {
				
				 Contact _contact = new Contact();
    			 _contact.setType(1);
    			 _contact.points[0] =  new Point(tconf.x + tconf.width, tconf.y);
    			 _contact.setTangential_area(3);
    			
    			  
	    		  Configuration _conf = conf.clone();
	    		  _conf.addRestrictedPoints(_contact.points[0], 3);
	    		  _conf.getContact_map().put(tconf.getMbr(), _contact);
	        	  confs.add(_conf);
    			 
    			 
    			
    			 non_touching = true;
			 }
			 else if(vertex_2)
			 {
				 Contact _contact = new Contact();
    			 _contact.setType(1);
    			 _contact.points[0] =  new Point(tconf.x, tconf.y);
    			 _contact.setTangential_area(4);
    			 
    			  Configuration _conf = conf.clone();
	    		  _conf.addRestrictedPoints(_contact.points[0], 4);
	    		  _conf.getContact_map().put(tconf.getMbr(), _contact);
	        	  confs.add(_conf);
    			 
    			 non_touching = true;
    			// Debug.echo(null,"----------",conf,tconf,oconf,_contact);
			 }else if(vertex_3)
			 {
				 Contact _contact = new Contact();
    			 _contact.setType(1);
    			 _contact.setTangential_area(1);
    			 _contact.points[0] =  new Point(tconf.x, tconf.y + tconf.height);
    			 
    			  Configuration _conf = conf.clone();
	    		  _conf.addRestrictedPoints(_contact.points[0], 1);
	    		  _conf.getContact_map().put(tconf.getMbr(), _contact);
	        	  confs.add(_conf);
	        	  
    			 non_touching = true;
			 }else if(vertex_4)
			 {
				 Contact _contact = new Contact();
    			 _contact.setType(1);
    			 _contact.setTangential_area(2);
    			 _contact.points[0] =  new Point(tconf.x + tconf.width, tconf.y + tconf.height);
    		     
    			 
    			  Configuration _conf = conf.clone();
	    		  _conf.addRestrictedPoints(_contact.points[0], 2);
	    		  _conf.getContact_map().put(tconf.getMbr(), _contact);
	        	  confs.add(_conf);
	        	  
    			 non_touching = true;
    			 
    			 
			 }else if(tr12)
			 {
				
				 Contact _contact = new Contact();
    			 _contact.setType(1);
    			 if(conf.getPermit_regions()[2] == 1)
    				 _contact.setTangential_area(3);
    			 else
    				 _contact.setTangential_area(4);
    			
    			 
   			      Configuration _conf = conf.clone();
	    		  _conf.getContact_map().put(tconf.getMbr(), _contact);
	        	  confs.add(_conf);
    			 
			 }else if(tr23)
			 {
				 Contact _contact = new Contact();
				 
				 if(conf.getPermit_regions()[2] == 1)
				 {
					 _contact.setType(1);
					 _contact.setTangential_area(4);
				 
				 }
    			 else
    			 {	
    				_contact.setTangential_area(14);    			 
    				_contact.setType(1);
    			 }
				      Configuration _conf = conf.clone();
		    		  _conf.getContact_map().put(tconf.getMbr(), _contact);
		        	  confs.add(_conf);
			 }else if(tr34)
			 {
				 Contact _contact = new Contact();
    			 _contact.setType(1);
    			 if(conf.getPermit_regions()[2] == 1)
    				 _contact.setTangential_area(1);
    			 else
    				 _contact.setTangential_area(2);
    		
    			   Configuration _conf = conf.clone();
 	    		  _conf.getContact_map().put(tconf.getMbr(), _contact);
 	        	  confs.add(_conf);
			 }else if(tr14)
			 {
				 Contact _contact = new Contact();
				 
				 if(conf.getPermit_regions()[3] == 1)
				 {
					 _contact.setType(1);
					 _contact.setTangential_area(3);
				 
				 }
    			 else
    			 {	
    				_contact.setTangential_area(23);    			 
    				_contact.setType(1);
    			 }
				   Configuration _conf = conf.clone();
		    		  _conf.getContact_map().put(tconf.getMbr(), _contact);
		        	  confs.add(_conf);
			 }
			 
			 if(non_touching)
			 {

				 Contact _contact = new Contact();
				 _contact.setTangential_area(-1);    			 
 				_contact.setType(0);
 			   Configuration _conf = conf.clone();
	    		  _conf.getContact_map().put(tconf.getMbr(), _contact);
	        	  confs.add(_conf);
			 }
				    			  		 
				//Debug.echo(null,"10 intialization: ",mbr,conf,tmbr,contacts);    			  		 
    		 
    	}
    	
    	 else 
    		 if(conf.getAngular() == 0 && tconf.getAngular() == 1)
    		 {
    			 /* no needs to distiguish between edge 12,23,34,14*/
    			 boolean non_touching = false;
    			 boolean tr1 = testRegionR_A(conf,tconf,1);// test at corner 1. 
    			 boolean tr2 = testRegionR_A(conf,tconf,2);
    			 boolean tr3 = testRegionR_A(conf,tconf,3);
    			 boolean tr4 = testRegionR_A(conf,tconf,4);
    			
    			 boolean tr23 = testRegularRegion23(conf,tconf);
    			 boolean tr14 = testRegularRegion14(conf,tconf);
    			// Debug.echo(null,"----------",conf,tconf,oconf,testVertexR_A13(conf,tconf,oconf,1));
    			
    			 //Debug.echo(null,"******",(testVertexR_A13(conf,tconf,oconf,1) ==  1 ) &&(testVertexR_A13(conf,tconf,oconf,14) ==  1 ) );
    			 
    			 boolean vertex_1 = (testVertexR_A13(conf,tconf,1) ==  2 ) &&(testVertexR_A13(conf,tconf,14) ==  2 ) ; // right-top corner
    			
    			 boolean vertex_3 = (testVertexR_A13(conf,tconf,3) ==  2 ) &&(testVertexR_A13(conf,tconf,23) ==  2 ) ;
    			 boolean vertex_2 = (testVertexR_A24(conf,tconf,2) ==  2 ) &&(testVertexR_A24(conf,tconf,23) ==  2 ) ;
    			 boolean vertex_4 = (testVertexR_A24(conf,tconf,4) ==  2 ) &&(testVertexR_A24(conf,tconf,14) ==  2 ) ;
    			
    			 if(vertex_1)
    			 {
    				 Contact _contact = new Contact();
	    			 _contact.setType(1);
	    			 _contact.setTangential_area(114);
	    			 
	    			  Configuration _conf = conf.clone();
	 	    		  _conf.getContact_map().put(tconf.getMbr(), _contact);
	 	        	  confs.add(_conf);
	 	        	  
	    			 non_touching = true;
	    			 
    			 }
    			 else if(vertex_2)
    			 {
    				 Contact _contact = new Contact();
	    			 _contact.setType(1);
	    			 _contact.setTangential_area(223);
	    			   Configuration _conf = conf.clone();
	 	    		  _conf.getContact_map().put(tconf.getMbr(), _contact);
	 	        	  confs.add(_conf);
	    			 non_touching = true;
	    			
    			 }else if(vertex_3)
    			 {
    				 Contact _contact = new Contact();
	    			 _contact.setType(1);
	    			 _contact.setTangential_area(233);
	    			   Configuration _conf = conf.clone();
	 	    		  _conf.getContact_map().put(tconf.getMbr(), _contact);
	 	        	  confs.add(_conf);
	    			 non_touching = true;
    			 }else if(vertex_4)
    			 {
    				 Contact _contact = new Contact();
	    			 _contact.setType(1);
	    			 _contact.setTangential_area(414);
	    			   Configuration _conf = conf.clone();
	 	    		  _conf.getContact_map().put(tconf.getMbr(), _contact);
	 	        	  confs.add(_conf);
	    			 non_touching = true;
	    			 
    			 }else if(tr1) //must touch
    			 {
    				 Contact _contact = new Contact();
	    			 _contact.setType(1);
	    			 _contact.setTangential_area(1);
	    			   Configuration _conf = conf.clone();
	 	    		  _conf.getContact_map().put(tconf.getMbr(), _contact);
	 	        	  confs.add(_conf);
    			 }else if(tr2) //must touch
    			 {
    				 Contact _contact = new Contact();
	    			 _contact.setType(1);
	    			 _contact.setTangential_area(2);
	    			   Configuration _conf = conf.clone();
	 	    		  _conf.getContact_map().put(tconf.getMbr(), _contact);
	 	        	  confs.add(_conf); 
    			 }else if(tr23) //must touch
    			 {
    				 Contact _contact = new Contact();
	    			 _contact.setType(1);
	    			 _contact.setTangential_area(23);
	    			   Configuration _conf = conf.clone();
	 	    		  _conf.getContact_map().put(tconf.getMbr(), _contact);
	 	        	  confs.add(_conf);
    			 }else if(tr3) //must touch
    			 {
    				 Contact _contact = new Contact();
	    			 _contact.setType(1);
	    			 _contact.setTangential_area(3);
	    			   Configuration _conf = conf.clone();
	 	    		  _conf.getContact_map().put(tconf.getMbr(), _contact);
	 	        	  confs.add(_conf);
    			 }else if(tr4) //must touch
    			 {
    				 Contact _contact = new Contact();
	    			 _contact.setType(1);
	    			 _contact.setTangential_area(4);
	    			   Configuration _conf = conf.clone();
	 	    		  _conf.getContact_map().put(tconf.getMbr(), _contact);
	 	        	  confs.add(_conf);
    			 }else if(tr14) //must touch
    			 {
    				 Contact _contact = new Contact();
	    			 _contact.setType(1);
	    			 _contact.setTangential_area(14);
	    			   Configuration _conf = conf.clone();
	 	    		  _conf.getContact_map().put(tconf.getMbr(), _contact);
	 	        	  confs.add(_conf);
    			 }
    			 
    			 if(non_touching)
    			 {
    				 Contact _contact = new Contact();
    				 _contact.setType(0);
	    			 _contact.setTangential_area(-1);
	    			   Configuration _conf = conf.clone();
	 	    		  _conf.getContact_map().put(tconf.getMbr(), _contact);
	 	        	  confs.add(_conf);
    			 }
    		 }
    		 else
    			 /* both are angular */
    			 if(conf.getAngular() == 1 && tconf.getAngular() == 1)
    	    	 {
    	    	    /* testFree region, conf must not be regular */
    	    		 int tr1 = testFreeRegion13(conf,tconf,1);
    	    		 int tr2 = testFreeRegion24(conf,tconf,2);
    	    		 int tr3 = testFreeRegion13(conf,tconf,3);
    	    		 int tr4 = testFreeRegion24(conf,tconf,4);
    	    		 boolean non_touching = false; 
    	    		 if(tr1 == 2)
    	    		 {
    	        			Contact _contact = new Contact();
    	        		    _contact.setType(-1);
    	        			_contact.setTangential_area(1);
    	        			
    	        			   Configuration _conf = conf.clone();
    	     	    		  _conf.getContact_map().put(tconf.getMbr(), _contact);
    	     	        	  confs.add(_conf);
    	     	        	  
    	        			non_touching = true;
    	        	 }
    	            if(tr2 == 2)
    	        	{
    					    Contact _contact = new Contact();
    					    _contact.setType(-1);
    					    _contact.setTangential_area(2);
    					    Configuration _conf = conf.clone();
    			    		  _conf.getContact_map().put(tconf.getMbr(), _contact);
    			        	  confs.add(_conf);
    					    non_touching = true;
    				}
    									    		
    	            if(tr3 == 2)
    				{
    								    			
	    				 Contact _contact = new Contact();
	    				_contact.setType(1);
	    				 _contact.setTangential_area(3);
	    				   Configuration _conf = conf.clone();
	    		    		  _conf.getContact_map().put(tconf.getMbr(), _contact);
	    		        	  confs.add(_conf);
	    				 non_touching = true;
    				}
    				if(tr4 == 2)
    				{
    					 Contact _contact = new Contact();
    					_contact.setType(1);
    					_contact.setTangential_area(4);
    					   Configuration _conf = conf.clone();
    			    		  _conf.getContact_map().put(tconf.getMbr(), _contact);
    			        	  confs.add(_conf);
    					non_touching = true;
    				 }
    				if(non_touching)
    				{
    					Contact _contact = new Contact();
    					_contact.setType(0);
    					_contact.setTangential_area(-1);
    					   Configuration _conf = conf.clone();
    			    		  _conf.getContact_map().put(tconf.getMbr(), _contact);
    			        	  confs.add(_conf);
    				}	
    	    	 
    	    	 }
    	    	
     return confs;
}
private static boolean testRegionR_A(final Configuration conf, final Configuration tconf, int region)
{
	MyPolygon rline = conf.getRegionLine(region);
	
	
	MyPolygon trline = new MyPolygon();
	
	if(region == 1 || region == 2 || region == 12 )
	{	
		
		if(tconf.getPermit_regions()[2] ==  1 )
		   /* should be restricted to permit area */
			trline = tconf.getRegionLine(3);
		else
			trline = tconf.getRegionLine(4);
		
		
		//Debug.echo(null, conf,tconf,region, QuantiShapeCalculator.isIntersected(rline, trline).size());
	}
	else
	{
		if(tconf.getPermit_regions()[2] ==  1)
			trline = tconf.getRegionLine(1);
		else
			trline = tconf.getRegionLine(2);
		
	}
	return QuantiShapeCalculator.isIntersected(rline, trline).size() > 1;	



}

/* use this when mbr is regular and tmbr is not regular, and can be used to test the four corner's relationship
 * No different between tmin or tmax, at least one should be touch, another one should disjoint
 * */
private static int testVertexR_A13(final Configuration conf, final Configuration tconf, int region)
{
	int result = 0;

	 MyPolygon min = new MyPolygon();
	 MyPolygon max = new MyPolygon();
	 MyPolygon tmin = new MyPolygon();
	 MyPolygon tmax = new MyPolygon();

     min = conf.getRegionLine(region);
     max = conf.getRegionLine(region);
	 
	 
      if(tconf.getPermit_regions()[2] == 1)
	  {
        	tmin = tconf.getCore_right();
        	//tmax = tmbr.getDiagonal_right();
	        if(region == 1)
	        	tmax = tconf.getRegion(3);
	        else
	        	tmax = tconf.getRegion(1);
	  }
	  else
		  if(tconf.getPermit_regions()[3] == 1)
		  {
			  tmax = tconf.getCore_left();
		      tmin = tconf.getDiagonal_left();
		  }
 
  
  		MyPolygon _region = new MyPolygon();
        
  		_region = max;
  		/*if(region == 23 && mbr.getId() == 1 && tconf.getAngular() == 1)
  		{
  			Debug.echo(null, min,max,tmin,tmax);
  		}*/
  		//Debug.echo(null,"look here",max,tmax,QuantiShapeCalculator.isIntersected(tmax, _region,true));
 
	if(QuantiShapeCalculator.isIntersected(tmax, _region,true))
	{	
	//	
		if(!testSolidOverlapping(conf.getRegion(5),tmin))		  
		 {

			result = testPotentialContact(min,max,tmin,tmax);
		  
		 }
       else 
    	   result = -1;
		
	}
	else
	{
		result = 0;
	}
	return result;

}
/* use this when mbr is regular and tmbr is not regular
 * No different between tmin or tmax, at least one should be touch, another one should disjoint
 * */
private static int testVertexR_A24(final Configuration conf, final Configuration tconf, int region)
{
	int result = 0;

	 MyPolygon min = new MyPolygon();
	 MyPolygon max = new MyPolygon();
	 MyPolygon tmin = new MyPolygon();
	 MyPolygon tmax = new MyPolygon();

     min = conf.getRegionLine(region);
     max = conf.getRegionLine(region);
	 
	 
      if(tconf.getPermit_regions()[3] == 1)
	  {
    		tmin = tconf.getCore_left();
        	//tmax = tconf.getDiagonal_right();
	        if(region == 2)
	        	tmax = tconf.getRegion(4);
	        else
	        	tmax = tconf.getRegion(2);
	  }
	  else
		  if(tconf.getPermit_regions()[2] == 1)
		  {
			  tmax = tconf.getCore_right();
		      tmin = tconf.getDiagonal_right();
		  }
 
  
  		MyPolygon _region = new MyPolygon();
        
  		_region = max;
  		/*if(region == 23 && mbr.getId() == 1 && tconf.getAngular() == 1)
  		{
  			Debug.echo(null, min,max,tmin,tmax);
  		}*/
		
	if(QuantiShapeCalculator.isIntersected(tmax, _region,true))
	{	
		//Debug.echo(null,"look here",mbr,tmbr,mbr.getRegion(5),tmin,!testSolidOverlapping(mbr.getRegion(5),tmin));
	       if(!testSolidOverlapping(conf.getRegion(5),tmin))		  
	  		 result = testPotentialContact(min,max,tmin,tmax);
	         else 
	      	   result = -1;
		
	}
	else
	{
		result = 0;
	}
	return result;

}
/* region numbered as 1,2,3,4 from right-top, anti clockwise*/
private static int testFreeRegion24(final Configuration conf,final Configuration tconf,int region)
{
	int result = 0;

	 MyPolygon min = new MyPolygon();
	 MyPolygon max = new MyPolygon();
	 MyPolygon tmin = new MyPolygon();
	 MyPolygon tmax = new MyPolygon();

	  if(conf.getPermit_regions()[2] == 1)
	  {
          min = conf.getDiagonal_right();
	      max = conf.getCore_right();
	  }
	  else
		  if(conf.getPermit_regions()[3] == 1)
		  {
			 if(tconf.getAngular() == 0)
			  max = conf.getDiagonal_left();
			 else 
				 max = conf.getRegion(2);
		      min = conf.getCore_left();
		  }
  
	  if(tconf.getPermit_regions()[2] == 1)
	  {
        tmin = tconf.getDiagonal_right();
	      tmax = tconf.getCore_right();
	  }
	  else
		  if(tconf.getPermit_regions()[3] == 1)
		  {
			  tmax = tconf.getDiagonal_left();
		      tmin = tconf.getCore_left();
		  }
 
  
  		MyPolygon _region = new MyPolygon();
		if(conf.getAngular() == 1)
		{
		
			if(conf.getPermit_regions()[3] == 1)
				_region = conf.getRegion(region);
			else
				//问题在这，如果用大区域描述2，那么会导致vertex touch的极端情况
				if(conf.getPermit_regions()[2] == 1)
					_region = conf.getRegionLarge(region);
			
		
		}

		
		
		if(QuantiShapeCalculator.isIntersected(tmax, _region,true))
		{	
			result = testPotentialContact(min,max,tmin,tmax);
			
		}
		else
		{
			result = 0;
		}
	return result;

}

private static int testFreeRegion13(final Configuration conf,final Configuration tconf,int region)
{
	 int result = 0;
	 MyPolygon min = new MyPolygon();
	 MyPolygon max = new MyPolygon();
	 MyPolygon tmin = new MyPolygon();
	 MyPolygon tmax = new MyPolygon();
	 
	 
		  if(conf.getPermit_regions()[2] == 1)
		  {
			  /* can not represent the max area using tiny diagonal. we use the region*/
			 if(tconf.getAngular() == 0)
			  max = conf.getDiagonal_right();
			 else  max = conf.getRegion(3);
			  min = conf.getCore_right();
		  }
		  else
			  if(conf.getPermit_regions()[3] == 1)
			  {

			      min = conf.getDiagonal_left();
			      max = conf.getCore_left();
			  }
	  
	 
	
		  if(tconf.getPermit_regions()[2] == 1)
		  {
			   tmin = tconf.getCore_right();
			   tmax = tconf.getDiagonal_right();
		  }
		  else
			  if(tconf.getPermit_regions()[3] == 1)
			  {
				  tmin = tconf.getDiagonal_left();
				   tmax = tconf.getCore_left();
			  }
	  
	  /* pay attention to touch relationship */
	  MyPolygon _region = new MyPolygon();
		if(conf.getAngular() == 1)
		{
			if(conf.getPermit_regions()[2] == 1)
				_region = conf.getRegion(region);
			else
				if(conf.getPermit_regions()[3] == 1)
					_region = conf.getRegionLarge(region);
		}

	
	
	//	Debug.echo(null,mbr,conf,tmbr,tconf," out ",testPotentialContact(min,max,tmin,tmax));
	
		//	Debug.echo(null,mbr,conf,tmbr,tconf," result ",QuantiShapeCalculator.isIntersected(tmax, _region,true),testPotentialContact(min,max,tmin,tmax));
		if(QuantiShapeCalculator.isIntersected(tmax, _region,true))
		{	
			result = testPotentialContact(min,max,tmin,tmax);
			
		}
		else
		{
			result = 0;
		}

		return result;

}





/* when MAX INTERSECTING MAX, MIN DISJOINT MIN
 * 
 * 1: Must Touching
 * 0: Must Non-Touching
 * 2: Touching or Non-Touching
 * -1: Invalid. Intersecting 
 * */
private static int testPotentialContact(MyPolygon min,MyPolygon max,MyPolygon tmin, MyPolygon tmax)
{
	int result = 0;
    boolean min_disjoint = false;
    boolean max_intersecting = false;
    
    /* If bot max are edges, then they could be considered as intersecting.*/
    
 //  Debug.echo(null, " in ", max,tmax,QuantiShapeCalculator.isIntersected(max,tmax,true));
    if(!QuantiShapeCalculator.isIntersected(min,tmin,false))
               min_disjoint = true;
    
    if(QuantiShapeCalculator.isIntersected(max,tmax,true))
        max_intersecting = true;

   // if(max.npoints == 2 && tmax.npoints == 2)
    	//max_intersecting = true;
    if(min_disjoint & max_intersecting)
    	result = 2; // can be either touching or non-touching
    else
    	if(!max_intersecting)
    		result = 0;
    		else
    			if(!min_disjoint)
    				result = -1;
    return result;
}

private static boolean testSolidOverlapping(MyPolygon m, MyPolygon n)
{
	return QuantiShapeCalculator.isIntersected(m, n).size() > 1;

}
}

