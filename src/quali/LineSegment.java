package quali;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class LineSegment extends Line2D{
    private Point sp;
    private Point ep;
	public LineSegment(Point sp,Point ep){
		setLine(sp,ep);
        this.sp = sp;
        this.ep = ep;
	}

	@Override
	public Rectangle2D getBounds2D() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Point2D getP1() {
		// TODO Auto-generated method stub
		
		return sp;
	}

	@Override
	public Point2D getP2() {
		// TODO Auto-generated method stub
		return ep;
	}

	@Override
	public double getX1() {
		// TODO Auto-generated method stub
		return sp.x;
	}

	@Override
	public double getX2() {
		// TODO Auto-generated method stub
		return ep.x;
	}

	@Override
	public double getY1() {
		// TODO Auto-generated method stub
		return sp.y;
	}

	@Override
	public double getY2() {
		// TODO Auto-generated method stub
		return ep.y;
	}

	@Override
	public void setLine(double x1, double y1, double x2, double y2) {
	 
		
	}

}
