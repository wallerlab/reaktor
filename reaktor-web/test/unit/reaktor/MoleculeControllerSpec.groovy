package reaktor



import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.*

@TestFor(MoleculeController)
@Mock(Molecule)
class MoleculeControllerSpec extends Specification {

    def populateValidParams(params) {
        assert params != null
		params["name"] = "specName"
        params["smilesString"] = "cccc"
    }

    void "Test the index action returns the correct model"() {

        when:"The index action is executed"
            controller.index()

        then:"The model is correct"
            !model.moleculeInstanceList
            model.moleculeInstanceCount == 0
    }

    void "Test the create action returns the correct model"() {
        when:"The create action is executed"
            controller.create()

        then:"The model is correctly created"
            model.moleculeInstance!= null
    }

    void "Test the save action correctly persists an instance"() {

        when:"The save action is executed with an invalid instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'POST'
            def molecule = new Molecule()
            molecule.validate()
            controller.save(molecule)

        then:"The create view is rendered again with the correct model"
            model.moleculeInstance!= null
            view == 'create'

        when:"The save action is executed with a valid instance"
            response.reset()
            populateValidParams(params)
            molecule = new Molecule(params)

            controller.save(molecule)

        then:"A redirect is issued to the show action"
            response.redirectedUrl == '/molecule/show/1'
            controller.flash.message != null
            Molecule.count() == 1
    }

    void "Test that the edit action returns the correct model"() {
        when:"The edit action is executed with a null domain"
            controller.edit(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the edit action"
            populateValidParams(params)
            def molecule = new Molecule(params)
            controller.edit(molecule)

        then:"A model is populated containing the domain instance"
            model.moleculeInstance == molecule
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when:"Update is called for a domain instance that doesn't exist"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'PUT'
            controller.update(null)

        then:"A 404 error is returned"
            response.redirectedUrl == '/molecule/index'
            flash.message != null


        when:"An invalid domain instance is passed to the update action"
            response.reset()
            def molecule = new Molecule()
            molecule.validate()
            controller.update(molecule)

        then:"The edit view is rendered again with the invalid instance"
            view == 'edit'
            model.moleculeInstance == molecule

        when:"A valid domain instance is passed to the update action"
            response.reset()
            populateValidParams(params)
            molecule = new Molecule(params).save(flush: true)
            controller.update(molecule)

        then:"A redirect is issues to the show action"
            response.redirectedUrl == "/molecule/show/$molecule.id"
            flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when:"The delete action is called for a null instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            controller.delete(null)

        then:"A 404 is returned"
            response.redirectedUrl == '/molecule/index'
            flash.message != null

        when:"A domain instance is created"
            response.reset()
            populateValidParams(params)
            def molecule = new Molecule(params).save(flush: true)

        then:"It exists"
            Molecule.count() == 1

        when:"The domain instance is passed to the delete action"
            controller.delete(molecule)

        then:"The instance is deleted"
            Molecule.count() == 0
            response.redirectedUrl == '/molecule/index'
            flash.message != null
    }
}
