package br.com.evans.jndi.states;

import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import br.com.evans.devices.arduino.RfCoded;
import br.com.evans.devices.core.Device;
import br.com.evans.notifications.core.Notifications;

public class SerialAnswerMonitor {
	public SerialAnswerMonitor() {
		this.generator.start();
	}
	
	private final Thread generator = new Thread("Serial Answer Monitoring") {
		@Override
		public void run() {
			while (!Thread.currentThread().isInterrupted()) {
				try {
					Context initCtx = new InitialContext();
	    			Context envCtx = (Context) initCtx.lookup("java:comp/env");
	    			
	    			//Get the device monitor so it can get device states
	    			DeviceMonitor deviceMonitor = (DeviceMonitor) envCtx.lookup("states/DeviceMonitorFactory");
	    			
	    			if (deviceMonitor.hadSerialAnswer()) {
	    				String feedback = deviceMonitor.getFeedback();
				    	for(Map.Entry<Integer, Device> entry : deviceMonitor.getStatesTreeMap().entrySet()) {
			    			Device value = entry.getValue();
			    			if (value instanceof RfCoded && ((RfCoded) value).getFeedbackId().equals(feedback.toLowerCase())) {
			    				System.out.println(Notifications.NOTIF_SWITCHING_VIRTUAL_STATUS);
			    					if (!value.getDeviceStatus() && Character.isUpperCase(feedback.charAt(0))) {
			    						((RfCoded) value).switchStatesFromFeedBack();				    				  
			    				} else if (value.getDeviceStatus() && Character.isLowerCase(feedback.charAt(0))) {
										((RfCoded) value).switchStatesFromFeedBack();
								}
							}
				    	}
	    			}
				} catch (NamingException e) {
		        	System.out.println(Notifications.EXCEP_DEVICEMONITOR_CONTEXT + "(Serial Answer Monitoring)");
		        	System.out.println("Killing [" + Thread.currentThread().getName() + "] thread to stop exception propagating.");
		        	Thread.currentThread().interrupt();
					e.printStackTrace();
				}
			}
		}
	};

}
