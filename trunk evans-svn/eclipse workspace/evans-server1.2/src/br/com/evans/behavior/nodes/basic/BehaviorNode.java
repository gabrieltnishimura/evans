package br.com.evans.behavior.nodes.basic;

import java.util.ArrayList;
import java.util.List;

/**
 * Every time the user enters a node [using ajax]
 * it should return the info to navigate to child nodes
 * @author Gabriel
 *
 */
public class BehaviorNode {
	int height;
	boolean isCommand; // when true this behavior must do something - maybe with reflection (must be passed as parameter from the constructor)
	private List<BehaviorNode> childNodes = null;
	
	public BehaviorNode(int height) {
		this.height = height;
		this.childNodes = new ArrayList<BehaviorNode>();
	}

	public void addBehaviorNode(BehaviorNode node) {
		this.childNodes.add(node);
	}
	
	public List<BehaviorNode> getChildNodes() {
		return childNodes;
	}

	public void setChildNodes(List<BehaviorNode> childNodes) {
		this.childNodes = childNodes;
	}
	
}
