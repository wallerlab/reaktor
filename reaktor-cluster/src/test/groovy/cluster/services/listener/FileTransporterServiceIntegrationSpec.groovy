package cluster.services.listener

import spock.lang.*

import org.springframework.jms.core.JmsTemplate

class FileTransporterServiceIntegrationSpec extends Specification{
	FileTransporterService fts = new FileTransporterService()

	def "test that sendNewFiles calls jmsTemplate.convertAndSend"() {
		setup:
		File filePath = new File("src/test/Test_Folder/8")
		fts.jmsTemplate = Mock(JmsTemplate)
		
		when:
		boolean hasError = true
		fts.sendNewFiles(filePath, hasError)
		
		then:
		1*fts.jmsTemplate.convertAndSend(_,_,_)
	}

}
