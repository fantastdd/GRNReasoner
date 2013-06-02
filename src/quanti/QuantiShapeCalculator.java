package quanti;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.Collections;
import java.util.LinkedList;

import quali.Configuration;

import common.MyPolygon;
import common.util.PointXComparator;
import common.util.PointYComparator;

public class QuantiShapeCalculator {

    public LinkedList<Point> FindLineIntersection(Point start1, Point end1,
	    Point start2, Point end2) {

	LinkedList<Point> points = new LinkedList<Point>();
	float denom = ((end1.x - start1.x) * (end2.y - start2.y))
		- ((end1.y - start1.y) * (end2.x - start2.x));

	Line2D.Double ld1 = new Line2D.Double();
	ld1.setLine(start1.x, start1.y, end1.x, end1.y);

	Line2D.Double ld2 = new Line2D.Double();
	ld2.setLine(start2.x, start2.y, end2.x, end2.y);
	// AB & CD are parallel
	if (denom == 0) {
	    points.add(start1);
	    points.add(start2);
	    points.add(end1);
	    points.add(end2);
	    if (ld1.intersectsLine(ld2)) {
		if (start1.y == end1.y && start1.y == end2.y)
		    Collections.sort(points, new PointXComparator());
		else
		    Collections.sort(points, new PointYComparator());

		points.pollFirst();
		points.pollLast();
	    } else {
		points.clear();
	    }

	} else {

	    float numer = ((start1.y - start2.y) * (end2.x - start2.x))
		    - ((start1.x - start2.x) * (end2.y - start2.y));

	    float r = numer / denom;

	    float numer2 = ((start1.y - start2.y) * (end1.x - start1.x))
		    - ((start1.x - start2.x) * (end1.y - start1.y));

	    float s = numer2 / denom;

	    if ((r < 0 || r > 1) || (s < 0 || s > 1))
		points.clear();
	    else
	    // Find intersection point
	    {
		Point result = new Point();
		result.x = (int) (start1.x + (r * (end1.x - start1.x)));
		result.y = (int) (start1.y + (r * (end1.y - start1.y)));
		points.add(result);

	    }
	}
	// System.out.println(points.size());
	return points;
    }

    public LinkedList<Point> isIntersected(Rectangle r1, Rectangle r2) {
	Polygon p1 = new Polygon();
	p1.addPoint(r1.x, r1.y);
	p1.addPoint(r1.x, r1.y + r1.height);
	p1.addPoint(r1.x + r1.width, r1.y + r1.height);
	p1.addPoint(r1.x + r1.width, r1.y);

	Polygon p2 = new Polygon();
	p2.addPoint(r2.x, r2.y);
	p2.addPoint(r2.x, r2.y + r2.height);
	p2.addPoint(r2.x + r2.width, r2.y + r2.height);
	p2.addPoint(r2.x + r2.width, r2.y);

	return isIntersectedWCA(p1, p2);
    }

    public boolean isIntersected(Rectangle r1, Rectangle r2,
	    boolean includeTouch) {
	Polygon p1 = new Polygon();
	p1.addPoint(r1.x, r1.y);
	p1.addPoint(r1.x, r1.y + r1.height);
	p1.addPoint(r1.x + r1.width, r1.y + r1.height);
	p1.addPoint(r1.x + r1.width, r1.y);

	Polygon p2 = new Polygon();
	p2.addPoint(r2.x, r2.y);
	p2.addPoint(r2.x, r2.y + r2.height);
	p2.addPoint(r2.x + r2.width, r2.y + r2.height);
	p2.addPoint(r2.x + r2.width, r2.y);

	return isIntersected(p1, p2, includeTouch);
    }

    // isIntersectedWCA = is intersected without containing/contained
    // adjudgement
    public LinkedList<Point> isIntersectedWCA(Polygon p1, Polygon p2) {
	LinkedList<Point> points = new LinkedList<Point>();
	// test the intersecting case
	for (int i = 0; i < p1.npoints; i++)
	    for (int j = 0; j < p2.npoints; j++) {

		LinkedList<Point> _points = FindLineIntersection(new Point(
			p1.xpoints[i], p1.ypoints[i]), new Point(
			p1.xpoints[(i + 1 >= p1.npoints) ? 0 : i + 1],
			p1.ypoints[(i + 1 >= p1.npoints) ? 0 : i + 1]),
			new Point(p2.xpoints[j], p2.ypoints[j]), new Point(
				p2.xpoints[(j + 1 >= p2.npoints) ? 0 : j + 1],
				p2.ypoints[(j + 1 >= p2.npoints) ? 0 : j + 1]));

		for (Point p : _points) {
		    if (!points.contains(p))
			points.add(p);
		}
		// Debug.echo(null ,p1," and ", p2, "\n Intersects at : ",p);

	    }

	return points;
    }

