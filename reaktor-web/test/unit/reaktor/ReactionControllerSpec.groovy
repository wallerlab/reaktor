package reaktor



import grails.test.mixin.*
import spock.lang.*

@TestFor(ReactionController)
@Mock(Reaction)
class ReactionControllerSpec extends Specification {

    def populateValidParams(params) {
        assert params != null
    }

    void "Test the index action returns the correct model"() {

        when:"The index action is executed"
            controller.index()

        then:"The model is correct"
            !model.reactionInstanceList
            model.reactionInstanceCount == 0
    }

    void "Test the create action returns the correct model"() {
        when:"The create action is executed"
            controller.create()

        then:"The model is correctly created"
            model.reactionInstance!= null
    }
	@Ignore
    void "Test the save action correctly persists an instance"() {

        when:"The save action is executed with an invalid instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'POST'
            def reaction = new Reaction()
            reaction.validate()
            controller.save(reaction)

        then:"The create view is rendered again with the correct model"
            model.reactionInstance!= null
            view == 'create'

        when:"The save action is executed with a valid instance"
            response.reset()
            populateValidParams(params)
            reaction = new Reaction(params)

            controller.save(reaction)

        then:"A redirect is issued to the show action"
            response.redirectedUrl == '/reaction/show/1'
            controller.flash.message != null
            Reaction.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        when:"The show action is executed with a null domain"
            controller.show(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the show action"
            populateValidParams(params)
            def reaction = new Reaction(params)
            controller.show(reaction)

        then:"A model is populated containing the domain instance"
            model.reactionInstance == reaction
    }

    void "Test that the edit action returns the correct model"() {
        when:"The edit action is executed with a null domain"
            controller.edit(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the edit action"
            populateValidParams(params)
            def reaction = new Reaction(params)
            controller.edit(reaction)

        then:"A model is populated containing the domain instance"
            model.reactionInstance == reaction
    }
	@Ignore
    void "Test the update action performs an update on a valid domain instance"() {
        when:"Update is called for a domain instance that doesn't exist"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'PUT'
            controller.update(null)

        then:"A 404 error is returned"
            response.redirectedUrl == '/reaction/index'
            flash.message != null


        when:"An invalid domain instance is passed to the update action"
            response.reset()
            def reaction = new Reaction()
            reaction.validate()
            controller.update(reaction)

        then:"The edit view is rendered again with the invalid instance"
            view == 'edit'
            model.reactionInstance == reaction

        when:"A valid domain instance is passed to the update action"
            response.reset()
            populateValidParams(params)
            reaction = new Reaction(params).save(flush: true)
            controller.update(reaction)

        then:"A redirect is issues to the show action"
            response.redirectedUrl == "/reaction/show/$reaction.id"
            flash.message != null
    }
	@Ignore
    void "Test that the delete action deletes an instance if it exists"() {
        when:"The delete action is called for a null instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            controller.delete(null)

        then:"A 404 is returned"
            response.redirectedUrl == '/reaction/index'
            flash.message != null

        when:"A domain instance is created"
            response.reset()
            populateValidParams(params)
            def reaction = new Reaction(params).save(flush: true)

        then:"It exists"
            Reaction.count() == 1

        when:"The domain instance is passed to the delete action"
            controller.delete(reaction)

        then:"The instance is deleted"
            Reaction.count() == 0
            response.redirectedUrl == '/reaction/index'
            flash.message != null
    }
}
