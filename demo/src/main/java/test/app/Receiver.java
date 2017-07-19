package test.app;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * Created by suzanne on 11.05.17.
 */
@Component
public class Receiver {

		@JmsListener(destination = "mailbox", containerFactory = "myFactory")
		public void receiveMessage(Email email) {
			System.out.println("Received <" + email + ">");
		}


}
