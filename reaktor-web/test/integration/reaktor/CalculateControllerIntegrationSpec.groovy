package reaktor

import reaktor.services.CalculateService
import spock.lang.*

/**
 *
 */
class CalculateControllerIntegrationSpec extends Specification {
	CalculateController cc = new CalculateController()
	def pcs

    def setup() {
    }

    def cleanup() {
    }

    void "test calculate calls ProductCalculatorService"() {
		cc.productCalculatorService = pcs
		cc.calculate()
    }

    void "test calculate forwards to result"() {
		cc.productCalculatorService = pcs
		cc.calculate()
		assert response.forwardedUrl == "calculate/result"
    }

	@Ignore
    void "test calculate forwards to result with xyzFileString param"() {
		when:
		params.MolTxt1 = "abcde"
		pcs = Mock(CalculateService)
		pcs.calculateProduct >> "Test"
		cc.productCalculatorService = pcs
		cc.calculate()
		
		then:
		response.forwardedParams.xyzFileString == "Test"
    }
	
	void "test upload"(){
	}
	
	void "test result"(){
	}
}
