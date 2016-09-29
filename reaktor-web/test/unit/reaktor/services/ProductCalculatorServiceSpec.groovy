package reaktor.services

import grails.test.mixin.TestMixin
import grails.test.mixin.Mock
import grails.test.mixin.support.GrailsUnitTestMixin
import reaktor.Molecule
import reaktor.Reaction
import reaktor.wrappers.*
import reaktor.populator.*
import reaktor.fileCreator.*
import spock.lang.*

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
@Mock(reaktor.Molecule)
class ProductCalculatorServiceSpec extends Specification {
	ProductCalculatorService pcs = new ProductCalculatorService()

    def setup() {
    }

    def cleanup() {
    }
	
	void "test createMessageString creates message String from folder"(){
		when:
		pcs.filePath = new File("test/Test_Folder/test_files/createMessageTest")
		String message = pcs.createMessageString()
		
		then:
		message == "define.inp:     \n     \n a coord\n desy   0.05\n ired \n *\n b all def2-SVP            \n *\n eht \n     \n           0\n     \n     \n     \n     \n     \n     \n     \n dft\non \n func\n b-p\n grid\nm4                   \n q\n ri \n on \njbas b all def2-SVP\n m  \n        1000\n q\n scf\n iter\n        100\n conv\n           7\n thi\n  1.000000011686097E-007\n           4\n ints\n y\n        1000  twoint\n*\n     \n q\n,startMols0.xyz:12\n\n  C        0.00000        1.40272        0.00000\n  H        0.00000        2.49029        0.00000\n  C       -1.21479        0.70136        0.00000\n  H       -2.15666        1.24515        0.00000\n  C       -1.21479       -0.70136        0.00000\n  H       -2.15666       -1.24515        0.00000\n  C        0.00000       -1.40272        0.00000\n  H        0.00000       -2.49029        0.00000\n  C        1.21479       -0.70136        0.00000\n  H        2.15666       -1.24515        0.00000\n  C        1.21479        0.70136        0.00000\n  H        2.15666        1.24515        0.00000\n,startMols1.xyz:12\n\n  C        0.00000        1.40272        0.00000\n  H        0.00000        2.49029        0.00000\n  C       -1.21479        0.70136        0.00000\n  H       -2.15666        1.24515        0.00000\n  C       -1.21479       -0.70136        0.00000\n  H       -2.15666       -1.24515        0.00000\n  C        0.00000       -1.40272        0.00000\n  H        0.00000       -2.49029        0.00000\n  C        1.21479       -0.70136        0.00000\n  H        2.15666       -1.24515        0.00000\n  C        1.21479        0.70136        0.00000\n  H        2.15666        1.24515        0.00000\n" || "define.inp:     \n     \n a coord\n desy   0.05\n ired \n *\n b all def2-SVP            \n *\n eht \n     \n           0\n     \n     \n     \n     \n     \n     \n     \n dft\non \n func\n b-p\n grid\nm4                   \n q\n ri \n on \njbas b all def2-SVP\n m  \n        1000\n q\n scf\n iter\n        100\n conv\n           7\n thi\n  1.000000011686097E-007\n           4\n ints\n y\n        1000  twoint\n*\n     \n q\n,startMols1.xyz:12\n\n  C        0.00000        1.40272        0.00000\n  H        0.00000        2.49029        0.00000\n  C       -1.21479        0.70136        0.00000\n  H       -2.15666        1.24515        0.00000\n  C       -1.21479       -0.70136        0.00000\n  H       -2.15666       -1.24515        0.00000\n  C        0.00000       -1.40272        0.00000\n  H        0.00000       -2.49029        0.00000\n  C        1.21479       -0.70136        0.00000\n  H        2.15666       -1.24515        0.00000\n  C        1.21479        0.70136        0.00000\n  H        2.15666        1.24515        0.00000\n,startMols0.xyz:12\n\n  C        0.00000        1.40272        0.00000\n  H        0.00000        2.49029        0.00000\n  C       -1.21479        0.70136        0.00000\n  H       -2.15666        1.24515        0.00000\n  C       -1.21479       -0.70136        0.00000\n  H       -2.15666       -1.24515        0.00000\n  C        0.00000       -1.40272        0.00000\n  H        0.00000       -2.49029        0.00000\n  C        1.21479       -0.70136        0.00000\n  H        2.15666       -1.24515        0.00000\n  C        1.21479        0.70136        0.00000\n  H        2.15666        1.24515        0.00000\n"	}
	
