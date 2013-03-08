package br.com.evans.mapping.basic;

import java.util.HashMap;
import java.util.Map;

public class WordMapping {
	private String commandToConvert;
	public Map<String, String> map;
	private Map<String, Device> deviceMap;
	public WordMapping() {
		map = new HashMap<String, String>();
		// maps the house in the format command <-> localization
		map.put("room lights" , "o_");
		map.put("bathroom lights" , "test1_"); // 2
		map.put("kitchen lights" , "test2_"); // 3
		map.put("garden lights" , "test3_"); // 4
		map.put("room secondary lights" , "test4_"); //5
		map.put("air conditioner" , "test5_");
		map.put("corridor lights" , "test6_");
		map.put("test lights" , "test7_");
		
		deviceMap = new HashMap<String, Device>();
		deviceMap.put("room lights", 			new Device(0, "o_", false, true));
		deviceMap.put("bathroom lights", 		new Device(1, "test1_", false, true));
		deviceMap.put("kitchen lights", 		new Device(1, "test2_", false, true));
		deviceMap.put("garden lights", 			new Device(1, "test3_", false, true));
		deviceMap.put("room secondary lights", 	new Device(1, "test4_", false, true));
		deviceMap.put("air conditioner", 		new Device(1, "test5_", false, true));
		deviceMap.put("corridor lights", 		new Device(1, "test6_", false, true));
		deviceMap.put("test lights", 			new Device(1, "test7_", false, true));
	}
	
	public String toArduino() {
//		return map.get(commandToConvert);
		return deviceMap.get(commandToConvert).getRf_code();
	}
	
	public void setCommandToConvert(String commandToConvert) {
		this.commandToConvert = commandToConvert;
	}
	
	public String getCommandToConvert() {
		return commandToConvert;
	}
}