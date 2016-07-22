package cluster.services.workspace;


import cluster.services.workspace.JavaFileDeleter;
import spock.lang.*

class FileDeleterSpec extends Specification{
	JavaFileDeleter fd = new JavaFileDeleter()

	public "test that deleteFolder deletes folder and everything in it"() {
		setup:
		fd.directory = new File("src/test/Test_Folder")
		"cp -R nestedFolderToCopy nestedFolder".execute(null, fd.directory)
		
		when:
		String folderName = "nestedFolder"
		new File(fd.directory, "nestedFolder").exists()
		fd.deleteFolder(folderName)
		
		then:
		!new File(fd.directory, "nestedFolder").exists()
	}

}
