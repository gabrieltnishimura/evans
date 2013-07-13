package br.com.evans.jdni.music;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import br.com.evans.jndi.db.MongoDBConnection;
import com.mongodb.DBCollection;


/**
 * This class is the class that manages and create multiple audio outputs
 * Also a singleton
 * 
 * @author Gcats
 *
 */
public enum MusicPlayer { // singleton
	INSTANCE;
	private static final String TASKLIST = "tasklist";
	private static final String KILL = "taskkill /F /IM ";
	
	MusicPlayer() {
		System.out.println("[STATUS] Created Music Player (singleton)");
	}
	
	public void outputToTextFile(List<String> musicPaths, int device) throws IOException {
				
		File file = new File("C:\\Users\\Public\\evans\\multi\\multiOut.txt");
		file.delete();
		
		file = new File("C:\\Users\\Public\\evans\\multi\\multiOut.txt");
		FileWriter writer = null;
		PrintWriter printer = null;
		try {
		    writer = new FileWriter(file, true);
		    printer = new PrintWriter(writer);
		    /*printer.append("2\n");
		    printer.append("2 C:\\yes.mp3\n");
		    printer.append("1 C:\\Users\\Gabriel\\Desktop\\test.mp3\n");*/
		    
		} catch (IOException e) {
			System.out.println("[EXCEPTION] Couldn't create file multiOut.txt");
			e.printStackTrace();
		}

		Context initCtx;
		try {
			initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			MongoDBConnection mongo = (MongoDBConnection) envCtx.lookup("db/MongoDBConnectionFactory"); 
			DBCollection coll = mongo.getCollection("test");
			
			printer.append("1\n");
			printer.append("1 " + coll.findOne().get("directory"));
			printer.close();
			
        } catch (NamingException e) {
        	System.out.println("[EXCEPTION] Problem when trying to load the context of ArduinoFactory");
			e.printStackTrace();
		}
	
	}

	public static boolean isProcessRunning(String serviceName) throws IOException   {
		Process p = Runtime.getRuntime().exec(TASKLIST);
		BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		while ((line = reader.readLine()) != null) {
			if (line.contains(serviceName)) {
				System.out.println("[STATUS] Service was running and is being terminated");
				return true;
			}
		}
		return false;
	}
	public static void killProcess(String serviceName) throws IOException {
		Runtime.getRuntime().exec(KILL + serviceName);
	}
	
	public void playMusic() throws IOException {
		System.out.println("[Trying to run multi.exe now]");
		try {
			if (isProcessRunning("multi.exe")) {
				killProcess("multi.exe");
			}
	        try { /* wait until killed one instance of multi.exe */
	            Thread.sleep(200);
	        } catch (InterruptedException ex) {
	        	System.out.println("[EXCEPTION] Couldn't wait 300 ms to kill process.");
	        }
	        
			Runtime.getRuntime().exec("C:\\Users\\Public\\evans\\multi\\multi.exe");
		} catch(IOException ex) {
			System.out.println("[EXCEPTION] Couldn't execute program");
		}
	}
}