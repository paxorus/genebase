package seismic.jsondb;

import java.io.File;
import java.io.PrintStream;

@SuppressWarnings("serial")
public class ServletCore extends Servlet {
	
	public String post(String request) {
		File f = new File("webapps/json-db-servlet/db/00001.db");
		try {
			f.createNewFile();
			PrintStream ps = new PrintStream(f);
			ps.println("a" + request + "a");
			ps.close();
		} catch (Exception ex) {
			
		}
		return request;
	}
}
