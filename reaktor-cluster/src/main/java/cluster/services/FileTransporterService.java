package cluster.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.stereotype.Service;

/**
 * Polls for jobexd log files, checks if there was an error, then sends files to
 * Reactor in a JMS TextMessage
 * 
 * @author suzanne
 *
 */
@Service
public class FileTransporterService {

	@Resource
	private JmsTemplate jmsTemplate;

	@Value("${cluster.fts.folderNames}")
	private String folderNames;

	@Value("${cluster.fts.productQueue}")
	private String productQueue;

	@Value("${cluster.fts.errorToSend}")
	private String errorToSend;

	@Value("${cluster.fts.simulatorError}")
	private String errorLine;

	private File filePath;

	/**
	 * Sends new files back to server
	 * 
	 * @param fileMessage
	 */
	@ServiceActivator(inputChannel = "filePollerChannel")
	public void sendNewFiles(
			org.springframework.messaging.Message<File> fileMessage) {
		File file = fileMessage.getPayload();
		filePath = file.getParentFile();
		boolean hasError = getErrorBool(file);
		convertAndSendMessage(hasError);
	}

	/*
	 * Determines if there was an error running simulation
	 */
	private boolean getErrorBool(File file) {
		boolean hasError = false;
		Path path = Paths.get(file.getPath());
		try {
			Stream<String> lines = Files.lines(path);
			hasError = lines.anyMatch(line -> line.contains(errorLine));
			lines.close();
			return hasError;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return hasError;
	}

	/*
	 * Converts and sends message to product queue
	 */
	private void convertAndSendMessage(boolean hasError) {
		String messageText = createMessageText(hasError);
		jmsTemplate.convertAndSend(productQueue, messageText,
				new MessagePostProcessor() {
					public Message postProcessMessage(Message message) {
						try {
							message.setJMSCorrelationID(filePath.getName());
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
	private String createMessageText(boolean hasError) {
		if (hasError) {
			return errorToSend;
		} else {
			StringBuilder fileString = new StringBuilder();
			String folderPrefix = "";
			String[] foldersToSend = folderNames.split(", ");
			for (String folderString : foldersToSend) {
				File folderFilePath = new File(filePath, folderString);
				fileString.append(folderPrefix + folderString + ";");
				waitForFiles(folderFilePath);
				File[] files = folderFilePath.listFiles();
				String prefix = "";
				for (File file : files) {
					if (!file.isHidden()) {
						Path path = Paths.get(file.getPath());
						fileString.append(prefix);
						fileString.append(file.getName() + ":");
						try {
							String newString = new String(
									Files.readAllBytes(path));
							fileString.append(newString);
							prefix = ",";
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				folderPrefix = "#";
			}
			return fileString.toString();
		}
	}

	private boolean waitForFiles(File folderFilePath) {
		
		boolean folderFilePathExists = folderFilePath.exists();
		int timeout = 0;
		while(!folderFilePathExists && timeout < 100){
			try {
				TimeUnit.MICROSECONDS.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			folderFilePathExists = folderFilePath.exists();
			timeout++;
		}
		return folderFilePathExists;
		
	}

}
