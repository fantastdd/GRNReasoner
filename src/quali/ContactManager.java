package quali;

import java.awt.Point;
import java.util.HashMap;
import java.util.LinkedList;

import quanti.QuantiShapeCalculator;

import common.MyPolygon;
import common.util.Debug;

public class ContactManager {
	
	public static LinkedList<HashMap<Integer, Contact>> getPossibleContacts(final Configuration conf, final TestNode node , int threshold) {
		LinkedList<HashMap<Integer, Contact>> contactMaps = new LinkedList<HashMap<Integer, Contact>>();

		// since we instantiate the conf by the order of the id. so we only need
		// to test all the conf with the ids smaller than the conf's.
		LinkedList<Neighbor> neighbors = conf.getNeighbors();

		// add the inititial map
		if (conf.lastValidNeighborId == -1)
			contactMaps.add(conf.getContact_map());
		else {
			for (Neighbor neighbor : neighbors) 
			{
				if (neighbors.indexOf(neighbor) > conf.lastValidNeighborId)
				{
					break;
				} 
				else
				{
					Configuration neighbor_conf = node.lookup(neighbor.getMbr());
					MBR neighbor_mbr = neighbor_conf.getMbr();
					// neighbor must be instantiated
					if (neighbor_mbr.getId() < conf.getMbr().getId()) 
					{
						// TODO non-used work if.
						if (contactMaps.isEmpty()) {
							
							//System.out.println(" Get Contact:  " + conf.toShortString() + "   " + neighbor_conf.toShortString());
							
							LinkedList<Contact> contacts = getContact(conf, neighbor_conf, threshold);
							/*  if(conf.getMbr().getId() == 12 && node.lookup(5).unary == 0 && conf.unary == 2)
							        System.out.println(" Get Contacts :  " +  contacts.size());*/
						
							for (Contact contact : contacts) 
							{
							 //
							//	if(conf.getMbr().getId() == 6 && node.lookup(1).unary == 0 && conf.unary == 1)
							//	 				System.out.println(" conf    " + conf.toShortString() + "    tconf    " + neighbor_conf.toShortString() + "   contact   " + contact );
								
								HashMap<Integer, Contact> _contactMap = new HashMap<Integer, Contact>();
								// clone the map and add to list
								HashMap<Integer, Contact> contactMap = conf.getContact_map();

						
								for (Integer key : contactMap.keySet()) {
									if (key == neighbor_mbr.getId())
											_contactMap.put(key, contact);
									else
										_contactMap.put(key, contactMap.get(key).clone());
								}
								contactMaps.add(_contactMap);
							}
							
							/*  if(conf.getMbr().getId() == 12 && node.lookup(5).unary == 0&& conf.unary == 2 )
							  {	 
									System.out.println(" map size :  " + contactMaps.size());
								  for (HashMap<Integer,Contact> contactMap : contactMaps)
								  {
									  System.out.println(contactMap.get(5));
								  }
							  }*/
							  
						} 
						else // pop up the contactmap in the list and split...
						{
							LinkedList<HashMap<Integer, Contact>> maps = new LinkedList<HashMap<Integer, Contact>>();
							for (HashMap<Integer, Contact> contactMap : contactMaps)
							{
								LinkedList<Contact> contacts = getContact(conf, neighbor_conf , threshold);
								for (Contact contact : contacts) 
								{
									HashMap<Integer, Contact> _contactMap = new HashMap<Integer, Contact>();
									for (Integer key : contactMap.keySet()) {
										if (key == neighbor_mbr.getId())
											_contactMap.put(key, contact);
										else
											_contactMap.put(key, contactMap
													.get(key).clone());
									}
									maps.add(_contactMap);
								}
							}
							//bug fix: MBR A has two contacts, MBR B has 0, MBR C has two contacts. without the following if, the contactsMaps will be cleared
							if(maps.isEmpty())
								continue;
							else{
										contactMaps.clear();
										contactMaps.addAll(maps);
							}
						}
					}
					// DEBUG TODO refine this branch later
					/*
					 * else { if(contactMaps.isEmpty())
					 * contactMaps.add(conf.getContact_map());
					 * 
					 * }
					 */
				}
			}
		}
		
	
	/*	  if(conf.getMbr().getId() == 12 && node.lookup(5).unary == 0&& conf.unary == 2 )
			  for (HashMap<Integer,Contact> contactMap : contactMaps)
			  {
				  System.out.println(contactMap.get(5));
			  }*/
		
		return contactMaps;

	}
	
