package reaktor.services

import spock.lang.*
import grails.plugin.jms.JmsService
import java.util.concurrent.CountDownLatch
import javax.jms.Message
import reaktor.*
import reaktor.parser.Parser
import reaktor.populator.Populator
import reaktor.wrapper.Wrapper
import reaktor.services.ProductCalculatorService
import static java.util.concurrent.TimeUnit.SECONDS

/**
 *
 */
class ListenerServiceIntegrationSpec extends Specification {
	ListenerService listenerService
	@Shared
	def jmsService
	
	private static final QUEUE_RECEPTION_TIMEOUT_SEC = 1
	private messageReceived = new CountDownLatch(1)

    def setup() { 
		listenerService.defaultFolder = new File("test/Test_Folder")
		listenerService.obabel = Mock(Wrapper)
		listenerService.productCalculatorService = Mock(ProductCalculatorService)
		listenerService.messageParser = Mock(Parser)
		listenerService.xyzDatabasePopulator = Mock(Populator)
    }

    def cleanup() {
		File file = new File("ProductData_1")
		file.delete()
    }
	
    void "test runMentorReactants calls productCalculatorService"() {
		when:
		def messageString = "CCC,cccc"
		jmsService.send(queue:"wallerlab.mentorQueue", messageString)
        messageReceived.await(QUEUE_RECEPTION_TIMEOUT_SEC, SECONDS)
		
		then:
		1*listenerService.productCalculatorService.calculateProduct(_,_,_)
    }
	
    void "test runMentorReactants calls obabelWrapper"() {
		when:
		def messageString = "CCC,cccc"
		jmsService.send(queue:"wallerlab.mentorQueue", messageString)
        messageReceived.await(QUEUE_RECEPTION_TIMEOUT_SEC, SECONDS)
		
		then:
		2*listenerService.obabel.run(_,_)
    }
	
	void "test updateReactionWithProducts calls messageParser.parse"(){
		when:
		Molecule molecule1 = Molecule.findByName("Methane")
		Molecule molecule2 = Molecule.findByName("dummy")
		listenerService.xyzDatabasePopulator.populate(_) >> [molecule1, molecule2]
		jmsService.send(queue:"wallerlab.productQueue", "message") {Message msg ->
				msg.setCorrelationId("1")
				msg
			}
        messageReceived.await(QUEUE_RECEPTION_TIMEOUT_SEC, SECONDS)
		
		then:
		1*listenerService.messageParser.parse(_) >> ["",""]
	}
	
	void "test updateReactionWithProducts calls xyzDatabasePopulator"(){
		setup:
		Molecule molecule1 = Molecule.findByName("Methane")
		Molecule molecule2 = Molecule.findByName("dummy")
		
		when:
		listenerService.messageParser.parse(_) >> ["",""]
		jmsService.send(queue:"wallerlab.productQueue", "message") {Message msg ->
				msg.setCorrelationId("1")
				msg
			}
        messageReceived.await(QUEUE_RECEPTION_TIMEOUT_SEC, SECONDS)
		
		then:
		1*listenerService.xyzDatabasePopulator.populate(_) >> [molecule1, molecule2]
	}
	
	void "test updateReactionWithProducts updates molecule reactions"(){
		when:
		listenerService.messageParser.parse(_) >> ["",""]
		Molecule molecule1 = Molecule.findByName("Methane")
		Molecule molecule2 = Molecule.findByName("dummy")
		listenerService.xyzDatabasePopulator.populate(_) >> [molecule1, molecule2]
		jmsService.send(queue:"wallerlab.productQueue", "message") {Message msg ->
				msg.setCorrelationId("1")
				msg
			}
        messageReceived.await(QUEUE_RECEPTION_TIMEOUT_SEC, SECONDS)
		molecule1.reactions.size()
		
		then:
		molecule1.reactions.size() == 1
		molecule2.reactions.size() == 1
	}
	
	void "test updateReactionWithProducts updates reaction products"(){
		when:
		listenerService.messageParser.parse(_) >> ["",""]
		Molecule molecule1 = Molecule.findByName("Methane")
		Molecule molecule2 = Molecule.findByName("dummy")
		listenerService.xyzDatabasePopulator.populate(_) >> [molecule1, molecule2]
		jmsService.send(queue:"wallerlab.productQueue", "message") {Message msg ->
				msg.setCorrelationId("1")
				msg
			}
        messageReceived.await(QUEUE_RECEPTION_TIMEOUT_SEC, SECONDS)
		
		then:
		Reaction reaction = Reaction.findById(new Long(1))
		reaction.products.size() == 2
	}
}
