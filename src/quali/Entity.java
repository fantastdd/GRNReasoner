package quali;
import java.awt.Rectangle;


public class Entity extends Rectangle{
private int x_zones = 3; // 1-2:1; 3-4:2;
private int y_zones = 3;//  1-2:1; 3-4:2;
private int id;
private static int count = 0;
public Entity(Rectangle rec)
{
   super(rec);	
   id = count++;
}

public Entity()
{}
public int getX_zones() {
	return x_zones;
}
public void setX_zones(int x_zones) {
	this.x_zones = x_zones;
}
public int getY_zones() {
	return y_zones;
}
public void setY_zones(int y_zones) {
	this.y_zones = y_zones;
}
public String toString()
{
	String result = " Solid Rectangle ID: " + id + " Info: "+ super.toString(); 
    return result;
}
}
