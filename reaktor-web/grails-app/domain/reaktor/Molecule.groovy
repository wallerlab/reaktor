package reaktor

/**
 * Defines a molecule
 * 
 * Parameters:
 * 	name = name of molecule
 * 	elementMap = map of elements and # of each in molecule
 * 
 * @author suzy
 *
 */
class Molecule {
	String name
	Map elementMap = [:].withDefault {'0'}
	
	static hasMany = [atoms : Atom, molecRxn : MolecRxn]
	
	/**
	 * Creates a map of the number of each element in the molecul 
	 * 
	 */
	void createElementMap() {
		if (atoms) {
			for (atom in atoms) {
				elementMap[atom.element] = elementMap[atom.element].toInteger()+1
				elementMap[atom.element] = elementMap[atom.element].toString()
			}
		}
		else {
			"$name has no atoms"
		}
	}
	
	/**
	 * Creates a name for the molecule
	 *
	 */
	public void createMoleculeName() {
		if (this.atoms.size() == 1) {
			this.name = this.atoms[0].element
		}
		else {
			StringBuilder nameString = new StringBuilder("")
			this.elementMap.each {element ->
				nameString.append(element.key)
				nameString.append(element.value)
			}
			def nameList = Molecule.findAllByNameIlike("${nameString}%")
			if (nameList){
				int num = 0
				for (Molecule molMatch : nameList){
					def matchName = molMatch.name
					try{
						Integer matchNameNum = Integer.parseInt(matchName.minus("${nameString}_"))
						if (matchNameNum >= num){
							num = matchNameNum+1
						}
					}
					catch (NumberFormatException ne){
						//do nothing
					}
				}
			nameString.append("_${num}")
			}
			this.name = nameString
		}
	}
	
	String toString() {
		"$name"
	}
	
    static constraints = {
    }
}
