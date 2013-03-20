package action;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JFrame;

import quali.RecEssentialPoints;


public class MouseController {
private MyMouseAdapter ma;
private MyMouseMotionAdapter mma;
private MyMouseAdapterSelfPaint masp;
private int startX;
private int startY;
private RecEssentialPoints essentialPoints;
private boolean isDragged = false;
public boolean isDragged() {
	return isDragged;
}

public void setDragged(boolean isDragged) {
	this.isDragged = isDragged;
}

public MouseController(JFrame frame)
{
   ma = new MyMouseAdapter(frame,this);
   mma = new MyMouseMotionAdapter(frame,this);
   masp = new MyMouseAdapterSelfPaint(frame,this);
   essentialPoints = new RecEssentialPoints(5);

}

public RecEssentialPoints getEssentialPoints() {
	return essentialPoints;
}

public void setEssentialPoints(RecEssentialPoints essentialPoints) {
	this.essentialPoints = essentialPoints;
}

public MouseAdapter getMa() {
	return ma;
}
public void setMa(MyMouseAdapter ma) {
	this.ma = ma;
}
public MouseMotionAdapter getMma() {
	return mma;
}
public void setMma(MyMouseMotionAdapter mma) {
	this.mma = mma;
}




public MyMouseAdapterSelfPaint getMasp() {
	return masp;
}

public void setMasp(MyMouseAdapterSelfPaint masp) {
	this.masp = masp;
}

public int getStartX() {
	return startX;
}
public void setStartX(int startX) {
	this.startX = startX;
}
public int getStartY() {
	return startY;
}
public void setStartY(int startY) {
	this.startY = startY;
}
}