	// any gap between (0 - threshold] will be removed between conf and tconf
	public static LinkedList<Contact> getContact(Configuration conf, Configuration tconf, int threshold)
	{
		if(threshold == 0)
			return getContact(conf,tconf);
		else
		{
			//clone the configuration and test
			Configuration _conf = conf.translate(tconf, threshold);
			//System.out.println("  after translate " + _conf.getMbr() + "  " + _conf.getMbr().getBounds() + "   " + " tconf " + tconf.getMbr() + "   " + tconf.getMbr().getBounds());
			//System.out.println(getContact(_conf,tconf));
			return getContact(_conf,tconf);
		}
	}
    public static Contact getPairContact(Configuration tconf, Configuration conf, int threshold)
    {
    	if(threshold == 0)
			return getPairContact(tconf,conf);
		else
		{
			//clone the configuration and test
			Configuration _conf = conf.translate(tconf, threshold);
			//System.out.println( conf.getMbr() + "  " + conf.getMbr().getBounds() + " has been translated to " + _conf.getMbr().getBounds() );
			return getPairContact(tconf,_conf);
		}
    	
    }
	// tconf is the known one, we want get the contact of conf.
	public static Contact getPairContact(Configuration tconf, Configuration conf) {
		
		final Contact _contact = tconf.getContact_map().get(conf.getMbr().getId());
		//print
		//if(tconf.getMbr().getId() == 1 && conf.unary == 1 && tconf.unary == 0 && conf.getMbr().getId() == 6)
		//		System.out.println(" get pair contact conf    " + conf.toShortString() + "    tconf    " + tconf.toShortString() + "   contact   " + _contact );
		// System.out.println(conf.getMbr() + "   " + tconf.getMbr());
		// System.out.println(tconf.toShortString() + "  " + conf.toShortString() + "   " + _contact);
		Contact contact = new Contact();
		if (conf.unary != 0 && tconf.unary != 0) {

			// April 6 : turn all the angular touch to edge touch
			if (_contact.getTangential_area() == 12) {
				contact.setTangential_area(34);
				contact.setType(1);
			} else if (_contact.getTangential_area() == 14) {
				contact.setTangential_area(23);
				contact.setType(1);
			} else if (_contact.getTangential_area() == 23) {
				contact.setTangential_area(14);
				contact.setType(1);
			} else if (_contact.getTangential_area() == 34) {
				contact.setTangential_area(12);
				contact.setType(1);
			} else if (_contact.getTangential_area() == 0) {
				contact.setTangential_area(0);
				contact.setType(0);
			}
			else if (_contact.getTangential_area() == 1) {
				contact.setTangential_area(3);
				contact.setType(1);
			} else if (_contact.getTangential_area() == 2) {
				contact.setTangential_area(4);
				contact.setType(1);
			} else if (_contact.getTangential_area() == 3) {
				contact.setTangential_area(1);
				contact.setType(1);
			} else if (_contact.getTangential_area() == 4) {
				contact.setTangential_area(2);
				contact.setType(1);
			}
		} 
		else

		/* ombr regular supported by regular mbr */
		if (conf.unary == 0 && tconf.unary == 0) {
			// System.out.println(conf + "    " + tconf + "   " + _contact);
			if (_contact.getTangential_area() == 34
					|| _contact.getTangential_area() == 3
					|| _contact.getTangential_area() == 4) {

				MyPolygon regionline1 = conf.getRegionLine(1);
				MyPolygon regionline2 = conf.getRegionLine(2);
				boolean region1Contact = QuantiShapeCalculator.isIntersected(
						regionline1, tconf.getRegionLine(34), true);
				boolean region2Contact = QuantiShapeCalculator.isIntersected(
						regionline2, tconf.getRegionLine(34), true);
				 
				//System.out.println("contact spcifics "+ tconf.toShortString() + "   " + tconf.getMbr().getBounds() + "   " + conf.toShortString() + "  " + conf.getMbr().getBounds() + "    " + region1Contact + "    " + region2Contact);
				
				if (region1Contact && region2Contact) {
					contact.setTangential_area(12);
					contact.setType(1);
				} else if (region1Contact) {
					contact.setTangential_area(1);
					contact.setType(1);
				} else if (region2Contact) {
					contact.setTangential_area(2);
					contact.setType(1);
				}
			} else 
				if (_contact.getTangential_area() == 12	|| _contact.getTangential_area() == 1 || _contact.getTangential_area() == 2) 
				{
						MyPolygon regionline3 = conf.getRegionLine(3);
						MyPolygon regionline4 = conf.getRegionLine(4);
						boolean region3Contact = QuantiShapeCalculator.isIntersected(
								regionline3, tconf.getRegionLine(12), true);
						boolean region4Contact = QuantiShapeCalculator.isIntersected(
								regionline4, tconf.getRegionLine(12), true);
						if (region3Contact && region4Contact) {
							contact.setTangential_area(34);
							contact.setType(1);
						} else if (region3Contact) {
							contact.setTangential_area(3);
							contact.setType(1);
						} else if (region4Contact) {
							contact.setTangential_area(4);
							contact.setType(1);
						}
			} else if (_contact.getTangential_area() == 23) {

				contact.setTangential_area(14);
				contact.setType(1);
			} else if (_contact.getTangential_area() == 14) {

				contact.setTangential_area(23);
				contact.setType(1);
			} else if (_contact.getTangential_area() == 0) {
				//Debug.echo(null, " Tangential Area = 0 detected");
				System.out.println(" Tangential Area = 0 detected");
			}

		} 
		else 
			//Fat Fixed April 4
			if (conf.unary != 0 && tconf.unary == 0) 
			{
				//TODO may be area = 12
				if (_contact.getTangential_area() == 1|| _contact.getTangential_area() == 2 ) 
				{

					if (conf.unary == 1 || conf.unary == 3)
					{
						//TODO could be 4
						contact.setTangential_area(3);
						contact.setType(1);
					}
			    	else if (conf.unary == 2 || conf.unary == 4) {
					//TODO 
					contact.setTangential_area(4);
					contact.setType(1);
				}

			} 
				else
					if (_contact.getTangential_area() == 23)
					{

						/*	 commented on April 4
						 * if (conf.unary == 1 ) {
								contact.setTangential_area(4);
								contact.setType(1);
							} 
							else
								if (conf.getPermit_regions()[3] == 1)
								{
									
									contact.setTangential_area(1);
									contact.setType(1);
								}*/
						 contact.setTangential_area(4);
						 contact.setType(1);
					} else 
						if (_contact.getTangential_area() == 14)
						{
							/*	 commented on April 4
							 * if (conf.getPermit_regions()[2] == 1) 
								{
									contact.setTangential_area(2);
									contact.setType(1);
								} else 
									if (conf.getPermit_regions()[3] == 1)
									{
										contact.setTangential_area(3);
										contact.setType(1);
									}*/
                             contact.setTangential_area(3);
                             contact.setType(1);
						} 
						else 
							if (_contact.getTangential_area() == 3 || _contact.getTangential_area() == 4)
							{
									/* no need to distinguish between region 1 or 2 of the tconf */
								/*	 commented on Apirl 4
								 * if (conf.getPermit_regions()[2] == 1) {
										contact.setTangential_area(1);
										contact.setType(1);
									} else if (conf.getPermit_regions()[3] == 1) {
										contact.setTangential_area(1);
										contact.setType(1);
									}*/
								contact.setTangential_area(1);
								contact.setType(1);

			} else 
				if (_contact.getTangential_area() == 114) {

						if (_contact.getType() == 1) {
								// System.out.println(tconf.toShortString() + "  " + conf.toShortString() + "  " + _contact +   "   " + _contact.points[0]);
							//TODO make restricted points check for fat gsr
							if (conf.addRestrictedPoints(_contact.points[0], 3, true))
							{
									contact.setTangential_area(3);
									contact.setType(1);
							}
					
						}
						else {
									if (conf.addRestrictedPoints(_contact.points[0], 3, false)) {
											contact.setTangential_area(3);
											contact.setType(0);
									}
								}

			} else if (_contact.getTangential_area() == 223) 
			{
				if (_contact.getType() == 1) {

					if (conf.addRestrictedPoints(_contact.points[0], 4, true)) {
						contact.setTangential_area(4);
						contact.setType(1);
					}
					// Debug.echo(null, conf.getMbr(),_contact,contact);
				} else {
					if (conf.addRestrictedPoints(_contact.points[0], 4, false)) {
						contact.setTangential_area(4);
						contact.setType(0);
					}
					// Debug.echo(null, conf.getMbr(),_contact,contact);
				}

			} else
				if (_contact.getTangential_area() == 233) {

				if (_contact.getType() == 1) {
					if (conf.addRestrictedPoints(_contact.points[0], 1, true)) {
						contact.setTangential_area(1);
						contact.setType(1);
					}
					// Debug.echo(null, conf.getMbr(),_contact,contact);
				} else {
						if (conf.addRestrictedPoints(_contact.points[0], 1, false)) {
							contact.setTangential_area(1);
							contact.setType(0);
					}
					// Debug.echo(null, conf.getMbr(),_contact,contact);
				}

			} else if (_contact.getTangential_area() == 414) {

				if (_contact.getType() == 1) {

					if (conf.addRestrictedPoints(_contact.points[0], 2, true)) {
						contact.setTangential_area(2);
						contact.setType(1);
					}
					// Debug.echo(null, conf.getMbr(),_contact,contact);
				} else {
					if (conf.addRestrictedPoints(_contact.points[0], 2, false)) {
						contact.setTangential_area(2);
						contact.setType(0);
					}
					// Debug.echo(null, conf.getMbr(),_contact,contact);
				}

			} else if (_contact.getTangential_area() == 0) {
				Debug.echo(null,
						" Tangential area 0 detected in pre intialization");
			}

		}

       //Fat GSRs fixed , April 4
		if (conf.unary == 0 && tconf.unary != 0) 
		{
			boolean tr2 = testRegionR_A(conf, tconf, 2);
			boolean tr1 = testRegionR_A(conf, tconf, 1);
			boolean tr14 = testRegionR_A(conf, tconf, 14);
			boolean tr23 = testRegionR_A(conf, tconf, 23);

			boolean tr3 = testRegionR_A(conf, tconf, 3);
			boolean tr4 = testRegionR_A(conf, tconf, 4);

			boolean vertex_1 = (testVertexR_A13(conf, tconf, 1) == 2)
					&& (testVertexR_A13(conf, tconf, 14) == 2); // right-top corner
			boolean vertex_3 = (testVertexR_A13(conf, tconf, 3) == 2)
					&& (testVertexR_A13(conf, tconf, 23) == 2);
			boolean vertex_2 = (testVertexR_A24(conf, tconf, 2) == 2)
					&& (testVertexR_A24(conf, tconf, 23) == 2);
			boolean vertex_4 = (testVertexR_A24(conf, tconf, 4) == 2)
					&& (testVertexR_A24(conf, tconf, 14) == 2);

			if (_contact.getTangential_area() == 4) {

				if (vertex_2) {
					if (_contact.getType() == 1) {
						if (!conf.v2) {
							contact.setTangential_area(223);
							contact.points[0] = new Point(conf.vertices[1]);
							contact.setType(1);
							conf.v2 = true;
						}
					} else {
						contact.setTangential_area(223);
						contact.setType(0);

					}

				} else if (tr1) {
					contact.setTangential_area(1);
					contact.setType(1);
				} else if (tr2) {
					contact.setTangential_area(2);
					contact.setType(1);

				} else if (tr23) {
					contact.setTangential_area(23);
					contact.setType(1);

				}

			} else if (_contact.getTangential_area() == 1) {
				if (vertex_3) {
					if (_contact.getType() == 1) {
						if (!conf.v3) {
							contact.setTangential_area(233);
							contact.setType(1);
							contact.points[0] = new Point(conf.vertices[2]);
							conf.v3 = true;
						}
					} else {
						contact.setTangential_area(233);
						contact.setType(0);

					}

				} else if (tr3) {
					contact.setTangential_area(3);
					contact.setType(1);
				} else if (tr4) {
					contact.setTangential_area(4);
					contact.setType(1);
				} else if (tr23) {
					contact.setTangential_area(23);
					contact.setType(1);

				}

			} else if (_contact.getTangential_area() == 2) {
				if (vertex_4) {
					if (_contact.getType() == 1) {
						if (!conf.v4) {
							contact.setTangential_area(414);
							contact.setType(1);
							contact.points[0] = new Point(conf.vertices[3]);
							conf.v4 = true;
						}
					} else {
						contact.setTangential_area(414);
						contact.setType(0);

					}

				} else if (tr4) {
					contact.setTangential_area(4);
					contact.setType(1);
				}

				else if (tr3) {
					contact.setTangential_area(3);
					contact.setType(1);
				} else if (tr14) {
					contact.setTangential_area(14);
					contact.setType(1);

				}

			} else if (_contact.getTangential_area() == 3) {
				if (vertex_1) 
				{
					if (_contact.getType() == 1) {
						if (!conf.v1) {
							
							contact.setTangential_area(114);
							contact.setType(1);
							contact.points[0] = new Point(conf.vertices[0]);
							//print
							//System.out.println( "  enter to this block  " + conf.toShortString() + "   " + tconf.toShortString() + "   " + contact);
							conf.v1 = true;
						}
					} 
					else {
					//	System.out.println( "  enter to this block  " + conf.toShortString() + "   " + tconf.toShortString() + "   " + contact);
						contact.setTangential_area(114);
						contact.setType(0);

					}

				} else if (tr2) {
					contact.setTangential_area(2);
					contact.setType(1);
				} else if (tr1) {
					contact.setTangential_area(1);
					contact.setType(1);
				} else if (tr14) {
					contact.setTangential_area(14);
					contact.setType(1);

				}

			} else if (_contact.getTangential_area() == 14) {
				contact.setTangential_area(23);
				contact.setType(0);
			} else if (_contact.getTangential_area() == 23) {
				contact.setTangential_area(14);
				contact.setType(0);
			} else if (_contact.getTangential_area() == 0) {
				System.out.println(" zero tangential area detected  ");
				contact.setTangential_area(0);
				contact.setType(0);
			}

		}

		// add on Jan
		if (_contact.getType() == 0) {
			contact.setType(0);
			contact.setTangential_area(-3);
		}

		return contact;

	}

	
	public static LinkedList<Contact> getContact(Configuration conf, Configuration tconf) {
		
		LinkedList<Contact> contacts = new LinkedList<Contact>();

		/* if both are regular */
		if (conf.unary == 0 && tconf.unary == 0) {

			boolean tr2 = testRegularRegion12(conf, tconf, 2);
			boolean tr1 = testRegularRegion12(conf, tconf, 1);
			boolean tr3 = testRegularRegion34(conf, tconf, 3);
			boolean tr4 = testRegularRegion34(conf, tconf, 4);
			boolean tr23 = testRegularRegion23(conf, tconf);
			boolean tr14 = testRegularRegion14(conf, tconf);
			// System.out.println(conf.getMbr() + "    " + tconf.getMbr() + "   " + tr1 + "   " + tr2 + "   " + tr3 + "   " + tr4 + "   " + tr23 + "   " + tr14);
			if (tr2 && tr1) {
				Contact _contact = new Contact();
				_contact.setTangential_area(12);
				_contact.setType(1);

				contacts.add(_contact);

			} else if (tr3 && tr4) {
				Contact _contact = new Contact();
				_contact.setTangential_area(34);
				_contact.setType(1);

				contacts.add(_contact);

			} else if (tr1) {
				Contact _contact = new Contact();
				_contact.setTangential_area(1);
				_contact.setType(1);

				contacts.add(_contact);

			} else if (tr2) {
				Contact _contact = new Contact();
				_contact.setTangential_area(2);
				_contact.setType(1);

				contacts.add(_contact);

			} else if (tr3) {
				Contact _contact = new Contact();
				_contact.setTangential_area(3);
				_contact.setType(1);
				contacts.add(_contact);
				// System.out.println( "    tr3 clause  " + conf + "    " +
				// tconf + "   " + tr3);
			} else if (tr4) {
				Contact _contact = new Contact();
				_contact.setTangential_area(4);
				_contact.setType(1);

				contacts.add(_contact);

			} else if (tr23) {
				Contact _contact = new Contact();
				_contact.setTangential_area(23);
				_contact.setType(1);

				contacts.add(_contact);

			} else if (tr14) {
				Contact _contact = new Contact();
				_contact.setTangential_area(14);
				_contact.setType(1);

				contacts.add(_contact);
			}

		}
       //FatGSR fixed April 4
		else if (conf.unary != 0 && tconf.unary == 0) {

			/* no needs to distiguish between edge 12,23,34,14 */
			boolean tr12 = testRegionR_A(tconf, conf, 12);
			boolean tr34 = testRegionR_A(tconf, conf, 34);

			boolean tr23 = testRegularRegion23(tconf, conf);
			boolean tr14 = testRegularRegion14(tconf, conf);
			
			boolean vertex_1 = (testVertexR_A13(tconf, conf, 1) == 2)
					&& (testVertexR_A13(tconf, conf, 14) == 2); // right-top corner
			
			boolean vertex_3 = (testVertexR_A13(tconf, conf, 3) == 2)
					&& (testVertexR_A13(tconf, conf, 23) == 2);
			
			boolean vertex_2 = (testVertexR_A24(tconf, conf, 2) == 2)
			       && (testVertexR_A24(tconf, conf, 23) == 2);
			
			boolean vertex_4 = (testVertexR_A24(tconf, conf, 4) == 2)
					&& (testVertexR_A24(tconf, conf, 14) == 2);

			if (vertex_1) {

				Contact _contact = new Contact();
				// TODO real case check using restricted points
				_contact.setType(1);
				_contact.points[0] = new Point(tconf.x + tconf.width, tconf.y);
				_contact.setTangential_area(3);
				contacts.add(_contact);

			} else if (vertex_2) {

				Contact _contact = new Contact();
				_contact.setType(1);
				_contact.points[0] = new Point(tconf.x, tconf.y);
				_contact.setTangential_area(4);

				contacts.add(_contact);

			} else if (vertex_3) {

				Contact _contact = new Contact();
				_contact.setType(1);
				_contact.points[0] = new Point(tconf.x, tconf.y + tconf.height);
				_contact.setTangential_area(1);

				contacts.add(_contact);

			} else if (vertex_4) {

				Contact _contact = new Contact();
				_contact.setType(1);
				_contact.points[0] = new Point(tconf.x + tconf.width, tconf.y
						+ tconf.height);
				_contact.setTangential_area(2);

				contacts.add(_contact);

			}

			else if (tr12) {

				Contact _contact = new Contact();
				_contact.setType(1);
				if (conf.getPermit_regions()[2] == 1)
					_contact.setTangential_area(3);
				else
					_contact.setTangential_area(4);

				contacts.add(_contact);

			} else if (tr23) {
				Contact _contact = new Contact();

				if (conf.getPermit_regions()[2] == 1) {
					_contact.setType(1);
					_contact.setTangential_area(4);

				} else {
					_contact.setTangential_area(14);
					_contact.setType(1);
				}
				contacts.add(_contact);

			} else if (tr34) {
				Contact _contact = new Contact();
				_contact.setType(1);

				if (conf.getPermit_regions()[2] == 1)
					_contact.setTangential_area(1);
				else
					_contact.setTangential_area(2);

				contacts.add(_contact);

			} else if (tr14) {
				Contact _contact = new Contact();

				if (conf.getPermit_regions()[3] == 1) {
					_contact.setType(1);
					_contact.setTangential_area(3);

				} else {
					_contact.setTangential_area(23);
					_contact.setType(1);
				}
				contacts.add(_contact);
			}
			// add on Jan
			/*//commented on April 6
			else {

				Contact _contact = new Contact();
				_contact.setType(0);
				_contact.setTangential_area(-3);

				contacts.add(_contact);
			}*/

		}
	    //FatGSR fixed April 4
		else if (conf.unary == 0 && tconf.unary != 0) {
			/* no needs to distinguish between edge 12,23,34,14 */
			
			boolean tr1 = testRegionR_A(conf, tconf, 1);// test the right half of the top edge.
			boolean tr2 = testRegionR_A(conf, tconf, 2);
			boolean tr3 = testRegionR_A(conf, tconf, 3);
			boolean tr4 = testRegionR_A(conf, tconf, 4);

			boolean tr23 = testRegionR_A(conf, tconf, 23);
			boolean tr14 = testRegionR_A(conf, tconf, 14);

			boolean vertex_1 = ( testVertexR_A13(conf, tconf, 1) == 2)
					                                  && (testVertexR_A13(conf, tconf, 14) == 2); // right-top corner
																
			boolean vertex_2 = (testVertexR_A24(conf, tconf, 2) == 2)
					&& (testVertexR_A24(conf, tconf, 23) == 2);
			boolean vertex_3 = (testVertexR_A13(conf, tconf, 3) == 2)
					&& (testVertexR_A13(conf, tconf, 23) == 2);
			boolean vertex_4 = (testVertexR_A24(conf, tconf, 4) == 2)
					&& (testVertexR_A24(conf, tconf, 14) == 2);

			if (vertex_1) {

				Contact _contact = new Contact();
				_contact.setType(1);
				_contact.setTangential_area(114);
				_contact.points[0] = new Point(conf.x + conf.width, conf.y);
				contacts.add(_contact);
              // System.out.println(" enter to this block   " + conf.toShortString() + "   " + tconf.toShortString() + "   " + _contact.points[0]);
				/*
				 * if(!conf.v1) { //TODO conf updates .v1 Contact _contact = new
				 * Contact(); _contact.points[0] = __contact.points[0];
				 * _contact.setType(1); _contact.setTangential_area(114);
				 * contacts.add(_contact); }
				 */

			} else if (vertex_2) {
				Contact _contact = new Contact();
				_contact.setType(1);
				_contact.setTangential_area(223);
				_contact.points[0] = new Point(conf.x, conf.y);
				contacts.add(_contact);

				/*
				 * if(!conf.v2) { Contact _contact = new Contact();
				 * _contact.points[0] = __contact.points[0];
				 * _contact.setType(1); _contact.setTangential_area(223);
				 * contacts.add(_contact); }
				 */

			} else if (vertex_3) {

				Contact _contact = new Contact();
				_contact.setType(1);
				_contact.setTangential_area(233);
				_contact.points[0] = new Point(conf.x, conf.y + conf.height);
				contacts.add(_contact);

				/*
				 * if(!conf.v3) { Contact _contact = new Contact();
				 * _contact.points[0] = __contact.points[0];
				 * _contact.setType(1); _contact.setTangential_area(233);
				 * contacts.add(_contact); }
				 */
			} else if (vertex_4) {

				Contact _contact = new Contact();
				_contact.setType(1);
				_contact.setTangential_area(414);
				_contact.points[0] = new Point(conf.x + conf.width, conf.y
						+ conf.height);
				contacts.add(_contact);
				/*
				 * if(!conf.v4) { Contact _contact = new Contact();
				 * _contact.setType(1); _contact.points[0] =
				 * __contact.points[0]; _contact.setTangential_area(414);
				 * contacts.add(_contact); }
				 */

			} else if (tr1) // must touch
			{
				Contact _contact = new Contact();
				_contact.setType(1);
				_contact.setTangential_area(1);
				contacts.add(_contact);
			} else if (tr2) // must touch
			{
				Contact _contact = new Contact();
				_contact.setType(1);
				_contact.setTangential_area(2);
				contacts.add(_contact);
			} else if (tr23) // must touch
			{
				Contact _contact = new Contact();
				_contact.setType(1);
				_contact.setTangential_area(23);
				contacts.add(_contact);
			} else if (tr3) // must touch
			{
				Contact _contact = new Contact();
				_contact.setType(1);
				_contact.setTangential_area(3);
				contacts.add(_contact);
			} else if (tr4) // must touch
			{
				Contact _contact = new Contact();
				_contact.setType(1);
				_contact.setTangential_area(4);
				contacts.add(_contact);
			} else if (tr14) // must touch
			{
				Contact _contact = new Contact();
				_contact.setType(1);
				_contact.setTangential_area(14);
				contacts.add(_contact);
			}

			/*
			 * if(non_touching) { Contact _contact = new Contact();
			 * _contact.setType(0); _contact.setTangential_area(-1);
			 * Configuration _conf = conf.clone();
			 * _conf.getContact_map().put(tconf.getMbr(), _contact);
			 * confs.add(_conf); }
			 */
		} else
		/* both are angular 
		 * commented on April 5: all using the edge touch
		 * */
		if (conf.unary != 0 && tconf.unary != 0) {
						/* testFree region, conf must not be regular */
						int tr1 = testFreeRegion13(conf, tconf, 1);
						int tr2 = testFreeRegion24(conf, tconf, 2);
						int tr3 = testFreeRegion13(conf, tconf, 3);
						int tr4 = testFreeRegion24(conf, tconf, 4);
						boolean potentialEdgeTouch = 	QuantiShapeCalculator.potentialEdgeTouch(conf, tconf);
						//System.out.println(tr1 + "  " + tr2 + "  " + tr3 + "  " + tr4 + "   " + potentialEdgeTouch);
						//boolean non_touching = false;
						if (tr1 == 2) {
							Contact _contact = new Contact();
							_contact.setType(1);
							if(potentialEdgeTouch)
								_contact.setTangential_area(14);
							else
								_contact.setTangential_area(2);
							contacts.add(_contact);
			
							//non_touching = true;
						}
						if (tr2 == 2) {
							Contact _contact = new Contact();
							_contact.setType(1);
							if(potentialEdgeTouch)
								_contact.setTangential_area(12);
							else
								_contact.setTangential_area(1);
							contacts.add(_contact);
							//non_touching = true;
						}
			
						if (tr3 == 2) {
			
							Contact _contact = new Contact();
							_contact.setType(1);
							
							if(potentialEdgeTouch)
								_contact.setTangential_area(23);
							else
								_contact.setTangential_area(3);
							
							contacts.add(_contact);
							//non_touching = true;
						}
						if (tr4 == 2) {
							Contact _contact = new Contact();
							_contact.setType(1);
							if(potentialEdgeTouch)
								_contact.setTangential_area(34);
							else
								_contact.setTangential_area(4);
							contacts.add(_contact);
						//non_touching = true;
						}
						
				//	Important comments	, do we need a non-touching relation..
		/*		  
				  if (non_touching) {
							Contact _contact = new Contact();
							_contact.setType(0);
							_contact.setTangential_area(-1);
							contacts.add(_contact);
						}*/

		}
		return contacts;

	}

