package reaktor

import grails.test.mixin.TestFor
import spock.lang.IgnoreRest
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Reaction)
class ReactionSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

	@Unroll
	void "test reaction validates for status and reactionType"(){
		when:
		Reaction reaction = new Reaction(status: status, reactionType: reactionType)

		then:
		reaction.validate() == validationBool

		where:
		status						|	reactionType	|	validationBool
		"unfinished"				|	"Reaction"		|	false
		"unfinished"				|	"Aggregation"	|	false
		"unfinished"				|	"nothing"		|	false
		"finished"					|	"Reaction"		|	true
		"finished"					|	"Aggregation"	|	true
		"finished"					|	"nothing"		|	false
		"calculating"				|	"Reaction"		|	true
		"calculating"				|	"Aggregation"	|	true
		"calculating"				|	"nothing"		|	false
		"waiting for parameters"	|	"Reaction"		|	true
		"waiting for parameters"	|	"Aggregation"	|	true
		"waiting for parameters"	|	"nothing"		|	false
		"enqueued"					|	"Reaction"		|	true
		"enqueued"					|	"Aggregation"	|	true
		"enqueued"					|	"nothing"		|	false
		"error while calculating"	|	"Reaction"		|	true
		"error while calculating"	|	"Aggregation"	|	true
		"error while calculating"	|	"nothing"		|	false
		"finished & cleaned"		|	"Reaction"		|	true
		"finished & cleaned"		|	"Aggregation"	|	true
		"finished & cleaned"		|	"nothing"		|	false
		"error & cleaned"			|	"Reaction"		|	true
		"error & cleaned"			|	"Aggregation"	|	true
		"error & cleaned"			|	"nothing"		|	false
	}

	void "test toString"(){
		when:
		Reaction reaction = new Reaction(status: "enqueued", reactionType: "Aggregation")
		
		then:
		reaction.toString() == "${reaction.id} ${reaction.dateCreated.toString()}"
	}
}
