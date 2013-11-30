package br.com.evans.db.dao.tasks;

import java.net.UnknownHostException;
import java.util.Date;

import org.jongo.Jongo;
import org.jongo.MongoCollection;

import br.com.evans.notifications.core.Notifications;

import com.mongodb.Mongo;

public class InsertTests {
	
	public static void main(String[] args) {
		MetronicTask task = new MetronicTask("Present 2013 Year IPO Statistics at Board Meeting", new Date(), true, null);
		
		try {
			Jongo jongo = new Jongo(new Mongo( "localhost" , 27017 ).getDB( "evans" ));
			MongoCollection taskList = jongo.getCollection("tasks");
			taskList.save(task);
		} catch (UnknownHostException e) {
			System.out.println(Notifications.EXCEP_MONGODB_CONNECTION);
			e.printStackTrace();
		}
	}
	
}
