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
	boolean hasProducts
	
	static hasMany = [molecules: MolecRxn]
	static belongsTo = [user: User] //not in plugin
	
	String toString(){
		"$id ${dateCreated.toString()}"
	}

    static constraints = {
		user nullable: true
		status inList:["finished", "calculating", "waiting for parameters",
			 "enqueued", "error while calculating", "finished & cleaned", "error & cleaned"]
    }
	
	/**
	 * Changes hasProducts to true if reaction has products
	 * 
	 * @return
	 */
	public void hasProducts(){
		hasProducts = molecules.parallelStream().anyMatch({molecule -> molecule.role == "product"})
	}
}
