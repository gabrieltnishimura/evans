package br.com.evans.behavior.nodes.core;

import java.util.List;
import br.com.evans.command.repository.CommandRepository;



public class DeviceBehavior extends BehaviorNode {
	public DeviceBehavior(String mapLocation, int height) {
		super(mapLocation, height);
	}

	@Override
	public void execute(List<String> parameters) {
		if (parameters.size() == 1 && parameters.get(0).equals("on")) {
			CommandRepository repository = new CommandRepository();
			repository.proccesNode(super.getName(), true);
		} else if (parameters.size() == 1 && parameters.get(0).equals("off")) {
			CommandRepository repository = new CommandRepository();
			repository.proccesNode(super.getName(), false);
		} else if (parameters.isEmpty()) {
			CommandRepository repository = new CommandRepository();
			repository.proccesNode(super.getName());
		} else {
			System.out.println("[ERROR] [DeviceBehavior] No valid parameters were passed.");
		}
	}
	
}
