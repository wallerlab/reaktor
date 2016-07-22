package reaktor.security

import grails.plugin.springsecurity.annotation.Secured
import grails.plugin.springsecurity.ui.RegistrationCode;

@Secured(["permitAll()"])
class RegisterController extends grails.plugin.springsecurity.ui.RegisterController {
	
	def changePassword(){
		
		def registrationCode = new RegistrationCode(username: springSecurityService.currentUser.username)
		registrationCode.save(flush: true)
		RegistrationCode regCode = RegistrationCode.findByUsername(springSecurityService.currentUser.username)
		def token = regCode.token
		render view: "resetPassword", model: [token: token]
		
	}
}
