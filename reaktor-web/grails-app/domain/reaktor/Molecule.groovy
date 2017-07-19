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
	String smilesString
	Map elementMap = [:].withDefault {'0'}
	Reaction reactantReaction
	Reaction productReaction
	
	static belongsTo = [reactantReaction: Reaction, productReaction: Reaction]
	static hasMany = [atoms : Atom]
	
	/**
	 * Creates a map of the number of each element in the molecule
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

	public String createXyzFileString(File mainFolder){
		//TODO: this probably needs to be changed once I find out aggregationfile structure
		File folder = new File(mainFolder, reactantReaction?.reactionFolderName)
		if(folder == null){
			folder = new File(mainFolder, productReaction.reactionFolderName)
		}
		File file = new File(folder, "product_geom/${name}.xyz")
		if(!file.exists()){
			file = new File(folder, "input_files/${name}.xyz")
		}
		return file.text
	}
	
	String toString() {
		"$name"
	}
	
    static constraints = {
		reactantReaction nullable: true
		productReaction nullable: true
    }
}
