package br.com.evans.servlets.login;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.com.evans.notifications.core.Notifications;
import br.com.evans.security.login.CookieManager;

/**
 * Servlet implementation class Logoff
 */
@WebServlet("/Logoff")
public class Logoff extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Logoff() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		
		HttpSession session = request.getSession();
		if (session != null)
			session.invalidate();
		
		System.out.println(Notifications.NOTIF_LOGOFF_REDIR);
		
		response.sendRedirect(request.getServletContext().getInitParameter("server-hostname") + 
				request.getContextPath()+"/login.jsp"); // login
		return;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		
		HttpSession session = request.getSession();
		if (session != null)
			session.invalidate();
		
		CookieManager.deleteUserCookie(request);
		
		System.out.println(Notifications.NOTIF_LOGOFF_REDIR);
		
		response.sendRedirect(request.getServletContext().getInitParameter("server-hostname") + 
				request.getContextPath()+"/login.jsp"); // login
		return;
	}

}
