package br.com.evans.filemanager.basic;
import java.io.File;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;

import br.com.evans.dao.musics.MusicObject;

public class FileIterator {
	private static String mainDir;
	private static ArrayList<String> nameList;
	private static final String ADDRESS = "localhost";
	private static final int PORT = 27017;
	private static final String DATABASE = "evans";
	private static final String COLLECTION = "musics";
	
	public FileIterator (String mainDir) {
		FileIterator.mainDir = mainDir;
	}
	
	public static void iterateThroughMainDir() {
		File dir = new File(mainDir);
		nameList = new ArrayList<String>();
		//Digest digest = new Digest();
		
		if (dir.isDirectory()) {
			MongoCollection jongoColl = null;
			
			try {
				Mongo mongoClient = new Mongo( ADDRESS , PORT );
				DB database = mongoClient.getDB( DATABASE );
				
				Jongo j = new Jongo(database);
				jongoColl = j.getCollection( COLLECTION );
				
				System.out.println("[STATUS] Created MongoDB connection.");
			} catch (UnknownHostException e) {
				System.out.println("[EXCEPTION] Couldn't create MongoDB connection");
				e.printStackTrace();
			}

			int i = 0;
			for (File child : dir.listFiles()) {
				if (".".equals(child.getName()) || "..".equals(child.getName())) {
					continue;  // Ignore the self and parent aliases.
				}
				if (child.getName().contains(".mp3")) { //adds to list the name of the mp3 file
					nameList.add(child.getName());
					String name;
					String dirName = mainDir + "\\" + child.getName();
//					String md5 = digest.digestmd5(dirName); // must be unique
//					System.out.println(md5);
							
					if (!child.getName().contains("-")) {
						name = child.getName().substring(child.getName().indexOf(" "), child.getName().indexOf(".mp3"));
						name = name.trim();
						if (!child.getName().contains(".")) {
							System.out.println("String has no '-' and has a '.'");
						}
					} else {
					name = child.getName().substring(child.getName().indexOf("- ") +2,
													 child.getName().indexOf(".mp3"));
					}
					
					MusicObject music = new MusicObject(name, dirName);
					jongoColl.save(music);
					
						System.out.println("["+ i++ +"]Added: ("+name+")" + "on\n" + dirName);
						System.out.print("also, this file is a mp3 file \n");
				}
			}
		} else {
			System.out.println(" ------ This path does not refer to a valid directory. -----");
		}

	}

	public DBObject findDocumentById(String id) {
	    BasicDBObject query = new BasicDBObject();
	    query.put("_id", new ObjectId(id));
	    //DBObject dbObj = collection.findOne(query);
	    //return dbObj;
	    return null;
	}
	
	public static void main(String[] args) {
		mainDir = "C:\\Users\\Gcats\\Music";
		mainDir = "F:\\Music\\Anime\\Kimi to Boku 2";
		iterateThroughMainDir();
		
		DB db;
		try {
			db = new Mongo().getDB("evans");
			Jongo jongo = new Jongo(db);
			MongoCollection friends = jongo.getCollection("musics");
			Iterable<MusicObject> all = friends.find().as(MusicObject.class);
			Iterator<MusicObject> it = all.iterator();
			it.next();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public String getMainDir() {
		return mainDir;
	}

	public void setMainDir(String mainDir) {
		FileIterator.mainDir = mainDir;
	}
	
}
