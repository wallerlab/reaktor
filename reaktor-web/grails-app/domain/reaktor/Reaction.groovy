package reaktor

import reaktor.security.User
/**
 * Defines a reaction
 * 
 * Params:
 * product = product of reaction
 * reactants = reactants of reaction
 * 
 * @author suzanne
 *
 */
class Reaction {
	User user //in plugin, is email address String
	String status
	Date dateCreated
	Date lastUpdated
	
	static mappedBy = [reactants: "reactantReaction", products: "productReaction"]
	
	static hasMany = [reactants : Molecule, products : Molecule]
	static belongsTo = [user: User] //not in plugin
	
	String toString(){
		"$id ${dateCreated.toString()}"
	}

    static constraints = {
		user nullable: true
		status inList:["finished", "calculating", "waiting for parameters",
			 "enqueued", "error while calculating", "finished & cleaned", "error & cleaned"]
    }
}
