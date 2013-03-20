package quali;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.LinkedList;

import quanti.QuantiShapeCalculator;

import common.MyPolygon;
import common.util.Debug;

public class MBRRegister {
private static LinkedList<MBR> mbrs = new LinkedList<MBR>();
private static MBR edge;
private static int count = 0;
public static void registerRectangle(Rectangle rec,boolean edge)
{
	MBR mbr = new MBR(rec);
	if(edge)
		MBRRegister.edge = mbr;
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
		  
		  LinkedList<LinkedList<Configuration>> insMBRs = new LinkedList<LinkedList<Configuration>>();
		// System.out.println(mbr);
		  insMBRs =  instantiateTangentialConf(mbr,node);
	      //System.out.println(insMBRs.size());
		  //Debug.echo(null, mbr,insMBRs.size());
		  LinkedList<Configuration> mconfs = insMBRs.get(0);
		  LinkedList<Configuration> tconfs = insMBRs.get(1);
		  for(Configuration _conf:mconfs)
		  {
			  Node _node = node.clone();
			  _node.push(mbr);
			  _node.updateRecord(mbr, _conf);
			  nodes.add(_node);
			  
		  }
	   
	    
	  }
	  else
		  nodes.add(node);
     
 return nodes;  

}	
private static LinkedList<LinkedList<Configuration>> instantiateTangentialConf(final MBR mbr,Node node)
{
    LinkedList<LinkedList<Configuration>> confs =  new LinkedList<LinkedList<Configuration>>();
	LinkedList<Configuration> mconfs = new LinkedList<Configuration>();
	LinkedList<Configuration> tconfs = new LinkedList<Configuration>();
	Configuration conf = node.lookupConf(mbr);
    HashMap<MBR,Contact> cmap = conf.getContact_map();
 
    for (MBR ombr:conf.getOverlapping_mbrs())
    {
    
    	
    	Contact contact = cmap.get(ombr);
    	
    	if(!contact.isInitialized())
    	{
    		Configuration oconf = node.lookupConf(ombr);
    		
    		LinkedList<Contact> iniContacts = new LinkedList<Contact>();
    	    
    		iniContacts = initializeContact(mbr,conf,ombr,oconf);
    		
    		LinkedList<Contact> tiniContacts = new LinkedList<Contact>();
    		
    		tiniContacts = initializeContact(ombr,oconf,mbr,conf);
    	 
    	
           for(Contact __contact : iniContacts)
            {
            	
            	Configuration _conf = conf.clone();
            	_conf.getContact_map().put(ombr, __contact);
            	mconfs.add(_conf);
            	
            
            	
            }
          
           for(Contact __contact : tiniContacts)
           {
           	
        		Configuration _conf = oconf.clone();
            	_conf.getContact_map().put(mbr, __contact);
            	tconfs.add(_conf);
           	
           }
           
         }
    	
    } 
    confs.add(mconfs);
    confs.add(tconfs);

    return confs;
}
private static boolean testRegularRegion34(final MBR mbr,  final MBR tmbr,int region)
{
	MyPolygon rline = mbr.getRegionLine(region);
	
	
	MyPolygon trline = tmbr.getRegionLine(12);
	
	return QuantiShapeCalculator.isIntersected(rline, trline, true);	
	
}
private static boolean testRegularRegion12(final MBR mbr, final MBR tmbr, int region)
{
	MyPolygon rline = mbr.getRegionLine(region);
	
	MyPolygon trline = tmbr.getRegionLine(34);
	
	return QuantiShapeCalculator.isIntersected(rline, trline, true);	
	
}

private static boolean testRegularRegion23(final MBR mbr, final MBR tmbr)
{
	/* region 23 refers to the left edge of the rectangle*/
	MyPolygon rline = mbr.getRegionLine(23);
	
	
	MyPolygon trline = tmbr.getRegionLine(14);
	
	return QuantiShapeCalculator.isIntersected(rline, trline, true);	
	
}

