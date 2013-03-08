package br.com.evans.jndi.basic;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.spi.ObjectFactory;

@SuppressWarnings("rawtypes") 
public class ArduinoConnectionFactory implements ObjectFactory {

	public ArduinoConnectionFactory() {
		ArduinoConnection.INSTANCE.initialize();
		try {
			Thread.sleep(1800); // the arduino takes some time to initialize and send some rf waves
		} catch (InterruptedException e) {
			System.out.println("[EXCEPTION] Problem during thread sleeping in ArduinoFactory");
			e.printStackTrace();
		}
	}
	
	public Object getObjectInstance(Object obj, Name name, Context nameCtx,Hashtable environment) throws NamingException {
		// Return the customized instance
		return (ArduinoConnection.INSTANCE);

	}

}