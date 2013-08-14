package br.com.evans.jndi.manager;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import br.com.evans.jndi.db.MongoDBConnection;

import com.mongodb.DBCollection;

public class JNDIManager {
	
	public static DBCollection getMongoCollection(String collectionName) {
		Context initCtx;
		try {
			initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			MongoDBConnection mongo = (MongoDBConnection) envCtx.lookup("db/MongoDBConnectionFactory"); // get db connection evans 
			DBCollection collection = mongo.getCollection(collectionName);
			
			return collection;
		} catch (NamingException e) {
			System.out.println("[EXCEPTION] Problem when trying to load the context of MongoDBFactory, couldn't get node");
			e.printStackTrace();
			return null;
		}
	}
}
