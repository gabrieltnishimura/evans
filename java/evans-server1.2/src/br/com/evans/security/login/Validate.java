package br.com.evans.security.login;

import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import br.com.evans.jndi.db.MongoDBConnection;
import br.com.evans.notifications.core.Notifications;
import br.com.evans.security.encryption.Digest;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class Validate {

	public static boolean checkUser(String username, String password) {
		boolean valid = false;
		
		Context initCtx;
		try {
			initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			MongoDBConnection mongo = (MongoDBConnection) envCtx.lookup("db/MongoDBConnectionFactory"); // get db connection evans 
			DBCollection collection = mongo.getCollection("users");
			
			BasicDBObject login = new BasicDBObject("login", username);
			BasicDBObject pw = new BasicDBObject("password", Digest.digestSHA2(password));
			ArrayList<BasicDBObject> authList = new ArrayList<BasicDBObject>();
			authList.add(login);
			authList.add(pw);
			
			BasicDBObject searchQuery = new BasicDBObject("$and", authList);
			DBObject found =  collection.findOne(searchQuery);
						
			if (found != null) {
				if (found.get("password").equals(Digest.digestSHA2(password))) {
					valid = true;	
				}
			}
			
        } catch (NamingException e) {
        	System.out.println(Notifications.EXCEP_MONGODB_CONTEXT + "couldn't query for the user.");
			e.printStackTrace();
		}
		
		return valid;	
	}
/*
	public static void main(String[] args) {
		com.mongodb.Mongo mongoClient;
		com.mongodb.DB database;
		final String ADDRESS = "localhost";
		final int PORT = 27017;
		final String DATABASE = "evans";
		
		try {
			mongoClient = new com.mongodb.Mongo( ADDRESS , PORT );
			database = mongoClient.getDB( DATABASE );
			System.out.println("[STATUS] Created MongoDB connection.");
			DBCollection coll = database.getCollection("users");
			
			BasicDBObject searchQuery = new BasicDBObject().append("login", "gcats").append("password", Digest.digestSHA2("super900"));
			coll.insert(searchQuery);
		} catch (java.net.UnknownHostException e) {
			System.out.println("[EXCEPTION] Couldn't create MongoDB connection");
			e.printStackTrace();
		}		
	}
	*/
}
