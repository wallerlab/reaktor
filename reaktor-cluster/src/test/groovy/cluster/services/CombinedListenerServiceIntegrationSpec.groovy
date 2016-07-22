package cluster.services

import spock.lang.*
import cluster.services.workspace.WorkspaceCreator
import cluster.services.terminal.CommandExecutor
import cluster.services.workspace.FileDeleter
import javax.jms.TextMessage

class CombinedListenerServiceIntegrationSpec extends Specification {
	
	CombinedListenerService cls = new CombinedListenerService()

	public "test that runSimulator calls workspaceCreator"() {
		setup:
		cls.workspaceCreator = Mock(WorkspaceCreator)
		cls.commandExecutor = Mock(CommandExecutor)
		
		when:
		TextMessage message = Mock()
		cls.runSimulator(message)
		
		then:
		1*cls.workspaceCreator.createWorkspace(_)
	}
	
	public "test that runSimulator calls commandExecutor"() {
		setup:
		cls.workspaceCreator = Mock(WorkspaceCreator)
		cls.commandExecutor = Mock(CommandExecutor)
		
		when:
		TextMessage message = Mock()
		cls.runSimulator(message)
		
		then:
		1*cls.commandExecutor.executeCommand(_)
	}
	
	public "test that cleanWorkspace calls JavaFileDeleter"(){
		setup:
		cls.fileDeleter = Mock(FileDeleter)
		
		when:
		TextMessage message = Mock()
		message.getText() >> "23"
		cls.cleanWorkspace(message)
		
		then:
		1*cls.fileDeleter.deleteFolder(_)
	}

}