	/*
	 * if only one intersected point, then it is a two regular mbr vertex touch,
	 * can be ignored
	 */
	private static boolean testRegularRegion34(final Configuration conf,
			final Configuration tconf, int region) {
		MyPolygon rline = conf.getRegionLine(region);

		MyPolygon trline = tconf.getRegionLine(12);
		// Debug.echo(null,mbr,rline,
		// tmbr,trline,QuantiShapeCalculator.isIntersected(rline, trline).size()
		// > 1);
		// System.out.println(QuantiShapeCalculator.isIntersected(rline,
		// trline));
		return QuantiShapeCalculator.isIntersectedWCA(rline, trline).size() > 1;

	}

	private static boolean testRegularRegion12(final Configuration conf,
			final Configuration tconf, int region) {

		MyPolygon rline = conf.getRegionLine(region);

		MyPolygon trline = tconf.getRegionLine(34);
		// Debug.echo(null,
		// " test ",rline,trline,QuantiShapeCalculator.isIntersected(rline,
		// trline));
		return QuantiShapeCalculator.isIntersectedWCA(rline, trline).size() > 1;

	}

	private static boolean testRegularRegion23(final Configuration conf,
			final Configuration tconf) {
		/* region 23 refers to the left edge of the rectangle */
		MyPolygon rline = conf.getRegionLine(23);

		MyPolygon trline = tconf.getRegionLine(14);

		return QuantiShapeCalculator.isIntersectedWCA(rline, trline).size() > 1;

	}

