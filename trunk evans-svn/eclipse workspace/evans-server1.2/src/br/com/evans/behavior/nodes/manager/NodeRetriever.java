package br.com.evans.behavior.nodes.manager;

import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import br.com.evans.behavior.nodes.core.BehaviorNode;
import br.com.evans.behavior.nodes.core.DeviceBehavior;
import br.com.evans.behavior.nodes.core.MusicBehavior;
import br.com.evans.behavior.nodes.core.ReportBehavior;
import br.com.evans.jndi.db.MongoDBConnection;

public class NodeRetriever {
	BehaviorNode parent = null;
	
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
		this.parent = this.getNodes(this.getBasicParentFromDB());
		
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
		
		//saveNode(parent);
		
		return this.parent;
	}
	
	public BehaviorNode getNodes(BehaviorNode node) {
		Context initCtx;
		try {
			initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			MongoDBConnection mongo = (MongoDBConnection) envCtx.lookup("db/MongoDBConnectionFactory"); // get db connection evans 
			DBCollection collection = mongo.getCollection("nodes");
			
			BasicDBObject searchQuery = node.getInternalId() == null ? 
					new BasicDBObject().append("id", node.getId()) : 
					new BasicDBObject().append("_id", node.getInternalId());
			
			DBObject nodeResult = collection.findOne(searchQuery);	
			node.setId((int) nodeResult.get("id"));
			node.setName((String) nodeResult.get("name"));
			//System.out.println("Processing node " + node.getName());
			
			List<String> responses = new ArrayList<String>();
			BasicDBList responseList = (BasicDBList) nodeResult.get("responses");
			for (int i = 0; i < responseList.size(); i++) {
				responses.add((String) responseList.get(i));
			}
			
			node.setResponses(responses);
			
			List<BehaviorNode> childs = new ArrayList<BehaviorNode>();
			BasicDBList childList = (BasicDBList) nodeResult.get("childs");
			if (childList != null) {
				for (int i = 0; i < childList.size(); i++) {
					ObjectId childId = (ObjectId) childList.get(i);
					BasicDBObject findType = new BasicDBObject().append("_id", childId);
					DBObject typeResult = collection.findOne(findType);	
					BehaviorNode childNode = null;
					
					if (typeResult.get("type").equals("device")) {//edge
						childNode = new DeviceBehavior(childId);
					} else if (typeResult.get("type").equals("music")) { //edge
						childNode = new MusicBehavior(childId);
					} else if (typeResult.get("type").equals("report")) { //edge
						childNode = new ReportBehavior(childId);
					} else { // vertex
						childNode = new BehaviorNode(childId); // need to specify child class (device, music, report)
					}
					
					childNode = this.getNodes(childNode);
					childs.add(childNode);
				}
			}
			node.setChilds(childs);
			
			return node;
        } catch (NamingException e) {
        	System.out.println("[EXCEPTION] Problem when trying to load the context of MongoDBFactory, couldn't get node");
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * Insert node, then insert relations;
	 * make it parent, get internal id.
	 * save children as a recursive operation
	 * @param node
	 * @return
	 */
	public ObjectId saveNode(BehaviorNode node) {
		Context initCtx;
		try {
			initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			MongoDBConnection mongo = (MongoDBConnection) envCtx.lookup("db/MongoDBConnectionFactory"); // get db connection evans 
			DBCollection collection = mongo.getCollection("nodes");
			
			BasicDBObject persistNode = new BasicDBObject();
			persistNode.put("id", node.getId());
			persistNode.put("name", node.getName());
			persistNode.put("type", node.getType());
			persistNode.put("responses", node.getResponses());
//			WriteResult result = collection.insert(persistNode);
			collection.insert(persistNode);
			
//			System.out.println(result.toString());
			List<ObjectId> childs = new ArrayList<ObjectId>();
			
			for (BehaviorNode child : node.getChilds()) {
				ObjectId childId = saveNode(child); 
				if (childId != null) {
					childs.add(childId); // recursivity
				}
			}
			
			ObjectId id = (ObjectId)persistNode.get( "_id" );
			persistNode.put("_id", id);
			
			BasicDBObject children = new BasicDBObject();
			if (!childs.isEmpty()) {			
				children.append("$set", new BasicDBObject().append("childs", childs));
			 
				BasicDBObject searchQuery = new BasicDBObject().append("_id", id);
				collection.update(searchQuery, children);
			}
			return id;
        } catch (NamingException e) {
        	System.out.println("[EXCEPTION] Problem when trying to load the context of MongoDBFactory, couldn't save node");
			e.printStackTrace();
			return null;
		}
	}
	
	public BehaviorNode getBasicParentFromDB() {
		Context initCtx;
		try {
			initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			MongoDBConnection mongo = (MongoDBConnection) envCtx.lookup("db/MongoDBConnectionFactory"); 
			DBCollection collection = mongo.getCollection("nodes");
			
			BasicDBObject searchQuery = new BasicDBObject().append("name", "menu");
			
			/*
			searchQuery.append("$not", new BasicDBObject().append("childs", null));
			DBCursor cursor = collection.find(searchQuery);
			*/
			
			DBObject parentResult = collection.findOne(searchQuery);
			List<String> responses = new ArrayList<String>();
			BehaviorNode parent = new BehaviorNode((int) parentResult.get("id"), (String) parentResult.get("name"), responses);
			
			/*List<ObjectId> idList = new ArrayList<ObjectId>();
			while(cursor.hasNext()) {
				ObjectId id = (ObjectId) cursor.next().get("_id");
				for (int i = 0; i < idList.size(); i++) {
					ObjectId eachId = idList.get(i);
					if (eachId.equals(id)) {
						idList.remove(id);
					}
				}
				
			}*/
			return parent;
        } catch (NamingException e) {
        	System.out.println("[EXCEPTION] Problem when trying to load the context of ArduinoFactory");
			e.printStackTrace();
			return null;
        } 
	}
	
	public boolean updateNode(BehaviorNode node) {
		Context initCtx;
		try {
			initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			MongoDBConnection mongo = (MongoDBConnection) envCtx.lookup("db/MongoDBConnectionFactory"); 
			DBCollection coll = mongo.getCollection("nodes");
			
			
			
			BasicDBObject doc = new BasicDBObject("id", node.getId()).
	                append("name", node.getName());
	                // append("parent", node.getParent().getId()). // parent mongodb id
			List<String> responses = new ArrayList<String>();
			
	        for (BehaviorNode childs : node.getChilds()) {
	        	//doc.append(childs, val)
	        }

			coll.insert(doc);
			
			
        } catch (NamingException e) {
        	System.out.println("[EXCEPTION] Problem when trying to load the context of ArduinoFactory");
			e.printStackTrace();
			return false;
        } 
		return true;
	}
	
	public BehaviorNode findNode(BehaviorNode node) {
		
		return node;
	}
}
