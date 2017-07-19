package reaktor.services



import grails.plugin.jms.JmsService
import reaktor.*
import reaktor.security.*
import reaktor.fileCreator.*
import reaktor.populator.*
import reaktor.wrapper.OBabelWrapper
import spock.lang.*

/**
 * Integration tests for CalculateService
 * 
 */
class CalculateServiceIntegrationSpec extends Specification {
	CalculateService pcs = new CalculateService()

    def setup() {
		pcs.mainFolder = new File("test/Test_Folder")
		pcs.incomingFolder = new File("test/Test_Folder/test_files")
		pcs.filePath = new File("test/Test_Folder")
		pcs.xyzFileCreator = Mock(FileCreator)
		pcs.defineFileCreator = Mock(FileCreator)
    }

	@Unroll
	void "test createInputFiles calls FileCreator 3x when files not in default folder"() {
		setup:
		Molecule reactant1 = Mock()
		Molecule reactant2 = Mock()
		Reaction reaction = Mock()

		when:
		reactant1.toString() >> a
		reactant2.toString() >> b
		reaction.reactants >> [reactant1, reactant2]
		reaction.reactionType >> c
		pcs.createInputFiles(reaction)

		then:
		d*pcs.xyzFileCreator.createFile(_,_)
		e*pcs.defineFileCreator.createFile(_,_)
		new File(pcs.filePath, "${a}.xyz").exists() == f
		new File(pcs.filePath, "${b}.xyz").exists() == g

		cleanup:
		pcs.filePath.eachFile{file ->
			if(file.name.endsWith(".xyz")){
				file.renameTo(new File(pcs.incomingFolder,file.name))
			}
		}
		
		where:
		a			|	b			|	c				|	d	|	e	|	f		|	g
		"reactant1"	|	"reactant2"	|	"Reaction"		|	2	|	1	|	false	|	false
		"benzene"	|	"bz"		|	"Reaction"		|	0	|	1	|	true	|	true
		"reactant1"	|	"reactant2"	|	"Aggregation"	|	2	|	0	|	false	|	false
		"benzene"	|	"bz"		|	"Aggregation"	|	0	|	0	|	true	|	true
	}

	void "test createReaction creates reaction"(){
		setup:
		Molecule reactant1 = new Molecule(name: "benzene", smilesString: "c1ccccc1")
		Molecule reactant2 = new Molecule(name: "bz", smilesString: "c1ccccc1")
		reactant1.save()
		reactant2.save()
		ArrayList mols = new ArrayList([reactant1, reactant2])
		User user = new User(username: "abc", password: "aA12!@")
		user.save(flush: true)
		
		when:
		Reaction reaction = pcs.createReaction(mols, user, "Reaction")
		
		then:
		Reaction.findAllByUser(user).size() == 1
		Reaction.findByUser(user).reactants.size() == 2
		[reaction] == Reaction.findAllByUser(user)
	}

	@Unroll
	void "test convertToXYZ calls obabel and returns list of reactant molecules"(){
		setup:
		pcs.obabel = Mock(OBabelWrapper)
		File file1 = new File("test/Test_Folder/test_files/benzene.xyz")
		File file2 = new File("test/Test_Folder/test_files/bz.xyz")

		when:
		pcs.convertToXYZ(a) == c

		then:
		b*pcs.obabel.run(_,_) >>> [file1, file2]

		where:
		a			|	b	|	c
		"CC.C#N"	|	2	|	[new File("test/Test_Folder/test_files/benzene.xyz"), new File("test/Test_Folder/test_files/bz.xyz")]
		"CC"		|	1	|	[new File("test/Test_Folder/test_files/benzene.xyz")]
	}

    void "test calculate calls populator"() {
		setup:
		pcs.jmsService = Mock(JmsService)
		File xyzFile1 = new File("test/Test_Folder/test_files/bz.xyz")
		File xyzFile2 = new File("test/Test_Folder/test_files/benzene.xyz")
		ArrayList input = new ArrayList([xyzFile1,xyzFile2])
		Molecule reactant1 = new Molecule(name: "benzene", smilesString: "c1ccccc1")
		Molecule reactant2 = new Molecule(name: "bz", smilesString: "c1ccccc1")
		pcs.xyzDatabasePopulator = Mock(Populator)
		pcs.obabel = Mock(OBabelWrapper)
		pcs.obabel.run(_) >> "c1ccccc1"
		User user = new User(username: "abc", password: "aA12!@")
		user.save(flush: true)
		String string = "Aggregation"
		
		when:
		pcs.calculate(input, user, string)
		
		then:
		1*pcs.xyzDatabasePopulator.populate(_) >> [reactant1, reactant2]
		
		cleanup:
		File aggFolder = new File(pcs.mainFolder, "AggregationData_${Reaction.findByUser(User.findByUsername("abc")).id}")
		println aggFolder.exists()
		println new File(aggFolder, "input_files").exists()
		new File(aggFolder, "input_files").eachFile{file ->
			if(file.name.endsWith(".xyz")){
				file.renameTo(new File(pcs.incomingFolder,file.name))
			}
		}
		aggFolder.deleteDir()
    }
	
	void "test calculate calls jmsService.send"(){
		setup:
		pcs.jmsService = Mock(JmsService)
		File xyzFile1 = new File("test/Test_Folder/test_files/bz.xyz")
		File xyzFile2 = new File("test/Test_Folder/test_files/benzene.xyz")
		ArrayList input = new ArrayList([xyzFile1,xyzFile2])
		Molecule reactant1 = new Molecule(name: "benzene", smilesString: "c1ccccc1")
		Molecule reactant2 = new Molecule(name: "bz", smilesString: "c1ccccc1")
		pcs.obabel = Mock(OBabelWrapper)
		pcs.obabel.run(_) >> "c1ccccc1"
		pcs.xyzDatabasePopulator = Mock(Populator)
		pcs.xyzDatabasePopulator.populate(_) >> [reactant1, reactant2]
		User user = new User(username: "abc", password: "aA12!@")
		user.save(flush: true)
		String string = "Aggregation"
		
		when:
		pcs.calculate(input, user, string)
		
		then:
		1*pcs.jmsService.send(_,_,_)
		
		cleanup:
		File aggFolder = new File(pcs.mainFolder, "AggregationData_${Reaction.findByUser(User.findByUsername("abc")).id}")
		new File(aggFolder, "input_files").eachFile{file ->
			if(file.name.endsWith(".xyz")){
				file.renameTo(new File(pcs.incomingFolder,file.name))
			}
		}
		aggFolder.deleteDir()
	}
}
