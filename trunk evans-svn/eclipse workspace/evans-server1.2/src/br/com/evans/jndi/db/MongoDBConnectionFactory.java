package br.com.evans.jndi.db;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.spi.ObjectFactory;

@SuppressWarnings("rawtypes") 
public class MongoDBConnectionFactory implements ObjectFactory {
	public MongoDBConnectionFactory() {
		//constructor
	}
	public Object getObjectInstance(Object obj, Name name, Context nameCtx,Hashtable environment) throws NamingException {
		// Return the customized instance
		return (MongoDBConnection.INSTANCE);

	}
}