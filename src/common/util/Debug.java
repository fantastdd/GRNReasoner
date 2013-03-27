package common.util;


public class Debug {
 public static void echo(Object ob, Object... message)
 {
	 for (int i = 0; i < message.length; i++) {
	}
	 if(ob==null) {
	}
	else ob.getClass().getName();
 }
}