	private static boolean testRegularRegion14(final Configuration conf,
			final Configuration tconf) {
		/* region 23 refers to the right edge of the rectangle */
		MyPolygon rline = conf.getRegionLine(14);

		MyPolygon trline = tconf.getRegionLine(23);

		return QuantiShapeCalculator.isIntersectedWCA(rline, trline).size() > 1;

	}
   // This method tests whether the conf's (regular)  edge can touch a specific sector of the tconf
  //Fixed April 4, correct April 5
	private static boolean testRegionR_A(final Configuration conf,  final Configuration tconf, int region)
	{
		MyPolygon rline = conf.getRegionLine(region);
        
		MyPolygon trline = new MyPolygon();

		if (region == 1 || region == 2 || region == 12) 
		{

			if (tconf.unary == 1 || tconf.unary == 3 )
				/* 
				 * should be restricted to permit area
				 * The fat GSRs share the same region line
				 * 
				 *  */
				trline = tconf.getRegionLine(3);
			else
				trline = tconf.getRegionLine(4);

			// System.out.println(tconf.getMbr() + "  " + trline + "   " +
			// conf.getMbr() + "  " + rline + "   " +
			// QuantiShapeCalculator.isIntersected(rline, trline).size());
			// Debug.echo(null, conf,tconf,region,
			// QuantiShapeCalculator.isIntersected(rline, trline).size());
		} 
		else if (region == 3 || region == 4 || region == 34) 
		{
			if (tconf.unary == 1 || tconf.unary == 3)
				trline = tconf.getRegionLine(1);
			else
				trline = tconf.getRegionLine(2);

		}
		else if (region == 23)
		{
			if (tconf.unary == 1 || tconf.unary == 4)
				trline = tconf.getRegionLine(140);
			else
				trline = tconf.getRegionLine(141);

		}
		else if (region == 14) {
			if (tconf.unary == 1 || tconf.unary == 4)
				trline = tconf.getRegionLine(231);
			else
				trline = tconf.getRegionLine(230);

		}

		return QuantiShapeCalculator.isIntersectedWCA(rline, trline).size() > 1;

	}

