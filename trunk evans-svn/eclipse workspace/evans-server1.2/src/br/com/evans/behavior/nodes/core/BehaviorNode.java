package br.com.evans.behavior.nodes.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Every time the user enters a node [using ajax]
 * it should return the info to navigate to child nodes
 * @author Gabriel
 *
 */
public class BehaviorNode implements Executable {
	int height;
	private BehaviorNode parent; 
	private String name;
	private List<BehaviorNode> childNodes = null;
	
	public BehaviorNode(String name, int height) {
		this.name = name;
		this.height = height;
		this.childNodes = new ArrayList<BehaviorNode>();
		this.parent = null;
	}

	public void addBehaviorNode(BehaviorNode node, BehaviorNode parent) {
		node.setParent(parent);
		this.childNodes.add(node);
	}
	
	public List<BehaviorNode> getChildNodes() {
		return childNodes;
	}

	public void setChildNodes(List<BehaviorNode> childNodes) {
		this.childNodes = childNodes;
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
	}
	
}
