package cluster.services;

import spock.lang.*

import org.springframework.jms.core.JmsTemplate;

class FileTransporterServiceIntegrationSpec extends Specification{
	FileTransporterService fts = new FileTransporterService()

	def "test that convertAndSendMessage calls jmsTemplate.convertAndSend"() {
		setup:
		fts.jmsTemplate = Mock(JmsTemplate)
		
		when:
		boolean hasError = true
		fts.convertAndSendMessage(hasError)
		
		then:
		1*fts.jmsTemplate.convertAndSend(_,_,_)
	}

}
