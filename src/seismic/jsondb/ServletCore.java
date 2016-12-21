package seismic.jsondb;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class ServletCore extends HttpServlet {
	
	// TODO: add a superclass layer which passes a request string and expects back a response string
	// unless doPost is overridden as below
	public void doPost(HttpServletRequest req, HttpServletResponse res) {
		String request = read(req);
		String response = "received: " + request;
		write(res, response);
		
		File f = new File("webapps/json-db-servlet/db/00000.db");
		try {
			f.createNewFile();
			PrintStream ps = new PrintStream(f);
			ps.println(response);
			ps.close();
		} catch (Exception ex) {
			
		}
	}
	
	// TODO: move this to a subclass of HttpServletRequest
	private String read(HttpServletRequest req) {
		// read req to input[]
        byte[] input = new byte[req.getContentLength()];
        
        try {
	        ServletInputStream sin = req.getInputStream();
	        int c;
	        for (int count = 0; (c = sin.read(input, count, input.length - count)) != -1;  count += c);
	        sin.close();
        } catch (IOException ioe) {
        	// TODO
        }
        return new String(input);
	}
	
	// TODO: move this to a subclass HttpServletResponse
	private void write(HttpServletResponse resp, String output) {
		try {
	        OutputStreamWriter writer = new OutputStreamWriter(resp.getOutputStream());
	        writer.write(output);
	
	        // set the response code and finish
	        resp.setStatus(HttpServletResponse.SC_OK);
	        writer.flush();
	        writer.close();
		} catch (IOException ioe) {
			// TODO
		}
	}
}
