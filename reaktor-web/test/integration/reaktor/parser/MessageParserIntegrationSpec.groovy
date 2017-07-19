package reaktor.parser

import javax.jms.TextMessage
import reaktor.Reaction

import spock.lang.*
import grails.plugin.mail.MailService
import reaktor.security.User

/**
 *
 */
class MessageParserIntegrationSpec extends Specification {
	MessageParser mp

    def setup() {
		mp = new MessageParser()
		mp.mainFolder = new File("test/Test_Folder")
    }

    def cleanup() {
    }
	
	void "test that parse changes the reaction status in the database to error when text says error"(){
		setup:
		TextMessage msg = Mock()
		msg.getJMSCorrelationID() >> "1"
		msg.getText() >>"Error#An error has occurred while calculating."
		mp.mailService = Mock(MailService)
		
		when:
		Reaction reaction = Reaction.get(new Long(1))
		mp.parse(msg)
		
		then:
		reaction.status == "error while calculating"
		
		cleanup:
		reaction.status = "calculating"
		reaction.save(flush: true)
	}
	
	void "test that parse changes the reaction status in the database to finished when no error"(){
		setup:
		TextMessage msg = Mock()
		msg.getJMSCorrelationID() >> "1"
		msg.getText() >> new File(mp.mainFolder,"msgParserTest.txt").text
		mp.mailService = Mock(MailService)
		def productFolder = new File(mp.mainFolder, "ReactionData_1")
		
		when:
		Reaction reaction = Reaction.get(new Long(1))
		mp.parse(msg)
		
		then:
		reaction.status == "finished"
		
		cleanup:
		List foldersToDelete = ["starting_structures", "trajectory_geom", "product_geom"]
		for(folderName in foldersToDelete){
			new File(productFolder, folderName).deleteDir()
		}
		reaction.status = "calculating"
		reaction.save(flush: true)
	}
	
	void "test that parse returns an empty ArrayList if an error has occurred"(){
		setup:
		TextMessage msg = Mock()
		msg.getJMSCorrelationID() >> "1"
		msg.getText() >>"Error#An error has occurred while calculating."
		mp.mailService = Mock(MailService)
		
		when:
		def moleculeData = mp.parse(msg)
		
		then:
		moleculeData.size() == 0
	}
	
	void "test that parse returns an ArrayList if no error has occurred"(){
		setup:
		def productFolder = new File(mp.mainFolder, "ReactionData_1")
		TextMessage msg = Mock()
		msg.getJMSCorrelationID() >> "1"
		msg.getText() >> new File(mp.mainFolder,"msgParserTest.txt").text
		mp.mailService = Mock(MailService)
		
		when:
		def moleculeData = mp.parse(msg)
		
		then:
		moleculeData.size() == 4
		
		cleanup:
		List foldersToDelete = ["starting_structures", "trajectory_geom", "product_geom"]
		for(folderName in foldersToDelete){
			new File(productFolder, folderName).deleteDir()
		}
	}
	
	void "test that sendMailToUser calls mailService.sendMail"(){
		setup:
		Reaction reaction = new Reaction(user:User.findByUsername("max"), status: "error while calculating", reactants: [])
		reaction.save(flush: true)
		mp.mailService = Mock(MailService)
		
		when:
		mp.sendMailToUser(reaction)
		
		then:
		1*mp.mailService.sendMail(_)
		
	}
	
	void "test that sendMailToUser puts errorSubject and errorMessage in mail"(){
		setup:
		Reaction reaction = new Reaction(user:User.findByUsername("max"), status: "error while calculating", reactants: [])
		reaction.save(flush: true)
		mp.mailService = Mock(MailService)
		mp.errorSubject = "Your reaction has encountered an error"
		mp.errorMessage = "Your reaction has encountered an error."
		Closure closure = {
			to "fake.email@fake.com"
			from 'do.not.reply@localhost'
			subject "Your reaction has encountered an error"
			body "Your reaction has encountered an error."
		}
		MessageParserMailTestHelper mpmth = new MessageParserMailTestHelper()
        closure.delegate = mpmth
        closure.resolveStrategy = Closure.DELEGATE_FIRST
		closure.call(mpmth)
		MessageParserMailTestHelper mpmth2 = new MessageParserMailTestHelper()
		
		when:
		mp.sendMailToUser(reaction)
		
		then:
		1*mp.mailService.sendMail({Closure realClosure ->
				realClosure.delegate = mpmth2
				realClosure.resolveStrategy = Closure.DELEGATE_FIRST
				realClosure.call(mpmth2)
				mpmth2.toField == mpmth.toField
				mpmth2.fromField == mpmth.fromField
				mpmth2.subjectField == mpmth.subjectField
				mpmth2.bodyField == mpmth.bodyField
			})
	}
	
	void "test that sendMailToUser puts finishedSubject and finishedMessage in mail"(){
		setup:
		Reaction reaction = new Reaction(user:User.findByUsername("max"), status: "finished", reactants: [])
		reaction.save(flush: true)
		mp.mailService = Mock(MailService)
		mp.finishedSubject = "Your reaction is finished"
		mp.finishedMessage = "Your reaction is finished. You can now see the products on our website:\nreaktor.wallerlab.org"
		Closure closure = {
			to "fake.email@fake.com"
			from 'do.not.reply@localhost'
			subject "Your reaction is finished"
			body "Your reaction is finished. You can now see the products on our website:\nreaktor.wallerlab.org"
		}
		MessageParserMailTestHelper mpmth = new MessageParserMailTestHelper()
        closure.delegate = mpmth
        closure.resolveStrategy = Closure.DELEGATE_FIRST
		closure.call(mpmth)
		MessageParserMailTestHelper mpmth2 = new MessageParserMailTestHelper()
		
		when:
		mp.sendMailToUser(reaction)
		
		then:
		1*mp.mailService.sendMail({Closure realClosure ->
				realClosure.delegate = mpmth2
				realClosure.resolveStrategy = Closure.DELEGATE_FIRST
				realClosure.call(mpmth2)
				mpmth2.toField == mpmth.toField
				mpmth2.fromField == mpmth.fromField
				mpmth2.subjectField == mpmth.subjectField
				mpmth2.bodyField == mpmth.bodyField
			})
	}
}


class MessageParserMailTestHelper{
	String toField
	String fromField
	String subjectField
	String bodyField
	
	void to(Object args){
		toField = args
	}
	void from(String args){
		fromField = args
	}
	void subject(String args){
		subjectField = args
	}
	void body(String args){
		bodyField = args
	}
}
