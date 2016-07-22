package cluster.services.terminal;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Service;

import cluster.services.workspace.SimpleWorkspaceCreator;

/**
 * executes terminal commands
 * 
 * @author suzanne
 *
 */
@Service
public class SimpleCommandExecutor implements CommandExecutor {
	
	private File filePath;
	
	/**
	 * given a String, executes it as a terminal command
	 * 
	 * @param command
	 * 
	 */
	@Override
	public void executeCommand(String command) {
		this.filePath = SimpleWorkspaceCreator.filePath;
		try {
			Process process = Runtime.getRuntime()
					.exec(command, null, filePath);
			process.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
