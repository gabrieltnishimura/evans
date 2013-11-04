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
 * J201 - to turn on with 18C, auto high
 * 
 * */

public class AirConditioner extends ArduinoDevice {
	
	int temperature; // 02 - 0E (18C ~ 30C) - intended temperature
	int tempAtLocation; // current temperature
	int fanMode;
	int speedMode;
	String code;
	
	public AirConditioner(String location, boolean isOn) {
		super(location, isOn);
		this.init(21, 1, 1, "test air conditioner", 32); // 21 degrees, auto, auto
	}

	public AirConditioner(String location, boolean isOn, int initialTemp, int fanMode, int speedMode, String code, int tempAtLocation) {
		super(location, isOn);
		this.init(initialTemp, fanMode, speedMode, code, tempAtLocation);
	}
	
	private void init(int initialTemp, int fanMode, int speedMode, String code, int tempAtLocation) {
		
		this.temperature = initialTemp;
		this.fanMode = fanMode;
		this.speedMode = speedMode;
		this.code = code;
		this.tempAtLocation = tempAtLocation;
		
	}
	
	@Override // also implements a rfcoded state switch
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
				arduino.writeInOutput(this.code.toUpperCase() + Integer.toHexString(this.temperature - 16) + this.fanMode + this.speedMode + "_");
			} else {
				arduino.writeInOutput(this.code + "_");
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
			this.temperature = (int) args[1]; // conversion from celsius degrees to hex coded string
			this.fanMode = (int) args[2];
			this.speedMode = (int) args[3];
			updateConfiguration();
		} else {
			System.out.println("[ERROR]Couldn't configure air conditioner, null arguments parameters received");
		}

	}

	private void updateConfiguration() {
		super.switchStates(); // maybe this is not needed

		Context initCtx;
    	try {
			initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env"); //get java naming context
			
			//Get the device monitor so it can change the device state
			DeviceMonitor deviceMonitor = (DeviceMonitor) envCtx.lookup("states/DeviceMonitorFactory"); //getting the connection can get a little costy(process) I guess
			//Get the arduino Connection so it can send data over serial
			((ArduinoConnection) envCtx.lookup("arduino/ArduinoConnectionFactory")).writeInOutput(
					this.code.toUpperCase() + Integer.toHexString(this.temperature - 16) + this.fanMode + this.speedMode + "_"
			);
			
			deviceMonitor.setOutdated(true); // notify reverseAjax

		} catch (NamingException e) {
        	System.out.println("[EXCEPTION] Problem when trying to load the context of ArduinoFactory or DeviceMonitorFactory");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void controlTemperature() {
		if (this.tempAtLocation > this.temperature && super.getDeviceStatus()) { // it's damn hot and air conditioner is somehow off
			this.switchStates();
		}
	}

	public void controlTemperature(int currentTemperature) {
		this.tempAtLocation = currentTemperature;
	
		if (this.tempAtLocation > this.temperature && !super.getDeviceStatus()) { // it's damn hot and air conditioner is somehow off
			this.switchStates();
		} else if (this.tempAtLocation == this.temperature && super.getDeviceStatus()) { // it's not that hot anymore
			this.switchStates();
		}
	}
}
