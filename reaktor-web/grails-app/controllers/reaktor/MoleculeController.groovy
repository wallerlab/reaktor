package reaktor

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import grails.plugin.springsecurity.annotation.Secured

@Secured(["ROLE_ADMIN"])
@Transactional(readOnly = true)
class MoleculeController {
	
	def mainFolder

	static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
	
	def index(Integer max) {
		params.max = Math.min(max ?: 10, 100)
		respond Molecule.list(params), model:[moleculeInstanceCount: Molecule.count()]
	}
	
	@Secured(["ROLE_ADMIN", "ROLE_USER"])
	def show(Molecule moleculeInstance) {
		def xyzFileString = moleculeInstance.createXyzFileString(mainFolder)
		StringBuilder fileString = new StringBuilder()
		xyzFileString.eachLine {line ->
			fileString << line.trim()
			fileString << "\\n"
		}
		println fileString
		[xyzFileString : fileString, name: moleculeInstance.name]
	}

	def showInReactionViewer(Molecule moleculeInstance){
		def xyzFileString = moleculeInstance.createXyzFileString(mainFolder)
		println xyzFileString.class
		render (text: xyzFileString, contentType: "text")
	}
	
	def create() {
		respond new Molecule(params)
	}

	@Transactional
	def save(Molecule moleculeInstance) {
		if (moleculeInstance == null) {
			notFound()
			return
		}

		if (moleculeInstance.hasErrors()) {
			respond moleculeInstance.errors, view:'create'
			return
		}

		moleculeInstance.save flush:true

		request.withFormat {
			form multipartForm {
				flash.message = message(code: 'default.created.message', args: [
					message(code: 'molecule.label', default: 'Molecule'),
					moleculeInstance.id
				])
				redirect moleculeInstance
			}
			'*' { respond moleculeInstance, [status: CREATED] }
		}
	}

	def edit(Molecule moleculeInstance) {
		respond moleculeInstance
	}

	@Transactional
	def update(Molecule moleculeInstance) {
		if (moleculeInstance == null) {
			notFound()
			return
		}

		if (moleculeInstance.hasErrors()) {
			respond moleculeInstance.errors, view:'edit'
			return
		}

		moleculeInstance.save flush:true

		request.withFormat {
			form multipartForm {
				flash.message = message(code: 'default.updated.message', args: [
					message(code: 'Molecule.label', default: 'Molecule'),
					moleculeInstance.id
				])
				redirect moleculeInstance
			}
			'*'{ respond moleculeInstance, [status: OK] }
		}
	}

	@Transactional
	def delete(Molecule moleculeInstance) {

		if (moleculeInstance == null) {
			notFound()
			return
		}

		moleculeInstance.delete flush:true

		request.withFormat {
			form multipartForm {
				flash.message = message(code: 'default.deleted.message', args: [
					message(code: 'Molecule.label', default: 'Molecule'),
					moleculeInstance.id
				])
				redirect action:"index", method:"GET"
			}
			'*'{ render status: NO_CONTENT }
		}
	}


	protected void notFound() {
		request.withFormat {
			form multipartForm {
				flash.message = message(code: 'default.not.found.message', args: [
					message(code: 'molecule.label', default: 'Molecule'),
					params.id
				])
				redirect action: "index", method: "GET"
			}
			'*'{ render status: NOT_FOUND }
		}
	}
}
