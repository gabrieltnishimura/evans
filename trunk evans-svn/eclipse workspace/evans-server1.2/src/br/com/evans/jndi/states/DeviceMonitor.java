package br.com.evans.jndi.states;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import br.com.evans.devices.arduino.RfCoded;
import br.com.evans.devices.core.Device;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public enum DeviceMonitor { // this is a SINGLETON!
	INSTANCE;
	private Map<Integer, Device> devices; // made it a little more general - since x10 device also has the same implementation
	private boolean isOutdated;
	private boolean serialAnswer; // need the confirmation that the device really switched
	private String feedback; // in case the device switches manually
	private SerialAnswerMonitor serialMonitor;
	
	DeviceMonitor() {
		System.out.println("[STATUS] Device Monitor singleton was created. Populating the state map");
		
		this.devices = new HashMap<Integer, Device>();
		// maps the house in the format order <-> device
		/*arduinoDevices.put(0, new RfCoded("air conditioner", false, "test1", "test"));
		arduinoDevices.put(1, new RfCoded("bathroom lights", false, "test2", "test"));
		arduinoDevices.put(2, new RfCoded("corridor lights", false, "test3", "test"));
		arduinoDevices.put(3, new RfCoded("garden lights", false,   "test4", "test"));*/
		devices.put(4, new RfCoded("kitchen", false, "a", "b"));
		devices.put(5, new RfCoded("bedside", false, "c", "d"));
		devices.put(6, new RfCoded("doorside", false, "e", "f"));
		devices.put(7, new RfCoded("bathroom", false, "g", "h"));
		devices.put(8, new RfCoded("room", false, "o", "p"));
		this.isOutdated = false;
		this.serialAnswer = false;
		
		this.setSerialMonitor(new SerialAnswerMonitor()); // wait for changes in serial feedback
	}
	
	public Device getDevice(String mapLocation) {
		for(Device each : devices.values())
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

	public TreeMap<Integer, Device> getStatesTreeMap() {
		return new TreeMap<Integer, Device>(devices);
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
        TreeMap<Integer, Device> treeMap = new TreeMap<Integer, Device>(devices);
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

	public boolean hadSerialAnswer() {
		return serialAnswer;
	}

	public void setSerialAnswer(boolean serialAnswer) {
		this.serialAnswer = serialAnswer;
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	public SerialAnswerMonitor getSerialMonitor() {
		return serialMonitor;
	}

	public void setSerialMonitor(SerialAnswerMonitor serialMonitor) {
		this.serialMonitor = serialMonitor;
	}
}
