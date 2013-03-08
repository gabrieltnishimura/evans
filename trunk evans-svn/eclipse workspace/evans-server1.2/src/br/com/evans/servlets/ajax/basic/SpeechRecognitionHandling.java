package br.com.evans.servlets.ajax.basic;

import java.io.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import br.com.evans.mapping.basic.WordMapping;

import br.com.evans.jndi.basic.ArduinoConnection;
import br.com.evans.jndi.states.DeviceMonitor;
import br.com.evans.servlets.core.ServletUtilities;
import net.sf.json.JSONException;
 
/** Simple servlet for testing. Makes use of a helper class. */

@WebServlet("/handlingSpeech")
public class SpeechRecognitionHandling extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String title = "Test Servlet with Utilities";
		out.println
		(ServletUtilities.headWithTitle(title) +
				"<body bgcolor=\"#fdf5e6\">\n" +
				"<h1>" + title + "</h1>\n" +
				"<p>Simple servlet for testing.</p>\n" +
				"</body></html>");
		
		Context initCtx;
		try {
			initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			ArduinoConnection arduino = (ArduinoConnection) envCtx.lookup("arduino/ArduinoConnectionFactory");
            
            WordMapping mapping = new WordMapping();
            mapping.setCommandToConvert("room lights");
            String arduinoMapCodedString = mapping.toArduino();
            
            if (arduinoMapCodedString != null) {
            	try {
					arduino.writeInOutput(arduinoMapCodedString); // ending character '_'
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
        } catch (NamingException e) {
        	System.out.println("[EXCEPTION] Problem when trying to load the context of ArduinoFactory");
			e.printStackTrace();
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String recognitionString = request.getParameter("recognitionString").toLowerCase();
		System.out.println("[AJAX] " + recognitionString);
		
		Context initCtx;
        	try {
    			initCtx = new InitialContext();
    			Context envCtx = (Context) initCtx.lookup("java:comp/env");
    			
    			//Get the device monitor so it can change the device state
    			DeviceMonitor deviceMonitor = (DeviceMonitor) envCtx.lookup("states/DeviceMonitorFactory"); //getting the connection can get a little costy(process) I guess
    			deviceMonitor.setLocalizationToConvert(recognitionString);
    			String arduinoMapCodedString = deviceMonitor.toArduinoRFCode();
    			
    				if (arduinoMapCodedString != null) {
    	    			//Get the arduino Connection so it can send data over serial
    	    			ArduinoConnection arduino = (ArduinoConnection) envCtx.lookup("arduino/ArduinoConnectionFactory");
    	    			
    	        		deviceMonitor.switchStatus();
    	        		
    	    			response.setContentType("application/json");
    	    			response.getWriter().write(deviceMonitor.createJsonMap());
    					arduino.writeInOutput(arduinoMapCodedString); // ending character '_'
    				}
    				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NamingException e) {
	        	System.out.println("[EXCEPTION] Problem when trying to load the context of ArduinoFactory or DeviceMonitorFactory");
				e.printStackTrace();
			} catch (JSONException jse) {
				response.setContentType("application/text");
				response.getWriter().write("[EXCEPTION] Problem when inserting data to the JSON Object");
			}

//		response.setContentType("application/text");
//		response.getWriter().write("Hello Gabriel, how are you doing?");
	}
}
