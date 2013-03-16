package br.com.evans.jndi.states;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import br.com.evans.devices.arduino.ArduinoDevice;
import br.com.evans.devices.arduino.RfCoded;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public enum DeviceMonitor { // this is a SINGLETON!
	INSTANCE;
	private Map<Integer, ArduinoDevice> arduinoDevices;
	private boolean isOutdated;
	
	DeviceMonitor() {
		System.out.println("[STATUS] Device Monitor singleton was created. Populating the state map");
		
		this.arduinoDevices = new HashMap<Integer, ArduinoDevice>();
		// maps the house in the format order <-> device
		arduinoDevices.put(0, new RfCoded("air conditioner", false, "test1_"));
		arduinoDevices.put(1, new RfCoded("bathroom lights", false, "test2_"));
		arduinoDevices.put(2, new RfCoded("corridor lights", false, "test3_"));
		arduinoDevices.put(3, new RfCoded("garden lights", false,   "test4_"));
//		arduinoDevices.put(4, new RfCoded("kitchen lights", false, "test5_"));
//		arduinoDevices.put(5, new RfCoded("room secondary lights", false, "test6_"));
//		arduinoDevices.put(6, new RfCoded("test lights", false, "test_7"));
		arduinoDevices.put(7, new RfCoded("test room lights", false, "o_"));
		this.isOutdated = false;
	}
	
	public ArduinoDevice getDevice(String mapLocation) {
		for(ArduinoDevice each : arduinoDevices.values())
		{
			if (each.getLocation().equals(mapLocation)) {
				return each;
			}
		}
		return null;
	}
	
	public boolean isOutdated() {
		return isOutdated;
	}

	public void setOutdated(boolean isOutdated) {
		this.isOutdated = isOutdated;
	}

	public TreeMap<Integer, ArduinoDevice> getStatesTreeMap() {
		return new TreeMap<Integer, ArduinoDevice>(arduinoDevices);
	}

	/**
	 * Creates a json map for the reverse ajax, so that javascript populates 
	 * the device list
	 * @return String of the json map
	 */
	public String createJsonMap() {
		JSONObject json = new JSONObject();
		JSONArray deviceList = new JSONArray();
		JSONObject device;
                //creates a ordered map of devices
        TreeMap<Integer, ArduinoDevice> treeMap = new TreeMap<Integer, ArduinoDevice>(arduinoDevices);
		Integer[] index = (Integer[])( treeMap.keySet().toArray( new Integer[treeMap.size()] ) );
		for (int i = 0; i < index.length; i++) {
			device = new JSONObject();
			device.put("device_name", treeMap.get(index[i]).getLocation());
			device.put("state", treeMap.get(index[i]).getDeviceStatus());
//			device.put("shift_register", this.toArduinoShiftRegisterCode());
//			System.out.println(index[i]);
			deviceList.add(device);
		}
		
		json.put("deviceList", deviceList);
		return json.toString();
	}
}
