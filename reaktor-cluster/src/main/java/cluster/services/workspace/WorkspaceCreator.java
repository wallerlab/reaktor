package cluster.services.workspace;

import javax.jms.TextMessage;
import java.io.File;

public interface WorkspaceCreator {
	File createWorkspace(TextMessage msg);
}
