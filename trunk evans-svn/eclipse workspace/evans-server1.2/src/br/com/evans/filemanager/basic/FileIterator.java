package br.com.evans.filemanager.basic;
import java.io.File;
import java.net.UnknownHostException;
import java.util.ArrayList;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import org.bson.types.ObjectId;

import br.com.evans.security.encryption.Digest;

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
		Digest digest = new Digest();
		
		if (dir.isDirectory()) {
			DBCollection coll = null;
			BasicDBObject doc = null;
			
			try {
				Mongo mongoClient = new Mongo( ADDRESS , PORT );
				DB database = mongoClient.getDB( DATABASE );
				coll = database.getCollection( COLLECTION );
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
					String md5 = digest.digestmd5(dirName); // must be unique
//					System.out.println(md5);
							
					if (!child.getName().contains("-")) {
						name = child.getName().substring(child.getName().indexOf(" "), child.getName().indexOf(".mp3"));
						if (!child.getName().contains(".")) {
							System.out.println("String has no '-' and has a '.'");
						}
					} else {
					name = child.getName().substring(child.getName().indexOf("- ") +2,
													 child.getName().indexOf(".mp3"));
					}
					
					ObjectId id = new ObjectId(); // uniqueness => db.evans.ensureIndex({directory:1}, {unique, true})s
					
					doc = new BasicDBObject("_id", id).append("name", name).append("directory", dirName);
					coll.insert(doc); /*persist!*/
					
						System.out.println("["+ i++ +"]Added: ("+name+") with [" +md5+ "]"+ "on\n" + dirName);
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
	}
	
	public String getMainDir() {
		return mainDir;
	}

	public void setMainDir(String mainDir) {
		FileIterator.mainDir = mainDir;
	}
	
}
