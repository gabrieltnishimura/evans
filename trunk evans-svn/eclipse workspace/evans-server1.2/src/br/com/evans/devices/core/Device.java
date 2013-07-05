package br.com.evans.devices.core;

public interface Device {
	/**
	 * Returns whether the device is on or off,
	 * Using a method to be defined
	 * @return status
	 */
	public boolean getDeviceStatus();
	
	/**
	 * Switch the device state
	 */
	public void switchStates();

	/**
	 * Returns the status of the interfacing device
	 * @TODO what is the interfacing device? what should I do in
	 * order to return it's status
	 * @return
	 */
	public int getInterfaceState();
	
	/**
	 * Returns the location of the devices
	 * @return location String
	 */
	public String getLocation();
	
}
