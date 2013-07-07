package common;

import java.awt.Point;

public class Edge {
public Point startPoint;
public Point endPoint;


public Edge(Point startPoint, Point endPoint) {
	super();
	this.startPoint = startPoint;
	this.endPoint = endPoint;
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
