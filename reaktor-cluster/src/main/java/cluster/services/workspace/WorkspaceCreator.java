package cluster.services.workspace;

import javax.jms.TextMessage;

public interface WorkspaceCreator {
	public void createWorkspace(TextMessage msg);
}
