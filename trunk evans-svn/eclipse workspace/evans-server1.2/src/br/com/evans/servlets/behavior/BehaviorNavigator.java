package br.com.evans.servlets.behavior;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.evans.behavior.nodes.manager.BehaviorNodeManager;

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
        // TODO Auto-generated constructor stub
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
    	// TODO Auto-generated method stub
    	super.init(config);
    	this.nodeManager = new BehaviorNodeManager();
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String reset = request.getParameter("reset").toLowerCase();
		if (reset.equals("1")) {
			this.nodeManager.navigateToParent();
			//System.out.println("[NODES] Resetting parent.");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String destination = request.getParameter("destination").toLowerCase();
		String jsonResponse;
		if (destination.equals("parent")) {
			 jsonResponse = this.nodeManager.createJsonMap();
			 response.getWriter().write(jsonResponse);
			 response.getWriter().flush();
		} else if (!destination.isEmpty()) {
			if (nodeManager.navigateTo(destination) != null) {
				response.setContentType("application/json");
				jsonResponse = this.nodeManager.createJsonMap();
				//System.out.println(jsonResponse);
				response.getWriter().write(jsonResponse);
				response.getWriter().flush();
			} else {
				System.out.println("[ERROR] Couldn't find the desired destination.");
			}
		} else {
			System.out.println("[ERROR] The desired destination is blank.");
		}
	}
}
