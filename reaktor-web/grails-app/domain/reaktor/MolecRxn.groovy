package reaktor

class MolecRxn {
	
	Molecule molecule
	Reaction reaction
	String role
	
	static belongsTo = [molecule: Molecule, reaction: Reaction]

    static constraints = {
		role inList: ["reactant", "product"]
    }
	
	public String toString(){
		return "$molecule $reaction.id $role"
	}
	
}
