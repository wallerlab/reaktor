package cluster.services.terminal;

import java.io.File;

public interface CommandExecutor {
	void executeCommand(String command, File filePath);
}
