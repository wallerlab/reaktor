package cluster.services.listener;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;

import cluster.file.ReaktorFileVisitor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.stereotype.Service;

/**
 * Sends files to Reaktor-web in a JMS TextMessage
 * 
 * @author suzanne
 *
 */
@Service
public class FileTransporterService {

	@Resource
	private JmsTemplate jmsTemplate;

	@Value("${cluster.fts.productQueue}")
	private String productQueue;

	@Value("${cluster.fts.errorToSend}")
	private String errorToSend;

	/**
	 * Sends all files in directory to broker
	 *
	 * @param directory
	 */
	public void sendNewFiles(File directory, boolean hasError){

		String messageText = createMessageText(hasError, directory);
		jmsTemplate.convertAndSend(productQueue, messageText,
				new MessagePostProcessor() {
					public Message postProcessMessage(Message message) {
						try {
							message.setJMSCorrelationID(directory.getName());
						} catch (JMSException e) {
							e.printStackTrace();
						}
						return message;
					}
				});

	}

	/*
	 * Writes contents of files in given folders into a String, or if there's an
	 * error, writes an error message.
	 */
	private String createMessageText(boolean hasError, File directory) {

		if (hasError) {
			return errorToSend;
		} else {
			Path directoryPath = Paths.get(directory.getPath());
			try{
				String reactionType = "reaction";
				ReaktorFileVisitor reaktorFileVisitor = new
						ReaktorFileVisitor(directoryPath, reactionType);
				Files.walkFileTree(directoryPath, reaktorFileVisitor);
				return reaktorFileVisitor.getFileString();
			} catch(IOException e){
				e.printStackTrace();
			}
		}
		return "";

	}

}
