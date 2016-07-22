package reaktor.security

import grails.test.mixin.TestFor
import spock.lang.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(User)
class UserSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "test username"() {
		setup:
		User user = new User()
		
		when:
		user.username = "suzanne"
		
		then:
		user.username == "suzanne"
    }

    void "test password"() {
		setup:
		User user = new User()
		
		when:
		user.password = "suzanne"
		
		then:
		user.password == "suzanne"
    }

    void "test password with validation"() {
		setup:
		User user = new User(username : "suzanne", password: "password")
		mockForConstraintsTests(User)
		
		assertTrue user.validate()
    }
	@Unroll
    void "test email does not validate when not in email format"() {
		setup:
		User user = new User(username : "suzanne", password: "password", email: a)
		mockForConstraintsTests(User)
		assert user.validate() == b
		
		where:
		a								|	b
		"email"							|	false
		"email@"						|	false
		".email@something.com"			|	false
		"email@.something.com"			|	false
		"email.@something.com"			|	false
		"email..this@something.com"		|	false
		"email!@this@something.com"		|	false
		"email#\$@something.com"		|	false
		"email@()something.com"			|	false
		"email@something.&^com"			|	false
		"email.this@something.com."		|	false
		"email.this@something.coma"		|	false
		"email.this@something.4co"		|	false
		"email@something.com"			|	true
		"email@something.co"			|	true
		"email.this@something.com"		|	true
		"email-this@something.com"		|	true
		"email.this@something.co.uk"	|	true
		"email.this@something.com.uk"	|	true
		"email.this@something-co.uk"	|	true
		"email.this@something-co.ukl"	|	true
		"email.this@something.coma.ukl"	|	true
    }
}