	/*
	 * use this when mbr is regular and tmbr is not regular, and can be used to
	 * test the four corner's relationship No different between min or max, at
	 * least one should be touch, another one should be disjoint
	 * 
	 * R: regular 
	 * A: angular
	 * 13: test the space area 1 or 3 of the angular rectangle, whether it touches the region of the conf
	 * 
	 * fixed April 4, correct April 5
	 */
	private static int testVertexR_A13(final Configuration conf, final Configuration tconf, int region) {
		
		int result = 0;

		MyPolygon min = new MyPolygon();
		MyPolygon max = new MyPolygon();
		MyPolygon tmin = new MyPolygon();
		MyPolygon tmax = new MyPolygon();

		min = conf.getRegionLine(region);
		max = conf.getRegionLine(region);

		if (tconf.unary == 1) {
			tmin = tconf.getCore_right();
			if (region == 1 || region == 14 )
				tmax = tconf.getRegion(3); // get the bottom left triangular space.
			else
				tmax = tconf.getRegion(1);
		} 
		else 
			if (tconf.unary == 2)
			{
			 tmax = tconf.getCore_left();
			 tmin = tconf.getDiagonal_left();
		}
		else
			if (tconf.unary == 3)
			{
				if (region == 1 || region == 14 )
				{	
					tmax = tconf.approx_u3t3;
				    tmin = tconf.approx_u3t3r;
				}
				else
				{	
					tmax = tconf.approx_u3t1;
				    tmin = tconf.approx_u3t1r;
				}
			}
			else if (tconf.unary == 4)
			{
				if (region == 1 || region == 14 )
				{	
					tmax = tconf.approx_u4t3;
					tmin = tconf.approx_u4t3r;
				}
				else
				{	
					tmax = tconf.approx_u4t1;
					tmin = tconf.approx_u4t1r;
				}
		}
	
			

		MyPolygon _region = max;


		if (QuantiShapeCalculator.isIntersected(tmax, _region, true)) {

			if (!testSolidOverlapping(conf.fullRec , tmin)) {

				result = minmaxEvaluation(min, max, tmin, tmax);

			} else
				result = -1;

		} else {
			result = 0;
		}
		return result;

	}

