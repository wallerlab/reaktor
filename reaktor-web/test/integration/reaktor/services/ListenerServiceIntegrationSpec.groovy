package reaktor.services

import spock.lang.*

import java.util.concurrent.CountDownLatch
import javax.jms.Message
import reaktor.*
import reaktor.parser.Parser
import reaktor.populator.Populator
import reaktor.wrapper.Wrapper

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
		listenerService.incomingFolder = new File("test/Test_Folder")
		listenerService.obabel = Mock(Wrapper)
		listenerService.calculateService = Mock(CalculateService)
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
		1*listenerService.calculateService.calculate(_,_,_)
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
		1*listenerService.messageParser.parse(_) >> [new File(listenerService.incomingFolder,
													 "IncomingFiles/molecule1.xyz"),new File(listenerService.incomingFolder,
													 "IncomingFiles/molecule3.xyz")]
	}
	
	void "test updateReactionWithProducts calls xyzDatabasePopulator.populate"(){
		setup:
		Molecule molecule1 = Molecule.findByName("Methane")
		Molecule molecule2 = Molecule.findByName("dummy")
		
		when:
		listenerService.obabel.run(_) >> ["C","CC"]
		listenerService.messageParser.parse(_) >> [new File(listenerService.incomingFolder,
				"IncomingFiles/molecule1.xyz"),new File(listenerService.incomingFolder,
				"IncomingFiles/molecule3.xyz")]
		jmsService.send(queue:"wallerlab.productQueue", "message") {Message msg ->
				msg.setCorrelationId("1")
				msg
			}
        messageReceived.await(QUEUE_RECEPTION_TIMEOUT_SEC, SECONDS)
		
		then:
		1*listenerService.xyzDatabasePopulator.populate(_) >> [molecule1, molecule2]
	}
	//@IgnoreRest
	void "test updateReactionWithProducts updates molecules and reaction"(){
		when:
		Molecule molecule1 = new Molecule(name: "Ethane", smilesString: "CC")
		Molecule molecule2 = new Molecule(name: "Propane", smilesString: "CCC")
		molecule1.save()
		molecule2.save(flush:true)
		listenerService.messageParser.parse(_) >> [new File(listenerService.incomingFolder,
				"IncomingFiles/molecule1.xyz"),new File(listenerService.incomingFolder,
				"IncomingFiles/molecule3.xyz")]
		listenerService.obabel.run(_) >> ["C","CC"]
		listenerService.xyzDatabasePopulator.populate(_) >> [molecule1, molecule2]
		jmsService.send(queue:"wallerlab.productQueue", "message") {Message msg ->
				msg.setCorrelationId("1")
				msg
			}
        messageReceived.await(QUEUE_RECEPTION_TIMEOUT_SEC, SECONDS)
		
		then:
		Molecule.findByName("Ethane").productReaction.id == 1
		Molecule.findByName("Propane").productReaction.id == 1
		Reaction.findById(1).products.size() == 2
	}

}
