package br.com.evans.servlets.ajax.basic;

import java.io.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import br.com.evans.devices.arduino.AirConditioner;
import br.com.evans.devices.arduino.RfCoded;
import br.com.evans.devices.core.Device;
import br.com.evans.jndi.states.DeviceMonitor;
import br.com.evans.notifications.core.Notifications;
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
		out.println(ServletUtilities.headWithTitle(title) + 
				"<body bgcolor=\"#fdf5e6\">\n<h1>" + title + 
				"</h1>\n<p>Simple servlet for testing.</p>\n</body></html>");
		
		Context initCtx;
		try {
			initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			
			//Get the device monitor so it can change the device state
			DeviceMonitor deviceMonitor = (DeviceMonitor) envCtx.lookup("states/DeviceMonitorFactory"); //getting the connection can get a little costy(process) I guess
			
			Device device = deviceMonitor.getDevice("room");
				if (device != null && device instanceof RfCoded) {  // switch states if the hashmap returns a valid RfCoded device
	        		device.switchStates(); // since it's not certain that the device will really switch states, wait for serial return
	        		// if it doesn't come, try to switch states again
	        		
	    			response.setContentType("text");
	    			response.getWriter().write(deviceMonitor.createJsonMap());
				}
        } catch (NamingException e) {
        	System.out.println(Notifications.EXCEP_ARDUINO_CONTEXT);
			e.printStackTrace();
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String recognitionString = request.getParameter("recognitionString").toLowerCase();
		System.out.println("[AJAX] received: '" + recognitionString + "'");
		
		Context initCtx;
		try {
			initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");

			//Get the device monitor so it can change the device state
			DeviceMonitor deviceMonitor = (DeviceMonitor) envCtx.lookup("states/DeviceMonitorFactory"); //getting the connection can get a little costy(process) I guess
			
			Device device = deviceMonitor.getDevice(recognitionString);
			if (device != null && (device instanceof RfCoded || device instanceof AirConditioner)) { // switch states if the hashmap returns a valid RfCoded device
				System.out.println(Notifications.NOTIF_SWITCHING_STATES_DEVICE);
				device.switchStates();

				response.setContentType("application/json");
				response.getWriter().write(deviceMonitor.createJsonMap());
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			System.out.println(Notifications.EXCEP_DEVICEMONITOR_CONTEXT);
			e.printStackTrace();
		} catch (JSONException jse) {
			response.setContentType("application/text");
			response.getWriter().write(Notifications.EXCEP_JSON_INSERT);
		}

//		response.setContentType("application/text");
//		response.getWriter().write("Hello Gabriel, how are you doing?");
	}
}
