package br.com.evans.jndi.db;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;

public enum MongoDBConnection {
	INSTANCE;
	Mongo mongoClient;
	DB database;
	private static final String ADDRESS = "localhost";
	private static final int PORT = 27017;
	private static final String DATABASE = "test";
	
	MongoDBConnection() {
		try {
			this.mongoClient = new Mongo( ADDRESS , PORT );
			this.database = mongoClient.getDB( DATABASE );
			System.out.println("[STATUS] Created MongoDB connection.");
		} catch (UnknownHostException e) {
			System.out.println("[EXCEPTION] Couldn't create MongoDB connection");
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
