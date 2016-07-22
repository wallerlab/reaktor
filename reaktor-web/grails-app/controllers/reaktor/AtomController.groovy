package reaktor

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import grails.plugin.springsecurity.annotation.Secured

@Secured(["ROLE_ADMIN"])
@Transactional(readOnly = true)
class AtomController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Atom.list(params), model:[atomInstanceCount: Atom.count()]
    }
	
	@Secured(["ROLE_ADMIN", "ROLE_USER"])
    def show(Atom atomInstance) {
        respond atomInstance
    }

    def create() {
        respond new Atom(params)
    }

    @Transactional
    def save(Atom atomInstance) {
        if (atomInstance == null) {
            notFound()
            return
        }

        if (atomInstance.hasErrors()) {
            respond atomInstance.errors, view:'create'
            return
        }

        atomInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'atom.label', default: 'Atom'), atomInstance.id])
                redirect atomInstance
            }
            '*' { respond atomInstance, [status: CREATED] }
        }
    }

    def edit(Atom atomInstance) {
        respond atomInstance
    }

    @Transactional
    def update(Atom atomInstance) {
        if (atomInstance == null) {
            notFound()
            return
        }

        if (atomInstance.hasErrors()) {
            respond atomInstance.errors, view:'edit'
            return
        }

        atomInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Atom.label', default: 'Atom'), atomInstance.id])
                redirect atomInstance
            }
            '*'{ respond atomInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Atom atomInstance) {

        if (atomInstance == null) {
            notFound()
            return
        }

        atomInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Atom.label', default: 'Atom'), atomInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'atom.label', default: 'Atom'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
