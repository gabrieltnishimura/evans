package br.com.evans.behavior.nodes.core;

import java.io.IOException;
import java.util.List;

import javax.naming.NamingException;

import br.com.evans.command.repository.CommandRepository;

public class MusicBehavior extends BehaviorNode {

	public MusicBehavior(String name, int height) {
		super(name, height);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void execute(List<String> parameters) {
		CommandRepository repository = new CommandRepository();
		if (parameters.size() == 1 && parameters.get(0).equals("on")) {
			try {
				repository.proccesNode(super.getName(), "", "", "", true);
			} catch (NamingException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (parameters.size() == 1 && parameters.get(0).equals("off")) {
			try {
				repository.proccesNode(super.getName(), "", "", "", true);
			} catch (NamingException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (parameters.isEmpty()) {
			try {
				repository.proccesNode(super.getName(), "", "", "", true);
			} catch (NamingException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.out.println("[ERROR] [DeviceBehavior] No valid parameters were passed.");
		}
	}
	
}
