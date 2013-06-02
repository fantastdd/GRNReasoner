package quali;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import quanti.QuantiShapeCalculator;
import ab.WorldinVision;

import common.util.MBRYComparator;
import common.util.NeighborComparatorByMBRID;

public class Node {

    private HashMap<Integer, Configuration> confs;
    //TODO conflist is not clonned.
    public LinkedList<Configuration> conflist;
    public QuantiShapeCalculator quantiShapeCalculator = new QuantiShapeCalculator();
    private int count_id = -1;

    // the pointer points to the next configuration (pop())
    public int current_id = -1;

    // the pointer points to the next stability verification conf
    public int[] stability_id;

    // 0-i confs have been instantiated
    public boolean instaniatedUntilIndex(int i) {
	return i <= current_id;
    }

    public Node() {
	confs = new HashMap<Integer, Configuration>();
	conflist = new LinkedList<Configuration>();

    }

    public Node(MBR[] mbrs, List<MBR> edges) {
	confs = new HashMap<Integer, Configuration>();

	conflist = new LinkedList<Configuration>();

	int counter = 0;
	for (MBR mbr : mbrs) {
	    MBR _mbr = mbr.clone();
	    Configuration conf = new Configuration(_mbr);
	    if (edges != null && edges.contains(_mbr))
		conf.setEdge(true);
	    _mbr.setId(counter++);
	    confs.put(_mbr.getId(), conf);
	    conflist.add(conf);

	}

	initialize();
	// initializeVO();
	// initializeHW();
    }

    public Configuration lookup(int id) {
	return confs.get(id);
    }

    public Configuration lookup(MBR mbr) {
	return lookup(mbr.getId());
    }

    public Configuration lookupByUID(int uid) {
	
	for (int key : confs.keySet())
	{
	    Configuration conf = confs.get(key);
	    if(conf.getMbr().uid == uid)
		return conf;
	}
	return null;
    }

    public Configuration pop() {
	if (++current_id < confs.size())
	    return confs.get(current_id);
	else
	    return null;
    }

    public boolean isCompleted() {
	return current_id >= confs.size() - 1;
    }

    private Configuration lookup(Neighbor neighbor) {
	for (Configuration conf : conflist) {
	    if (conf.getMbr().equals(neighbor)) {
		return conf;
	    }
	}
	return null;
    }

    private LinkedList<Configuration> generateId(
	    LinkedList<Configuration> expand_conf) {
	if (expand_conf.isEmpty()) {
	    return expand_conf;
	}
	LinkedList<Configuration> newConfs = new LinkedList<Configuration>();
	newConfs.addAll(expand_conf);
	expand_conf.clear();
	for (Configuration conf : newConfs) {
	    if (!conf.isEdge()) {
		if (conf.getMbr().getId() == -1) {
		    conf.getMbr().setId(++count_id);

		    for (Neighbor neighbor : conf.getNeighbors()) {
			expand_conf.add(lookup(neighbor));
			/*
			 * MBR mbr = neighbor.getMbr(); if(mbr.getId() == -1) {
			 * mbr.setId(++count_id); ++count_assigned; }
			 */
		    }
		}
	    }
	}
	generateId(expand_conf);
	return expand_conf;
    }

