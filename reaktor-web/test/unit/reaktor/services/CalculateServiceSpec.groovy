package reaktor.services

import grails.test.mixin.TestMixin
import grails.test.mixin.Mock
import grails.test.mixin.support.GrailsUnitTestMixin
import reaktor.Reaction
import reaktor.fileCreator.*
import spock.lang.*

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
@Mock(reaktor.Molecule)
class CalculateServiceSpec extends Specification {
	CalculateService pcs = new CalculateService()

	@Unroll
	void "test createMessageString creates message String from folder"() {
		when:
		pcs.filePath = new File(a)
		String message = pcs.createMessageString(b)

		then:
		message == c

		where:
		a																|	b				|	c
		"test/Test_Folder/test_files/createMessageTest_noHiddenFiles"	| 	"Aggregation"	|	"Aggregation,define.inp:     \n     \n a coord\n desy   0.05\n ired \n *\n b all def2-SVP            \n *\n eht \n     \n           0\n     \n     \n     \n     \n     \n     \n     \n dft\non \n func\n b-p\n grid\nm4                   \n q\n ri \n on \njbas b all def2-SVP\n m  \n        1000\n q\n scf\n iter\n        100\n conv\n           7\n thi\n  1.000000011686097E-007\n           4\n ints\n y\n        1000  twoint\n*\n     \n q\n,startMols0.xyz:12\n\n  C        0.00000        1.40272        0.00000\n  H        0.00000        2.49029        0.00000\n  C       -1.21479        0.70136        0.00000\n  H       -2.15666        1.24515        0.00000\n  C       -1.21479       -0.70136        0.00000\n  H       -2.15666       -1.24515        0.00000\n  C        0.00000       -1.40272        0.00000\n  H        0.00000       -2.49029        0.00000\n  C        1.21479       -0.70136        0.00000\n  H        2.15666       -1.24515        0.00000\n  C        1.21479        0.70136        0.00000\n  H        2.15666        1.24515        0.00000\n,startMols1.xyz:12\n\n  C        0.00000        1.40272        0.00000\n  H        0.00000        2.49029        0.00000\n  C       -1.21479        0.70136        0.00000\n  H       -2.15666        1.24515        0.00000\n  C       -1.21479       -0.70136        0.00000\n  H       -2.15666       -1.24515        0.00000\n  C        0.00000       -1.40272        0.00000\n  H        0.00000       -2.49029        0.00000\n  C        1.21479       -0.70136        0.00000\n  H        2.15666       -1.24515        0.00000\n  C        1.21479        0.70136        0.00000\n  H        2.15666        1.24515        0.00000\n"
		"test/Test_Folder/test_files/createMessageTest_hiddenFiles"		|	"Aggregation"	|	"Aggregation,define.inp:     \n     \n a coord\n desy   0.05\n ired \n *\n b all def2-SVP            \n *\n eht \n     \n           0\n     \n     \n     \n     \n     \n     \n     \n dft\non \n func\n b-p\n grid\nm4                   \n q\n ri \n on \njbas b all def2-SVP\n m  \n        1000\n q\n scf\n iter\n        100\n conv\n           7\n thi\n  1.000000011686097E-007\n           4\n ints\n y\n        1000  twoint\n*\n     \n q\n,startMols0.xyz:12\n\n  C        0.00000        1.40272        0.00000\n  H        0.00000        2.49029        0.00000\n  C       -1.21479        0.70136        0.00000\n  H       -2.15666        1.24515        0.00000\n  C       -1.21479       -0.70136        0.00000\n  H       -2.15666       -1.24515        0.00000\n  C        0.00000       -1.40272        0.00000\n  H        0.00000       -2.49029        0.00000\n  C        1.21479       -0.70136        0.00000\n  H        2.15666       -1.24515        0.00000\n  C        1.21479        0.70136        0.00000\n  H        2.15666        1.24515        0.00000\n,startMols1.xyz:12\n\n  C        0.00000        1.40272        0.00000\n  H        0.00000        2.49029        0.00000\n  C       -1.21479        0.70136        0.00000\n  H       -2.15666        1.24515        0.00000\n  C       -1.21479       -0.70136        0.00000\n  H       -2.15666       -1.24515        0.00000\n  C        0.00000       -1.40272        0.00000\n  H        0.00000       -2.49029        0.00000\n  C        1.21479       -0.70136        0.00000\n  H        2.15666       -1.24515        0.00000\n  C        1.21479        0.70136        0.00000\n  H        2.15666        1.24515        0.00000\n"
		"test/Test_Folder/test_files/createMessageTest_noHiddenFiles"	|	"Reaction"		|	"Reaction,define.inp:     \n     \n a coord\n desy   0.05\n ired \n *\n b all def2-SVP            \n *\n eht \n     \n           0\n     \n     \n     \n     \n     \n     \n     \n dft\non \n func\n b-p\n grid\nm4                   \n q\n ri \n on \njbas b all def2-SVP\n m  \n        1000\n q\n scf\n iter\n        100\n conv\n           7\n thi\n  1.000000011686097E-007\n           4\n ints\n y\n        1000  twoint\n*\n     \n q\n,startMols0.xyz:12\n\n  C        0.00000        1.40272        0.00000\n  H        0.00000        2.49029        0.00000\n  C       -1.21479        0.70136        0.00000\n  H       -2.15666        1.24515        0.00000\n  C       -1.21479       -0.70136        0.00000\n  H       -2.15666       -1.24515        0.00000\n  C        0.00000       -1.40272        0.00000\n  H        0.00000       -2.49029        0.00000\n  C        1.21479       -0.70136        0.00000\n  H        2.15666       -1.24515        0.00000\n  C        1.21479        0.70136        0.00000\n  H        2.15666        1.24515        0.00000\n,startMols1.xyz:12\n\n  C        0.00000        1.40272        0.00000\n  H        0.00000        2.49029        0.00000\n  C       -1.21479        0.70136        0.00000\n  H       -2.15666        1.24515        0.00000\n  C       -1.21479       -0.70136        0.00000\n  H       -2.15666       -1.24515        0.00000\n  C        0.00000       -1.40272        0.00000\n  H        0.00000       -2.49029        0.00000\n  C        1.21479       -0.70136        0.00000\n  H        2.15666       -1.24515        0.00000\n  C        1.21479        0.70136        0.00000\n  H        2.15666        1.24515        0.00000\n"
		"test/Test_Folder/test_files/createMessageTest_hiddenFiles"		|	"Reaction"		|	"Reaction,define.inp:     \n     \n a coord\n desy   0.05\n ired \n *\n b all def2-SVP            \n *\n eht \n     \n           0\n     \n     \n     \n     \n     \n     \n     \n dft\non \n func\n b-p\n grid\nm4                   \n q\n ri \n on \njbas b all def2-SVP\n m  \n        1000\n q\n scf\n iter\n        100\n conv\n           7\n thi\n  1.000000011686097E-007\n           4\n ints\n y\n        1000  twoint\n*\n     \n q\n,startMols0.xyz:12\n\n  C        0.00000        1.40272        0.00000\n  H        0.00000        2.49029        0.00000\n  C       -1.21479        0.70136        0.00000\n  H       -2.15666        1.24515        0.00000\n  C       -1.21479       -0.70136        0.00000\n  H       -2.15666       -1.24515        0.00000\n  C        0.00000       -1.40272        0.00000\n  H        0.00000       -2.49029        0.00000\n  C        1.21479       -0.70136        0.00000\n  H        2.15666       -1.24515        0.00000\n  C        1.21479        0.70136        0.00000\n  H        2.15666        1.24515        0.00000\n,startMols1.xyz:12\n\n  C        0.00000        1.40272        0.00000\n  H        0.00000        2.49029        0.00000\n  C       -1.21479        0.70136        0.00000\n  H       -2.15666        1.24515        0.00000\n  C       -1.21479       -0.70136        0.00000\n  H       -2.15666       -1.24515        0.00000\n  C        0.00000       -1.40272        0.00000\n  H        0.00000       -2.49029        0.00000\n  C        1.21479       -0.70136        0.00000\n  H        2.15666       -1.24515        0.00000\n  C        1.21479        0.70136        0.00000\n  H        2.15666        1.24515        0.00000\n"


	}


