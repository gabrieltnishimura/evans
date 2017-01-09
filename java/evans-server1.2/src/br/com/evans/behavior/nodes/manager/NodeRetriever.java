package br.com.evans.behavior.nodes.manager;

import br.com.evans.behavior.nodes.core.BehaviorNode;
import br.com.evans.behavior.nodes.core.BehaviorNodeDAO;

public class NodeRetriever {
	
	
	/**
	 * Populate parent node from NoSQL database
	 * @return
	 */
	public BehaviorNode retrieveParentNode() {
		/*// performance ?
		if (this.parent == null) {
			System.out.println("[NODES] Retrieving nodes from database");
			this.parent = this.getNodes(this.getBasicParentFromDB());
		}*/
		BehaviorNodeDAO dao = new BehaviorNodeDAO();
		BehaviorNode parent = dao.retrieveNodes(dao.getBasicParentFromDB());
		
		/*List<String> response = new ArrayList<String>();
		response.add("Welcome to E.V.A.N.S., please issue a command, such as lights, music, status or temperature");
		response.add("Hello user, tell me what I should do.");
		response.add("You're using E.V.A.N.S. Home Automation System ver1.2. Waiting for user input...");
		response.add("evans_control ->_ ");
		response.add("E.V.A.N.S. Demonstration: response through reverse ajax: ");

		
		BehaviorNode parent = new BehaviorNode(0, "menu", response, "vertex");
		BehaviorNode lights = new BehaviorNode(1, "lights", response, "vertex");
		BehaviorNode music = new BehaviorNode(2, "music", response, "vertex");
		BehaviorNode status = new BehaviorNode(3, "status", response, "vertex");
		BehaviorNode temperature = new BehaviorNode(4, "temperature", response, "vertex");
		
		lights.addBehaviorNode(new BehaviorNode(5, "kitchen", response, "device"), lights);
		lights.addBehaviorNode(new BehaviorNode(6, "bedside", response, "device"), lights);
		lights.addBehaviorNode(new BehaviorNode(7, "doorside", response, "device"), lights);
		lights.addBehaviorNode(new BehaviorNode(8, "bathroom", response, "device"), lights);
		lights.addBehaviorNode(new BehaviorNode(9, "room", response, "device"), lights);
		
		music.addBehaviorNode(new BehaviorNode(10, "shuffle", response, "music"), music);
		music.addBehaviorNode(new BehaviorNode(11, "songs by", response, "music"), music);
		music.addBehaviorNode(new BehaviorNode(12, "feeling like", response, "music"), music);
		
		status.addBehaviorNode(new BehaviorNode(13, "room status", response, "report"), status);
		status.addBehaviorNode(new BehaviorNode(14, "current temperature", response, "report"), status);
		status.addBehaviorNode(new BehaviorNode(15, "system status", response, "report"), status);
		
		temperature.addBehaviorNode(new BehaviorNode(16, "it's hot", response, "device"), temperature);
		temperature.addBehaviorNode(new BehaviorNode(17, "it's too cold", response, "device"), temperature);
		temperature.addBehaviorNode(new BehaviorNode(18, "timer", response, "device"), temperature);
		
		parent.addBehaviorNode(lights, parent);
		parent.addBehaviorNode(music, parent);
		parent.addBehaviorNode(status, parent);
		parent.addBehaviorNode(temperature, parent);*/
		
		//dao.saveNode(parent);
		
		return parent;
	}
	
}