    // Initialise using Variable Ordering
    public void initializeHW() {
	for (int i = 0; i < conflist.size(); i++) {
	    Configuration conf = conflist.get(i);
	    MBR mbr = conf.getMbr();
	    mbr.setId(-1);
	    for (int j = 0; j < conflist.size(); j++) {
		if (i != j) {
		    Configuration _conf = conflist.get(j);
		    MBR mbr1 = _conf.getMbr();
		    // System.out.println(" test  " + mbr + "   "+ mbr1 );
		    if (quantiShapeCalculator.isIntersected(mbr, mbr1, false)) {

			Neighbor neighbor = new Neighbor(mbr1, (byte) 0, 0);

			conf.getNeighbors().add(neighbor);

			// System.out.println(" trigger the containing case" +
			// mbr + "  " + mbr1);
		    } else {
			Neighbor _neighbor = createNeighbor(mbr, mbr1);
			// System.out.println(mbr + "  construct  " + mbr1 +
			// "   " + _neighbor.getGap() + "   " +
			// _neighbor.getNeighborType());
			if (_neighbor != null
				&& (_neighbor.getNeighborType() == 0 || _neighbor
					.getGap() == 0))
			    conf.getNeighbors().add(_neighbor);
		    }
		}
	    }

	    // ========================== Early Determination: Those who do not
	    // have neighbors from region 3 will be considered to be regular
	    // ======================

	    {
		if (conf.getNeighbors().isEmpty())
		    conf.setEdge(true);
		else {
		    int count = 0;
		    for (Neighbor neighbor : conf.getNeighbors()) {
			if (neighbor.getNeighborType() == 3
				|| (neighbor.getNeighborType() == 0 && (conf.y + conf.height) < neighbor
					.getMbr().getHeight()
					+ neighbor.getMbr().getY()))
			    count++;
		    }
		    if (count == 0)
			conf.setEdge(true);
		}
	    }

	}

	// ======================= Variable Ordering
	// ===================================================

	Collections.sort(conflist, new MBRYComparator());
	for (Configuration conf : conflist) {
	    if (conf.isEdge()) {
		conf.getMbr().setId(++count_id);
	    }
	}
	for (Configuration conf : conflist) {
	    if (conf.getMbr().getId() == -1) {
		double r1 = (double) conf.width / (double) conf.height;
		double r2 = (double) conf.height / (double) conf.width;
		if (r1 > 4 || r2 > 4)
		    conf.getMbr().setId(++count_id);
	    }
	}
	for (Configuration conf : conflist) {
	    if (conf.getMbr().getId() == -1) {
		int area = conf.getHeight() * conf.getWidth();
		if (area > 8000)
		    conf.getMbr().setId(++count_id);
	    }
	}
	/*
	 * for (Configuration conf : conflist) { if(conf.getMbr().getId() == -1)
	 * { double r1 = (double)conf.width/(double)conf.height; double r2 =
	 * (double)conf.height/(double)conf.width; if( Math.abs(r1- 1) < 0.5 ||
	 * Math.abs(r2 - 1) < 0.5) conf.getMbr().setId(++count_id); } }
	 */

	for (Configuration conf : conflist) {
	    LinkedList<Configuration> confs = new LinkedList<Configuration>();
	    confs.add(conf);
	    generateId(confs);
	}

	// ========================== Ordering End
	// ==============================================
	for (Configuration conf : conflist) {
	    Collections.sort(conf.getNeighbors(),
		    new NeighborComparatorByMBRID());

	    for (Neighbor neighbor : conf.getNeighbors()) {
		if (neighbor.getGap() > WorldinVision.gap) {
		    conf.lastValidNeighborId = conf.getNeighbors().indexOf(
			    neighbor) - 1;
		    break;
		} else {
		    // initialize the contact map
		    conf.getContact_map().put(neighbor.getMbr().getId(),
			    new Contact());
		    // System.out.println(conf.getMbr() + "   " +
		    // neighbor.getMbr());
		}
	    }
	    // the mbr touches all others.
	    if (conf.lastValidNeighborId == -2
		    && !conf.getNeighbors().isEmpty())
		// conf.lastValidNeighborId = conf.getNeighbors().size() - 1;
		conf.lastValidNeighborId = conf.getNeighbors().getLast()
			.getMbr().getId();

	    // =====================DEBUG output the neighbor
	    {
		System.out.println(conf.getMbr() + "  "
			+ conf.getMbr().getBounds()
			+ "  lastValid Neighbor id :  "
			+ conf.lastValidNeighborId + "     edge:  "
			+ conf.isEdge() + "  unary " + conf.unary + "h/w: "
			+ ((double) conf.height / (double) conf.width) + "w/h"
			+ ((double) conf.width / (double) conf.height));
		for (Neighbor neighbor : conf.getNeighbors()) {

		    System.out.println("    " + neighbor.getMbr() + "    "
			    + neighbor.getGap() + "   index:  "
			    + conf.getNeighbors().indexOf(neighbor.getMbr())
			    + " neighbor type:  " + neighbor.getNeighborType());

		}

	    }
	    // ===========================
	}

	// Initialise the array of stability id
	stability_id = new int[conflist.size()];
	for (int i = 0; i < conflist.size(); i++) {
	    Configuration conf = conflist.get(i);
	    if (conf.isEdge())
		stability_id[i] = 1;
	    else
		stability_id[i] = 0;

	    confs.put(conf.getMbr().getId(), conf);
	}

    }

