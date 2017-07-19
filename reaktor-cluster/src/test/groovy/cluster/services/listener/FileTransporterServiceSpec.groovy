package cluster.services.listener

import spock.lang.*

class FileTransporterServiceSpec extends Specification {
	File testDirectory = new File("src/test/Test_Folder")
	FileTransporterService fts = new FileTransporterService()
	
	def "test createMessageText creates message text from files in folder when hasError is false"(){
		setup:
		File message = new File(testDirectory, "message_test.txt")
		
		when:
		File filePath = new File(testDirectory,a)
		String msgText = fts.createMessageText(false, filePath)
		File messageText = new File(testDirectory, "messageText.txt")
		messageText.createNewFile()
		messageText.append(msgText)
		
		then:
		messageText.length() == message.length()
		msgText.length() == b

		cleanup:
		messageText.delete()

		where:
		a		|	b
		"8"		|	514789
		//"10"	|	514780
	}
	
	def "test createMessageText creates error text when hasError is true"(){
		setup:
		File filePath = new File(testDirectory,"8")
		fts.errorToSend = "Error#An error occurred while computing."
		
		when:
		String msgText = fts.createMessageText(true, filePath)
		
		then:
		msgText == "Error#An error occurred while computing."
	}
}
