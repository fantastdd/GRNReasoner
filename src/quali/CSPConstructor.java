package quali;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.LinkedList;



public class CSPConstructor {
private LinkedList<Entity> added_entities;
private LinkedList<Constraint> added_cons; 
private double threshold = 0.5;
public CSPConstructor()
{
  added_entities = new LinkedList<Entity>();
  added_cons = new LinkedList<Constraint>();
}
public void formConstraint(Entity e1, Entity e2)
{
  if(e1.intersects(e2))
  {
	  
	  Constraint con = new Constraint(e1,e2);
	 Rectangle[] recs1 = segmentation(e1);
	 Rectangle[] recs2 = segmentation(e2);

	 for (int i = 1; i < recs1.length; i++)
		 for ( int j = 1; j < recs2.length; j++)
		 {
			 if (lockX(threshold,recs1[i],recs2[j]))
			 {
			
				 con.readLockX( (i%2==0)?2:1,  (j%2==0)?2:1);
			 }
			 if (lockY(threshold,recs1[i],recs2[j]))
			 {
			
				 con.readLockY( (i%2==0)?2:1,  (j%2==0)?2:1);
			 }
	 }
	  
	  added_cons.add(con);
	  if(!added_entities.contains(e1))
		  added_entities.add(e1);
	  if(!added_entities.contains(e2))
		  added_entities.add(e2);
  }
  
}
private boolean lockX(double threshold,Rectangle rec1, Rectangle rec2)
{
	
	if(!rec1.intersects(rec2))
	   return false;
	 Rectangle rec = rec1.intersection(rec2);
     double overlap = rec.getWidth();
     double threshold1 = rec1.getWidth() * this.threshold;
     double threshold2 = rec2.getWidth() * this.threshold;
     if (overlap >= threshold1 && overlap >= threshold2)
    	 return true;
     return false;
    	 

}

private boolean lockY(double threshold,Rectangle rec1, Rectangle rec2)
{
     if(!rec1.intersects(rec2))
	   return false;
	 Rectangle rec = rec1.intersection(rec2);
     double overlap = rec.getHeight();
     double threshold1 = rec1.getHeight() * this.threshold;
     double threshold2 = rec2.getHeight() * this.threshold;
     if (overlap >= threshold1 && overlap >= threshold2)
    	 return true;
     return false;
    	 

}



private Rectangle[] segmentation(Rectangle rec)
{
	
	
    Rectangle[] recs = new Rectangle[5];
   
    recs[0] = rec;
    recs[1] = new Rectangle((int) rec.getCenterX(),rec.y,(int)rec.getWidth()/2,(int)rec.getHeight()/2);
    recs[2] = new Rectangle(rec.x,rec.y,(int)rec.getWidth()/2,(int)rec.getHeight()/2);
    recs[3] = new Rectangle(rec.x,(int)rec.getCenterY(),(int)rec.getWidth()/2,(int)rec.getHeight()/2); 
    recs[4] = new Rectangle((int) rec.getCenterX(),(int)rec.getCenterY(),(int)rec.getWidth()/2,(int)rec.getHeight()/2); 
    return recs;



}
public String report()
{
   String report = " -----  Report on CSP ------";
   for(Constraint c: this.added_cons)
   {
	   
	    report+= c + "\n"; 
	   	System.out.println(c);
	   
   }
   return report;
}
}