    // Initialise using Variable Ordering. From lowest blocks to higher
    public void initializeVO() {
	for (int i = 0; i < conflist.size(); i++) {
	    Configuration conf = conflist.get(i);
	    MBR mbr = conf.getMbr();
	    mbr.setId(-1);
	    for (int j = 0; j < conflist.size(); j++) {
		if (i != j) {
		    Configuration _conf = conflist.get(j);
		    MBR mbr1 = _conf.getMbr();
		    // System.out.println(" test  " + mbr + "   "+ mbr1 );
		    if (quantiShapeCalculator.isIntersected(mbr, mbr1, false)) {

			Neighbor neighbor = new Neighbor(mbr1, (byte) 0, 0);

			conf.getNeighbors().add(neighbor);

			// System.out.println(" trigger the containing case" +
			// mbr + "  " + mbr1);
		    } else {
			Neighbor _neighbor = createNeighbor(mbr, mbr1);
			// System.out.println(mbr + "  construct  " + mbr1 +
			// "   " + _neighbor.getGap() + "   " +
			// _neighbor.getNeighborType());
			if (_neighbor != null
				&& (_neighbor.getNeighborType() == 0 || _neighbor
					.getGap() <= WorldinVision.gap))
			    conf.getNeighbors().add(_neighbor);
		    }
		}
	    }

	    // ========================== Early Determination: Those who do not
	    // have neighbors from region 3 will be considered to be regular
	    // ======================

	    {
		if (conf.getNeighbors().isEmpty())
		    conf.setEdge(true);
		else {
		    int count = 0;
		    for (Neighbor neighbor : conf.getNeighbors()) {
			if (neighbor.getNeighborType() == 3
				|| (neighbor.getNeighborType() == 0 && (conf.y + conf.height) < neighbor
					.getMbr().getHeight()
					+ neighbor.getMbr().getY()))
			    count++;
		    }
		    if (count == 0)
			conf.setEdge(true);
		}
	    }

	}

	// ======================= Variable Ordering
	// ===================================================

	Collections.sort(conflist, new MBRYComparator());
	for (Configuration conf : conflist) {
	    if (conf.isEdge()) {
		conf.getMbr().setId(++count_id);

	    }
	}

	for (Configuration conf : conflist) {
	    LinkedList<Configuration> confs = new LinkedList<Configuration>();
	    confs.add(conf);
	    generateId(confs);
	}

	/*
	 * for(Configuration conf:conflist) { if(!conf.isEdge()) {
	 * if(conf.getMbr().getId() == -1) { conf.getMbr().setId(++count_id);
	 * for (Neighbor neighbor: conf.getNeighbors()) { MBR mbr =
	 * neighbor.getMbr(); if(mbr.getId() == -1) mbr.setId(++count_id); } } }
	 * }
	 */
	for (Configuration conf : conflist) {
	    Collections.sort(conf.getNeighbors(),
		    new NeighborComparatorByMBRID());

	    for (Neighbor neighbor : conf.getNeighbors()) {
		if (neighbor.getGap() > WorldinVision.gap) {
		    conf.lastValidNeighborId = conf.getNeighbors().indexOf(
			    neighbor) - 1;
		    break;
		} else {
		    // initialize the contact map
		    conf.getContact_map().put(neighbor.getMbr().getId(),
			    new Contact());
		    // System.out.println(conf.getMbr() + "   " +
		    // neighbor.getMbr());
		}
	    }
	    // the mbr touches all others.
	    if (conf.lastValidNeighborId == -2
		    && !conf.getNeighbors().isEmpty())
		conf.lastValidNeighborId = conf.getNeighbors().getLast()
			.getMbr().getId();

	    // =====================DEBUG output the neighbor
	    {
		System.out.println(conf.getMbr() + "  "
			+ conf.getMbr().getBounds()
			+ "  lastValid Neighbor id :  "
			+ conf.lastValidNeighborId + "     edge:  "
			+ conf.isEdge() + "  unary " + conf.unary + "h/w: "
			+ ((double) conf.height / (double) conf.width) + "w/h"
			+ ((double) conf.width / (double) conf.height));
		for (Neighbor neighbor : conf.getNeighbors()) {

		    System.out.println("    " + neighbor.getMbr() + "    "
			    + neighbor.getGap() + "   index:  "
			    + conf.getNeighbors().indexOf(neighbor.getMbr())
			    + " neighbor type:  " + neighbor.getNeighborType());

		}

	    }
	    // ===========================
	}

	// Initialize the array of stability id
	stability_id = new int[conflist.size()];
	for (int i = 0; i < conflist.size(); i++) {
	    Configuration conf = conflist.get(i);
	    if (conf.isEdge())
		stability_id[i] = 1;
	    else
		stability_id[i] = 0;

	    confs.put(conf.getMbr().getId(), conf);
	}

    }

