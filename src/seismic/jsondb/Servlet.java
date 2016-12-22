package seismic.jsondb;

import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public abstract class Servlet extends HttpServlet {
	
	public void doPost(HttpServletRequest req, HttpServletResponse res) {
		String request = read(req);
		String response = post(request);
		write(res, response);
	}
	
	public abstract String post(String request);
	
	protected String read(HttpServletRequest req) {
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
	
	protected void write(HttpServletResponse resp, String output) {
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
