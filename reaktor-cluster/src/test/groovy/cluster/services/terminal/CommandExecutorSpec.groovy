package cluster.services.terminal;

import cluster.services.terminal.SimpleCommandExecutor;
import cluster.services.workspace.SimpleWorkspaceCreator;
import spock.lang.*

class CommandExecutorSpec extends Specification {
	SimpleCommandExecutor cmdExec = new SimpleCommandExecutor()
	
	def "test that executeCommand sets the filePath"() {
		setup:
		SimpleWorkspaceCreator.filePath = new File("src/test/Test_Folder/8/product_geom")
		
		when:
		cmdExec.executeCommand("cp molecule0.xyz molecule3.xyz")
		
		then:
		cmdExec.filePath == new File("src/test/Test_Folder/8/product_geom")
	}
	
	def "test that executeCommand executes a command"() {
		setup:
		SimpleWorkspaceCreator.filePath = new File("src/test/Test_Folder/8/product_geom")
		
		when:
		cmdExec.executeCommand("cp molecule0.xyz molecule3.xyz")
		
		then:
		new File("src/test/Test_Folder/8/product_geom/molecule3.xyz").exists()
		
		cleanup:
		"rm molecule3.xyz".execute(null, cmdExec.filePath)
	}

}
