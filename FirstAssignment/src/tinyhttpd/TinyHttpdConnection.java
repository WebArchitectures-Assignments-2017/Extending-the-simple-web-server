/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tinyhttpd;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;

import java.util.StringTokenizer;


class TinyHttpdConnection extends Thread {

    Socket sock;

    TinyHttpdConnection(Socket s) {
        sock = s;
        setPriority(NORM_PRIORITY - 1);
        start();
    }

    public void run() {
        System.out.println("=========");
        OutputStream out = null;
        try {
            out = sock.getOutputStream();
            BufferedReader d = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            String req = d.readLine();
            System.out.println("Request: " + req);
            
            StringTokenizer st = new StringTokenizer(req);
            
            if ((st.countTokens() >= 2) && st.nextToken().equals("GET")) {
                if ((req = st.nextToken()).startsWith("/")) {
                    req = req.substring(1);
                }
                
                
                if (req.endsWith("/") || req.equals("")) {
                    req = req + "index.html";
                }
                
                
                
                //check if the user required to start an external process by a string control on the request
               if(req.substring(0,8).equals("process/")){
                	
            	   req = req.substring(8);
                	try{
                		//catch from the request the name of the process to run
                		String splitted[]=req.split("\\?");
                		String processname= splitted[0];
                		//catch from the request the parameters that 
                		//will be passed to the external process as arguments
                		String[] params = valueparams(splitted[1],out);
                		Response.startprocess(processname, params[0],params[1],out);
                	
                	}catch(ArrayIndexOutOfBoundsException err){
                		
                		StringWriter errors = new StringWriter();
               	    	err.printStackTrace(new PrintWriter(errors));
               	    	new PrintStream(out).println(errors.toString()+"\n please insert request url correctly, view all the instruction in index.html");
                	}}
                	
                else{Response.newfile(req, out);}
                
                
                
                    
            } else {
                    new PrintStream(out).println("400 Bad Request");
                    System.out.println("400 Bad Request: " + req);
                    sock.close();
                
            }
        } catch (IOException e) {
            System.out.println("Generic I/O error " + e);
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                System.out.println("I/O error on close" + ex);
            }
        }
    }
    
    
    
    
    
    
  
    //function used to take out the parameters from a string
    //the string has to look like "param1=value&param2=value"
    public String[] valueparams(String query,OutputStream out){
    	try{String parts[]=query.split("&");
    	 String params1[]=parts[0].split("=");
         String params2[]=parts[1].split("=");
         return new String[]{params1[1], params2[1]};}catch(ArrayIndexOutOfBoundsException err){
    		StringWriter errors = new StringWriter();
   	    	err.printStackTrace(new PrintWriter(errors));
   	    	new PrintStream(out).println(errors.toString()+"\n please insert two parameters divided by &, view all the instruction in index.html"); 
    	}
       return null;
        }
}