    private static boolean testCornerPoint(Polygon p, int x, int y) {
	for (int i = 0; i < p.npoints; i++) {
	    if (p.xpoints[i] == x && p.ypoints[i] == y)
		return true;
	}
	return false;

    }

    /*
     * including touch or not. touched/intersected, including "contain"
     * relationship touch = true: if touch/overlapping/contertained, return
     * true. TODO Important: when one of the pologons is a single point, the
     * isIntersectedWCA(p1,p2) will return true;
     */
    public boolean isIntersected(Polygon p1, Polygon p2, boolean touch) {
	boolean result = false;
	boolean containing = false;
	boolean contained = false;
	if (touch) {
	    result = /* overlapping */!isIntersectedWCA(p1, p2).isEmpty();

	}

	else {

	    LinkedList<Point> points = isIntersectedWCA(p1, p2);
	    if (!points.isEmpty()) {
		if (points.size() > 2) {
		    result = true;
		} else {
		    result = false;
		    for (Point p : points) {
			if (!(testCornerPoint(p1, p.x, p.y) || testCornerPoint(
				p2, p.x, p.y)) /* predicular */)
			    result = true;
		    }
		}
	    } else
		result = false;

	}
	/* contained relationship */
	// NOTE: either p1 contains p2 or p2 contains p1. assuming each poly has
	// only four points. Contain only return the points which are inside the
	// polygon. Those on the edge will not be considered
	int count = 0;
	for (int i = 0; i < p1.npoints; i++) {
	    int[] x = new int[1];
	    x[0] = p1.xpoints[i];
	    int[] y = new int[1];
	    y[0] = p1.ypoints[i];
	    if (p2.contains(new Point(p1.xpoints[i], p1.ypoints[i]))
	    // check boundary
		    || (p2.npoints > 1 && !isIntersectedWCA(p2,
			    new Polygon(x, y, 1)).isEmpty())) {
		/*
		 * System.out.println("go into this loop");
		 * System.out.println(p2.getBounds());
		 * System.out.println(p1.xpoints[i] + "    " + p1.ypoints[i]);
		 * System.out.println(!isIntersectedWCA(p2, new
		 * Polygon(x,y,1)).isEmpty() + "    " + isIntersectedWCA(p2, new
		 * Polygon(x,y,1)));
		 */
		// contained = true;
		count++;
	    }
	}
	contained = (count == p1.npoints && p1.npoints > 0) ? true : false;
	count = 0;
	/* containing relationship */
	for (int i = 0; i < p2.npoints; i++) {
	    int[] x = new int[1];
	    x[0] = p2.xpoints[i];
	    int[] y = new int[1];
	    y[0] = p2.ypoints[i];
	    if (p1.contains(new Point(p2.xpoints[i], p2.ypoints[i]))
		    || (p1.npoints > 1 && !isIntersectedWCA(p1,
			    new Polygon(x, y, 1)).isEmpty())) {
		/*
		 * System.out.println("go into containing loop");
		 * System.out.println(p2.getBounds());
		 * System.out.println(p1.xpoints[i] + "    " + p1.ypoints[i]);
		 * System.out.println(!isIntersectedWCA(p2, new
		 * Polygon(x,y,1)).isEmpty() + "    " + isIntersectedWCA(p2, new
		 * Polygon(x,y,1)));
		 */

		count++;

	    }
	}
	containing = (count == p2.npoints && p2.npoints > 0) ? true : false;
	// System.out.println(result + "   " + contained + "   " + containing);
	result |= contained || containing;

	return result;

    }

