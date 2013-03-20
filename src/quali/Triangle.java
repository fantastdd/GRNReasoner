package quali;

import java.awt.Point;

import common.MyPolygon;

public class Triangle extends MyPolygon{
	
	public Triangle(Point... points){
		super();
		for(int i = 0; i < points.length; i++)
		this.addPoint(points[i].x,points[i].y);
	}

}
