package br.com.evans.db.dao.tasks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jongo.MongoCollection;

import br.com.evans.db.dao.notifications.MetronicNotification;
import br.com.evans.jndi.manager.JNDIManager;

public class TaskImpl implements TaskDAO {
	
	@Override
	public void saveMetronicTask(MetronicTask task) {
		//DBCollection collection = JNDIManager.getMongoCollection("notifications"); @deprecated
		MongoCollection collection = JNDIManager.getJongoCollection("notifications");
		
		collection.save(task);
	}
	
	@Override
	public List<MetronicNotification> listNotifications(int limit) {
		//DBCollection collection = JNDIManager.getMongoCollection("notifications"); @deprecated
		MongoCollection  collection = JNDIManager.getJongoCollection("notifications");
		
		Iterable<MetronicNotification> notifications = collection.find().sort("{_id: -1}").limit(limit).as(MetronicNotification.class);
		
		List<MetronicNotification> notificationList = new ArrayList<MetronicNotification>();
		
		Iterator<MetronicNotification> iterator = notifications.iterator();
		
		while(iterator.hasNext()) {
			notificationList.add((MetronicNotification) iterator.next());
		}
		
		return notificationList;
	}
}
