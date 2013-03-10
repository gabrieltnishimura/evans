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
 * ---------------------------- First Example --------------------------------
 * 1) Each device will have its own switching state method implementation,
 * for instance, it is a light connected to a arduino through a shift 
 * registered relay on pin 3. 
 * 2) The implementation method should handle the arduino connection 
 * and send a string that contains a shift register binary converted 0-255
 * 3) The arduino should receive correctly the string, change the states 
 * and send a string back, reporting the states (with a decimal number) to 
 * the mainframe (this server), device order has to be strictly defined so that
 * the mainframe doesn't mix up which device it should turn on/off
 * 3.5) A function similar to toArduinoShiftRegisterCode from DeviceMonitor must 
 * be made on the arduino, so manual overrides work
 * 4) This way manual overrides also get reported to the mainframe (this server)
 * 
 * ---------------------------- Second Example --------------------------------
 * 1) For instance, it is a light connected to a arduino through a relay 
 * on pin 4 of the arduino 
 * 2) The implementation method should handle the arduino connection 
 * and send (via serial) a string that contains the string linked to the 
 * device with the right format ("o_")
 * 3) The arduino should receive correctly the string, change the states 
 * and send a string back, reporting the states to the mainframe (this server)
 * It can send two kinds of information. Maybe a binary -> decimal converted
 * number (assuming you know the order of the device in the pin	)
 * 4) This way manual overrides also get reported to the mainframe (this server)
 * 
 * ---------------------------- Third Example --------------------------------
 * 1) For instance, it is a light connected to a arduino through x10 
 * appliance module
 * 2) The implementation method should handle the x10 connection 
 * and send (via serial) the state changing string to the x10 transmitter
 * 3) The x10 does all the work, It only doesn't report states.
 * 
 * ---------------------------- Forth Example --------------------------------
 * 1) For instance, it is a input connected to a EletricImp 
 * 2) The implementation method should handle a POST from the EletricImp,
 * understanding what kind of info will be transmitted, so data can be 
 * processed correctly
 * @author Gcats
 *
 */
public class Device implements DeviceImpl {
	private int id;
	private String rf_code;
	private boolean status;
	
	/**
	 * Constructor of Device class
	 * @todo [id] must make something better than this to order devices
	 * 
	 * @param id id of the device, defines the order of the device when shiftregistering. 
	 * @param rf_code radio frequency code of the device, when sent to arduino it should alter states
	 * @param status status of the device: whether is on/off
	 * @param isOutput 
	 */
	public Device(int id, boolean status) {
		this.id = id;
		this.status = status;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public boolean getDeviceStatus() {
		return status;
	}

	@Override
	public void switchStates() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getInterfaceState() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
