package reaktor

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_ADMIN'])
@Transactional(readOnly = true)
class ReactionController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
	
	def springSecurityService
	def jmsService

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Reaction.list(params), model:[reactionInstanceCount: Reaction.count()]
    }
	
	@Secured(['ROLE_ADMIN','ROLE_USER'])
	def getUserIndex(){
		//in plugin, is loadCurrentUser().email
		ArrayList userReactions = Reaction.findAllByUser(springSecurityService.loadCurrentUser())
		Map userreactionsmap = ["data":userReactions]
		JSON.use('deep'){
			render userreactionsmap as JSON
		}
	}
	
	@Transactional
	def clean(Reaction reactionInstance){
		if (reactionInstance == null) {
			notFound()
			return
		}
		
		jmsService.send(queue:"wallerlab.cleanQueue", reactionInstance.id.toString())
		if(reactionInstance.status == "finished"){
			reactionInstance.properties = [status:"finished & cleaned"]
			reactionInstance.save flush: true
		} else {
			reactionInstance.properties = [status:"error & cleaned"]
			reactionInstance.save flush: true
		}
		request.withFormat{
			form multipartForm {
				flash.message = "Folder ${reactionInstance.id} has been cleaned from the server."
				redirect action:"show", method:"GET"
				return
			}
		}
	}
	
	@Secured(['ROLE_ADMIN','ROLE_USER'])
    def show(Reaction reactionInstance) {
        respond reactionInstance
    }

    def create() {
        respond new Reaction(params)
    }

    @Transactional
    def save(Reaction reactionInstance) {
        if (reactionInstance == null) {
            notFound()
            return
        }

        if (reactionInstance.hasErrors()) {
            respond reactionInstance.errors, view:'create'
            return
        }

        reactionInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'reaction.label', default: 'Reaction'), reactionInstance.id])
                redirect reactionInstance
            }
            '*' { respond reactionInstance, [status: CREATED] }
        }
    }

    def edit(Reaction reactionInstance) {
        respond reactionInstance
    }

    @Transactional
    def update(Reaction reactionInstance) {
        if (reactionInstance == null) {
            notFound()
            return
        }

        if (reactionInstance.hasErrors()) {
            respond reactionInstance.errors, view:'edit'
            return
        }

        reactionInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Reaction.label', default: 'Reaction'), reactionInstance.id])
                redirect reactionInstance
            }
            '*'{ respond reactionInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Reaction reactionInstance) {

        if (reactionInstance == null) {
            notFound()
            return
        }

        reactionInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Reaction.label', default: 'Reaction'), reactionInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'reaction.label', default: 'Reaction'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