    // test the overlapping blocks
    public void initialize() {

	// Initialize Stability id array
	stability_id = new int[confs.size()];
	for (int i = 0; i < confs.size(); i++) {
	    stability_id[i] = 0;
	}

	for (int i = 0; i < confs.size(); i++) {
	    Configuration conf = lookup(i);
	    MBR mbr = conf.getMbr();
	    for (int j = 0; j < confs.size(); j++) {
		if (i != j) {
		    Configuration _conf = lookup(j);
		    MBR mbr1 = _conf.mbr;

		    if (quantiShapeCalculator.isIntersected(mbr, mbr1, false)) {

			conf.getContact_map().put(mbr1.getId(), new Contact());
			Neighbor neighbor = new Neighbor(mbr1, (byte) 0, 0);
			conf.getNeighbors().add(neighbor);

			// System.out.println(" trigger the containing case" +
			// mbr + "  " + mbr1);
		    } else {
			Neighbor _neighbor = createNeighbor(mbr, mbr1);
			// System.out.println(mbr + "  construct  " + mbr1 +
			// "   " + _neighbor.getGap() + "   " +
			// _neighbor.getNeighborType());

			// if( _neighbor != null &&( _neighbor.getNeighborType()
			// == 0 || _neighbor.getGap() == 0))
			if (_neighbor != null
				&& (_neighbor.getNeighborType() == 0 || _neighbor
					.getGap() <= WorldinVision.gap))
			    conf.getNeighbors().add(_neighbor);
		    }
		}
	    }

	    // sort the neighbors according the gap in between in ascending
	    // order.
	    // Collections.sort(conf.getNeighbors(), new NeighborComparator());

	    // sort the neighbors according the mbr id in between in ascending
	    // order.
	    Collections.sort(conf.getNeighbors(),
		    new NeighborComparatorByMBRID());

	    for (Neighbor neighbor : conf.getNeighbors()) {
		if (neighbor.getGap() > WorldinVision.gap) {
		    conf.lastValidNeighborId = conf.getNeighbors().indexOf(
			    neighbor) - 1;
		    break;
		} else {
		    // initialize the contact map
		    conf.getContact_map().put(neighbor.getMbr().getId(),
			    new Contact());
		    // System.out.println(conf.getMbr() + "   " +
		    // neighbor.getMbr());
		}
	    }
	    // the mbr touches all others.
	    if (conf.lastValidNeighborId == -2
		    && !conf.getNeighbors().isEmpty())
		// conf.lastValidNeighborId = conf.getNeighbors().size() - 1;
		conf.lastValidNeighborId = conf.getNeighbors().getLast()
			.getMbr().getId();

	    // ========================== Early Determination: Those who do not
	    // have neighbors from region 3 will be considered to be regular
	    // ======================

	    {
		if (conf.lastValidNeighborId == -2)
		    conf.setEdge(true);
		else {
		    int count = 0;
		    for (Neighbor neighbor : conf.getNeighbors()) {
			if ((neighbor.getNeighborType() == 3)
				|| /*
				    * ( neighbor.getNeighborType() == 0 &&
				    * (conf.y + conf.height) <
				    * neighbor.getMbr().getHeight() +
				    * neighbor.getMbr().getY())
				    */
				(neighbor.getNeighborType() == 0 && (conf.y + conf.height / 2) < neighbor
					.getMbr().y + neighbor.getMbr().height))
			    count++;
		    }
		    if (count == 0) {
			conf.setEdge(true);

		    }

		}
	    }

	    // =====================DEBUG output the neighbor
	    {
		System.out.println(conf.getMbr() + "  "
			+ conf.getMbr().getBounds()
			+ "  lastValid Neighbor id :  "
			+ conf.lastValidNeighborId + "     edge:  "
			+ conf.isEdge() + "  unary " + conf.unary + " h/w: "
			+ ((double) conf.height / (double) conf.width) + " w/h"
			+ ((double) conf.width / (double) conf.height)
			+ "  area " + conf.width * conf.height);
		for (Neighbor neighbor : conf.getNeighbors()) {

		    System.out.println("    " + neighbor.getMbr() + "    "
			    + neighbor.getGap() + "   index:  "
			    + conf.getNeighbors().indexOf(neighbor.getMbr())
			    + " neighbor type:  " + neighbor.getNeighborType());

		}

	    }
	    // ===========================

	    // System.out.println("  conf " + conf.getMbr() + "   " +
	    // " last valid id:  " + conf.lastValidNeighborId);

	}

    }

