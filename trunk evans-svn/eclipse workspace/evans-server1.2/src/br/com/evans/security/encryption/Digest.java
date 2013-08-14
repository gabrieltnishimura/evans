package br.com.evans.security.encryption;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
   
   public static String digestSHA2(String yetToDigest) {   
	   MessageDigest md;
	   try {
		   md = MessageDigest.getInstance("SHA-256");
		   md.update(yetToDigest.getBytes());

		   byte byteData[] = md.digest();

		   //convert the byte to hex format method 2
		   StringBuffer hexString = new StringBuffer();
		   for (int i=0;i<byteData.length;i++) {
			   String hex=Integer.toHexString(0xff & byteData[i]);
			   	if(hex.length()==1) hexString.append('0');
			   	
			   hexString.append(hex);
		   }
			//System.out.println("Hex format : " + );	   
		   return hexString.toString();
	   
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
   }
   
}
