package br.com.evans.command.repository;

import java.io.IOException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import br.com.evans.jdni.music.MusicPlayer;
import br.com.evans.jndi.manager.JNDIManager;
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
			e.printStackTrace();
		}
	}
	
	public void proccesNode(String music, String artist, String album, String playlist, boolean shuffle) throws IOException, NamingException {
		MusicPlayer musicPlayer = JNDIManager.getMusicPlayer();
		//musicPlayer.outputToTextFile(null, 1); // @deprecated since new implementation
		//musicPlayer.getMusicList();
		String playlistId = "";
		int toRoomCode = 0;
		musicPlayer.setPlaylist(playlistId, toRoomCode);
		musicPlayer.playMusic(toRoomCode);
	}
	
	public void proccesNode(String report, String specs) throws NamingException {

		
	}
}
