package br.com.evans.jdni.music;

/**
 * This class is the class that manages and create multiple audio outputs
 * Also a singleton
 * 
 * @author Gcats
 *
 */
public enum MusicPlayer { // singleton
	INSTANCE;
	
	MusicPlayer() {
		System.out.println("[STATUS] Created Music Player (singleton)");
	}

}