private static boolean testRegularRegion14(final MBR mbr, final MBR tmbr)
{
	/* region 23 refers to the right edge of the rectangle*/
	MyPolygon rline = mbr.getRegionLine(14);
	
	
	MyPolygon trline = tmbr.getRegionLine(23);
	
	return QuantiShapeCalculator.isIntersected(rline, trline, true);	
	
}

	
private static LinkedList<Contact> initializeContact(final MBR mbr,Configuration conf,final MBR tmbr,Configuration oconf)
{
	  LinkedList<Contact> contacts = new LinkedList<Contact>();
	  //Debug.echo(null, mbr,conf,tmbr,oconf);
	  /* if both are regular */		 
     if(conf.getAngular() == 0 && oconf.getAngular() == 0)
     {
          if( testRegularRegion12(mbr,tmbr,12) )
          {
    			  Contact _contact = new Contact();
	    		  _contact.setTangential_area(12);
	    		  _contact.setType(-1);
	    		  contacts.add(_contact);  
    	  }
	      else
	    	  if(testRegularRegion12(mbr,tmbr,1)){
	    	    		Contact _contact = new Contact();
	    	    		_contact.setTangential_area(1);
	    	    		_contact.setType(-1);
	    	    		contacts.add(_contact);  
	        	}
	    		else
		    		if(testRegularRegion12(mbr,tmbr,2)){
		    	    		 Contact _contact = new Contact();
		    	    		  _contact.setTangential_area(2);
		    	    		  _contact.setType(-1);
		    	    		  contacts.add(_contact);  
		        	}
    	
          if( testRegularRegion34(mbr,tmbr,34))
          {
        	  Contact _contact = new Contact();
    		  _contact.setTangential_area(34);
    		  _contact.setType(1);
    		  contacts.add(_contact);  
          }
    	 else
    		if(testRegularRegion34(mbr,tmbr,3)){
    	    		 Contact _contact = new Contact();
    	    		  _contact.setTangential_area(3);
    	    		  _contact.setType(1);
    	    		  contacts.add(_contact);  
        	  }
    		  else
	    		if(testRegularRegion12(mbr,tmbr,4)){
	    	    		 Contact _contact = new Contact();
	    	    		  _contact.setTangential_area(5);
	    	    		  _contact.setType(1);
	    	    		  contacts.add(_contact);  
	        	}
          
          if(testRegularRegion23(mbr,tmbr))
          {
        	  Contact _contact = new Contact();
    		  _contact.setTangential_area(23);
    		  _contact.setType(0);
    		  contacts.add(_contact);  
          }
          
          if(testRegularRegion14(mbr,tmbr))
          {
        	  Contact _contact = new Contact();
    		  _contact.setTangential_area(14);
    		  _contact.setType(0);
    		  contacts.add(_contact);  
          }
          
          
          
         }
    	
     else
    	 if(conf.getAngular() == 1)
    	 {
    	    /* testFree region, mbr must not be regular */
    		 boolean tr1 = testFreeRegion13(mbr,conf,tmbr,oconf,1);
    		 boolean tr2 = testFreeRegion24(mbr,conf,tmbr,oconf,2);
    		 boolean tr3 = testFreeRegion13(mbr,conf,tmbr,oconf,3);
    		 boolean tr4 = testFreeRegion24(mbr,conf,tmbr,oconf,4);
    	    /* although tr1 and tr2 are not necssarily to be distingusihed, we go with the same procedure as we do with tr3/tr4 */

    			 
    			 if(tr1 && tr2)
    	    	 {
    	    		if(conf.getPermit_regions()[2] == 1)
    	    		{
    	    			 Contact _contact = new Contact();
    	        		 _contact.setType(-1);
    	        		 _contact.setTangential_area(1);
    	        		 contacts.add(_contact);  
    	    		}
    	    		else
    	    			if(conf.getPermit_regions()[3] == 1)
    	        		{
    	       				Contact _contact = new Contact();
    	           			 _contact.setType(-1);
    	           			 _contact.setTangential_area(2);
    	           			 contacts.add(_contact);  
    	       			}
    	    	} 
    		  else 
    				/* an regular tmbr support an angular mbr at the bottom of that mbr*/
    			if(tr3 && tr4)
        		{
        			if(conf.getPermit_regions()[2] == 1)
        			{
        				 Contact _contact = new Contact();
            			 _contact.setType(1);
            			 _contact.setTangential_area(3);
            			 contacts.add(_contact);  
        			}
        			else
        				if(conf.getPermit_regions()[3] == 1)
            			{
           				 Contact _contact = new Contact();
               			 _contact.setType(1);
               			 _contact.setTangential_area(4);
               			 contacts.add(_contact);  
            			}
        		}
    			else
    	    		if(tr2 && tr3)
    	    		{
    	    			if(conf.getPermit_regions()[2] == 1)
    	    			{
    	    				 Contact _contact = new Contact();
    	        			 _contact.setType(-1);
    	        			 _contact.setTangential_area(2);
    	        			 contacts.add(_contact);  
    	    			}
    	    			else
    	    				if(conf.getPermit_regions()[3] == 1)
    	        			{
    	       				 Contact _contact = new Contact();
    	           			 _contact.setType(1);
    	           			 _contact.setTangential_area(3);
    	           			 contacts.add(_contact);  
    	       			}
    	    			
    	    		}
    	    		else
    	    			if(tr1 && tr4)
    	        		{
	    	        			if(conf.getPermit_regions()[2] == 1)
	    	        			{
	    	        				 Contact _contact = new Contact();
	    	            			 _contact.setType(1);
	    	            			 _contact.setTangential_area(4);
	    	            			 contacts.add(_contact);  
	    	        			}
	    	        			else	if(conf.getPermit_regions()[3] == 1)
	    	            				{
			    	           				 Contact _contact = new Contact();
			    	               			 _contact.setType(-1);
			    	               			 _contact.setTangential_area(1);
			    	               			 contacts.add(_contact);  
	    	            				}
    	        			
    	        		} 
    	        		
        				else if(tr1)
        					 {
        						Contact _contact = new Contact();
        						_contact.setType(-1);
        						_contact.setTangential_area(1);
        						contacts.add(_contact);  
        					 }
        					else if(tr2)
        					{
				    			 Contact _contact = new Contact();
				    			 _contact.setType(-1);
				    			 _contact.setTangential_area(2);
				    			 contacts.add(_contact);  
				    		}
								    		
				    			  else if(tr3)
				    			  		{
							    			//if(!contacts.contains(_contact));
							    			 Contact _contact = new Contact();
							    			 _contact.setType(1);
							    			 _contact.setTangential_area(3);
							    			 contacts.add(_contact);  
				    			  		}
				    			  		else if(tr4)
				    			  			 {
									    			 Contact _contact = new Contact();
									    			 _contact.setType(1);
									    			 _contact.setTangential_area(4);
									    			 contacts.add(_contact);  		    	  
				    			  			 }
    		 
    	}
    	
    	 else 
    		 if(oconf.getAngular() == 1)
    		 {
    			 boolean tr1 = testSolidRegion(mbr,conf,tmbr,oconf,1);
    			 boolean tr2 = testSolidRegion(mbr,conf,tmbr,oconf,2);
    			 boolean tr3 = testSolidRegion(mbr,conf,tmbr,oconf,3);
    			 boolean tr4 = testSolidRegion(mbr,conf,tmbr,oconf,4);
    			 boolean tr23 = testSolidRegion(mbr,conf,tmbr,oconf,23);
    			 boolean tr14 = testSolidRegion(mbr,conf,tmbr,oconf,34);
    			 if(tr1 && tr14)
    			 {
    				 Contact _contact = new Contact();
	    			 _contact.setType(-1);
	    			 _contact.setTangential_area(114);
	    			 contacts.add(_contact);  
    			 }
    			 else if(tr2 && tr23)
    			 {
    				 Contact _contact = new Contact();
	    			 _contact.setType(-1);
	    			 _contact.setTangential_area(223);
	    			 contacts.add(_contact);  
    			 }else if(tr3 && tr23)
    			 {
    				 Contact _contact = new Contact();
	    			 _contact.setType(1);
	    			 _contact.setTangential_area(233);
	    			 contacts.add(_contact);  
    			 }else if(tr4 && tr14)
    			 {
    				 Contact _contact = new Contact();
	    			 _contact.setType(1);
	    			 _contact.setTangential_area(414);
	    			 contacts.add(_contact);  
    			 }else if(tr1)
    			 {
    				 Contact _contact = new Contact();
	    			 _contact.setType(-1);
	    			 _contact.setTangential_area(1);
	    			 contacts.add(_contact);  
    			 }else if(tr2)
    			 {
    				 Contact _contact = new Contact();
	    			 _contact.setType(-1);
	    			 _contact.setTangential_area(2);
	    			 contacts.add(_contact);  
    			 }else if(tr23)
    			 {
    				 Contact _contact = new Contact();
	    			 _contact.setType(0);
	    			 _contact.setTangential_area(23);
	    			 contacts.add(_contact);  
    			 }else if(tr3)
    			 {
    				 Contact _contact = new Contact();
	    			 _contact.setType(1);
	    			 _contact.setTangential_area(3);
	    			 contacts.add(_contact);  
    			 }else if(tr4)
    			 {
    				 Contact _contact = new Contact();
	    			 _contact.setType(1);
	    			 _contact.setTangential_area(4);
	    			 contacts.add(_contact);  
    			 }else if(tr14)
    			 {
    				 Contact _contact = new Contact();
	    			 _contact.setType(0);
	    			 _contact.setTangential_area(14);
	    			 contacts.add(_contact);  
    			 }
    			 
    		 }
     return contacts;
}
/* use this when mbr is regular and tmbr is not regular
 * No different between tmin or tmax, at least one should be touch, another one should disjoint
 * */
