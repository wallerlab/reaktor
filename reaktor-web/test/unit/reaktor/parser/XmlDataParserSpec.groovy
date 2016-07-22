package reaktor.parser

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
class XmlDataParserSpec extends Specification {
	XmlDataParser xmlParser = new XmlDataParser()

    def setup() {
    }

    def cleanup() {
    }
	
	void "test getFileToParse takes well-formatted xml as File and returns it"(){
		when:
		File source = new File("test/Test_Folder/test_files/good_reactants.xml")
		def xmlFile = xmlParser.getFileToParse(source)
		
		then:
		xmlFile.readLines()[1].contains("cml")
	}
	
	void "test getFileToParse takes well-formatted xml as filename String and returns as File"(){
		when:
		String source = "test/Test_Folder/test_files/good_reactants.xml"
		def xmlFile = xmlParser.getFileToParse(source)
		
		then:
		xmlFile.readLines()[1].contains("cml")
	}

    void "test getFileToParse changes badly formatted xml to well-formatted xml"() {
		when:
		String source = "test/Test_Folder/test_files/bad_reactants.xml"
		def xmlFile = xmlParser.getFileToParse(source)
		
		then:
		xmlFile.readLines()[1] == "<molecules>"
    }
	
	void "test checkIsFile returns true if source is a File"(){
		when:
		File source = new File("test/Test_Folder/test_files/good_reactants.xml")
		def isFile = xmlParser.checkIsFile(source)
		
		then:
		isFile == true
	}
	
	void "test checkIsFile returns true if source is a filename String"(){
		when:
		String source = "test/Test_Folder/test_files/good_reactants.xml"
		def isFile = xmlParser.checkIsFile(source)
		
		then:
		isFile == true
	}
	
	void "test checkIsFile returns false if source is neither a File nor a filename String"(){
		when:
		String source = new File("test/Test_Folder/test_files/good_reactants.xml").text
		def isFile = xmlParser.checkIsFile(source)
		
		then:
		isFile == false
	}
	
	void "test parse parses String as text"(){
		when:
		String source = new File("test/Test_Folder/test_files/good_reactants.xml").text
		def parsedText = xmlParser.parse(source)
		
		then:
		parsedText instanceof Node
	}
	
	void "test parse parses File"(){
		when:
		File source = new File("test/Test_Folder/test_files/bad_reactants.xml")
		def parsedText = xmlParser.parse(source)
		
		then:
		parsedText instanceof Node
	}
	
	void "test parse parses filename String as File"(){
		when:
		String source = "test/Test_Folder/test_files/bad_reactants.xml"
		def parsedText = xmlParser.parse(source)
		
		then:
		parsedText instanceof Node
	}
}
