package reaktor

import grails.test.spock.IntegrationSpec

class ReactionIntegrationSpec extends IntegrationSpec {

    def setup() {
    }

    def cleanup() {
    }

    void "test createProjectFolderName"(){
        when:
        Reaction reaction = new Reaction(status: status, reactionType: reactionType)
        reaction.save()
        reaction.createProjectFolderName()

        then:
        reaction.reactionFolderName == "${reactionType}Data_${reaction.id}"

        where:
        status       |   reactionType
        "enqueued"   |   "Reaction"
        "enqueued"   |   "Aggregation"
    }
}