private static boolean testSolidRegion(final MBR mbr, final Configuration conf,final MBR tmbr, final Configuration tconf,int region)
{
	boolean result = false;

	 MyPolygon min = new MyPolygon();
	 MyPolygon max = new MyPolygon();
	 MyPolygon tmin = new MyPolygon();
	 MyPolygon tmax = new MyPolygon();

     min = mbr.getRegionLine(region);
     max = mbr.getRegionLine(region);
	 
	 
      if(tconf.getPermit_regions()[2] == 1)
	  {
        	tmin = tmbr.getDiagonal_right();
        	tmax = tmbr.getCore_right();
	  }
	  else
		  if(tconf.getPermit_regions()[3] == 1)
		  {
			  tmax = mbr.getDiagonal_left();
		      tmin = mbr.getCore_left();
		  }
 
  
  		MyPolygon _region = new MyPolygon();
        
  		_region = max;
		
	if(QuantiShapeCalculator.isIntersected(tmax, _region,true))
		if(testPotentialContact(min,max,tmin,tmax))
		{
			result = true;
		}
		else
			if(testPotentialContact(min,max,tmax,tmin))
			{
				result = true;
			}
	return result;

}

/* region numbered as 1,2,3,4 from right-top, anti clockwise*/
private static boolean testFreeRegion24(final MBR mbr, final Configuration conf,final MBR tmbr, final Configuration tconf,int region)
{
	boolean result = false;

	 MyPolygon min = new MyPolygon();
	 MyPolygon max = new MyPolygon();
	 MyPolygon tmin = new MyPolygon();
	 MyPolygon tmax = new MyPolygon();

	  if(conf.getPermit_regions()[2] == 1)
	  {
          min = mbr.getDiagonal_right();
	      max = mbr.getCore_right();
	  }
	  else
		  if(conf.getPermit_regions()[3] == 1)
		  {
			  max = mbr.getDiagonal_left();
		      min = mbr.getCore_left();
		  }
  
  if(tconf.getAngular() == 0)
  {
	   tmin = tmbr.getRegion(5);
	   tmax = tmbr.getRegion(5);
  }
 else
	  if(tconf.getPermit_regions()[2] == 1)
	  {
        tmin = tmbr.getDiagonal_right();
	      tmax = tmbr.getCore_right();
	  }
	  else
		  if(tconf.getPermit_regions()[3] == 1)
		  {
			  tmax = mbr.getDiagonal_left();
		      tmin = mbr.getCore_left();
		  }
 
  
  		MyPolygon _region = new MyPolygon();
		if(conf.getAngular() == 1)
		{
		
			if(conf.getPermit_regions()[3] == 1)
				_region = mbr.getRegion(region);
			else
				if(conf.getPermit_regions()[2] == 1)
					_region = mbr.getRegionLarge(region);
			
		
		}

		
		
	if(QuantiShapeCalculator.isIntersected(tmax, _region,true))
		if(testPotentialContact(min,max,tmin,tmax))
		{
			result = true;
		}
	return result;

}

