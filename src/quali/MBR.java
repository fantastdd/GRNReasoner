package quali;

import java.awt.Point;
import java.awt.Rectangle;

public class MBR extends Rectangle
{

private int id;

public int getId() {
	return id;
}

public void setId(int id) {
	this.id = id;
}




public MBR(Rectangle rec) {
	

    this.width = (int) rec.getWidth();
    this.height = (int) rec.getHeight();
    this.setLocation(new Point((int)rec.getLocation().getX(),(int)rec.getLocation().getY()));
   
}
 


public MBR()
{}



public int hashcode()
{
	 int hash  = 7;
	 hash = 31 *hash + this.height;
	 hash = 31 * hash + this.width;
	 hash = 31 * hash + this.getLocation().hashCode();
	 return hash;
	

}

public boolean equals(Object obj)
{
	MBR mbr = null;
   if(obj instanceof MBR)
   {  
	   mbr = (MBR) obj;
	
   }
   else
	   if (obj instanceof Neighbor)
	   {
		   mbr = ( (Neighbor) obj ).getMbr();
	   }
   
   if(mbr!= null  & mbr.getLocation().equals(this.getLocation()) & mbr.getHeight() == this.height && mbr.width == this.width)
	   return true;
	 
   return false;
   
}
public String toString()
{
   String result = " MBR " + this.id;
   return result;

}


}
