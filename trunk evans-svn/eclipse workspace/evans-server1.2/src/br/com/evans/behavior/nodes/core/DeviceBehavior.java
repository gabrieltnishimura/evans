package br.com.evans.behavior.nodes.core;

import java.util.List;

import org.bson.types.ObjectId;

import br.com.evans.command.repository.CommandRepository;

public class DeviceBehavior extends BehaviorNode {
	CommandRepository repository;
	public DeviceBehavior(int id, String mapLocation, List<String> responseWhenAccessed) {
		super(id, mapLocation, responseWhenAccessed);
		this.repository = null;
	}

	public DeviceBehavior(ObjectId internalId) {
		super(internalId);
	}
	
	@Override
	public void execute(List<String> parameters) {
		if (parameters.size() == 1 && parameters.get(0).equals("on")) {
			this.repository = new CommandRepository();
			this.repository.proccesNode(super.getName(), true);
		} else if (parameters.size() == 1 && parameters.get(0).equals("off")) {
			this.repository = new CommandRepository();
			this.repository.proccesNode(super.getName(), false);
		} else if (parameters.isEmpty()) {
			this.repository = new CommandRepository();
			this.repository.proccesNode(super.getName());
		} else {
			System.out.println("[ERROR] [DeviceBehavior] No valid parameters were passed.");
		}
	}
	
}
