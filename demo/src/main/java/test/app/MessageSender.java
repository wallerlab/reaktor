package test.app;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by suzanne on 11.05.17.
 */
@Component
public class MessageSender {

	@Resource
	private JmsTemplate jmsTemplate;

	public void sendMessage(){

		System.out.println("Sending an email message.");
		jmsTemplate.convertAndSend("mailbox", new Email("info@example.com", "Hello"));
	}
}
