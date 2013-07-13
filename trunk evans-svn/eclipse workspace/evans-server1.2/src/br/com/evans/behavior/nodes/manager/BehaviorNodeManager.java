package br.com.evans.behavior.nodes.manager;

import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import br.com.evans.behavior.nodes.core.BehaviorNode;
import br.com.evans.behavior.nodes.core.DeviceBehavior;
import br.com.evans.behavior.nodes.core.MusicBehavior;
import br.com.evans.behavior.nodes.core.ReportBehavior;

/**
 * This class manages nodes. For example on which node the user is
 * currently using. Must be designed for multiple instances of users
 * loading diferent nodes whenever they want.
 * Maybe it should be a singleton as well
 * @author Gabriel
 *
 */
public class BehaviorNodeManager {
	private BehaviorNode parent;
	private BehaviorNode currentBehavior; // multiple instances of currentBehavior
	
	public BehaviorNodeManager() {
		//System.out.println("[NODES] Initializing behavior nodes");		
		setParent(new BehaviorNode("menu", 0));
		
		BehaviorNode lights = new BehaviorNode("lights", 1);
		BehaviorNode music = new BehaviorNode("music", 1);
		BehaviorNode status = new BehaviorNode("status", 1);
		BehaviorNode temperature = new BehaviorNode("temperature", 1);
		
		lights.addBehaviorNode(new DeviceBehavior("kitchen", 2), lights);
		lights.addBehaviorNode(new DeviceBehavior("bedside", 2), lights);
		lights.addBehaviorNode(new DeviceBehavior("doorside", 2), lights);
		lights.addBehaviorNode(new DeviceBehavior("bathroom", 2), lights);
		lights.addBehaviorNode(new DeviceBehavior("room", 2), lights);
		
		music.addBehaviorNode(new MusicBehavior("shuffle", 2), music);
		music.addBehaviorNode(new MusicBehavior("songs by", 2), music);
		music.addBehaviorNode(new MusicBehavior("feeling like", 2), music);
		
		status.addBehaviorNode(new ReportBehavior("room status", 2), status);
		status.addBehaviorNode(new ReportBehavior("current temperature", 2), status);
		status.addBehaviorNode(new ReportBehavior("system status", 2), status);
		
		temperature.addBehaviorNode(new DeviceBehavior("it's hot", 2), temperature);
		temperature.addBehaviorNode(new DeviceBehavior("it's too cold", 2), temperature);
		temperature.addBehaviorNode(new DeviceBehavior("timer", 2), temperature);
		
		this.parent.addBehaviorNode(lights, this.parent);
		this.parent.addBehaviorNode(music, this.parent);
		this.parent.addBehaviorNode(status, this.parent);
		this.parent.addBehaviorNode(temperature, this.parent);
	}
	
	public BehaviorNode navigateTo(String nameOfBehavior) {
		for (BehaviorNode node : getCurrentBehavior().getChildNodes()) {
			if (node.getName().equals(nameOfBehavior)) {
				if (node.getChildNodes().isEmpty()) {
					System.out.println("[NODES] Node is empty, executing its instruction now.");
					node.execute(new ArrayList<String>());
				}
				return this.currentBehavior = node;
			}
		}
		return null;
	}
	
	public BehaviorNode navigateToParent() {
		this.currentBehavior = parent;
		return this.getCurrentBehavior();
	}
	
	public BehaviorNode navigateBackwards() {
		if (this.currentBehavior.getParent() != null) {
			this.currentBehavior = this.currentBehavior.getParent();
			return this.currentBehavior;
		} else {
			return null;
		}
	}

	public BehaviorNode getParent() {
		return parent;
	}

	public void setParent(BehaviorNode parent) {
		this.currentBehavior = parent;
		this.parent = parent;
	}

	public BehaviorNode getCurrentBehavior() {
		return currentBehavior;
	}

	public BehaviorNode setCurrentBehavior(BehaviorNode currentBehavior) {
		this.currentBehavior = currentBehavior;
		return currentBehavior;
	}
	
	public String createJsonMap() {
		JSONObject finalJSON = new JSONObject();
		JSONArray auxiliaryArray = new JSONArray();
		JSONObject auxiliaryJSON = new JSONObject();
		
		insertToJson(this.currentBehavior, auxiliaryArray, auxiliaryJSON, finalJSON, true);
		return finalJSON.toString();
	}
	
	/**
	 * Given the node estructure BehaviorNode, this function builds a json tree
	 * from a root node.
	 * @param node the parent node
	 * @param array a auxiliary json array
	 * @param auxiliaryJSON a auxiliary json object
	 * @param finalJSON the resulting json object
	 */
	private void insertToJson(BehaviorNode node, JSONArray array, JSONObject auxiliaryJSON, JSONObject finalJSON, boolean hasParents) {
		if (node.getParent() == null || hasParents) { // first to be added
			for (BehaviorNode child : node.getChildNodes()) {
				insertToJson(child, array, auxiliaryJSON, finalJSON, false);
			}
			if (auxiliaryJSON.size() == 0) {
				finalJSON.put("childs", array);
			} else {
				finalJSON.put("childs", auxiliaryJSON);	
			}
		} else if (node.getChildNodes().size() == 0) { // if has no childs
			JSONObject json = new JSONObject();
			json.put("name", node.getName());
			array.add(json);
		} else { // has childs but is not root
			for (BehaviorNode child : node.getChildNodes()) {
				insertToJson(child, array, auxiliaryJSON, finalJSON, false);
			}
			auxiliaryJSON.put(node.getName(), array);
			array.clear();
		}
	}

}
