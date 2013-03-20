package quali;

import java.awt.Rectangle;
import java.util.LinkedList;



public class EntityRegister {
private static LinkedList<Entity> recs = new LinkedList<Entity>();
private static   CSPConstructor cspc = new   CSPConstructor();
public static void registerRectangle(Rectangle rec)
{
	Entity entity = new Entity(rec);
	if(!recs.contains(entity))
     	recs.add(entity);

}
public static void constructCSP()
{

  if(recs.size() > 1)
  {
	  for ( int i = 0; i < recs.size(); i++)
		  for ( int j = i + 1; j < recs.size(); j++)
		  {  
			  //System.out.println(cspc);
			  cspc.formConstraint(recs.get(i), recs.get(j));
			  
		  }
	  cspc.report();
  }	
}
}
