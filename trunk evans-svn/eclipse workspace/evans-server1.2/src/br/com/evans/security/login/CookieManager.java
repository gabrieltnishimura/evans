package br.com.evans.security.login;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class CookieManager {

	/**
	 * Creates a cookie for a certain username. 
	 * Added one parameter for custom cookie age.
	 * @param username
	 * @param cookieAge
	 * @param response
	 */
	public static void newCookieForUsername(String username, int cookieAge, HttpServletResponse response) {	
		Cookie cookie = new Cookie("loggedUser", username);
		cookie.setMaxAge(cookieAge); // 7 days
		response.addCookie(cookie);
	}
	
	/**
	 * Creates cookie and sets username logged as value.
	 * Default cookie age for seven days.
	 * @param username
	 * @param response
	 */
	public static void newCookieForUsername(String username, HttpServletResponse response) {	
		Cookie cookie = new Cookie("loggedUser", username);
		cookie.setMaxAge(60*60*24*7); // 7 days
		response.addCookie(cookie);
	}
	
	/**
	 * Checks if there is a cookie with 'loggedUser' value
	 * the same as the user username. 
	 * @param username
	 * @param request
	 * @return whether it's okay to authorize the login
	 */
	public static boolean isCookieValidForUser(String username, HttpServletRequest request) {
		boolean valid = false;
		
		Cookie[] cookies = request.getCookies();	 
		if (cookies != null) {
			for(Cookie each : cookies){
				if(each.getName().equals("loggedUser")){
					if (each.getValue().equals(username)) // dont know how to find username
						valid = true;
				}
			}
		}
		return valid;
	}
	
	/**
	 * Checks if there is a not expired cookie
	 * with 'loggedUser' property in request from user browser.
	 * If there is, it means that the user is logged on.
	 * @param request
	 * @return whether it's okay to authorize the login
	 */
	public static boolean isCookieValidForUser(HttpServletRequest request) {
		boolean valid = false;
		
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for(Cookie each : cookies){
				if(each.getName().equals("loggedUser")){
					valid = true;
				}
			}
		}
		return valid;
	}
	
	
	/**
	 * Logs user off, deleting cookie
	 * @param request
	 * @return 
	 */
	public static void deleteUserCookie(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for(Cookie each : cookies){
				if(each.getName().equals("loggedUser")){
					each.setMaxAge(0);
					return;
				}
			}
		}
	}
}
