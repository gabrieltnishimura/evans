package br.com.evans.db.dao.tags;

import org.jongo.marshall.jackson.oid.Id;

public class MetronicTag {

	@Id
	private long _id;
	private String name;
	
	public String getName() {
		return name;
	}
	public void setName(String tag) {
		this.name = tag;
	}
}
