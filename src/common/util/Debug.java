package common.util;

import java.lang.reflect.Method;


public class Debug {
 public static void echo(Object ob, Object... message)
 {
	 String result = "";
	 for (int i = 0; i < message.length; i++)
		 result+= "  "+ message[i];
	 String classname = "";
	 if(ob==null)
		 classname = " main class";
	 else
		 classname = ob.getClass().getName();
	// System.out.println("Debug: In: "+ classname + " Message: " + result );
	// System.out.println(result + "\n");
 }
}