	/*
	 * use this when mbr is regular and tmbr is not regular No different between
	 * tmin or tmax, at least one should be touching , another one should be
	 * disjoint
	 * 
	 *  R: regular 
	 * A: angular
	 * 24: test the space area 2 or 4 of the angular rectangle, whether it touches the region of the conf
	 * 
	 * fixed April 4 , correct April 5
	 */
	private static int testVertexR_A24(final Configuration conf,
			final Configuration tconf, int region) {
		int result = 0;

		MyPolygon min = new MyPolygon();
		MyPolygon max = new MyPolygon();
		MyPolygon tmin = new MyPolygon();
		MyPolygon tmax = new MyPolygon();

		min = conf.getRegionLine(region);
		max = conf.getRegionLine(region);

		if (tconf.unary == 2) {
			tmin = tconf.getCore_left();
			// tmax = tconf.getDiagonal_right();
			if (region == 2 || region == 23)
				tmax = tconf.getRegion(4);
			else
				tmax = tconf.getRegion(2);
		} else    
			if(tconf.unary == 1) 
			{
			tmax = tconf.getCore_right();
			tmin = tconf.getDiagonal_right();
		} else
			if (tconf.unary == 3)
			{
				if (region == 2 || region == 23 )
				{	
					tmax = tconf.approx_u3t4;
				    tmin = tconf.approx_u3t4r;
				}
				else
				{	
					tmax = tconf.approx_u3t2;
				    tmin = tconf.approx_u3t2r;
				}
			}
			else if (tconf.unary == 4)
			{
				if (region == 2 || region == 23 )
				{	
					tmax = tconf.approx_u4t4;
					tmin = tconf.approx_u4t4r;
				}
				else
				{	
					tmax = tconf.approx_u4t2;
					tmin = tconf.approx_u4t2r;
				}
		}
		MyPolygon _region = new MyPolygon();

		_region = max;

		if (QuantiShapeCalculator.isIntersected(tmax, _region, true)) {
			// System.out.println("  In test VertexRA_24 and pass the tmax _region test"
			// + conf.toShortString() + "   " + tconf.toShortString() + "    ");
			if (!testSolidOverlapping(conf.fullRec, tmin))
				result = minmaxEvaluation(min, max, tmin, tmax);
			else
				result = -1;

		} else {
			result = 0;
		}
		return result;

	}

