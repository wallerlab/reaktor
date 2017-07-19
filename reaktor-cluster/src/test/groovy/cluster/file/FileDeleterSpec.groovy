package cluster.file

import com.google.common.io.Files
import spock.lang.*

class FileDeleterSpec extends Specification{
	JavaFileDeleter fd = new JavaFileDeleter()


	def "test that deleteFolder deletes folder and everything in it"() {
		setup:
		fd.directory = new File("src/test/Test_Folder")
		File folderCopy = new File(fd.directory, "nestedFolder")
		folderCopy.mkdir()
		File folderToCopy = new File(fd.directory, "nestedFolderToCopy")
		folderToCopy.eachFileRecurse { file ->
			if(file.isDirectory()){
				new File(folderCopy,file.name).mkdir()
			} else{
				File file1
				if(file.parent == folderToCopy.path){
					file1 = new File(folderCopy,file.name)
				} else{
					String parentFilename = file.parent.tokenize("/")[-1]
					file1 = new File(folderCopy,"${parentFilename}/${file.name}")
				}
				file1.createNewFile()
				Files.copy(file, file1)
			}
		}
		
		when:
		String folderName = "nestedFolder"
		boolean folderCopyExisted = folderCopy.exists()
		fd.deleteFolder(folderName)
		
		then:
		folderCopyExisted
		!folderCopy.exists()
	}

}
