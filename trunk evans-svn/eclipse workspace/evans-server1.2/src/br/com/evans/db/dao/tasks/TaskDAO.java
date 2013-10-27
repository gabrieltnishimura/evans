package br.com.evans.db.dao.tasks;

import java.util.List;

import br.com.evans.db.dao.notifications.MetronicNotification;

public interface TaskDAO {
	
	public void saveMetronicTask(MetronicTask task);
	public List<MetronicNotification> listNotifications(int limit);
	
}