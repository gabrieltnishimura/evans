package br.com.evans.devices.arduino;

import java.io.IOException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import br.com.evans.jndi.arduino.ArduinoConnection;
import br.com.evans.jndi.states.DeviceMonitor;

/**
 * Temperature table
 * 18 - 30
 * 02 - 0E
 * 
 * Speed Mode
 * 0 - Auto
 * 1 - Cool
 * 2 - Dry
 * 3 - Fan
 * 
 * Fan Mode
 * 0 - Auto
 * 1 - High
 * 2 - Med
 * 3 - Low
 * 4 - Quiet
 * 
 * message has format
 * 
 * j_ - to turn off
 * J0E
 * 
 * */

public class AirConditioner extends ArduinoDevice {
	
	String temperature; // 02 - 0E (18C ~ 30C)
	int fanMode;
	int speedMode;
	boolean upToDate;
	String code;
	
	public AirConditioner(String location, boolean isOn) {
		super(location, isOn);
		this.init(21, 0, 0, "test air conditioner"); // 21 degrees, auto, auto
	}

	public AirConditioner(String location, boolean isOn, int initialTemp, int fanMode, int speedMode, String code) {
		super(location, isOn);
		this.init(initialTemp, fanMode, speedMode, code);
	}
	
	private void init(int initialTemp, int fanMode, int speedMode, String code) {
		
		this.temperature = Integer.toHexString(initialTemp - 16);
		this.fanMode = fanMode;
		this.speedMode = speedMode;
		this.code = code;
		
	}
	
	@Override // implements a rfcoded state switch also
	public void switchStates() {
		super.switchStates(); // change states as defined in ArduinoDevice
		Context initCtx;
    	try {
			initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env"); //get java naming context
			
			//Get the device monitor so it can change the device state
			DeviceMonitor deviceMonitor = (DeviceMonitor) envCtx.lookup("states/DeviceMonitorFactory"); //getting the connection can get a little costy(process) I guess
			//Get the arduino Connection so it can send data over serial
			ArduinoConnection arduino = (ArduinoConnection) envCtx.lookup("arduino/ArduinoConnectionFactory");
			
			if (this.getDeviceStatus()) {
				arduino.writeInOutput(this.code.toUpperCase() + this.temperature + this.fanMode + this.speedMode + "_");
				upToDate = true;
			} else {
				arduino.writeInOutput(this.code + "_");
				upToDate = true;
			}
			
			deviceMonitor.setOutdated(true); // notify reverseAjax

		} catch (NamingException e) {
        	System.out.println("[EXCEPTION] Problem when trying to load the context of ArduinoFactory or DeviceMonitorFactory");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	@Override
	/**
	 * Receives a Object array that configures the air conditioner in the format:
	 * 
	 * args = {temperature, fanMode, speedMode}
	 */
	public void configure(Object[] args) {
		if (args != null && args[1] != null && args[2] != null && args[3] != null) {
			this.temperature = Integer.toHexString((int)args[1] - 16);
			this.fanMode = (int) args[2];
			this.speedMode = (int) args[3];
		} else {
			System.out.println("[ERROR]Couldn't configure air conditioner, null arguments parameters received");
		}

	}

}
