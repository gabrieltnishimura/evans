package br.com.evans.servlets.init;

import java.io.IOException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.evans.jndi.states.DeviceMonitor;

/**
 * Servlet implementation class ArduinoInit
 */
@WebServlet(name = "DeviceManagerInit", urlPatterns = "/DeviceManagerInit", loadOnStartup = 1)  
public class DeviceMonitorInit extends HttpServlet {
	private static final long serialVersionUID = 1L;

    @Override
    public void init(ServletConfig config) throws ServletException {
    	// TODO Auto-generated method stub
    	super.init(config);
		Context initCtx;
		try {
			initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			envCtx.lookup("states/DeviceMonitorFactory"); // init arduino
        } catch (NamingException e) {
        	System.out.println("[EXCEPTION] Problem when trying to load the context of DeviceMonitor");
			e.printStackTrace();
		}
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
		Context initCtx = new InitialContext();
		Context envCtx = (Context) initCtx.lookup("java:comp/env");
		
		//Get the device monitor so it can get device states
		DeviceMonitor deviceMonitor = (DeviceMonitor) envCtx.lookup("states/DeviceMonitorFactory");
		deviceMonitor.setOutdated(true);
		} catch (NamingException e) {
        	System.out.println("[EXCEPTION] Problem when trying to load the context of DeviceMonitorFactory");
			e.printStackTrace();
		}
	}

}
