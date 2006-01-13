package ch.bbv.notification.client;

import java.rmi.RemoteException;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import ch.bbv.notification.communication.ClientProfile;
import ch.bbv.notification.server.registration.RegisterHandler;


/**
 * Clientside wrapper class, but why we dont create a clientside serviceLocater for all jndi lookup?
 * @author Bruno Persi
 */
public class RegisterHandlerWrapper {
	
	private Properties _jndiEnv;
	
	private final String REGISTERHANDLER_JNDI_NAME = "ejb/RegisterHandler";
	
	public RegisterHandlerWrapper(){
		//JBoss use
		_jndiEnv = new Properties();
		_jndiEnv.put("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
		_jndiEnv.put("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
		_jndiEnv.put("java.naming.provider.url","jnp://host:1099");
	}
	
	public void register(ClientProfile profile){
		assert profile != null;
		try{
			lookupRegisterHandler().register(profile);
		} catch (RemoteException re){
			re.printStackTrace();
			throw new IllegalStateException(re);
		}
	}
	
	private RegisterHandler lookupRegisterHandler(){
		try{
			InitialContext ic = new InitialContext(_jndiEnv);
			return (RegisterHandler)ic.lookup(REGISTERHANDLER_JNDI_NAME);
		} catch (NamingException ne){
			ne.printStackTrace();
			throw new IllegalStateException(ne.getMessage());
		} 
	}
	
	public void deRegister(ClientProfile profile){
		assert profile != null;
		try{
			lookupRegisterHandler().deRegister(profile);
		} catch (RemoteException re){
			re.printStackTrace();
			throw new IllegalStateException(re);
		}
	}

}
