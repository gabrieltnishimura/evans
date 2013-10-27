package br.com.evans.db.dao.notifications;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jongo.MongoCollection;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

import br.com.evans.jndi.manager.JNDIManager;

public class NotificationDAO {
	
	public static void saveMetronicNotification(MetronicNotification notification) {
		DBCollection collection = JNDIManager.getMongoCollection("notifications");
		
		BasicDBObject notificationSaveQuery = new BasicDBObject("timestamp", notification.getDate());
		notificationSaveQuery.put("information", notification.getInformation());
		notificationSaveQuery.put("type", notification.getType());
		
		collection.save(notificationSaveQuery);
	}
	
	public static List<MetronicNotification> listNotifications(int limit) {
		//DBCollection collection = JNDIManager.getMongoCollection("notifications");
		MongoCollection  collection = JNDIManager.getJongoCollection("notifications");
		
		Iterable<MetronicNotification> notifications = collection.find().sort("_id: -1").limit(limit).as(MetronicNotification.class);
		
		List<MetronicNotification> notificationList = new ArrayList<MetronicNotification>();
		
		Iterator<MetronicNotification> iterator = notifications.iterator();
		
		while(iterator.hasNext()) {
			notificationList.add((MetronicNotification) iterator.next());
		}
		
		return notificationList;
	}
}
