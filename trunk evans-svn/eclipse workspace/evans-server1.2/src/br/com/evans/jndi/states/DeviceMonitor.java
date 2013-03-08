package br.com.evans.jndi.states;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import br.com.evans.mapping.basic.Device;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public enum DeviceMonitor {
	INSTANCE;
	private String localizationToConvert;
	private Map<String, Device> states;
	private boolean isOutdated;
	
	DeviceMonitor() {
		System.out.println("[STATUS] Device Monitor singleton was created. Populating the state map");
		
		setStates(new HashMap<String, Device>());
		// maps the house in the format command <-> device
		states.put("air conditioner" , 			new Device(0, "test1_", false, true));
		states.put("bathroom lights" , 			new Device(1, "test2_", false, true));
		states.put("corridor lights" , 			new Device(2, "test3_", false, true));
		states.put("garden lights" , 			new Device(3, "test4_", false, true));
		states.put("kitchen lights" , 			new Device(4, "test5_", false, true));
		states.put("room secondary lights" , 	new Device(5, "test6_", false, true));
		states.put("test lights" , 				new Device(6, "test7_", false, true));
		states.put("test room lights" ,			new Device(7, "o_", false, true));
		setOutdated(false);
	}
	
	public String toArduinoRFCode() {
		return states.get(localizationToConvert).getRf_code();
	}

	public int toArduinoShiftRegisterCode() {
		String bit = "";
		
		int[] id = new int[states.size()];
		boolean[] status = new boolean[states.size()];
		
		for(Device each : states.values())
		{
			id[each.getId()] = each.getId();
			status[each.getId()] = each.isStatus();
		}
		
		Arrays.sort(id);
		
		for(int i = states.size() - 1; i >= 0; i--) {
			if (status[id[i]])
				bit += "1";
			else
				bit += "0";
		}
		
		return Integer.parseInt(bit, 2);
	}
	
	public Boolean toStatus() {
		return states.get(localizationToConvert).isStatus();
	}
	
	public void setLocalizationToConvert(String newLocalization) {
		this.localizationToConvert = newLocalization;
	}
	
	public String getLocalizationToConvert() {
		return localizationToConvert;
	}
	
	public boolean isOutdated() {
		return isOutdated;
	}

	public void setOutdated(boolean isOutdated) {
		this.isOutdated = isOutdated;
	}
	
	public Map<String, Device> getStates() {
		return states;
	}

	public void setStates(Map<String, Device> states) {
		this.states = states;
	}
	
	public TreeMap<String, Device> getStatesTreeMap() {
		return new TreeMap<String, Device>(states);
	}
	
	public void switchStatus() {
		if (states.get(localizationToConvert).isStatus()) {
			System.out.println("[STATUS] " + localizationToConvert + " was [ON] and changed to [OFF]");
			states.get(localizationToConvert).setStatus(false);
		} else {
			System.out.println("[STATUS] " + localizationToConvert + " was [OFF] and changed to [ON]");
			states.get(localizationToConvert).setStatus(true);
		}
		setOutdated(true); // notificates reverseAjax
	}

	/**
	 * Creates a json map for the reverse ajax, so that javascript populates 
	 * the device list
	 * @return
	 */
	public String createJsonMap() {
		JSONObject json = new JSONObject();
		JSONArray deviceList = new JSONArray();
		JSONObject device;
                //creates a ordered map of devices
        TreeMap<String, Device> treeMap = new TreeMap<String, Device>(states);
		String[] index = (String[])( treeMap.keySet().toArray( new String[treeMap.size()] ) );
		for (int i = 0; i < index.length; i++) {
			device = new JSONObject();
			device.put("device_name", index[i]);
			device.put("state", treeMap.get(index[i]).isStatus());
			device.put("shift_register", this.toArduinoShiftRegisterCode());
//			System.out.println(index[i]);
			deviceList.add(device);
		}
		
		json.put("deviceList", deviceList);
		return json.toString();
	}
}
