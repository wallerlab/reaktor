package cluster.services.listener;

import cluster.services.terminal.CommandExecutor;
import cluster.file.FileDeleter;
import cluster.services.workspace.WorkspaceCreator;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * Listener service for queues needed by the cluster
 * 
 * @author suzanne
 *
 */
@Service
public class JMSMessageListenerService {
	
	@Resource
	private WorkspaceCreator workspaceCreator;
	
	@Resource
	private CommandExecutor commandExecutor;

	@Resource
	private DirectoryListenerService directoryListenerService;
	
	@Resource
	private FileDeleter fileDeleter;

	@Value("${cluster.cls.command}")
	private String command;
	
	/**
	 * Listens to reactant queue in order to start new jobs
	 * 
	 * @param message
	 */
	@JmsListener(destination="wallerlab.reactantQueue")
	public void runSimulator(TextMessage message){
		File directory = workspaceCreator.createWorkspace(message);
		commandExecutor.executeCommand(command, directory);
		//TODO: update tests
		directoryListenerService.createDirectoryListener(directory);
	}

	/**
	 * Listens to clean queue in order to clean up folders on cluster
	 * 
	 * @param message
	 */
	@JmsListener(destination="wallerlab.cleanQueue")
	public void cleanWorkspace(TextMessage message){
		try {
			String folderName = message.getText();
			fileDeleter.deleteFolder(folderName);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
}
