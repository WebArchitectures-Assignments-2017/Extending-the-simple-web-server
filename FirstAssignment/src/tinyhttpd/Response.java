package tinyhttpd;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;

public class Response {
	  //function used to start an external exe process located on folder "process" in the project-folder
	public static void startprocess(String processname, String param1,String param2,OutputStream out){
	       
     	 try {
    		
    	      String line;
    	      Process p = Runtime.getRuntime().exec("process/./"+processname+".exe "+param1+" "+param2);
    	      BufferedReader bri = new BufferedReader
    	        (new InputStreamReader(p.getInputStream()));
    	      BufferedReader bre = new BufferedReader
    	        (new InputStreamReader(p.getErrorStream()));
    	     
    	      while ((line = bri.readLine()) != null) {
    	    	new PrintStream(out).println(line);
    	    	
    	      }
    	      bri.close();
    	      while ((line = bre.readLine()) != null) {
    	    	new PrintStream(out).println(line);
    	      }
    	      bre.close();
    	      p.waitFor();
    	      
    	    }
    	    catch (Exception err) {
    	    	StringWriter errors = new StringWriter();
    	    	err.printStackTrace(new PrintWriter(errors));
    	    	new PrintStream(out).println(errors.toString());
    	    }
 	 }
//fuction used to open a file located in the project-folder (the name is passed as parameter)
	  public static void newfile(String req, OutputStream out) throws IOException{
      try {
          FileInputStream fis = new FileInputStream(req);
          byte[] data = new byte[fis.available()];
          fis.read(data);
          out.write(data);
      } catch (FileNotFoundException e) {
          new PrintStream(out).println("404 Not Found");
          System.out.println("404 Not Found: " + req);

      }}
 
}