	void "test that createInputFiles moves both reactant files"(){
		when:
		pcs.filePath = new File("test/Test_Folder")
		pcs.incomingFolder = new File("test/Test_Folder/test_files")
		pcs.xyzFileCreator = Mock(XyzFileCreator)
		pcs.defineFileCreator = Mock(DefineFileCreator)
		Reaction reaction = Mock(Reaction)
		reaction.id >> new Long(1)
		reaction.reactants >> new ArrayList(["benzene","bz"])
		reaction.reactionType >> "Reaction"
		pcs.createInputFiles(reaction)
		
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
		pcs.filePath.eachFile{file ->
			if(file.name.endsWith(".xyz")){
				file.renameTo(new File(pcs.incomingFolder,file.name))
			}
		}
	}
	@Unroll
	void "test if createProjectFilePath creates folder if filePath doesn't exist"(){
		setup:
		pcs.mainFolder = new File("test/Test_Folder")
		Reaction reaction = Mock(Reaction)
		reaction.reactionFolderName >> b
		
		expect:
		pcs.createProjectFilePath(reaction)
		new File(pcs.mainFolder, c).exists()
		
		cleanup:
		new File(pcs.mainFolder, b).deleteDir()

		where:
		a				|	b						|	c
		"Reaction"		|	"ReactionData_12345"	| 	"ReactionData_12345/input_files"
		"Aggregation"	|	"AggregationData_12345"	| 	"AggregationData_12345/input_files"
	}
}
