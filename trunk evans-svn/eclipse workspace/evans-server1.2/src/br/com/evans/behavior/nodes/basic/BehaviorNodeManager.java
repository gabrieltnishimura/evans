package br.com.evans.behavior.nodes.basic;

/**
 * This class manages nodes. For example on which node the user is
 * currently using. Must be designed for multiple instances of users
 * loading diferent nodes whenever they want.
 *  
 * @author Gabriel
 *
 */
public class BehaviorNodeManager {
	private BehaviorNode parent;
	
	public BehaviorNodeManager() {
		setParent(new BehaviorNode(0));
	}
	
	public void addBehaviorNodeToParent(BehaviorNode node) {
	}

	public BehaviorNode getParent() {
		return parent;
	}

	public void setParent(BehaviorNode parent) {
		this.parent = parent;
	}
}
