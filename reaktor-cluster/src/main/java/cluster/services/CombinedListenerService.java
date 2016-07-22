package cluster.services;

import cluster.services.terminal.CommandExecutor;
import cluster.services.workspace.FileDeleter;
import cluster.services.workspace.WorkspaceCreator;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

/**
 * Listener service for queues needed by the cluster
 * 
 * @author suzanne
 *
 */
@Service
public class CombinedListenerService {
	
	@Resource
	private WorkspaceCreator workspaceCreator;
	
	@Resource
	private CommandExecutor commandExecutor;
	
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
		workspaceCreator.createWorkspace(message);
		commandExecutor.executeCommand(command);
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
