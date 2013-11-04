package br.com.evans.devices.x10;

import br.com.evans.devices.core.Device;

//TODO Implement X10 Device comm
public class X10Device implements Device{

	@Override
	public boolean getDeviceStatus() {
		return false;
	}

	@Override
	public void switchStates() {
		
	}

	@Override
	public int getInterfaceState() {
		return 0;
	}

	@Override
	public String getLocation() {
		return null;
	}

	@Override
	public void configure(Object[] args) {
		
	}

}
