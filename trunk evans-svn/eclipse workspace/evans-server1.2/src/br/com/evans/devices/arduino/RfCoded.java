package br.com.evans.devices.arduino;

import java.io.IOException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import br.com.evans.jndi.arduino.ArduinoConnection;
import br.com.evans.jndi.states.DeviceMonitor;

public class RfCoded extends ArduinoDevice {
	String rfId;
	String feedbackId;
	
	public RfCoded(String map_code, boolean isOn, String rfId, String feedbackId) {
		super(map_code, isOn); //instanciates ArduinoDevice with state and map_code @param
		this.rfId = rfId;
		this.setFeedbackId(feedbackId);
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
				arduino.writeInOutput(this.rfId.toUpperCase() + "_");
			} else {
				arduino.writeInOutput(this.rfId.toLowerCase() + "_");
			}
			
			/* in case the system is waiting for the feedback
			while(!deviceMonitor.hadSerialAnswer()) {
				if (this.getDeviceStatus()) {
					arduino.writeInOutput(this.rfId.toUpperCase() + "_");
				} else {
					arduino.writeInOutput(this.rfId.toLowerCase() + "_");
				}
				
				try {
				    Thread.sleep(1000);
				} catch(InterruptedException ex) {
				    Thread.currentThread().interrupt();
				}
			}*/
			
			deviceMonitor.setOutdated(true); // notify reverseAjax

		} catch (IOException e) {
			e.printStackTrace();
		} catch (NamingException e) {
        	System.out.println("[EXCEPTION] Problem when trying to load the context of ArduinoFactory or DeviceMonitorFactory");
			e.printStackTrace();
		}
	}

	public String getFeedbackId() {
		return feedbackId;
	}

	public void setFeedbackId(String feedbackId) {
		this.feedbackId = feedbackId;
	}

	//TODO alter program structure so it has no repeated code 
	public void switchStatesFromFeedBack() {
		super.switchStates();
		Context initCtx;
    	try {
			initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env"); //get java naming context
			
			//Get the device monitor so it can change the device state
			DeviceMonitor deviceMonitor = (DeviceMonitor) envCtx.lookup("states/DeviceMonitorFactory"); //getting the connection can get a little costy(process) I guess
			deviceMonitor.setSerialAnswer(false);
			deviceMonitor.setOutdated(true); // notify reverseAjax
			
		} catch (NamingException e) {
        	System.out.println("[EXCEPTION] Problem when trying to load the context of ArduinoFactory or DeviceMonitorFactory");
			e.printStackTrace();
		}
	}

	@Override
	public void configure(Object[] args) {		
	}
	
}
