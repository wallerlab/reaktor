package cluster.services

import java.util.ArrayList;
import java.util.Arrays;

import spock.lang.*

import org.springframework.jms.core.JmsTemplate

class FileTransporterServiceSpec extends Specification {
	FileTransporterService fts = new FileTransporterService()
	
	def "test createMessageText creates message text from files in folder when hasError is false"(){
		setup:
		fts.filePath = new File("src/test/Test_Folder/8")
		fts.folderNames = "starting_structures, trajectory_geom, product_geom"
		
		when:
		String msgText = fts.createMessageText(false)
		
		then:
		msgText.length() == 514789
	}
	
	def "test createMessageText creates error text when hasError is true"(){
		setup:
		fts.filePath = new File("src/test/Test_Folder/8")
		fts.folderNames = "starting_structures, trajectory_geom, product_geom"
		fts.errorToSend = "Error#An error occurred while computing."
		
		when:
		String msgText = fts.createMessageText(true)
		
		then:
		msgText == "Error#An error occurred while computing."
	}
@Ignore //file missing	
	def "test that getErrorBool returns true if error has occurred"(){
		when:
		File file = new File("src/test/Test_Folder/jobexd_sn_error.log")
		fts.errorLine = "energy or dftd3 step seems to be in error"
		
		then:
		fts.getErrorBool(file) == true
	}

	def "test that getErrorBool returns false if no error has occurred"(){
		when:
		File file = new File("src/test/Test_Folder/jobexd_sn_noError.log")
		fts.errorLine = "energy or dftd3 step seems to be in error"
		
		then:
		fts.getErrorBool(file) == false
	}
	
	def "test sendNewFiles sets filePath"(){
		when:
		fts.jmsTemplate = Mock(JmsTemplate)
		fts.errorLine = "energy or dftd3 step seems to be in error"
		fts.folderNames = "starting_structures, trajectory_geom, product_geom"
		fts.errorToSend = "Error#An error occurred while computing."
		fts.productQueue = "wallerlab.productQueue"
		org.springframework.messaging.Message message = Mock()
		message.getPayload() >> new File("src/test/Test_Folder/8/jobexd_sn.log")
		fts.sendNewFiles(message)
		
		then:
		fts.filePath.getPath() == "src/test/Test_Folder/8"
	}
}
