package br.com.evans.jndi.manager;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jongo.Jongo;
import org.jongo.MongoCollection;

import br.com.evans.jndi.db.MongoDBConnection;

import com.mongodb.DB;
import com.mongodb.DBCollection;

public class JNDIManager {
	
	public static DBCollection getMongoCollection(String collectionName) {
		try {
			Context envCtx = (Context)  new InitialContext().lookup("java:comp/env");
			return ((MongoDBConnection) envCtx.lookup("db/MongoDBConnectionFactory")).getCollection(collectionName);
		} catch (NamingException e) {
			System.out.println("[EXCEPTION] Problem when trying to load the context of MongoDBFactory, couldn't return collection");
			e.printStackTrace();
			return null;
		}
	}
	
	public static MongoCollection getJongoCollection(String collectionName) {
		try {
			Context envCtx = (Context)  new InitialContext().lookup("java:comp/env");
			return new Jongo((DB) ((MongoDBConnection) envCtx.lookup("db/MongoDBConnectionFactory")).getMongoDB()).getCollection(collectionName);
		} catch (NamingException e) {
			System.out.println("[EXCEPTION] Problem when trying to load the context of MongoDBFactory, couldn't return jongo collection");
			e.printStackTrace();
			return null;
		}
	}
}
