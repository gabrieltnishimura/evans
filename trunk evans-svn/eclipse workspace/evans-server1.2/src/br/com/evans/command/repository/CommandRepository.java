package br.com.evans.command.repository;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import br.com.evans.jdni.music.MusicPlayer;
import br.com.evans.jndi.states.DeviceMonitor;

public class CommandRepository {

	public void proccesNode(String mapLocation) {
		Context initCtx;
		try {
			initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			//Get the device monitor so it can get device states
			DeviceMonitor deviceMonitor = (DeviceMonitor) envCtx.lookup("states/DeviceMonitorFactory");
			deviceMonitor.getDevice(mapLocation).switchStates();	
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void proccesNode(String mapLocation, boolean status) {
		Context initCtx;
		try {
			initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			//Get the device monitor so it can get device states
			DeviceMonitor deviceMonitor = (DeviceMonitor) envCtx.lookup("states/DeviceMonitorFactory");
			deviceMonitor.getDevice(mapLocation).switchStates();	
			
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void proccesNode(String music, String artist, String album, String playlist, boolean shuffle) throws NamingException {
		Context initCtx = new InitialContext();
		Context envCtx = (Context) initCtx.lookup("java:comp/env");
		
		//Get the device monitor so it can get the player
		MusicPlayer musicPlayer = (MusicPlayer) envCtx.lookup("music/MusicPlayerFactory");
		
	}
	
	public void proccesNode(String report, String specs) throws NamingException {

		
	}
}
