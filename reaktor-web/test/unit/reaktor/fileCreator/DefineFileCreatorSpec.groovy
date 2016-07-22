package reaktor.fileCreator

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification
import reactor.calculatorService.*

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
class DefineFileCreatorSpec extends Specification {
	DefineFileCreator dfc = new DefineFileCreator()

    def setup() {
    }

    def cleanup() {
		File file = new File("test/Test_Folder/define.inp")
		file.delete()
    }

    void "test that createFile creates 'define.inp' file"() {
		when:
		String fileName = "define.inp"
		dfc.createFile(fileName, "test/Test_Folder")
		
		then:
		new File("test/Test_Folder", fileName).exists()
    }

    void "test that createFile creates 'define.inp' file with text"() {
		when:
		String fileName = "define.inp"
		dfc.createFile(fileName, "test/Test_Folder")
		
		then:
		new File("test/Test_Folder", fileName).text == "     \n     \n a coord\n sy c1 \n *\n no \n b all def2-SVP            \n *\n eht \n y    \n 0\n y    \n     \n     \n     \n     \n     \n     \n dft\non \n func b-p\n grid m4\n q\n ri \n on \n m  \n        1000\n q\n scf\n iter\n        500\n conv\n           6\n     \n q\n"
    }
}
