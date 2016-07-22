package reaktor.populator

import reaktor.*
import reaktor.parser.XmlDataParser

/**
 * Populates the database using XML input
 * 
 * @author suzy
 *
 */
class XmlDatabasePopulator implements Populator {
	
	private def moleculeXMLNode
	private ArrayList molecules

	/**
	 * Takes data in the form of nodes from the XMLDataParser
	 * class and populates the database with it.
	 * 
	 * @param data
	 * 	
	 */
	@Override
	public ArrayList populate(Object input) {
		
		Object data = new XmlDataParser().parse(input)
		(Node) data
		molecules = []
		for(Node molecule : data){
			moleculeXMLNode = molecule
			createMoleculeFromXml()
		}
		return molecules
		
	}

	/*
	 * Creates Molecule instance from XML data. If there is no molecule id,
	 * creates a name for the molecule.
	 * 
	 */
	private void createMoleculeFromXml() {
		
		if(moleculeXMLNode.bondArray){
			ArrayList separatedMolecules = separateInputMolecules()
			for (TreeSet atomsInMolec : separatedMolecules){
				Molecule molecule = new Molecule()
				for (String atomReference : atomsInMolec){
					def xmlAtom = moleculeXMLNode.atomArray[0].children().find{it["@id"] == atomReference}
					Atom atom = createAtomFromXml(xmlAtom)
					molecule.addToAtoms(atom)
				}
				completeMoleculeProperties(molecule)
				molecule = saveMolecule(molecule)
				println "molecule is " + molecule
				molecules << molecule
			}
		}
		else{
			Molecule molecule = new Molecule(name : moleculeXMLNode["@id"])
			moleculeXMLNode.atomArray[0].each {xmlAtom ->
				Atom atom = createAtomFromXml(xmlAtom)
				molecule.addToAtoms(atom)
			}
			completeMoleculeProperties(molecule)
			molecule = saveMolecule(molecule)
			molecules << molecule
		}
		
	}
	
	/*
	 * Populates molecule's element map and, if it doesn't already have a name,
	 * creates a name for it.
	 * 
	 */
	private void completeMoleculeProperties(Molecule molecule){
		
		molecule.createElementMap()
		if (molecule.name == null) {
			molecule.createMoleculeName()
		}
		
	}

	/*
	 * Finishes creating molecule. Checks if it's already in database. If it is,
	 * returns the one in the database. If not, saves to database and returns
	 * newly created molecule.
	 * 
	 */
	private Molecule saveMolecule(Molecule molecule) {
		
		ArrayList moleculeMatch = findMatchingMolecule(molecule)
		if(moleculeMatch.size() == 0) {
			molecule.save(flush: true)
		}
		else {
			Molecule foundMolecule = moleculeMatch[0]
			molecule.delete()
			molecule = foundMolecule
		}
		return molecule
		
	}


	/*
	 * Creates Atom instance from XML data
	 * 
	 */
	private Atom createAtomFromXml(xmlAtom) {
		
		def atom = new Atom()
		atom.element = xmlAtom["@elementType"]
		atom.idInMolecule = xmlAtom["@id"]
		atom.xCoord = xmlAtom["@x3"].toDouble()//*ANGTOHAR
		atom.yCoord = xmlAtom["@y3"].toDouble()//*ANGTOHAR
		atom.zCoord = xmlAtom["@z3"].toDouble()//*ANGTOHAR
		atom.save(flush: true)
		return atom
		
	}

	/*
	 * Checks to see if the molecule being saved to the database is
	 * already there.
	 * 
	 */
	private List findMatchingMolecule(Molecule molecule) {
		
		String newMolName
		if(molecule.name.contains("_")){
			newMolName = molecule.name.split("_")[0]
		}
		else{
			newMolName = molecule.name
		}
		def moleculeMatch = Molecule.findAllByNameIlike("${newMolName}%")
		while(moleculeMatch) {
			if (moleculeMatch[0].atoms.size() == molecule.atoms.size()){
				for (Atom atom : molecule.atoms) {
					for (Atom matchAtom : moleculeMatch[0].atoms) {
						if (atom.element == matchAtom.element && atom.xCoord == matchAtom.xCoord
						&& atom.yCoord == matchAtom.yCoord && atom.zCoord == matchAtom.zCoord) {
							return [moleculeMatch[0]]
						}
					}
				}
			}
			moleculeMatch.remove(0)
		}
		return []
		
	}



	/*
	 * Takes XML input and creates molecules based on the bonds in the system
	 * 
	 */
	private List separateInputMolecules() {
		
		ArrayList fragments = new ArrayList()
		SortedSet fragment = new TreeSet()
		moleculeXMLNode.bondArray[0].each{bond ->
			SortedSet bondFrag = new TreeSet()
			List atomRefs = bond["@atomRefs2"].split(" ")
			for (String atomRef : atomRefs){
				bondFrag.add(atomRef)
				//2 is the # of atoms in bond
				if (bondFrag.size() == 2){
					if (fragment.isEmpty() || fragment.contains(bondFrag[0])
					|| fragment.contains(bondFrag[1])){
						fragment.addAll(bondFrag)
					}
					else {
						fragments = addToFragments(fragments, fragment)
						fragment = bondFrag
					}
				}
			}
		}
		fragments = addToFragments(fragments, fragment)
		fragments = finalListCheck(fragments)
		return fragments
		
	}

	/*
	 * Depending on whether an atomID from fragment is in a set of atomIDs corresponding
	 * to a molecule in fragments, either adds the fragment to an existing molecule 
	 * or creates a new one.
	 * 
	 */
	private List addToFragments(ArrayList fragments, SortedSet fragment) {
		
		boolean fragmentAdded = false
		fragLoop: for (TreeSet frag : fragments){
			for (String atomId : fragment){
				if (frag.contains(atomId)){
					frag.addAll(fragment)
					fragmentAdded = true
					break fragLoop
				}
			}
		}
		if (!fragmentAdded){
			fragments << fragment
		}
		return fragments
		
	}

	/*
	 * Checks the fragment list one last time for fragments that should be in
	 * another set in the list
	 * 
	 */
	private List finalListCheck(ArrayList fragments) {
		
		ArrayList fragmentsToRemove = []
		for(int i = 0; i < fragments.size()-1; i++){
			for(int j = i+1; j < fragments.size(); j++){
				for(String atomID : fragments[j]){
					if(fragments[i].contains(atomID)){
						fragments[i].addAll(fragments[j])
						fragmentsToRemove << fragments[j]
						break
					}
				}
			}
		}
		fragments.removeAll(fragmentsToRemove)
		return fragments
		
	}
	
}