	void "test that createPyreactorInputFiles moves reactant1 file"(){
		when:
		pcs.filePath = new File("test/Test_Folder")
		pcs.defaultFolder = new File("test/Test_Folder/Incoming Files")
		pcs.xyzFileCreator = Mock(XyzFileCreator)
		pcs.defineFileCreator = Mock(DefineFileCreator)
		pcs.reaction = Mock(Reaction)
		pcs.reaction.id >> new Long(1)
		ArrayList moleculesForPyreactor = new ArrayList(["benzene","bz"])
		pcs.createInputFiles(moleculesForPyreactor)
		
		then:
		new File(pcs.filePath, "benzene.xyz").text == """12
benzene
  C        0.00000        1.40272        0.00000
  H        0.00000        2.49029        0.00000
  C       -1.21479        0.70136        0.00000
  H       -2.15666        1.24515        0.00000
  C       -1.21479       -0.70136        0.00000
  H       -2.15666       -1.24515        0.00000
  C        0.00000       -1.40272        0.00000
  H        0.00000       -2.49029        0.00000
  C        1.21479       -0.70136        0.00000
  H        2.15666       -1.24515        0.00000
  C        1.21479        0.70136        0.00000
  H        2.15666        1.24515        0.00000"""
		
		cleanup:
		pcs.filePath.eachFile{file ->
			if(file.name.endsWith(".xyz")){
				file.renameTo(new File(pcs.defaultFolder,file.name))
			}
		}
	}
	
	void "test that createPyreactorInputFiles moves reactant2 file"(){
		when:
		pcs.filePath = new File("test/Test_Folder")
		pcs.defaultFolder = new File("test/Test_Folder/test_files")
		pcs.xyzFileCreator = Mock(XyzFileCreator)
		pcs.defineFileCreator = Mock(DefineFileCreator)
		pcs.reaction = Mock(Reaction)
		pcs.reaction.id >> new Long(1)
		ArrayList moleculesForPyreactor = new ArrayList(["benzene","bz"])
		pcs.createInputFiles(moleculesForPyreactor)
		
		then:
		new File(pcs.filePath, "bz.xyz").text == """12
bz
  C        0.00000        1.40272        0.00000
  H        0.00000        2.49029        0.00000
  C       -1.21479        0.70136        0.00000
  H       -2.15666        1.24515        0.00000
  C       -1.21479       -0.70136        0.00000
  H       -2.15666       -1.24515        0.00000
  C        0.00000       -1.40272        0.00000
  H        0.00000       -2.49029        0.00000
  C        1.21479       -0.70136        0.00000
  H        2.15666       -1.24515        0.00000
  C        1.21479        0.70136        0.00000
  H        2.15666        1.24515        0.00000"""
		
		cleanup:
		new File("${pcs.filePath}").eachFile{file ->
			if(file.name.endsWith(".xyz")){
				file.renameTo(new File(pcs.defaultFolder,file.name))
			}
		}
	}
	
	void "test if createProjectFilePath creates folder if filePath doesn't exist"(){
		when:
		pcs.mainFolder = new File("test/Test_Folder")
		pcs.reaction = Mock(Reaction)
		pcs.reaction.id >> new Long(12345)
		pcs.createProjectFilePath()
		
		then:
		new File(pcs.mainFolder, "ProductData_12345/input_files").exists()
		
		cleanup:
		new File(pcs.mainFolder, "ProductData_12345").deleteDir()
	}
	
	void "test if createProjectFilePath creates the correct file path"() {
		when:
		pcs.mainFolder = new File("test/Test_Folder")
		pcs.reaction = Mock(Reaction)
		pcs.reaction.id >> new Long(12345)
		pcs.createProjectFilePath()
		
		then:
		pcs.filePath.path == "test/Test_Folder/ProductData_12345/input_files"
		
		cleanup:
		new File(pcs.mainFolder, "ProductData_12345").deleteDir()
	}
	
	void "test if createTwoXyzFiles sends back same arraylist when size == 2"(){
		when:
		pcs.reaction = Mock(Reaction)
		Molecule mol1 = Mock()
		Molecule mol2 = Mock()
		pcs.reaction.reactants >> [mol1, mol2]
		ArrayList newMolsForPR = pcs.createTwoXyzFiles()
		
		then:
		newMolsForPR == [mol1, mol2]
	}
}
