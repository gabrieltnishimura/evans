package br.com.evans.security.login;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;

import br.com.evans.jndi.db.MongoDBConnection;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

/**
 * SessionManager makes sure the user is on the same session 
 * that he logged.
 * @author Gabriel
 *
 */
public class SessionManager {

	public static void updateLastSessionInfo(String username, HttpSession session) {	
		Context initCtx;
		try {
			initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			MongoDBConnection mongo = (MongoDBConnection) envCtx.lookup("db/MongoDBConnectionFactory"); // get db connection evans 
			DBCollection collection = mongo.getCollection("users");
			
			BasicDBObject query = new BasicDBObject("login", username);
			BasicDBObject sessionInfo = new BasicDBObject();
			BasicDBObject info = new BasicDBObject().append("lastLoginTime", session.getCreationTime()).append("sessionId", session.getId());
			sessionInfo.append("$set", info);
			collection.update(query, sessionInfo);
			
        } catch (NamingException e) {
        	System.out.println("[EXCEPTION] Problem when trying to load the context of MongoDBFactory, couldn't query for user");
			e.printStackTrace();
		}
	}
	
	public static boolean isUserOnValidSession(String username, HttpSession session) {
		boolean valid = false;
		
		Context initCtx;
		try {
			initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			MongoDBConnection mongo = (MongoDBConnection) envCtx.lookup("db/MongoDBConnectionFactory"); // get db connection evans 
			DBCollection collection = mongo.getCollection("users");
			
			BasicDBObject query = new BasicDBObject("login", username);
			DBObject user = collection.findOne(query);
			
			if (user != null) {
				if (user.get("lastLoginTime").equals(session.getCreationTime())) {
					valid = true;
				} else if (user.get("sessionId").equals(session.getId())) {
					valid = true;
				}
			} else {
				System.out.println("[ERROR] There is no such user on the database. There is no such valid session.");
			}
			
        } catch (NamingException e) {
        	System.out.println("[EXCEPTION] Problem when trying to load the context of MongoDBFactory, couldn't query for user");
			e.printStackTrace();
		}
		
		return valid;
	}
	
}
