package br.com.evans.servlet.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.com.evans.notifications.core.Notifications;
import br.com.evans.security.login.CookieManager;

public class SessionFilter implements Filter {
	
	private ArrayList<String> urlList;
	
	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String url = request.getServletPath();
		boolean allowedRequest = false;
		
		for (String inner : urlList) {
			if (url.contains(inner)) {
				allowedRequest = true;				
			}
		}
		/*if (this.urlList.contains(url)) {
			allowedRequest = true;
		}*/
		
		if (!allowedRequest && !CookieManager.isCookieValidForUser(request)) { // is not on the allow list AND is not a cookie user
			HttpSession session = request.getSession(false);
			if (session == null || session.getAttribute("logged") == null) {
				System.out.println(url+" was not allowed. Redirecting to login.jsp.");
				response.sendRedirect(request.getServletContext().getInitParameter("server-hostname") + 
						request.getContextPath()+"/login.jsp");
				return;
			}
		}
		
		chain.doFilter(req, res);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		System.out.println(Notifications.NOTIF_SESSION_FILTER_STARTED);
		
		String urls = config.getInitParameter("avoid-urls");
		StringTokenizer token = new StringTokenizer(urls, ",");
		
		this.urlList = new ArrayList<String>();
		
		while(token.hasMoreTokens()) {
			String currentToken = token.nextToken();
			System.out.println("Allowed request: [" + currentToken + "]");
			this.urlList.add(currentToken);
		}

	}

}