    // TODO fix it later
    public boolean potentialEdgeTouch(Configuration conf, Configuration tconf) {
	boolean result = false;
	// when to bounding boxes just touch each other, then there wont be an
	// edge touch

	if (conf.unary < 3) {
	    if (tconf.unary < 3) {
		// print ======

		/*
		 * if(conf.getMbr().getId() == 9 && conf.unary == 2 &&
		 * tconf.getMbr().getId() == 6 && tconf.unary == 2)
		 * System.out.println
		 * ((double)conf.limit_horizontal/(double)conf.limit_vertical +
		 * "   " +
		 * (double)tconf.limit_horizontal/(double)tconf.limit_vertical);
		 */
		// print end ====
		double d2 = (double) conf.width
			/ (double) (conf.height - conf.limit_vertical);
		double d1 = (double) (conf.width - conf.limit_horizontal)
			/ (double) (conf.height);

		double td2 = (double) (tconf.width)
			/ (double) (tconf.height - tconf.limit_vertical);
		double td1 = (double) (tconf.width - tconf.limit_horizontal)
			/ (double) (tconf.height);
		/*
		 * if(conf.unary == 1 && tconf.unary == 2 &&
		 * conf.getMbr().getId() == 20 && tconf.getMbr().getId() == 19)
		 * System.out.println(d1 + "  " + d2 + "  " + td1 + "  " + td2 +
		 * "   ");
		 */
		if ((d1 <= td2 && d1 >= td1) || (d2 >= td1 && d2 <= td2))
		    result = true;
		// result = (conf.limit_horizontal/conf.limit_vertical) ==
		// (tconf.limit_horizontal/tconf.limit_vertical);
	    } else if (tconf.unary == 3) {
		double d2 = (double) (conf.width)
			/ (double) (conf.height - conf.limit_vertical);
		// double d1 = (double)(conf.width - conf.limit_horizontal) /
		// (double)(conf.height);

		double td1 = (double) (tconf.height - tconf.limit_vertical)
			/ (double) tconf.limit_horizontal;
		// double td2 = (double) tconf.height /(double)
		// tconf.limit_horizontal;// td2 is infinitely large

		if (d2 >= td1)
		    result = true;
		// return false;
	    } else

	    {
		double d2 = (double) (conf.width)
			/ (double) (conf.height - conf.limit_vertical);
		// double d1 = (double)(conf.width - conf.limit_horizontal) /
		// (double)(conf.height);

		double td1 = (double) (tconf.width - tconf.limit_vertical)
			/ (double) tconf.limit_vertical;
		// double td2 = (double)tconf.limit_vertical/(double)(
		// tconf.width - tconf.limit_horizontal);
		if (d2 >= td1)
		    result = true;
		// return false;
	    }
	} else {
	    if (tconf.unary < 3) {

		if (conf.unary == 3) {

		    // double d1 = (double)tconf.limit_vertical /
		    // (double)tconf.limit_horizontal;
		    double d2 = (double) (tconf.width)
			    / (double) (tconf.height - tconf.limit_vertical);
		    // double d1 = (double)(tconf.width -
		    // tconf.limit_horizontal) / (double)(tconf.height);

		    double td1 = (double) (conf.height - conf.limit_vertical)
			    / (double) conf.limit_horizontal;
		    // double td2 = (double) conf.height /(double)
		    // conf.limit_horizontal;
		    /*
		     * if(conf.unary == 3 && tconf.unary == 1 &&
		     * conf.getMbr().getId() == 17&& tconf.getMbr().getId() ==
		     * 16) System.out.println(tconf.limit_vertical + "  " +
		     * tconf.limit_horizontal + "  " + d1 + "  " + d2 + "  "
		     * +td1 + " " + td2);
		     */
		    if (d2 >= td1)
			result = true;
		} else {

		    double d2 = (double) (tconf.width)
			    / (double) (tconf.height - tconf.limit_vertical);
		    // double d1 = (double)(tconf.width -
		    // tconf.limit_horizontal) / (double)(tconf.height);

		    double td1 = (double) (conf.width - conf.limit_vertical)
			    / (double) conf.limit_vertical;
		    // double td2 = (double)conf.limit_vertical/(double)(
		    // conf.width - conf.limit_horizontal);
		    if (d2 >= td1)
			result = true;
		}
	    } else {
		// TODO fixed later
		// return true;
		if (conf.unary == 3) {
		    /*
		     * double u3d1 = (double)( conf.height -
		     * conf.limit_vertical) / (double)conf.limit_horizontal;
		     * double u3d2 = (double) conf.height /(double)
		     * conf.limit_horizontal;
		     */
		    if (tconf.unary == 3) {
			/*
			 * double td1 = (double)( tconf.height -
			 * tconf.limit_vertical) /
			 * (double)tconf.limit_horizontal; double td2 = (double)
			 * tconf.height /(double) tconf.limit_horizontal;
			 * 
			 * if( ( u3d1 <= td2 && u3d1 >= td1 ) || (u3d2 >= td1 &&
			 * u3d2 <= td2)) result = true;
			 */
			result = true;

		    } else {
			/*
			 * double td1 = (double)tconf.limit_vertical /
			 * (double)tconf.width; double td2 =
			 * (double)tconf.limit_vertical/(double)( tconf.width -
			 * tconf.limit_horizontal); if( ( u3d1 <= td2 && u3d1 >=
			 * td1 ) || (u3d2 >= td1 && u3d2 <= td2)) result = true;
			 */
			result = false;
		    }

		} else {
		    double u4d1 = (double) conf.limit_vertical
			    / (double) conf.width;
		    double u4d2 = (double) conf.limit_vertical
			    / (double) (conf.width - conf.limit_horizontal);
		    if (tconf.unary == 3) {
			/*
			 * double td1 = (double)( tconf.height -
			 * tconf.limit_vertical) /
			 * (double)tconf.limit_horizontal; double td2 = (double)
			 * tconf.height /(double) tconf.limit_horizontal;
			 * 
			 * if( ( u4d1 <= td2 && u4d1 >= td1 ) || (u4d2 >= td1 &&
			 * u4d2 <= td2)) result = true;
			 */
			result = false;

		    } else {
			/*
			 * double td1 = (double)tconf.limit_vertical /
			 * (double)tconf.width; double td2 =
			 * (double)tconf.limit_vertical/(double)( tconf.width -
			 * tconf.limit_horizontal); if( ( u4d1 <= td2 && u4d1 >=
			 * td1 ) || (u4d2 >= td1 && u4d2 <= td2))
			 */
			result = true;
		    }
		}

	    }

	}
	return result;
    }


