package br.com.evans.servlets.behavior;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.evans.behavior.nodes.core.BehaviorNode;
import br.com.evans.behavior.nodes.manager.BehaviorNodeManager;
import br.com.evans.notifications.core.Notifications;

/**
 * Servlet implementation class BehaviorNavigator
 */
@WebServlet("/BehaviorNavigator")
public class BehaviorNavigator extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private BehaviorNodeManager nodeManager;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BehaviorNavigator() {
        super();
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
    	super.init(config);
    	this.nodeManager = new BehaviorNodeManager();
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String reset = request.getParameter("reset");
		if (reset != null && reset.toLowerCase().equals("1")) {
			this.nodeManager.navigateToParent();
		} else {
			String name = request.getParameter("name").toLowerCase();
			BehaviorNodeManager auxiliaryBehavior = new BehaviorNodeManager();
			//System.out.println(auxiliaryBehavior.createJsonMap());
			auxiliaryBehavior.getParent();
			BehaviorNode node = BehaviorNode.searchForChild(name, auxiliaryBehavior.getParent());
			if (node != null) {
				node.execute(new ArrayList<String>()); // execute node instruction
			} else {
				System.out.println(Notifications.ERROR_NON_EDGE_EXECUTION);
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String destination = request.getParameter("destination").toLowerCase();
		String jsonResponse;
		if (destination.equals("parent")) {
			response.setContentType("application/json");
			 jsonResponse = this.nodeManager.createJsonMap();
			 response.getWriter().write(jsonResponse);
			 response.getWriter().flush();
		} else if (!destination.isEmpty()) {
			if (destination.contains("reset_")) {
				
			} if (nodeManager.navigateTo(destination) != null) {
				response.setContentType("application/json");
				jsonResponse = this.nodeManager.createJsonMap();
				//System.out.println(jsonResponse);
				response.getWriter().write(jsonResponse);
				response.getWriter().flush();
			} else {
				System.out.println(Notifications.ERROR_INVALID_NODE);
			}
		} else {
			System.out.println(Notifications.ERROR_DESTINATION_BLANK);
		}
	}
}