	/* region numbered as 1,2,3,4 from right-top, anti clockwise 
	 * 
	 * correct April 6
	 * */
	private static int testFreeRegion24(final Configuration conf,
			final Configuration tconf, int region) {
		int result = 0;

		MyPolygon min = new MyPolygon();
		MyPolygon  max = new MyPolygon();
		MyPolygon tmin = new MyPolygon();
		MyPolygon tmax = new MyPolygon();

		if (conf.unary == 1) {
			min = conf.diagonal_right;
			max = (region == 4)? conf.core_right4 : conf.core_right2;
		} else if (conf.unary == 2) {
			max = conf.getRegion(2);
			min = conf.core_left;
		}
		else
			if (conf.unary == 3)
			{
				max = (region == 4)?conf.approx_u3t4:conf.approx_u3t2;
			    min = (region == 4)?conf.approx_u3t4r:conf.approx_u3t2r;
			}
			else
				if(conf.unary == 4)
				{
					max = (region == 4)?conf.approx_u4t4:conf.approx_u4t2;
				    min = (region == 4)?conf.approx_u4t4r:conf.approx_u4t2r;
				}

		
		if (tconf.unary == 1) {
			tmin = tconf.getDiagonal_right();
			tmax = (region == 2)?tconf.core_right4:tconf.core_right2;
				
		} else if (tconf.unary == 2) 
		{
			tmax = tconf.diagonal_left;
			tmin = tconf.core_left;
		}   else
	    	 if
	    	 (tconf.unary == 3)
	    	 {
	 			tmax = (region == 4)?tconf.approx_u3t2:tconf.approx_u3t4;
			    tmin = (region == 4)?tconf.approx_u3t2r:tconf.approx_u3t4r;
	    	 } 
	    	 else
	    		 if
	    		 (tconf.unary == 4)
		    	 {
		 			tmax = (region == 4)?tconf.approx_u4t2:tconf.approx_u4t4;
				    tmin = (region == 4)?tconf.approx_u4t2r:tconf.approx_u4t4r;
		    	 } 

		MyPolygon _region = new MyPolygon();

		if (conf.unary == 2)
			_region = conf.getRegion(region);
		else
		if (conf.unary == 1)
			_region = conf.getRegionLarge(region);
		else
			if(conf.unary == 3 || conf.unary == 4)
			{
				_region = max;
			}
		/*
		 * tmin should not be in opposite region of test region. e.g. oppsite
		 * region of 1 is 3
		 */

		
		
		if (QuantiShapeCalculator.isIntersected(tmin,
				conf.getRegionLarge((region == 2 ? 4 : 2)), true)
				|| QuantiShapeCalculator.isIntersected(min,
						tconf.getRegionLarge(region), true))
			result = -1;
		
		if (result != -1) {
			if (QuantiShapeCalculator.isIntersected(tmax, _region, true))
			{
				result = minmaxEvaluation(min, max, tmin, tmax);

			} else {
				result = 0;
			}
		}
		return result;

	}
	/* 
  * Massive fixes for FatGSR
  * April 4, correct April 5
  * */
	private static int testFreeRegion13(final Configuration conf,
			final Configuration tconf, int region) {
		 
		int result = 0;
		MyPolygon min = new MyPolygon();
		MyPolygon max = new MyPolygon();
		MyPolygon tmin = new MyPolygon();
		MyPolygon tmax = new MyPolygon();

		if (conf.unary == 1) {
			/*
			 * can not represent the max area using tiny diagonal. we use the
			 * region
			 */
			min = conf.core_right;
			max = conf.getRegion(region);
		}
		else 
			if (conf.unary == 2)
			{
				min = conf.diagonal_left;
				//4.5 comment: max should be trapezoid
				max = (region == 1)? conf.core_left1:conf.core_left3;
	
			}	
			else 
				if (conf.unary == 3)
				{
					max = (region == 1)?conf.approx_u3t1:conf.approx_u3t3;
				    min = (region == 1)?conf.approx_u3t1r:conf.approx_u3t3r;
				}
				else
					if(conf.unary == 4)
					{
						max = (region == 1)?conf.approx_u4t1:conf.approx_u4t3;
					    min = (region == 1)?conf.approx_u4t1r:conf.approx_u4t3r;
					}
		

		if (tconf.unary == 1) 
		{
			tmin = tconf.core_right;
			// because max is a region, we can still use the diagonal here
			tmax = tconf.diagonal_right;
		} else 
		     if
		     (tconf.unary == 2)
		     {
		    	 tmin = tconf.diagonal_left;
		    	 tmax = (region == 3)?tconf.core_left1:tconf.core_left3;

		     } 
		     else
		    	 if
		    	 (tconf.unary == 3)
		    	 {
		 			tmax = (region == 1)?tconf.approx_u3t3:tconf.approx_u3t1;
				    tmin = (region == 1)?tconf.approx_u3t3r:tconf.approx_u3t1r;
		    	 } 
		    	 else
		    		 if
		    		 (tconf.unary == 4)
			    	 {
			 			tmax = (region == 1)?tconf.approx_u4t3:tconf.approx_u4t1;
					    tmin = (region == 1)?tconf.approx_u4t3r:tconf.approx_u4t1r;
			    	 } 

		/* pay attention to touch relationship */
		MyPolygon _region = new MyPolygon();

		//TODO edit to here...
		
		if (conf.unary == 1)
			_region = conf.getRegion(region);
		else if (conf.unary == 2)
			_region = conf.getRegionLarge(region);
		else
			if(conf.unary == 3 || conf.unary == 4)
			{
				_region = max;
			}
		
		/*
		 * tmin should not be in opposite region of test region. e.g. oppisite
		 * region of 1 is 3
		 */
		//System.out.println(QuantiShapeCalculator.isIntersected(tmin,conf.getRegionLarge((region == 1 ? 3 : 1)), true) + "   " + QuantiShapeCalculator.isIntersected(min,tconf.getRegionLarge(region), true));
		if (QuantiShapeCalculator.isIntersected(tmin,conf.getRegionLarge((region == 1 ? 3 : 1)), true)
				|| QuantiShapeCalculator.isIntersected(min,tconf.getRegionLarge(region), true))
			result = -1;

		if (result != -1) {
			
			//System.out.println(tmax + "  " + _region);
			
			if (QuantiShapeCalculator.isIntersected(tmax, _region, true))
			{
				result = minmaxEvaluation(min, max, tmin, tmax);
			

			} else 
			{
				result = 0;
			}
		}

		return result;

	}