    private Neighbor createNeighbor(MBR pmbr, MBR rmbr) {

	double r_x = rmbr.getX();
	double r_y = rmbr.getY();
	double r_mx = rmbr.getX() + rmbr.getWidth();
	double r_my = rmbr.getY() + rmbr.getHeight();

	double x = pmbr.getX();
	double y = pmbr.getY();
	double mx = pmbr.getX() + pmbr.getWidth();
	double my = pmbr.getY() + pmbr.getHeight();

	Neighbor neighbor = null;
	// System.out.println("   go to here " + (r_my > y || r_y < my) );

	if (r_x >= mx && r_my > y && r_y < my) {
	    // in the view region 4.
	    neighbor = new Neighbor(rmbr, (byte) 4, r_x - mx);
	} else if (r_mx <= x && r_my > y && r_y < my) {
	    // in the view region 2
	    neighbor = new Neighbor(rmbr, (byte) 2, x - r_mx);

	} else if (r_y >= my && r_x < mx && r_mx > x)
	    // in the view region 3
	    neighbor = new Neighbor(rmbr, (byte) 3, r_y - my);
	else if (r_my <= y && r_x < mx && r_mx > x)
	// in the view region 1
	{
	    neighbor = new Neighbor(rmbr, (byte) 1, y - r_my);
	}

	return neighbor;

    }

    public HashMap<Integer, Configuration> getConfs() {
	return confs;
    }

    @Override
    public Node clone() {
	Node _node = new Node();
	// clone hash map
	for (Integer key : confs.keySet()) {
	    _node.getConfs().put(key, confs.get(key).clone());
	}
	_node.current_id = current_id;

	_node.stability_id = new int[confs.size()];
	System.arraycopy(stability_id, 0, _node.stability_id, 0, confs.size());

	return _node;
    }

    public void update(Configuration newlyUpdatedConf) {

	confs.put(newlyUpdatedConf.getMbr().getId(), newlyUpdatedConf);
	// also update the contact map if necessary
	for (Integer neighbor_mbrid : newlyUpdatedConf.getContact_map()
		.keySet()) {

	    Configuration neighbor_conf = lookup(neighbor_mbrid);

	    HashMap<Integer, Contact> neighbor_contactMap = neighbor_conf
		    .getContact_map();

	    Integer mbr = newlyUpdatedConf.getMbr().getId();

	    ContactManager cm = new ContactManager();
	    if (neighbor_contactMap.containsKey(mbr)) {
		Contact contact = cm.getPairContact(newlyUpdatedConf,
			neighbor_conf, WorldinVision.gap);
		neighbor_contactMap.put(mbr, contact);
		/*
		 * System.out.println( neighbor_mbrid +
		 * " updates   the contact map using  " + mbr + "    " +
		 * newlyUpdatedConf.getContact_map().get(neighbor_mbrid) +
		 * " on  " + contact + contact.points[0]);
		 */
	    }

	}
    }

    public void updateConf(Configuration newlyUpdatedConf) {

	confs.put(newlyUpdatedConf.getMbr().getId(), newlyUpdatedConf);
	// also update the contact map if necessary

    }

    @Override
    public String toString() {
	String result = "================= The Configurations  ============\n";
	for (Integer id : confs.keySet()) {
	    result += confs.get(id) + "\n";

	}
	result += "================= End  =============\n";
	return result;

    }
}
