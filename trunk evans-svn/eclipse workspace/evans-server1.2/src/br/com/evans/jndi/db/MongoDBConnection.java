package br.com.evans.jndi.db;

import java.net.UnknownHostException;

import br.com.evans.notifications.core.Notifications;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;

public enum MongoDBConnection {
	INSTANCE;
	Mongo mongoClient;
	DB database;
	private static final String ADDRESS = "localhost";
	private static final int PORT = 27017;
	private static final String DATABASE = "evans";
	
	MongoDBConnection() {
		try {
			this.mongoClient = new Mongo( ADDRESS , PORT );
			this.database = mongoClient.getDB( DATABASE );
			System.out.println(Notifications.NOTIF_CREATED_MONGODB_SINGLETON);
		} catch (UnknownHostException e) {
			System.out.println(Notifications.EXCEP_MONGODB_CONNECTION);
			e.printStackTrace();
		}
	}
	
	public Mongo getMongoClient() {
		return this.mongoClient;
	}
	
	public DB getMongoDB() {
		return this.database;
	}
	
	public DBCollection getCollection(String collection) {
		DBCollection coll = this.database.getCollection(collection);
		return coll;
	}
}
