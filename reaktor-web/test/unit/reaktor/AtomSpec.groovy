package reaktor

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Atom)
class AtomSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "test element"() {
		setup:
		Atom atom = new Atom()
		
		when:
		atom.element = "C"
		
		then:
		atom.element == "C"
    }

    void "test xCoord"() {
		setup:
		Atom atom = new Atom()
		
		when:
		atom.xCoord = 1.2345
		
		then:
		atom.xCoord == 1.2345
    }

    void "test yCoord"() {
		setup:
		Atom atom = new Atom()
		
		when:
		atom.yCoord = 1.2345
		
		then:
		atom.yCoord == 1.2345
    }

    void "test zCoord"() {
		setup:
		Atom atom = new Atom()
		
		when:
		atom.zCoord = 1.2345
		
		then:
		atom.zCoord == 1.2345
    }

    void "test idInMolecule"() {
		setup:
		Atom atom = new Atom()
		
		when:
		atom.idInMolecule = "a1"
		
		then:
		atom.idInMolecule == "a1"
    }
	
	void "test toString"(){
		setup:
		Atom atom = new Atom(element: "C", xCoord: 1.2345, yCoord: 2.3456, zCoord: 3.4567, idInMolecule: "a0")
		
		when:
		String atomString = atom.toString()
		
		then:
		atomString == "C 1.2345 2.3456 3.4567"
	}
}
