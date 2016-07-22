package reaktor

/**
 * Defines an atom
 * 
 * Parameters: 
 * 	element = atomic symbol of the atom
 * 	x, y, z = coordinates of the atom
 * 
 * @author suzy
 *
 */
class Atom {
	String element
	Double xCoord, yCoord, zCoord
	String idInMolecule
	
	static belongsTo = [molecule : Molecule]
	
	String toString() {
		"$element ${xCoord} ${yCoord} ${zCoord}"
	}
    static constraints = {
    }

}
