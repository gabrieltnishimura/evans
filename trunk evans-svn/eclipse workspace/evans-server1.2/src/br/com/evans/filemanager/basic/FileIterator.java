package br.com.evans.filemanager.basic;
import java.io.File;
import java.net.UnknownHostException;
import java.util.ArrayList;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;

import br.com.evans.filemanager.md5.Digest;

public class FileIterator {
	private static String mainDir;
	private static ArrayList<String> nameList;
	private static final String ADDRESS = "localhost";
	private static final int PORT = 27017;
	private static final String DATABASE = "test";
	
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
				coll = database.getCollection("test");
				System.out.println("[STATUS] Created MongoDB connection.");
			} catch (UnknownHostException e) {
				System.out.println("[EXCEPTION] Couldn't create MongoDB connection");
				e.printStackTrace();
			}
						
			for (File child : dir.listFiles()) {
				if (".".equals(child.getName()) || "..".equals(child.getName())) {
					continue;  // Ignore the self and parent aliases.
				}
				if (child.getName().contains(".mp3")) { //adds to list the name of the mp3 file
					nameList.add(child.getName());
					String name;
					String dirName = mainDir + "\\" + child.getName();
					String md5 = digest.digestmd5(dirName);
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
					
					doc = new BasicDBObject("name", name).append("directory", dirName).append("md5", md5);
					coll.insert(doc); /*persist!*/
					
						System.out.println("Added: ("+name+") with [" +md5+ "]"+ "on\n" + dirName);
						System.out.print("also, this file is a mp3 file \n");
				}
			}
		} else {
			System.out.println(" ------ This path does not refer to a valid directory. -----");
		}

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
