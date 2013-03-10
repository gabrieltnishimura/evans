package br.com.evans.mapping.basic;

public interface DeviceImpl {
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
	 * @return
	 */
	public int getInterfaceState();
}
