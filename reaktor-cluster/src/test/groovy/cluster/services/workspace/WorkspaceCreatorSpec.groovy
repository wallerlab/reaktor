package cluster.services.workspace;

import java.nio.file.Paths
import java.nio.file.Path

import javax.jms.TextMessage

import cluster.services.workspace.SimpleWorkspaceCreator;
import spock.lang.*

@Ignore
class WorkspaceCreatorSpec extends Specification{
	SimpleWorkspaceCreator wsc = new SimpleWorkspaceCreator()
	
	def "test copySubmitScriptToFolder copies submitScript to correct folder"() {
		setup:
		wsc.directory = new File("src/test/Test_Folder")
		wsc.filePath = new File("src/test/Test_Folder/8/product_geom")
		wsc.startFiles = new ArrayList([Paths.get(wsc.filePath.getPath()+"/molecule0.xyz"),
			Paths.get(wsc.filePath.getPath()+"/molecule1.xyz")])
		wsc.submitScript = "submitscript"
		wsc.runSimulation = "python ~/bin/pyreactor/Reactor.py "
		wsc.email = "mcananam@uni-muenster.de"
		wsc.simulator = "pyreactor"
		
		when:
		wsc.copySubmitScriptToFolder()
		
		then:
		new File(wsc.filePath, "submitscript").exists()
	}
	
	def "test copySubmitScriptToFolder changes job name"() {
		setup:
		wsc.directory = new File("src/test/Test_Folder")
		wsc.filePath = new File("src/test/Test_Folder/8/product_geom")
		wsc.startFiles = new ArrayList([Paths.get(wsc.filePath.getPath()+"/molecule0.xyz"),
			Paths.get(wsc.filePath.getPath()+"/molecule1.xyz")])
		wsc.submitScript = "submitscript"
		wsc.runSimulation = "python ~/bin/pyreactor/Reactor.py "
		wsc.email = "mcananam@uni-muenster.de"
		wsc.simulator = "pyreactor"
		
		when:
		wsc.copySubmitScriptToFolder()
		
		then:
		new File(wsc.filePath, "submitscript").text.contains("product_geom")
	}
	
	def "test copySubmitScriptToFolder changes email"() {
		setup:
		wsc.directory = new File("src/test/Test_Folder")
		wsc.filePath = new File("src/test/Test_Folder/8/product_geom")
		wsc.startFiles = new ArrayList([Paths.get(wsc.filePath.getPath()+"/molecule0.xyz"),
			Paths.get(wsc.filePath.getPath()+"/molecule1.xyz")])
		wsc.submitScript = "submitscript"
		wsc.runSimulation = "python ~/bin/pyreactor/Reactor.py "
		wsc.email = "mcananam@uni-muenster.de"
		wsc.simulator = "pyreactor"
		
		when:
		wsc.copySubmitScriptToFolder()
		
		then:
		new File(wsc.filePath, "submitscript").text.contains("mcananam@uni-muenster.de")
	}
	
	def "test copySubmitScriptToFolder writes file names to job commands"() {
		setup:
		wsc.directory = new File("src/test/Test_Folder")
		wsc.filePath = new File("src/test/Test_Folder/8/product_geom")
		wsc.startFiles = new ArrayList([Paths.get(wsc.filePath.getPath()+"/molecule0.xyz"),
			Paths.get(wsc.filePath.getPath()+"/molecule1.xyz")])
		wsc.submitScript = "submitscript"
		wsc.runSimulation = "python ~/bin/pyreactor/Reactor.py "
		wsc.email = "mcananam@uni-muenster.de"
		wsc.simulator = "pyreactor"
		
		when:
		wsc.copySubmitScriptToFolder()
		
		then:
		new File(wsc.filePath, "submitscript").text.contains("python ~/bin/pyreactor/Reactor.py molecule0.xyz molecule1.xyz")
		
		cleanup:
		"rm submitscript".execute(null, wsc.filePath)
	}
	
	def "test writeFilesToFolder creates files from message to folder"(){
		setup:
		wsc.filePath = new File("src/test/Test_Folder")
		TextMessage msg = Mock()
		msg.getText() >> "file1.xyz:this is a test,file2.inp:hello\n12345"
		wsc.suffix = ".xyz"
		
		when:
		wsc.writeFilesToFolder(msg)
		
		then:
		new File(wsc.filePath, "file1.xyz").exists()
		new File(wsc.filePath, "file2.inp").exists()
	}
	
	def "test writeFilesToFolder writes to files from message to folder"(){
		setup:
		wsc.filePath = new File("src/test/Test_Folder")
		TextMessage msg = Mock()
		msg.getText() >> "file1.xyz:this is a test,file2.inp:hello\n12345"
		wsc.suffix = ".xyz"
		
		when:
		wsc.writeFilesToFolder(msg)
		
		then:
		new File(wsc.filePath, "file1.xyz").text == "this is a test"
		new File(wsc.filePath, "file2.inp").text == "hello\n12345"
	}
	
	def "test writeFilesToFolder adds only xyz files to startFiles"(){
		setup:
		wsc.filePath = new File("src/test/Test_Folder")
		TextMessage msg = Mock()
		msg.getText() >> "file1.xyz:this is a test,file2.inp:hello\n12345"
		wsc.suffix = ".xyz"
		
		when:
		wsc.writeFilesToFolder(msg)
		
		then:
		wsc.startFiles == new ArrayList<Path>([Paths.get(wsc.filePath.getPath()+"/file1.xyz")])
		
		cleanup:
		"rm file1.xyz file2.inp".execute(null, wsc.filePath)
	}
	
	def "test createWorkingFolder assigns the filePath"(){
		setup:
		wsc.directory = new File("src/test/Test_Folder")
		TextMessage msg = Mock()
		msg.getJMSCorrelationID() >> "abcFolder"
		
		when:
		wsc.createWorkingFolder(msg)
		
		then:
		wsc.filePath == new File("src/test/Test_Folder/abcFolder")
		
		cleanup:
		"rm -R abcFolder".execute(null, wsc.directory)
	}
	
	def "test createWorkingFolder creates the working folder"(){
		setup:
		wsc.directory = new File("src/test/Test_Folder")
		TextMessage msg = Mock()
		msg.getJMSCorrelationID() >> "abcFolder"
		
		when:
		wsc.createWorkingFolder(msg)
		
		then:
		wsc.filePath.exists()
		
		cleanup:
		"rm -R abcFolder".execute(null, wsc.directory)
	}
	
	def "test createWorkspace creates the workspace"(){
		setup:
		wsc.directory = new File("src/test/Test_Folder")
		TextMessage msg = Mock()
		msg.getJMSCorrelationID() >> "abcFolder"
		msg.getText() >> "file1.xyz:this is a test,file2.inp:hello\n12345,file3.xyz:blah\nblah\t\n"
		wsc.submitScript = "submitscript"
		wsc.runSimulation = "python ~/bin/pyreactor/Reactor.py "
		wsc.email = "mcananam@uni-muenster.de"
		wsc.simulator = "pyreactor"
		wsc.suffix = ".xyz"
		
		when:
		wsc.createWorkspace(msg)
		
		then:
		new File(wsc.filePath, "file1.xyz").text == "this is a test"
		new File(wsc.filePath, "file2.inp").text == "hello\n12345"
		new File(wsc.filePath, "file3.xyz").text == "blah\nblah\t\n"
		
		cleanup:
		"rm -R abcFolder".execute(null, wsc.directory)
	}
}
