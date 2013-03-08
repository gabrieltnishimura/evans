package br.com.evans.mapping.basic;

/**
 * There will be lots of devices from now on.
 * A device can be a Arduino, a x10 module, or even a EletricImp
 * As the implementation of each device must have
 * it's own interface defined 
 * (whether is a in/out, what kind of communication it
 * should stablish with serial/rf/wifi, and what kind of
 * information it should output to the device)
 * 
 * 1) Each device will have its own switching state method implementation,
 * for instance, it is a light connected to a arduino through a shift 
 * registered relay on pin 3. 
 * 2) The implementation method should handle the arduino connection 
 * and send a string that contains a shift register binary converted 0-255
 * 3) The arduino should receive correctly the string, change the states 
 * and send a string back, reporting the states 
 * @author Gcats
 *
 */
public class Device {
	private int id;
	private String rf_code;
	private boolean status;
	private boolean isOutput;
	
	/**
	 * Constructor of Device class
	 * @todo [id] must make something better than this to order devices
	 * 
	 * @param id id of the device, defines the order of the device when shiftregistering. 
	 * @param rf_code radio frequency code of the device, when sent to arduino it should alter states
	 * @param status status of the device: whether is on/off
	 * @param isOutput 
	 */
	public Device(int id, String rf_code, boolean status, boolean isOutput) {
		this.id = id;
		this.rf_code = rf_code;
		this.status = status;
		this.isOutput = isOutput;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getRf_code() {
		return rf_code;
	}
	
	public void setRf_code(String rf_code) {
		this.rf_code = rf_code;
	}
	
	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public boolean isOutput() {
		return isOutput;
	}

	public void setOutput(boolean isOutput) {
		this.isOutput = isOutput;
	}
	
}
