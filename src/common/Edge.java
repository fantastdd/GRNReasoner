package common;

import java.awt.Point;

public class Edge {
public Point startPoint;
public Point endPoint;

/* Support area at left side */
public boolean left = false;


public Edge(Point startPoint, Point endPoint) {
	super();
	this.startPoint = startPoint;
	this.endPoint = endPoint;
}
public Edge(Point startPoint, Point endPoint, boolean left) {
	super();
	this.startPoint = startPoint;
	this.endPoint = endPoint;
	this.left = left;
}
public Point getStartPoint() {
	return startPoint;
}
public void setStartPoint(Point startPoint) {
	this.startPoint = startPoint;
}
public Point getEndPoint() {
	return endPoint;
}
public void setEndPoint(Point endPoint) {
	this.endPoint = endPoint;
}

}
