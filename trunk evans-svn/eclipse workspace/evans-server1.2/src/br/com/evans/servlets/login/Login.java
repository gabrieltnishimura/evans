package br.com.evans.servlets.login;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.com.evans.security.login.CookieManager;
import br.com.evans.security.login.SessionManager;
import br.com.evans.security.login.Validate;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
	
		String user = request.getParameter("username");
		String password = request.getParameter("password");
		String remember =request.getParameter("remember");
		if (Validate.checkUser(user, password)) {
			HttpSession session = request.getSession(true);
			session.setAttribute("logged", user);
			if (remember != null && Integer.parseInt(remember) == 1) {
				System.out.println("[LOGIN]User " + user + " logged in with remember me option");
				session.setMaxInactiveInterval(7*24*60*60);

				response.setContentType("text/html");
				CookieManager.newCookieForUsername(user, response); // cookie for 7 days as default

			} else {
				System.out.println("[LOGIN]User " + user + " logged in.");
			}
			SessionManager.updateLastSessionInfo(user, session);
			
			System.out.println(session.toString());
			response.sendRedirect(request.getServletContext().getInitParameter("server-hostname") + 
					request.getContextPath() + "/index.html"); // loggedPage
			return;
		} else {
			System.out.println("[LOGIN]Wrong username or password.");
	        request.setAttribute("wrongPw", "1");
			//response.sendRedirect("http://localhost:8080"+request.getContextPath()+"/login.jsp"); // login
			request.getRequestDispatcher("/login.jsp").forward(request, response);
			return;
		}
	}

}
