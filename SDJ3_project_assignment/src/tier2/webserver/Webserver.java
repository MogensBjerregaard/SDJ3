package tier2.webserver;

import java.io.IOException;

public class Webserver {

	public static void main(String[] args) {
		try {
			Runtime.
			   getRuntime().
			   exec("cmd /c start \"\" /min C:/Users/mob/Documents/SDJ3/week10/axis2-1.7.7-bin/axis2-1.7.7/bin/axis2server.bat");
			
		} catch (IOException e) {
			System.out.println("Unable to start Axis2 Webserver.\nNo webservices was started.\n"+e.getMessage());
		}

	}

}
