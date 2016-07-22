package reaktor.parser

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import javax.jms.TextMessage
import spock.lang.*

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
class MessageParserSpec extends Specification {
	MessageParser mp = new MessageParser()
	File testFolder = new File("test/Test_Folder")

    def setup() {
    }

    def cleanup() {
    }
	
    void "test that populateReactionFolder creates start, trajectory, and product folders"() {
		setup:
		mp.reactionFolder = new File(testFolder,"ProductData_10000")
		mp.reactionFolder.mkdir()
		
		when:
		def foldersWithFiles = new File(testFolder,"msgParserTest.txt").text.split('#')
		mp.populateReactionFolder(foldersWithFiles)
		
		then:
		new File(mp.reactionFolder, "starting_structures").exists()
		new File(mp.reactionFolder, "trajectory_geom").exists()
		new File(mp.reactionFolder, "product_geom").exists()
		
		cleanup:
		mp.reactionFolder.deleteDir()
    }
	
    void "test that populateReactionFolder creates files in those folders"() {
		setup:
		mp.reactionFolder = new File(testFolder,"ProductData_10000")
		mp.reactionFolder.mkdir()
		
		when:
		String[] foldersWithFiles = new File(testFolder,"msgParserTest.txt").text.split("#")
		mp.populateReactionFolder(foldersWithFiles)
		
		then:
		new File(mp.reactionFolder, "starting_structures").list().length == 3
		new File(mp.reactionFolder, "trajectory_geom").list().length == 3
		new File(mp.reactionFolder, "product_geom").list().length == 4
		
		cleanup:
		mp.reactionFolder.deleteDir()
    }

    void "test that populateReactionFolder writes text to those files"() {
		setup:
		mp.reactionFolder = new File(testFolder,"ProductData_10000")
		mp.reactionFolder.mkdir()
		
		when:
		String[] foldersWithFiles = new File(testFolder,"msgParserTest.txt").text.split("#")
		mp.populateReactionFolder(foldersWithFiles)
		
		then:
		new File(mp.reactionFolder, "starting_structures/start_0.xyz").text.length() == 740
		new File(mp.reactionFolder, "trajectory_geom/trj_1.xyz").text.length() == 230264
		new File(mp.reactionFolder, "product_geom/product_25.xyz").text.length() == 608
		
		cleanup:
		mp.reactionFolder.deleteDir()
    }

    void "test that populateReactionFolder returns string with only product data"() {
		setup:
		mp.reactionFolder = new File(testFolder,"ProductData_10000")
		mp.reactionFolder.mkdir()
		
		when:
		String[] foldersWithFiles = new File(testFolder,"msgParserTest.txt").text.split("#")
		ArrayList moleculeData = mp.populateReactionFolder(foldersWithFiles)
		
		then:
		moleculeData.size == 4
		
		cleanup:
		mp.reactionFolder.deleteDir()
    }
}