    public static void main(String args[]) {
	/*
	 * MyPolygon mp1 = new MyPolygon(); mp1.addPoint(10, 10);
	 * mp1.addPoint(10,12); mp1.addPoint(12,12); mp1.addPoint(12,10);
	 * 
	 * 
	 * MyPolygon mp2 = new MyPolygon(); mp2.addPoint(12, 10);
	 * mp2.addPoint(12,14); mp2.addPoint(13,14); mp2.addPoint(13,10);
	 * 
	 * MyPolygon mp3 = new MyPolygon(); mp3.addPoint(12, 10);
	 * mp3.addPoint(12,7);
	 * System.out.println(QuantiShapeCalculator.isIntersected(mp3, mp2));
	 */

	MyPolygon m1 = new MyPolygon();
	m1.addPoint(200, 150);
	m1.addPoint(250, 200);
	m1.addPoint(300, 150);
	m1.addPoint(250, 100);

	MyPolygon m2 = new MyPolygon();
	m2.addPoint(200, 200);
	m2.addPoint(200, 210);
	m2.addPoint(1200, 210);
	m2.addPoint(1200, 200);

	MyPolygon m3 = new MyPolygon();
	m3.addPoint(300, 201);
	m3.addPoint(500, 201);

	MyPolygon m4 = new MyPolygon();
	m4.addPoint(286, 400);
	m4.addPoint(210, 530);
	m4.addPoint(434, 660);
	m4.addPoint(510, 530);

	MyPolygon m5 = new MyPolygon();
	m5.addPoint(300, 480);
	m5.addPoint(260, 480);

	MyPolygon m6 = new MyPolygon();
	m6.addPoint(286, 400);
	m6.addPoint(286, 400);

	/*
	 * MyPolygon m3 = new MyPolygon(); m2.addPoint(200, 100);
	 * m2.addPoint(200, 125);
	 * 
	 * Line2D.Double ld1 = new Line2D.Double(); ld1.setLine(200, 100, 200,
	 * 150);
	 * 
	 * Line2D.Double ld2 = new Line2D.Double(); ld2.setLine(200, 100, 300,
	 * 100);
	 * 
	 * System.out.println(QuantiShapeCalculator.FindLineIntersection(new
	 * Point(200,100), new Point(200,150), new Point(200,100), new
	 * Point(200,130)));
	 */
	// System.out.println(QuantiShapeCalculator.isIntersected(m4, m5));
	// System.out.println(QuantiShapeCalculator.isIntersectedWCA(m4,m6));
	System.out.println(m1.contains(new Point(300, 480)));
	/*
	 * LinkedList<Integer> test1 = new LinkedList<Integer>(); test1.add(1);
	 * 
	 * LinkedList<Integer> test2 = new LinkedList<Integer>();
	 * test2.addAll(test1); test1.clear(); System.out.println(test2);
	 */

    }

}