	/*
	 * when MAX INTERSECTING MAX, MIN DISJOINT MIN
	 * 
	 * 1: Must be Touching ? 0: Must be Non-Touching 2: Touching or Non-Touching
	 * -1: Invalid. Intersecting
	 */
	private static int minmaxEvaluation(MyPolygon min, MyPolygon max,
			MyPolygon tmin, MyPolygon tmax) {
		int result = 0;
		boolean min_disjoint = false;
		boolean max_intersecting = false;

		/* If bot max are edges, then they could be considered as intersecting. */

		// Debug.echo(null, " in ",
		// max,tmax,QuantiShapeCalculator.isIntersected(max,tmax,true));
		//System.out.println(max + "   " + tmax + "  " + QuantiShapeCalculator.isIntersected(max, tmax, true));
		
		if (!QuantiShapeCalculator.isIntersected(min, tmin, false))
			min_disjoint = true;

		if (QuantiShapeCalculator.isIntersected(max, tmax, true))
			max_intersecting = true;

		// if(max.npoints == 2 && tmax.npoints == 2)
		// max_intersecting = true;
		if (min_disjoint & max_intersecting)
			result = 2; // can be either touching or non-touching
		else if (!max_intersecting)
			result = 0;
		else if (!min_disjoint)
			result = -1;
		return result;
	}

	private static boolean testSolidOverlapping(MyPolygon m, MyPolygon n) {
		return QuantiShapeCalculator.isIntersectedWCA(m, n).size() > 1;

	}
}