private static boolean testFreeRegion13(final MBR mbr, final Configuration conf,final MBR tmbr, final Configuration tconf,int region)
{
	boolean result = false;
	 MyPolygon min = new MyPolygon();
	 MyPolygon max = new MyPolygon();
	 MyPolygon tmin = new MyPolygon();
	 MyPolygon tmax = new MyPolygon();
	 
	 
		  if(conf.getPermit_regions()[2] == 1)
		  {
			  max = mbr.getDiagonal_right();
		      min = mbr.getCore_right();
		  }
		  else
			  if(conf.getPermit_regions()[3] == 1)
			  {

			      min = mbr.getDiagonal_left();
			      max = mbr.getCore_left();
			  }
	  
	 
	 
	  if(tconf.getAngular() == 0)
	  {
		   tmin = tmbr.getRegion(5);
		   tmax = tmbr.getRegion(5);
	  }
	 else
		  if(tconf.getPermit_regions()[2] == 1)
		  {
			   tmin = tmbr.getCore_right();
			   tmax = tmbr.getDiagonal_right();
		  }
		  else
			  if(tconf.getPermit_regions()[3] == 1)
			  {
				  tmin = tmbr.getDiagonal_left();
				   tmax = tmbr.getCore_left();
			  }
	  
	  /* pay attention to touch relationship */
	  MyPolygon _region = new MyPolygon();
		if(conf.getAngular() == 1)
		{
			if(conf.getPermit_regions()[2] == 1)
				_region = mbr.getRegion(region);
			else
				if(conf.getPermit_regions()[3] == 1)
					_region = mbr.getRegionLarge(region);
		}

	
	
	//	Debug.echo(null,mbr,conf,tmbr,tconf," out ",testPotentialContact(min,max,tmin,tmax));
	
	
		if(QuantiShapeCalculator.isIntersected(tmax, _region,true))
			if(testPotentialContact(min,max,tmin,tmax))
			{
			   	result = true;
			}
		
	//	Debug.echo(null,mbr,conf,tmbr,tconf," result ",result);
		return result;

}






/* when MAX INTERSECTING MAX, MIN DISJOINT MIN*/
private static boolean testPotentialContact(MyPolygon min,MyPolygon max,MyPolygon tmin, MyPolygon tmax)
{
    boolean min_disjoint = false;
    boolean max_intersecting = false;
    
    /* If bot max are edges, then they could be considered as intersecting.*/
    
  //   Debug.echo(null, " in ", min,max,tmin,tmax,QuantiShapeCalculator.isIntersected(min,tmin,false),QuantiShapeCalculator.isIntersected(max,tmax,true));
    if(!QuantiShapeCalculator.isIntersected(min,tmin,false))
               min_disjoint = true;
    
    if(QuantiShapeCalculator.isIntersected(max,tmax,true))
        max_intersecting = true;

   // if(max.npoints == 2 && tmax.npoints == 2)
    	//max_intersecting = true;
    
    return min_disjoint&max_intersecting;
}
}

