package br.com.evans.behavior.nodes.core;

import java.util.List;

import org.bson.types.ObjectId;

public class ReportBehavior extends BehaviorNode {

	public ReportBehavior(int id, String name, List<String> responseWhenAccessed) {
		super(id, name, responseWhenAccessed);
		// TODO Auto-generated constructor stub
	}

	public ReportBehavior(ObjectId internalId) {
		super(internalId);
	}
	
}
