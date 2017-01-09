package br.com.evans.db.dao.notifications;

import java.util.Date;

import org.jongo.marshall.jackson.oid.Id;

public class MetronicNotification {
	@Id
	private long _id;
	private String information;
	private String type;
	private Date date;
	
	public MetronicNotification() {
		
	}
	
	public String getInformation() {
		return information;
	}
	
	public void setInformation(String information) {
		this.information = information;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
}
