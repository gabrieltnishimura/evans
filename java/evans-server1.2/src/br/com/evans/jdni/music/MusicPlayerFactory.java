package br.com.evans.jdni.music;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.spi.ObjectFactory;

@SuppressWarnings("rawtypes") 
public class MusicPlayerFactory implements ObjectFactory {

	public MusicPlayerFactory() {
		//constructor
	}
	public Object getObjectInstance(Object obj, Name name, Context nameCtx,Hashtable environment) throws NamingException {
		// Return the customized instance
		return (MusicPlayer.INSTANCE);

	}

}