package reaktor.populator

import reaktor.Atom
import reaktor.Molecule
import reaktor.wrapper.OBabelWrapper

/**
 * Database populator for xyz files
 * 
 * @author suzanne
 *
 */
class XyzDatabasePopulator implements Populator {

	/**
	 * Populates database from an ArrayList of user-input xyz files
	 * 
	 * @param ArrayList of .xyz Files
	 * @returns ArrayList of Molecules
	 */
	@Override
	public ArrayList populate(Object data) {
		
		(ArrayList) data
		ArrayList xyzMoleculesForCalculation = []
		if(!data.isEmpty()){
			for(File xyzFile : data){
				def moleculeToCalculate = createMoleculeFromXyz(xyzFile)
				xyzMoleculesForCalculation.addAll(moleculeToCalculate)
			}
		}
		return xyzMoleculesForCalculation
		
	}

	/*
	 * Creates molecule in database from xyz file
	 * 
	 */
	private List createMoleculeFromXyz(File xyzFile) {
		
		Integer numAtoms = Integer.parseInt(xyzFile.readLines()[0])
		String moleculeName = xyzFile.readLines()[1].trim()
		Molecule molecule = new Molecule(name: moleculeName)
		for (int i = 2; i < xyzFile.readLines().size(); i++){
			String line = xyzFile.readLines()[i]
			def lineList = line.split("   ").findAll{it.trim() != ""}
			Atom atom = createAtomFromXyz(lineList, molecule)
			molecule.addToAtoms(atom)
		}
		molecule.createElementMap()
		if (molecule.name == null || molecule.name.startsWith("Energy")) {
			molecule.createMoleculeName()
		}
		molecule.save(flush: true)
		xyzFile.renameTo(new File(xyzFile.parent,"${molecule.name}.xyz"))
		return [molecule]
		
	}
	
	/*
	 * Creates atom in database from xyz file
	 *
	 */
	private Atom createAtomFromXyz(Collection lineList, Molecule molecule) {
		
		Atom atom = new Atom()
		atom.element = lineList[0].trim()
		if (molecule.atoms == null){
			atom.idInMolecule = "a0"
		} else {
			atom.idInMolecule = "a${molecule.atoms.size()}"
		}
		atom.xCoord = Double.parseDouble(lineList[1].trim())
		atom.yCoord = Double.parseDouble(lineList[2].trim())
		atom.zCoord = Double.parseDouble(lineList[3].trim())
		atom.save(flush: true)
		return atom
		
	}
}
