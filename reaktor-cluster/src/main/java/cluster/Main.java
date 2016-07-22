package cluster;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.support.ResourcePropertySource;

/**
 * Computer cluster side of reactor. Listens for and runs jobs created on the
 * server side.
 * 
 * @author suzanne
 *
 */
public class Main {
	
	static Logger log = Logger.getLogger("cluster");
	static AnnotationConfigApplicationContext appContext;
	static StandardEnvironment environment;
	
	public static void main(String[] args) {
		
		configSpring();
        serverSleep();

	}

	private static void configSpring() {
		
		environment = new StandardEnvironment();
		try{
			environment.getPropertySources().addFirst(new ResourcePropertySource("application.properties"));
		}
		catch(IOException e){
			e.printStackTrace();
		}
		appContext = new AnnotationConfigApplicationContext();
		appContext.register(Config.class);
		appContext.setEnvironment(environment);
		appContext.refresh();
		
	}

	private static void serverSleep() {
		
		boolean stopServer = false;
        while(!stopServer){
        	try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
        log.info("shutting down server");
        
	}

}
