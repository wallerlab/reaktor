package reaktor.security

import reaktor.security.User;
import grails.plugin.springsecurity.annotation.Secured

@Secured(["ROLE_ADMIN"])
class UserController extends grails.plugin.springsecurity.ui.UserController {

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond User.list(params), model:[userInstanceCount: User.count()]
    }
	
    def show(User userInstance) {
        respond userInstance
    }
}
