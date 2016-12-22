package seismic.jsondb;

import java.io.File;
import java.io.PrintStream;

@SuppressWarnings("serial")
public class Login extends Servlet {
	
	public String post(String request) {
		
		File f = new File("webapps/json-db-servlet/db/users.db");
		try {
			f.createNewFile();
			PrintStream ps = new PrintStream(f);
			ps.println(request);
			ps.close();
		} catch (Exception ex) {
			
		}
		return request;
	}
}
