package cluster.services.workspace

import cluster.services.workspace.SimpleWorkspaceCreator
import spock.lang.*

import javax.jms.TextMessage
import java.nio.file.Path
import java.nio.file.Paths


class SimpleWorkspaceCreatorSpec extends Specification{
	SimpleWorkspaceCreator wsc = new SimpleWorkspaceCreator()

	def "test copySubmitScriptToFolder copies submitScript to correct folder"() {
		setup:
		wsc.directory = new File("src/test/Test_Folder")
		File filePath = new File("src/test/Test_Folder/8/product_geom")
		List<Path> startFiles = new ArrayList([Paths.get(filePath.getPath()
				+"/molecule0.xyz"),
										Paths.get(filePath.getPath()+"/molecule1.xyz")])
		wsc.submitScript = "submitscript"
		wsc.runReaction = "python ~/bin/PyAR/Reactor.py "
		wsc.runAggregation = "python ~/bin/PyAR/nucleation_genetic.py "
		wsc.email = "fake@email.com"
		wsc.simulator = "pyreactor"
		File file = new File(filePath, "submitscript")

		when:
		wsc.copySubmitScriptToFolder("Reaction", startFiles, filePath)

		then:
		file.exists()

		cleanup:
		file.delete()
	}

	def "test copySubmitScriptToFolder changes job name"() {
		setup:
		wsc.directory = new File("src/test/Test_Folder")
		File filePath = new File("src/test/Test_Folder/8/product_geom")
		List<Path> startFiles = new ArrayList([Paths.get(filePath.getPath()
				+"/molecule0.xyz"),
										Paths.get(filePath.getPath()+"/molecule1.xyz")])
		wsc.submitScript = "submitscript"
		wsc.runReaction = "python ~/bin/PyAR/Reactor.py "
		wsc.runAggregation = "python ~/bin/PyAR/nucleation_genetic.py "
		wsc.email = "fake@email.com"
		wsc.simulator = "pyreactor"
		File file = new File(filePath, "submitscript")

		when:
		wsc.copySubmitScriptToFolder("Reaction", startFiles, filePath)

		then:
		file.text.contains("product_geom")

		cleanup:
		file.delete()
	}

	def "test copySubmitScriptToFolder changes email"() {
		setup:
		wsc.directory = new File("src/test/Test_Folder")
		File filePath = new File("src/test/Test_Folder/8/product_geom")
		List<Path> startFiles = new ArrayList([Paths.get(filePath.getPath()+"/molecule0.xyz"),
										Paths.get(filePath.getPath()+"/molecule1.xyz")])
		wsc.submitScript = "submitscript"
		wsc.runReaction = "python ~/bin/PyAR/Reactor.py "
		wsc.runAggregation = "python ~/bin/PyAR/nucleation_genetic.py "
		wsc.email = "fake@email.com"
		wsc.simulator = "pyreactor"
		File file = new File(filePath, "submitscript")

		when:
		wsc.copySubmitScriptToFolder("Reaction", startFiles, filePath)

		then:
		file.text.contains("fake@email.com")

		cleanup:
		file.delete()
	}

	@Unroll
	def "test copySubmitScriptToFolder writes file names to job commands"() {
		setup:
		wsc.directory = new File("src/test/Test_Folder")
		File filePath = new File("src/test/Test_Folder/8/product_geom")
		wsc.submitScript = "submitscript"
		wsc.runReaction = "python ~/bin/PyAR/Reactor.py "
		wsc.runAggregation = "python ~/bin/PyAR/nucleation_genetic.py "
		wsc.email = "fake@email.com"
		wsc.simulator = "pyreactor"
		File file = new File(filePath, "submitscript")

		when:
		wsc.copySubmitScriptToFolder(a, c, filePath)

		then:
		file.text.contains(b)

		cleanup:
		file.delete()

		where:
		a				|	b 						|	c
		"Reaction"		|	"python ~/bin/PyAR/Reactor.py molecule0.xyz " +
				"molecule1.xyz"						|	[Paths.get("src/test/Test_Folder/8/product_geom/molecule0.xyz"),Paths.get("src/test/Test_Folder/8/product_geom/molecule1.xyz")]
		"Aggregation"	|	"python ~/bin/PyAR/nucleation_genetic.py " +
				"molecule0.xyz"						|	[Paths.get
				("src/test/Test_Folder/8/product_geom/molecule0.xyz")]
		"Aggregation"	|	"python ~/bin/PyAR/nucleation_genetic.py " +
				"molecule0.xyz molecule1.xyz"		|	[Paths.get
				("src/test/Test_Folder/8/product_geom/molecule0.xyz"), Paths.get("src/test/Test_Folder/8/product_geom/molecule1.xyz")]


	}

