package reaktor.wrapper

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.*

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
class OBabelWrapperSpec extends Specification {
	OBabelWrapper obw = new OBabelWrapper()

    def setup() {
		obw.incomingFolder = new File("test/Test_Folder")
    }

    def cleanup() {
    }
	@Unroll
    void "test run uses openBabel to create file"() {
		expect:
		obw.run(a, b) == c
		new File(obw.incomingFolder, b).exists()
		
		cleanup:
		new File(obw.incomingFolder, b).delete()
		
		where:
		a 								| 	b 				|	c
		"N#N"							|	"nitrogen.xyz"	|	new File("test/Test_Folder/nitrogen.xyz")
		"c1ccccc1"						|	"bz_mol.xyz"	|	new File("test/Test_Folder/bz_mol.xyz")
    }

	@Unroll
    void "test run uses openBabel to create file with data"() {
		expect:
		obw.run(a, b)
		new File(obw.incomingFolder, b).readLines()[0] == c
		
		cleanup:
		new File(obw.incomingFolder, b).delete()
		
		where:
		a 								| 	b 				|	c
		"N#N"							|	"nitrogen.xyz"	|	"2"
		"c1ccccc1"						|	"bz_mol.xyz"	|	"12"
    }

	@Unroll
	void "test run converts file to smiles"(){
		setup:
		obw.incomingFolder = new File(obw.incomingFolder, "test_files")

		expect:
		obw.run(file) == smilesString

		where:
		smilesString		|	file
		"c1ccccc1"			|	new File("test/Test_Folder/test_files/benzene.xyz")
		"c1ccccc1"			|	new File("test/Test_Folder/test_files/molecule0.xyz")
		"c1ccccc1"			|	new File("test/Test_Folder/test_files/molecule1.xyz")
		"C=C"				|	new File("test/Test_Folder/test_files/molecule6.xyz")
		"C=C"				|	new File("test/Test_Folder/test_files/ethene.xyz")
		"C1CCCC1"			|	new File("test/Test_Folder/test_files/pentane.mrv")
		"c1ccccc1.C1CCCCC1"	|	new File("test/Test_Folder/test_files/two_mols.mrv")
		"c1ccccc1.C1CCCCC1"	|	new File("test/Test_Folder/test_files/two_mols_close.mrv")

	}
	
	void "test getErrorString gets error string"(){
		setup:
		Process process = new ProcessBuilder(System.getenv("obabel"), "N#O", "NO.xyz", "--gen3d")
			.directory(obw.incomingFolder).start()
		
		when:
		def errorString = obw.getErrorString(process)
		
		then:
		errorString
	}
}
