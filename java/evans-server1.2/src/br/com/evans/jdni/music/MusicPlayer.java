package br.com.evans.jdni.music;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jongo.MongoCollection;

import br.com.evans.dao.musics.MusicObject;
import br.com.evans.jndi.manager.JNDIManager;
import br.com.evans.music.mp3spi.core.MusicManager;
import br.com.evans.notifications.core.Notifications;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;


/**
 * This class is the class that manages and create multiple audio outputs
 * Also a singleton
 * 
 * @author Gcats
 *
 */
public enum MusicPlayer { // singleton
	INSTANCE;
	
	private MusicManager manager;
	
	MusicPlayer() {
		System.out.println(Notifications.NOTIF_CREATED_PLAYER_SINGLETON);
		this.manager = new MusicManager();
	}
	
	public List<String> getMusicList() {

		DBCollection coll = JNDIManager.getMongoCollection("musics");
		
		List<DBObject> list = coll.find().toArray();
		Iterator<DBObject> it = list.iterator();
		
		List<String> returnList = new ArrayList<String>();
		
		while(it.hasNext()) {
			returnList.add((String) it.next().get("directory"));
		}
		
		return returnList;
	}
	
	public void setPlaylist(String fromPlaylist, Integer toRoomCode) {
		MongoCollection musics = JNDIManager.getJongoCollection("musics");
		Iterator<MusicObject> it = (Iterator<MusicObject>) musics.find().as(MusicObject.class).iterator();
		while(it.hasNext()) {
			this.manager.addFileToRoom(0, it.next().getDirectory());
		}
	}
	
	public void playMusic(Integer roomCode) {
		this.manager.playRoom(roomCode);
	}
}