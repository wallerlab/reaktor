package reaktor.services



import grails.plugin.jms.JmsService
import reaktor.*
import reaktor.security.*
import reaktor.fileCreator.*
import reaktor.populator.*
import reaktor.wrappers.*
import spock.lang.*

/**
 * Integration tests for ProductCalculatorService
 * 
 */
class ProductCalculatorServiceIntegrationSpec extends Specification {
	ProductCalculatorService pcs = new ProductCalculatorService()

    def setup() {
		pcs.mainFolder = new File("test/Test_Folder")
		pcs.defaultFolder = new File("test/Test_Folder/IncomingFiles")
		pcs.filePath = new File("test/Test_Folder")
		pcs.xyzFileCreator = Mock(FileCreator)
		pcs.defineFileCreator = Mock(FileCreator)
    }

    def cleanup() {
		File file = new File("ProductData_1")
		file.delete()
    }
	
	void "test createInputFiles calls FileCreator 3x when files not in default folder"() {
		setup:
		Molecule reactant1 = Mock()
		Molecule reactant2 = Mock()
		reactant1.toString() >> "reactant1"
		reactant2.toString() >> "reactant2"
		ArrayList mols = new ArrayList([reactant1, reactant2])
		
		when:
		pcs.createInputFiles(mols)
		
		then:
		2*pcs.xyzFileCreator.createFile(_,_)
		1*pcs.defineFileCreator.createFile(_,_)
		
		cleanup:
		pcs.filePath.eachFile{file ->
			if(file.name.endsWith(".xyz")){
				file.renameTo(new File(pcs.defaultFolder,file.name))
			}
		}
	}
	
	void "test createInputFiles calls FileCreator 1x when files in default folder"() {
		setup:
		Molecule reactant1 = Mock()
		Molecule reactant2 = Mock()
		reactant1.toString() >> "benzene"
		reactant2.toString() >> "bz"
		ArrayList mols = new ArrayList([reactant1, reactant2])
		
		when:
		pcs.createInputFiles(mols)
		
		then:
		1*pcs.defineFileCreator.createFile(_,_)
		
		cleanup:
		pcs.filePath.eachFile{file ->
			if(file.name.endsWith(".xyz")){
				file.renameTo(new File(pcs.defaultFolder,file.name))
			}
		}
	}
	
	void "test createTwoXyzFiles calls xyzFileCreator.createFilesFromMultipleFiles when it has more than 2 objects in array list"(){
		when:
		Molecule reactant1 = Mock()
		Molecule reactant2 = Mock()
		Molecule reactant3 = Mock()
		pcs.reaction = Mock(Reaction)
		pcs.reaction.reactants >> [reactant1, reactant2, reactant3]
		pcs.reaction.reactants.size() >> 3
		pcs.createTwoXyzFiles()
		
		then:
		1*pcs.xyzFileCreator.createFilesFromMultipleFiles(_)
	}
	
	void "test createReaction creates reaction"(){
		setup:
		Molecule reactant1 = new Molecule(name: "benzene")
		Molecule reactant2 = new Molecule(name: "bz")
		ArrayList mols = new ArrayList([reactant1, reactant2])
		User user = new User(username: "abc", password: "aA12!@")
		user.save(flush: true)
		
		when:
		pcs.createReaction(mols, user)
		
		then:
		Reaction.findByUser(user)
	}

    void "test calculateProduct calls populator"() {
		setup:
		pcs.jmsService = Mock(JmsService)
		File xyzFile1 = new File("test/Test_Folder/IncomingFiles/bz.xyz")
		File xyzFile2 = new File("test/Test_Folder/IncomingFiles/benzene.xyz")
		ArrayList input = new ArrayList([xyzFile1,xyzFile2])
		Molecule reactant1 = new Molecule(name: "benzene")
		Molecule reactant2 = new Molecule(name: "bz")
		Populator populator = Mock(Populator)
		populator.populate(input) >> [reactant1, reactant2]
		User user = new User(username: "abc", password: "aA12!@")
		user.save(flush: true)
		
		when:
		pcs.calculateProduct(input, user, populator)
		
		then:
		1*populator.populate(_) >> [reactant1, reactant2]
		
		cleanup:
		File file3 = new File(pcs.mainFolder, "ProductData_5")
		file3.deleteDir()
    }
	
	void "test calculateProduct calls jmsService.send"(){
		setup:
		pcs.jmsService = Mock(JmsService)
		File xyzFile1 = new File("test/Test_Folder/IncomingFiles/bz.xyz")
		File xyzFile2 = new File("test/Test_Folder/IncomingFiles/benzene.xyz")
		ArrayList input = new ArrayList([xyzFile1,xyzFile2])
		Molecule reactant1 = new Molecule(name: "benzene")
		Molecule reactant2 = new Molecule(name: "bz")
		Populator populator = Mock(Populator)
		populator.populate(input) >> [reactant1, reactant2]
		User user = new User(username: "abc", password: "aA12!@")
		user.save(flush: true)
		
		when:
		pcs.calculateProduct(input, user, populator)
		
		then:
		1*pcs.jmsService.send(_,_,_)
		
		cleanup:
		File file4 = new File(pcs.mainFolder, "ProductData_6")
		file4.deleteDir()
	}
}
