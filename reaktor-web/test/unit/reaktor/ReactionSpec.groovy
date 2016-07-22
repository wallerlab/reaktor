package reaktor

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Reaction)
class ReactionSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "test status accepts from allowed list"() {
		setup:
		Reaction reaction = new Reaction(status: "finished")
		mockForConstraintsTests(Reaction)
		
		assertTrue reaction.validate()
    }
	
	void "test status doesn't accept when not in allowed list"(){
		setup:
		Reaction reaction = new Reaction(status: "unfinished")
		mockForConstraintsTests(Reaction)
		
		assertFalse reaction.validate()
	}
}
