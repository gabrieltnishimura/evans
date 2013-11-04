package br.com.evans.behavior.nodes.core;

import java.io.IOException;
import java.util.List;

import javax.naming.NamingException;

import org.bson.types.ObjectId;

import br.com.evans.command.repository.CommandRepository;

public class MusicBehavior extends BehaviorNode {

	public MusicBehavior(int id, String name, List<String> responseWhenAccessed) {
		super(id, name, responseWhenAccessed);
	}
	
	public MusicBehavior(ObjectId internalId) {
		super(internalId);
	}
	
	@Override
	public void execute(List<String> parameters) {
		CommandRepository repository = new CommandRepository();
		if (parameters.size() == 1 && parameters.get(0).equals("on")) {
			try {
				repository.proccesNode(super.getName(), "", "", "", true);
			} catch (NamingException | IOException e) {
				e.printStackTrace();
			}
		} else if (parameters.size() == 1 && parameters.get(0).equals("off")) {
			try {
				repository.proccesNode(super.getName(), "", "", "", true);
			} catch (NamingException | IOException e) {
				e.printStackTrace();
			}
		} else if (parameters.isEmpty()) {
			try {
				repository.proccesNode(super.getName(), "", "", "", true);
			} catch (NamingException | IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("[ERROR] [DeviceBehavior] No valid parameters were passed.");
		}
	}
	
}
