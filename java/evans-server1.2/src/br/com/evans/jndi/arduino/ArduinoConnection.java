package br.com.evans.jndi.arduino;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import javax.naming.Context;
import javax.naming.InitialContext;

import br.com.evans.devices.arduino.AirConditioner;
import br.com.evans.jndi.states.DeviceMonitor;
import br.com.evans.notifications.core.Notifications;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

/**
 * This class is the class that creates the serial connection
 * between the arduino and the computer when initialize() is 
 * invoked.
 * 
 * @author Gcats
 *
 */
public enum ArduinoConnection implements SerialPortEventListener {
	INSTANCE;
	
	SerialPort serialPort;
        /** The port we're normally going to use. */
	String PORT_NAMES[] = { 
			"COM3" // Windows
			};
	/** Buffered input stream from the port */
	InputStream input;
	/** The output stream to the port */
	OutputStream output;
	/** Milliseconds to block while waiting for port open */
	int TIME_OUT = 2000;
	/** Default bits per second for COM port. */
	int DATA_RATE = 9600;

	/*
	 * To initialize you need to have the same DATA_RATE
	 * in the arduino (Serial.begin(DATA_RATE)) and in the computer
	 * serialPort.serialPortParams(...DATA_RATE...).
	 */
	public void initialize() {
		CommPortIdentifier portId = null;
		Enumeration<?> portEnum = CommPortIdentifier.getPortIdentifiers();

		// iterate through, looking for the port
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			for (String portName : PORT_NAMES) {
				if (currPortId.getName().equals(portName)) {
					portId = currPortId;
					break;
				}
			}
		}

		if (portId == null) {
			System.out.println(Notifications.ERROR_FIND_PORT + PORT_NAMES[0]);
			return;
		}

		try {
			// open serial port, and use class name for the appName.
			serialPort = (SerialPort) portId.open(this.getClass().getName(),
					TIME_OUT);

			// set port parameters
			serialPort.setSerialPortParams(DATA_RATE,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			// open the streams
			input = serialPort.getInputStream();
			System.out.println(Notifications.NOTIF_CREATED_ARDUINO_SINGLETON);
			output = serialPort.getOutputStream();
			
			// add event listeners
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}
	
	/**
	 * This writes a string to the serial output, so the arduino 
	 * can read it using Serial.available()
	 */
	public void writeInOutput(String string) throws IOException {
		System.out.println(Notifications.SERIAL_SENDING + string);
		byte[] stringByteArray = string.getBytes("UTF-8");
		output.write(stringByteArray);
	}
	
	/**
	 * This should be called when you stop using the port.
	 * This will prevent port locking on platforms like Linux.
	 */
	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	/**
	 * Handle an event on the serial port. Read the data and print/use it.
	 */
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				String received = new String(this.read(16));
				received = received.substring(0, received.length() - 2);
				System.out.println("[SERIAL] Received from arduino: '" + received + "'");
				
				Context initCtx = new InitialContext();
    			Context envCtx = (Context) initCtx.lookup("java:comp/env"); // must initialize context, do something with feedback
				
				if (received.contains("temp")) { //TODO feedback location (device) and temperature
					String[] temp = received.split("_|\\.");
						if (temp != null && temp[0] != null) {
			    			DeviceMonitor deviceMonitor = (DeviceMonitor) envCtx.lookup("states/DeviceMonitorFactory");
			    			((AirConditioner) deviceMonitor.getDevice("air conditioner")).controlTemperature(Integer.parseInt(temp[1]));
			    			System.out.println("[STATUS] Received temperature change feedback '"+temp[1]+"'");
						}
				} else {
	    			//received data from serial - a device changed states (not on temperature format)
	    			DeviceMonitor deviceMonitor = (DeviceMonitor) envCtx.lookup("states/DeviceMonitorFactory");
	    			deviceMonitor.setSerialAnswer(true);
	    			deviceMonitor.setFeedback(received.substring(received.length() - 1, received.length()));
	    			System.out.println("[STATUS] Setting feedback from serial as '"+received+"'");
				}
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}
		// Ignore all the other eventTypes, but you should consider the other ones.
	}
	
	public byte[] read(int intWaitTime) throws IOException {

        try { /* wait until transmission has ended*/
            Thread.sleep(intWaitTime);
        } catch (InterruptedException ex) {
        	System.out.println(Notifications.EXCEP_SLEEP + "ArduinoConnection");
        }

        byte[] buffer = new byte[input.available()];
        while (input.available() > 0) {
            input.read(buffer,0,input.available());
        }

        return buffer;
    }

}