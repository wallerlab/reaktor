package cluster.services.terminal

import spock.lang.*

class CommandExecutorSpec extends Specification {
	SimpleCommandExecutor cmdExec = new SimpleCommandExecutor()
	
	def "test that executeCommand executes a command"() {
		setup:
		File filePath = new File("src/test/Test_Folder/8/product_geom")
		
		when:
		cmdExec.executeCommand("cp molecule0.xyz molecule3.xyz", filePath)
		
		then:
		new File("src/test/Test_Folder/8/product_geom/molecule3.xyz").exists()
		
		cleanup:
		File file = new File("src/test/Test_Folder/8/product_geom/molecule3.xyz")
		file.delete()
	}

}
