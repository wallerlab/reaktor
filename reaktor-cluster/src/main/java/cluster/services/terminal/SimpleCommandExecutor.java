package cluster.services.terminal;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Service;

import cluster.services.workspace.SimpleWorkspaceCreator;

/**
 * Executes terminal commands
 * 
 * @author suzanne
 *
 */
@Service
public class SimpleCommandExecutor implements CommandExecutor {
	
	/**
	 * Given a String, executes it as a terminal command in filePath.
	 * 
	 * @param command terminal command
	 * @param filePath path to run command in
	 * 
	 */
	@Override
	public void executeCommand(String command, File filePath) {

		try{
			String[] commands = command.split(" ");
			Process process = new ProcessBuilder(commands).directory
					(filePath).start();
			process.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
