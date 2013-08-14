package br.com.evans.behavior.nodes.core;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import br.com.evans.jndi.manager.JNDIManager;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class BehaviorNodeDAO {
	private BehaviorNode parent = null;
	
	public BehaviorNode retrieveNodes(BehaviorNode node) {
		DBCollection collection = JNDIManager.getMongoCollection("nodes");
		if (collection != null) {
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
					
					childNode = this.retrieveNodes(childNode);
					childs.add(childNode);
				}
			}
			node.setChilds(childs);
			
			return node;
		}
		return null;
	}
	
	
	/**
	 * Insert node, then insert relations;
	 * make it parent, get internal id.
	 * save children as a recursive operation
	 * @param node
	 * @return
	 */
	public ObjectId saveNode(BehaviorNode node) {
		DBCollection collection = JNDIManager.getMongoCollection("nodes");
		if (collection != null) { // catching exception
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
		}
		return null;
	}
	
	public BehaviorNode getBasicParentFromDB() {
		DBCollection collection = JNDIManager.getMongoCollection("nodes");
		if (collection != null) {
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
		}
		return null;
	}
	/*TODO complete function updateNode*/
	public boolean updateNode(BehaviorNode node) {
		DBCollection collection = JNDIManager.getMongoCollection("nodes");
		if (collection != null) {
			BasicDBObject newDocument = new BasicDBObject();
			newDocument.put("name", node.getName());
			newDocument.put("responses", node.getResponses());
			newDocument.put("type", node.getType());
			newDocument.put("name", node.getName());
			newDocument.put("name", node.getName());
		 
			BasicDBObject searchQuery = new BasicDBObject().append("id", node.getId());
		 
			collection.update(searchQuery, newDocument);
			
			
			BasicDBObject doc = new BasicDBObject("id", node.getId()).
	                append("name", node.getName());
	                // append("parent", node.getParent().getId()). // parent mongodb id
			//List<String> responses = new ArrayList<String>();
			
	        /*for (BehaviorNode childs : node.getChilds()) {
	        	doc.append(childs, val)
	        }*/
	
	        collection.insert(doc);
	
			return true;
		} 
		return false;
	}
	
	public BehaviorNode findNode(BehaviorNode node) {
		
		return node;
	}


	public BehaviorNode getParent() {
		return parent;
	}


	public void setParent(BehaviorNode parent) {
		this.parent = parent;
	}
}
