package br.com.evans.devices.arduino;

import java.io.IOException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import br.com.evans.jndi.arduino.ArduinoConnection;
import br.com.evans.jndi.states.DeviceMonitor;

public class RfCoded extends ArduinoDevice {
	String rfId;
	
	public RfCoded(String map_code, boolean isOn, String rfId) {
		super(map_code, isOn); //instanciates ArduinoDevice with state and map_code @param
		this.rfId = rfId;
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
			deviceMonitor.setOutdated(true); // notify reverseAjax
			
			//Get the arduino Connection so it can send data over serial
			ArduinoConnection arduino = (ArduinoConnection) envCtx.lookup("arduino/ArduinoConnectionFactory");
			if (this.getDeviceStatus()) {
				arduino.writeInOutput(this.rfId.toUpperCase() + "_"); // ending character '_'
			} else {
				arduino.writeInOutput(this.rfId.toLowerCase() + "_"); // ending character '_'
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NamingException e) {
        	System.out.println("[EXCEPTION] Problem when trying to load the context of ArduinoFactory or DeviceMonitorFactory");
			e.printStackTrace();
		}
	}

}
