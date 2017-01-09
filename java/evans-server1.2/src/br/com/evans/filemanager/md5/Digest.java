package br.com.evans.filemanager.md5;

import java.security.MessageDigest;

public class Digest {
	private byte[] output;
	private MessageDigest md;
	
	public Digest() {
		try {
			md = MessageDigest.getInstance("MD5");
	        System.out.println("Message digest object info: ");
	        System.out.println("   Algorithm = "+md.getAlgorithm());
	        System.out.println("   Provider = "+md.getProvider());
	        System.out.println("   toString = "+md.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public String digestmd5(String yetToDigest) {
	      try {
	    	  
	         md.update(yetToDigest.getBytes()); 
	      	 output = md.digest();
//	         System.out.println();
//	         System.out.println("MD5(\""+yetToDigest+"\") =");
//	         System.out.println("   "+bytesToHex(output));
	      return bytesToHex(output);
	      
	      } catch (Exception e) {
	         System.out.println("Couldn't digest. Exception: "+e);
	      }
		return null;
	   }
	
	   public static String bytesToHex(byte[] b) {
	      char hexDigit[] = {'0', '1', '2', '3', '4', '5', '6', '7',
	                         '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
	      StringBuffer buf = new StringBuffer();
	      for (int j=0; j<b.length; j++) {
	         buf.append(hexDigit[(b[j] >> 4) & 0x0f]);
	         buf.append(hexDigit[b[j] & 0x0f]);
	      }
	      return buf.toString();
	   }
	   
}
