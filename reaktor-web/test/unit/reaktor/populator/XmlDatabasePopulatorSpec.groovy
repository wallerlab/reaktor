package reaktor.populator

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
class XmlDatabasePopulatorSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "test addToFragments adds fragment to fragment already in list"() {
		setup:
		XmlDatabasePopulator xdp = new XmlDatabasePopulator()
		SortedSet frag1 = new TreeSet(["a1","a2","a3"])
		SortedSet frag2 = new TreeSet(["a13","a4","a5","a6"])
		SortedSet fragment = new TreeSet(["a10","a15","a2"])
		ArrayList fragments = new ArrayList([frag1, frag2])
		SortedSet outFrag1 = new TreeSet(["a1","a10","a15","a2","a3"])
		SortedSet outFrag2 = new TreeSet(["a13","a4","a5","a6"])
		ArrayList outFragments = new ArrayList([outFrag1, outFrag2])
		
		when:
		ArrayList fragArray = xdp.addToFragments(fragments, fragment)
		
		then:
		fragArray == outFragments
    }

    void "test addToFragments creates new fragment in list when fragment not in list"() {
		setup:
		XmlDatabasePopulator xdp = new XmlDatabasePopulator()
		SortedSet frag1 = new TreeSet(["a1","a2","a3"])
		SortedSet frag2 = new TreeSet(["a13","a4","a5","a6"])
		SortedSet fragment = new TreeSet(["a10","a12","a15"])
		ArrayList fragments = new ArrayList([frag1, frag2])
		ArrayList outFragments = new ArrayList([frag1, frag2, fragment])
		
		when:
		ArrayList fragArray = xdp.addToFragments([fragments,fragment])
		
		then:
		fragArray == outFragments
    }
	
	void "test that finalListCheck adds one fragment in list to another when one atomID in each fragment is the same"(){
		setup:
		XmlDatabasePopulator xdp = new XmlDatabasePopulator()
		SortedSet frag1 = new TreeSet(["a1","a2","a3"])
		SortedSet frag2 = new TreeSet(["a13","a4","a5","a6"])
		SortedSet frag3 = new TreeSet(["a10","a15","a2"])
		ArrayList fragments = new ArrayList([frag1, frag2, frag3])
		SortedSet outFrag1 = new TreeSet(["a1","a10","a15","a2","a3"])
		SortedSet outFrag2 = new TreeSet(["a13","a4","a5","a6"])
		ArrayList outFragments = new ArrayList([outFrag1, outFrag2])
		
		when:
		ArrayList fragArray = xdp.finalListCheck(fragments)
		
		then:
		fragArray == outFragments
	}
	
	void "test that finalListCheck adds no fragment in list to another when no atomIDs are the same"(){
		setup:
		XmlDatabasePopulator xdp = new XmlDatabasePopulator()
		SortedSet frag1 = new TreeSet(["a1","a2","a3"])
		SortedSet frag2 = new TreeSet(["a13","a4","a5","a6"])
		SortedSet frag3 = new TreeSet(["a10","a12","a15"])
		ArrayList fragments = new ArrayList([frag1, frag2, frag3])
		
		when:
		ArrayList fragArray = xdp.finalListCheck(fragments)
		
		then:
		fragArray == fragments
	}
	
	void "test that separateInputMolecules correctly separates highly branched molecules"(){
		setup:
		XmlDatabasePopulator xdp = new XmlDatabasePopulator()
		XmlParser parser = new XmlParser()
		SortedSet fragment1 = new TreeSet()
		SortedSet fragment2 = new TreeSet()
		def i = 1
		while(i < 142){
			if(i < 43) fragment1 << "a"+i.toString()
			else if(i < 52) fragment2 << "a"+i.toString()
			else if(i < 132) fragment1 << "a"+i.toString()
			else fragment2 << "a"+i.toString()
			i++
		}
		ArrayList outFragments = new ArrayList([fragment1,fragment2])
		
		when:
		File highlyBranchedMolecules = new File("test/Test_Folder/test_files/HighlyBranched.xml")
		xdp.moleculeXMLNode = parser.parse(highlyBranchedMolecules).molecule
		def fragments = xdp.separateInputMolecules()
		
		then:
		fragments == outFragments
	}
}
