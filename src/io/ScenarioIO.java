package io;


import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import quali.MBR;

public class ScenarioIO{

	private String filename;

public ScenarioIO(String filename) {
	this.filename = filename;
}
public ScenarioIO() {
	// TODO Auto-generated constructor stub
}

public  LinkedList<LinkedList<MBR>> load(String filename) throws IOException
{
	
	LinkedList<LinkedList<MBR>> scenarios = new LinkedList<LinkedList<MBR>>();
	File file = new File(filename);
	if(file.exists())
	{
		LinkedList<MBR> scenario = new LinkedList<MBR>();
		BufferedReader br = new BufferedReader(new FileReader(file));
		while(br.ready())
		{
			String line = br.readLine();
		if(!line.contains("$"))
		{	// read in form of x,y
			if(!line.contains("#"))
			{double sx = Double.parseDouble(line.substring(0,line.indexOf(" ")));
			String line2 = line.substring(line.indexOf(" ")+1);
			double sy = Double.parseDouble(
											line2.substring(0,line2.indexOf(" "))
										   );
			
			String line3 = line2.substring(line2.indexOf(" ")+1);
			double width = Double.parseDouble(
					line3.substring(0,line3.indexOf(" "))
					   );
			String line4 = line3.substring(line3.indexOf(" ") + 1);
			double height = Double.parseDouble(line4);
			
			Rectangle rec = new  Rectangle((int)sx,(int)sy,(int)width,(int)height);
			
			MBR mbr = new MBR(rec);
			scenario.add(mbr);
			}
		}
		else 
		{ 
			scenarios.add(scenario);
			scenario = new LinkedList<MBR>();
		}
		}
		return scenarios;
	}

		return null;
}




public  void save(List<MBR> mbrs,boolean append) throws IOException 
{
	save(mbrs,filename,append);

}




public  void save(List<MBR> mbrs,String filename,boolean append) throws IOException 
{
	
	File file = new File(filename);
	BufferedWriter br = new BufferedWriter(new FileWriter(file,append));
	for (MBR mbr: mbrs)
	{
		
		br.append(mbr.getX()+" "+mbr.getY()+" "+mbr.getWidth()+" "+ mbr.height+"\n");
	}
	br.append("$\n");
    br.close();


}


}
