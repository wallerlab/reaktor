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
		obw.defaultFolder = new File("test/Test_Folder")
    }

    def cleanup() {
    }
	@Unroll
    void "test run uses openBabel to create file"() {
		expect:
		obw.run(a, b)
		new File(obw.defaultFolder, b).exists()
		
		cleanup:
		new File(obw.defaultFolder, b).delete()
		
		where:
		a 								| 	b 						
		"N#N"							|	"nitrogen.xyz"
		"[Cu+2].[O-]S(=O)(=O)[O-]"		|	"cuso4.xyz"
		"test_files/ethene.xyz"			|	"ethene.mol"
    }
	@Unroll
    void "test run uses openBabel to create file with data"() {
		expect:
		obw.run(a, b)
		new File(obw.defaultFolder, b).readLines()[0] == c
		
		cleanup:
		new File(obw.defaultFolder, b).delete()
		
		where:
		a 								| 	b 				|	c
		"N#N"							|	"nitrogen.xyz"	|	"2"
		"[Cu+2].[O-]S(=O)(=O)[O-]"		|	"cuso4.xyz"		|	"6"
		"test_files/ethene.xyz"			|	"ethene.mol"	|	"ethene"
    }
	
	void "test getErrorString gets error string"(){
		setup:
		Process process = new ProcessBuilder(System.getenv("obabel"), "N#O", "NO.xyz", "--gen3d")
			.directory(obw.defaultFolder).start()
		
		when:
		def errorString = obw.getErrorString(process)
		
		then:
		errorString
	}
}
