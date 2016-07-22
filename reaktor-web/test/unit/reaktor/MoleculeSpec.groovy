package reaktor

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Molecule)
class MoleculeSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "test name"() {
		setup:
		Molecule molec = new Molecule()
		
		when:
		molec.name = "C6H6"
		
		then:
		molec.name == "C6H6"
    }
}
