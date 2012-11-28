package servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.stream.JsonWriter;

@WebServlet(
        name = "HelloServlet", 
        urlPatterns = {"/hello", "/hello/describe"}
    )
public class HelloServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
    	try {
			if(req.getServletPath().toLowerCase().endsWith("describe")){
				doOptions(req, resp);
			}
			else{
				processGet(req, resp);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public void processGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		resp.setContentType("text/json");
    	PrintWriter out;
		out = resp.getWriter();
		JsonWriter writer = new JsonWriter(out);
    	writer.setIndent("    ");
    	writer.beginObject();
    	writer.name("message").value("process GET request for endpoint");
    	writer.name("path").value(req.getRequestURI());
    	writer.endObject();
	}

    @Override
	public void doOptions(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		ServletOutputStream output = resp.getOutputStream();
		InputStream input = getServletContext().getResourceAsStream("restdocs/api.hello.json");
		byte[] buffer = new byte[1024];
	    int bytesRead;
	    while ((bytesRead = input.read(buffer)) != -1)
	    {
	        output.write(buffer, 0, bytesRead);
	    }
	}    
}
