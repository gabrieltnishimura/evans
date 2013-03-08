package br.com.evans.servlets.ajax.reverse;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.evans.jndi.states.DeviceMonitor;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Servlet implementation class ReverseAjax
 */
@WebServlet(urlPatterns = "/reverseAjax", asyncSupported=true)
public class ReverseAjax extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final Queue<AsyncContext> asyncContexts = new ConcurrentLinkedQueue<AsyncContext>();

	private final Thread generator = new Thread("Event generator") {
		@Override
		public void run() {
			while (!Thread.currentThread().isInterrupted()) {
				try {
					Context initCtx = new InitialContext();
	    			Context envCtx = (Context) initCtx.lookup("java:comp/env");
	    			
	    			//Get the device monitor so it can get device states
	    			DeviceMonitor deviceMonitor = (DeviceMonitor) envCtx.lookup("states/DeviceMonitorFactory");
	
	    			if (deviceMonitor.isOutdated()) {
						while (!asyncContexts.isEmpty()) {
							String devicesState = deviceMonitor.createJsonMap();
							
							AsyncContext asyncContext = asyncContexts.poll();
							HttpServletResponse peer = (HttpServletResponse) asyncContext.getResponse();

							System.out.println("[REVERSE AJAX] " + devicesState);
							peer.getWriter().write(devicesState);
							peer.setStatus(HttpServletResponse.SC_OK);
							peer.setContentType("application/json");
							asyncContext.complete();
							deviceMonitor.setOutdated(false);
						}
	    			}
				} catch (IOException e) {
					throw new RuntimeException(e.getMessage(), e);
				}  catch (NamingException e) {
		        	System.out.println("[EXCEPTION] Problem when trying to load the context of DeviceMonitorFactory");
					e.printStackTrace();
				}
			}
		}
	};

	@Override
	public void init() throws ServletException {
		generator.start();
	}

	@Override
	public void destroy() {
		generator.interrupt();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		AsyncContext asyncContext = req.startAsync();
		asyncContext.setTimeout(0);
		asyncContexts.offer(asyncContext);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	}
	
}
