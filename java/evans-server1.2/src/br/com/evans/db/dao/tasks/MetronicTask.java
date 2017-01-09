package br.com.evans.db.dao.tasks;

import java.util.Date;

import org.jongo.marshall.jackson.oid.Id;

import br.com.evans.db.dao.tags.MetronicTag;

public class MetronicTask {
	@Id
	private long _id;
	private String information;
	private Date date;
	private boolean pending;
	private MetronicTag tag;
	
	public MetronicTask(String information, Date date, boolean pending, MetronicTag tag) {
		this.information = information;
		this.date = date;
		this.pending = pending;
		this.tag = tag;
	}
	
	public String getInformation() {
		return information;
	}
	
	public void setInformation(String information) {
		this.information = information;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}

	public boolean isPending() {
		return pending;
	}

	public void setPending(boolean pending) {
		this.pending = pending;
	}

	public MetronicTag getTag() {
		return tag;
	}

	public void setTag(MetronicTag tag) {
		this.tag = tag;
	}
	
}
