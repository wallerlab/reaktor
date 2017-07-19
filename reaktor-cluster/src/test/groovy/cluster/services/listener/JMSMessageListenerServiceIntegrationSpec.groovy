package cluster.services.listener

import cluster.file.FileDeleter
import spock.lang.*
import cluster.services.workspace.*
import cluster.services.terminal.CommandExecutor
import javax.jms.TextMessage

class JMSMessageListenerServiceIntegrationSpec extends Specification {
	
	JMSMessageListenerService jmls = new JMSMessageListenerService()

	def setup(){
		jmls.workspaceCreator = Mock(WorkspaceCreator)
		jmls.commandExecutor = Mock(CommandExecutor)
		jmls.directoryListenerService = Mock(DirectoryListenerService)
		jmls.fileDeleter = Mock(FileDeleter)
	}

	def "test that runSimulator calls workspaceCreator"() {
		when:
		TextMessage message = Mock()
		jmls.runSimulator(message)
		
		then:
		1*jmls.workspaceCreator.createWorkspace(_)
	}

	def "test that runSimulator calls commandExecutor"() {
		when:
		TextMessage message = Mock()
		jmls.runSimulator(message)
		
		then:
		1*jmls.commandExecutor.executeCommand(_,_)
	}

	def "test that runSimulator calls directoryListenerCreatorService"() {
		when:
		TextMessage message = Mock()
		jmls.runSimulator(message)

		then:
		1*jmls.directoryListenerService.createDirectoryListener(_)
	}

	def "test that cleanWorkspace calls JavaFileDeleter"(){
		when:
		TextMessage message = Mock()
		message.getText() >> "23"
		jmls.cleanWorkspace(message)
		
		then:
		1*jmls.fileDeleter.deleteFolder(_)
	}

}