	def "test writeFilesToFolder creates files from message to folder"(){
		setup:
		File filePath = new File("src/test/Test_Folder")
		ArrayList<String> textForFiles = new ArrayList<>(Arrays.asList("file1.xyz:this is a test,file2.inp:hello\n12345".split(",")))
		wsc.suffix = ".xyz"
		File fileXyz = new File(filePath, "file1.xyz")
		File fileInp = new File(filePath, "file2.inp")

		when:
		wsc.writeFilesToFolder(textForFiles, filePath) == "Reaction"

		then:
		fileXyz.exists()
		fileInp.exists()

		cleanup:
		fileInp.delete()
		fileXyz.delete()
	}

	def "test writeFilesToFolder writes to files from message to folder"(){
		setup:
		File filePath = new File("src/test/Test_Folder")
		ArrayList<String> textForFiles = new ArrayList<>(Arrays.asList("file1.xyz:this is a test,file2.inp:hello\n12345".split(",")))
		wsc.suffix = ".xyz"
		File fileXyz = new File(filePath, "file1.xyz")
		File fileInp = new File(filePath, "file2.inp")

		when:
		wsc.writeFilesToFolder(textForFiles, filePath)

		then:
		fileXyz.text == "this is a test"
		fileInp.text == "hello\n12345"

		cleanup:
		fileInp.delete()
		fileXyz.delete()
	}

	def "test writeFilesToFolder adds only xyz files to startFiles"(){
		setup:
		File filePath = new File("src/test/Test_Folder")
		ArrayList<String> textForFiles = new ArrayList<>(Arrays.asList("file1.xyz:this is a test,file2.inp:hello\n12345".split(",")))
		wsc.suffix = ".xyz"
		File fileXyz = new File(filePath, "file1.xyz")
		File fileInp = new File(filePath, "file2.inp")

		when:
		List<Path> startFiles = wsc.writeFilesToFolder(textForFiles, filePath)

		then:
		startFiles == new ArrayList<Path>([Paths.get(filePath.getPath()+"/file1.xyz")])

		cleanup:
		fileXyz.delete()
		fileInp.delete()
	}

	def "test getSimulationType"(){
		setup:
		ArrayList<String> textForFiles = new ArrayList<>(Arrays.asList
				("Reaction,file1.xyz:this is a test,file2.inp:hello\n12345"
						.split(",")))

		when:
		String simulationType = wsc.getSimulationType(textForFiles)

		then:
		simulationType == "Reaction"
	}

	def "test createWorkingFolder assigns the filePath"(){
		setup:
		wsc.directory = new File("src/test/Test_Folder")
		TextMessage msg = Mock()
		msg.getJMSCorrelationID() >> "abcFolder"
		File abcFolder = new File("src/test/Test_Folder/abcFolder")

		when:
		File file = wsc.createWorkingFolder(msg)

		then:
		file == abcFolder

		cleanup:
		abcFolder.deleteDir()
	}

	def "test createWorkingFolder creates the working folder"(){
		setup:
		wsc.directory = new File("src/test/Test_Folder")
		TextMessage msg = Mock()
		msg.getJMSCorrelationID() >> "abcFolder"
		File abcFolder = new File("src/test/Test_Folder/abcFolder")

		when:
		File file = wsc.createWorkingFolder(msg)

		then:
		file.exists()

		cleanup:
		abcFolder.deleteDir()
	}

	def "test createWorkspace creates and returns the workspace"(){
		setup:
		wsc.directory = new File("src/test/Test_Folder")
		TextMessage msg = Mock()
		msg.getJMSCorrelationID() >> "abcFolder"
		msg.getText() >> "Reaction,file1.xyz:this is a test,file2.inp:hello\n12345,file3.xyz:blah\nblah\t\n"
		wsc.submitScript = "submitscript"
		wsc.runReaction = "python ~/bin/PyAR/Reactor.py "
		wsc.runAggregation = "python ~/bin/PyAR/nucleation_genetic.py "
		wsc.email = "fake@email.com"
		wsc.simulator = "pyreactor"
		wsc.suffix = ".xyz"
		File abcFolder = new File("src/test/Test_Folder/abcFolder")

		when:
		File directory = wsc.createWorkspace(msg)

		then:
		new File("src/test/Test_Folder/abcFolder/file1.xyz").text == "this is a test"
		new File("src/test/Test_Folder/abcFolder/file2.inp").text == "hello\n12345"
		new File("src/test/Test_Folder/abcFolder/file3.xyz").text == "blah\nblah\t\n"
		directory == new File("src/test/Test_Folder/abcFolder")

		cleanup:
		abcFolder.deleteDir()
	}
}
