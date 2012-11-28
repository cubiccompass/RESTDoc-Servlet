# RESTDoc Servlet Example

This is an example of creating a self-describing REST API using REST Docs and Java servlets.

## Prerequisites

* Basic Java knowledge, including an installed version of the JVM and Maven.
* Basic Git knowledge, including an installed version of Git.
* A Java web application. If you don't have one follow the first step to create an example. Otherwise skip that step.

## Handling RESTDoc Requests

In the example servlet below, the doOptions() handler fetches a predefined JSON file that documents the API endpoint. Appending "/describe" to the end of any REST API endpoint will also serve the RESTDoc for that particular interface.

    :::java
    package servlet;
    
    import java.io.IOException;
    
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
					doOption(req, resp);
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

		public void doOption(HttpServletRequest req, HttpServletResponse resp) throws IOException{
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

## Run your Application

To generate the start scripts simply run:

    :::term
    $ mvn package

And then simply run the script:

    :::term
    $ sh target/bin/webapp

That's it. Your application should start up on port 8080. You can see the JSP at http://localhost:8080 and the servlet at http://localhost:8080/hello and http://localhost:8080/hello/describe

# Deploy your Application to Heroku

## Create a Procfile

You declare how you want your application executed in `Procfile` in the project root. Create this file with a single line:

    :::term
    web: sh target/bin/webapp

## Optionally Choose a JDK
By default, OpenJDK 1.6 is installed with your app. However, you can choose to use a newer JDK by specifying `java.runtime.version=1.7` in the `system.properties` file.

Here's what a `system.properties` file looks like:

    :::term
    java.runtime.version=1.7

You can specify 1.6, 1.7, or 1.8 (1.8 is in beta) for Java 6, 7, or 8 (with lambdas), respectively.

## Deploy to Heroku

Commit your changes to Git:

    :::term
    $ git add .
    $ git commit -m "Ready to deploy"

Create the app on the Cedar stack:

    :::term
    $ heroku create --stack cedar
    Creating high-lightning-129... done, stack is cedar
    http://high-lightning-129.herokuapp.com/ | git@heroku.com:high-lightning-129.git
    Git remote heroku added

Deploy your code:

    :::term
    Counting objects: 227, done.
    Delta compression using up to 4 threads.
    Compressing objects: 100% (117/117), done.
    Writing objects: 100% (227/227), 101.06 KiB, done.
    Total 227 (delta 99), reused 220 (delta 98)

    -----> Heroku receiving push
    -----> Java app detected
    -----> Installing Maven 3.0.3..... done
    -----> Installing settings.xml..... done
    -----> executing .maven/bin/mvn -B -Duser.home=/tmp/build_1jems2so86ck4 -s .m2/settings.xml -DskipTests=true clean install
           [INFO] Scanning for projects...
           [INFO]                                                                         
           [INFO] ------------------------------------------------------------------------
           [INFO] Building petclinic 0.1.0.BUILD-SNAPSHOT
           [INFO] ------------------------------------------------------------------------
           ...
           [INFO] ------------------------------------------------------------------------
           [INFO] BUILD SUCCESS
           [INFO] ------------------------------------------------------------------------
           [INFO] Total time: 36.612s
           [INFO] Finished at: Tue Aug 30 04:03:02 UTC 2011
           [INFO] Final Memory: 19M/287M
           [INFO] ------------------------------------------------------------------------
    -----> Discovering process types
           Procfile declares types -> web
    -----> Compiled slug size is 62.7MB
    -----> Launching... done, v5
           http://pure-window-800.herokuapp.com deployed to Heroku

Congratulations! Your web app should now be up and running on Heroku. Open it in your browser with:

    :::term  
    $ heroku open
