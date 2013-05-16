package quali;

import java.awt.Point;

public class Contact {

private int type = -1; // 1:support 0: non-touching, -1: un-initialized -2: invalid
private int tangential_area = -2;  // -2: non-touching -1: do not care 
public Point[] points = new Point[2];
public boolean strongEdgeSupport = false;
public int getTangential_area() {
	return tangential_area;
}
public void setTangential_area(int tangential_area) {
	this.tangential_area = tangential_area;
}


public Contact clone()
{
	Contact contact = new Contact();
	contact.type = type;
	contact.tangential_area = tangential_area;
    contact.points = points;
    contact.strongEdgeSupport = strongEdgeSupport;
	return contact;
	
}
public Contact() {
	
}
public int hashcode()
{
	 int hash  = 7;
	 hash = 31 *hash + this.type;
	 hash = 31 * hash + this.tangential_area;
	
	 return hash;
	

}
public boolean equals(Contact contact)
{
    if(this.type == contact.type && this.tangential_area == contact.tangential_area)
		   return true;
    
    
    return false;
   
}

public String toString()
{
  String result = "";
  if(type == 1)
   result += " Region: " + tangential_area + " ,touching" ;
  else if(type == -1)
	   result += " Region: " + tangential_area + " un-initialized ";
  else if(type == 0)
	   result += " Region: " + tangential_area + " non-touching";
  else if(type == 2)
	   result += " " + " Either touching or non-touching ";
  else if(type == -2)
	  result += " invalid ";
  return result;
}
public int getType() {
	return type;
}
public void setType(int type) {
	this.type = type;
}

public boolean isInitialized()
{
	return (this.tangential_area != -2);
}

}
