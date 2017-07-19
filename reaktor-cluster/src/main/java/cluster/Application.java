package cluster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

/**
 * Computer cluster side of reactor. Listens for and runs jobs created on the
 * server side.
 * 
 * @author suzanne
 *
 */
@SpringBootApplication
public class Application {
	
	public static void main(String[] args) {

		SpringApplication.run(Application.class, args);

	}

}
