package br.com.evans.devices.arduino;

import java.io.IOException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import br.com.evans.jndi.basic.ArduinoConnection;
import br.com.evans.jndi.states.DeviceMonitor;

public class RfCoded extends ArduinoDevice {
	String rfId;
	
	public RfCoded(String map_code, boolean isOn, String rfId) {
		super(map_code, isOn);
		this.rfId = rfId;
	}
	
	@Override // implements a rfcoded state switch
	public void switchStates() {
		super.switchStates(); // change states
		Context initCtx;
    	try {
			initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			//Get the device monitor so it can change the device state
			DeviceMonitor deviceMonitor = (DeviceMonitor) envCtx.lookup("states/DeviceMonitorFactory"); //getting the connection can get a little costy(process) I guess
			deviceMonitor.setOutdated(true);
			//Get the arduino Connection so it can send data over serial
			ArduinoConnection arduino = (ArduinoConnection) envCtx.lookup("arduino/ArduinoConnectionFactory");
			arduino.writeInOutput(this.rfId); // ending character '_'
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NamingException e) {
        	System.out.println("[EXCEPTION] Problem when trying to load the context of ArduinoFactory or DeviceMonitorFactory");
			e.printStackTrace();
		}
	}

}