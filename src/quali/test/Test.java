package quali.test;

import io.ScenarioIO;

import java.io.IOException;
import java.util.LinkedList;

import main.ScenarioPanel;
import quali.MBR;
import quali.MBRReasoner;
import quali.MBRRegisterWithFuzzyShape;
import quali.Node;

import common.util.Debug;

public class Test {
	public static void main(String args[]) throws IOException
	{
        Long time = System.currentTimeMillis();
	
		ScenarioIO sio = new ScenarioIO("s6");

		LinkedList<LinkedList<MBR>> scenarios = sio.load("s6");
		for(LinkedList<MBR> scenario: scenarios)
		{
			Debug.echo(null, scenario);
		}
		
		LinkedList<MBR> s1 = scenarios.get(0);
		
		int count = 0;
		for(MBR mbr: s1){
			
		   if(count == 0)
			{
			   MBRRegisterWithFuzzyShape.registerMBR(mbr,true);
			   count = 1;
			}  
		   else
			   MBRRegisterWithFuzzyShape.registerMBR(mbr,false);
		}
		Node node = MBRRegisterWithFuzzyShape.constructNode();

		
		
		MBRReasoner MBRR = new MBRReasoner();
		
		MBRR.reason(node);
		ScenarioPanel sp = new ScenarioPanel();
		sp.run(s1);
	
	System.out.println((System.currentTimeMillis() - time ));
	}

}
