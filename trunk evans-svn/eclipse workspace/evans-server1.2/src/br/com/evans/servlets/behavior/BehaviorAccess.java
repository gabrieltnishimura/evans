package br.com.evans.servlets.behavior;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.evans.behavior.nodes.manager.BehaviorNodeManager;

/**
 * Servlet implementation class BehaviorAccess
 */
@WebServlet("/BehaviorAccess")
public class BehaviorAccess extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BehaviorAccess() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	BehaviorNodeManager manager = new BehaviorNodeManager();
        request.setAttribute("childs", manager.getParent().getChilds()); // Store products in request scope.
        request.getRequestDispatcher("/behavior_backend.jsp").forward(request, response); // Forward to JSP page to display them in a HTML table
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
