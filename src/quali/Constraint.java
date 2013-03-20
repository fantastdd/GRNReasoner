package quali;


public class Constraint {
private Entity e1;
private Entity e2;
private int[] lock_x = {0,0,0,0};// lock_e1_x1,lock_e2_x1,lock_e1_x2,lock_e2_x2;
private int[] lock_y = {0,0,0,0};
public Constraint() {
	super();
}
public Entity getE1() {
	return e1;
}
public void setE1(Entity e1) {
	this.e1 = e1;
}
public Constraint(Entity e1, Entity e2) {
	super();
	this.e1 = e1;
	this.e2 = e2;
}
public Constraint(Entity e1, Entity e2, int[] lock_x, int[] lock_y) {
	super();
	this.e1 = e1;
	this.e2 = e2;
	this.lock_x = lock_x;
	this.lock_y = lock_y;
}
public void readLockX(int e1, int e2)
{
    if (lock_x[0] == 0)
    {
    	lock_x[0] = e1;
    	lock_x[1] = e2;
    }	
    else
    {
    	lock_x[2] = e1;
    	lock_x[2] = e2;
    }

}
public void readLockY(int e1, int e2)
{
    if(lock_y[0] == 0)
    {
    	lock_y[0] = e1;
    	lock_y[1] = e2;
    }
    else
    {
    	lock_y[2] = e1;
    	lock_y[3] = e2;
    }


}

public Entity getE2() {
	return e2;
}
public void setE2(Entity e2) {
	this.e2 = e2;
}
public int[] getLock_x() {
	return lock_x;
}
public void setLock_x(int[] lock_x) {
	this.lock_x = lock_x;
}
public int[] getLock_y() {
	return lock_y;
}
public void setLock_y(int[] lock_y) {
	this.lock_y = lock_y;
}
public String toString()
{
  String result = " mutex lock between " + e1 +"  and  " + e2 + " is\n x: [";
  for ( int i = 0; i < lock_x.length; i++)
	  result+= " " + lock_x[i];
  result+=" ] \ny: [";
  for ( int j = 0; j < lock_y.length; j++)
	  result+= " " + lock_y[j];
  result += " ]";
  
  return result;
}

}
