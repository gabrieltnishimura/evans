package br.com.evans.behavior.nodes.core;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import br.com.evans.notifications.core.Notifications;

/**
 * Every time the user enters a node [using ajax]
 * it should return the info to navigate to child nodes
 * @author Gabriel
 *
 */
public class BehaviorNode  implements Executable {
	private BehaviorNode parent; 
	private int id;
	private ObjectId internalId = null; 
	private String name = null;
	private String type = null;
	private List<String> responses = null; // response
	private List<BehaviorNode> childs = null;
	
	public BehaviorNode(int id, String name, List<String> responses) {
		this.id = id;
		this.name = name;
		this.responses = responses;
		this.childs = new ArrayList<BehaviorNode>();
		this.parent = null;
	}

	public BehaviorNode(int id, String name, List<String> responses, String type) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.responses = responses;
		this.childs = new ArrayList<BehaviorNode>();
		this.parent = null;
	}
	
	public BehaviorNode(ObjectId internalId) {
		this.internalId = internalId;
		this.parent = null;
	}
	
	public BehaviorNode(int id) {
		this.id = id;
		this.parent = null;
	}
	
	public BehaviorNode(int id, String type, String name, List<String> responses) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.responses = responses;
		this.childs = new ArrayList<BehaviorNode>();
		this.parent = null;
	}
	
	public void addBehaviorNode(BehaviorNode node, BehaviorNode parent) {
		node.setParent(parent);
		this.childs.add(node);
	}
	
	public List<BehaviorNode> getChilds() {
		return childs;
	}

	public void setChilds(List<BehaviorNode> childs) {
		this.childs = childs;
	}

	public static BehaviorNode searchForChild(String node, BehaviorNode parent) {
		if (node.equals(parent.getName())) { // have found the correct node
			return parent;
		} else { // there are childs inside the parent
			for (BehaviorNode each : parent.getChilds()) {
				BehaviorNode child = searchForChild(node, each); // should return each in case each is node
				if (child != null) { // if this child is not null and has no childs
					return child;
				}
			}
		}
		return null;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BehaviorNode getParent() {
		return parent;
	}

	public void setParent(BehaviorNode parent) {
		this.parent = parent;
	}

	@Override
	public void execute(List<String> parameters) {
		// implement this on later classes
		System.out.println(Notifications.ERROR_NON_EDGE_EXECUTION);
	}

	public List<String> getResponses() {
		return this.responses;
	}

	public void setResponses(List<String> responses) {
		this.responses = responses;
	}
	
	public String getLink() {
		if(this.getChilds().isEmpty()) { // is command
			return "BehaviorNavigator?name="+this.name;
		}
		return null;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ObjectId getInternalId() {
		return internalId;
	}

	public void setInternalId(ObjectId internalId) {
		this.internalId = internalId;
	}
	
}
