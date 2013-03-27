package action;

import java.awt.event.MouseAdapter;
import javax.swing.JFrame;

import quali.RecEssentialPoints;


public class QualiMouseController {
private QualiMouseAdapter ma;
private QualiMouseMotionAdapter mma;
private QualiMouseAdapterSelfPaint masp;
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

public QualiMouseController(JFrame frame)
{
   ma = new QualiMouseAdapter(frame,this);
   mma = new QualiMouseMotionAdapter(frame,this);
   masp = new QualiMouseAdapterSelfPaint(frame,this);
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
public void setMa(QualiMouseAdapter ma) {
	this.ma = ma;
}
public QualiMouseMotionAdapter getMma() {
	return mma;
}
public void setMma(QualiMouseMotionAdapter mma) {
	this.mma = mma;
}




public QualiMouseAdapterSelfPaint getMasp() {
	return masp;
}

public void setMasp(QualiMouseAdapterSelfPaint masp) {
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
