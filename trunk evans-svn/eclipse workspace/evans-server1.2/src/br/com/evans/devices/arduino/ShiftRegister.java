package br.com.evans.devices.arduino;

import java.util.Map;

public class ShiftRegister {
	private int order;
	private final Map<String, Integer> mapping;
	
	public ShiftRegister(Map<String, Integer> mapping) {
		this.mapping = mapping;
		for (Integer order : mapping.values()) {
			this.order = order;
		}
	}

	//	public int toArduinoShiftRegisterCode() {
	//	String bit = "";
	//	
	//	int[] id = new int[states.size()];
	//	boolean[] status = new boolean[states.size()];
	//	
	//	for(ArduinoDevice each : states.values())
	//	{
	//		id[each.getId()] = each.getId();
	//		status[each.getId()] = each.getDeviceStatus();
	//	}
	//	
	//	Arrays.sort(id);
	//	
	//	for(int i = states.size() - 1; i >= 0; i--) {
	//		if (status[id[i]])
	//			bit += "1";
	//		else
	//			bit += "0";
	//	}
	//	
	//	return Integer.parseInt(bit, 2);
	//}

}
