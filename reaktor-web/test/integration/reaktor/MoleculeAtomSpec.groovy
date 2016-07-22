package reaktor



import spock.lang.*

/**
 *
 */
class MoleculeAtomSpec extends Specification {
	Molecule mol = new Molecule(name: "newMolecule")

    def setup() {
    }

    def cleanup() {
    }

    void "test that Molecule has atoms"() {
		when:
		Atom atom1 = new Atom()
		mol.addToAtoms(atom1)
		mol.save(flush:true)
		
		then:
		mol.atoms
    }

    void "test that Molecule has 2 Atoms when given 2 atoms"() {
		when:
		Atom atom1 = new Atom()
		Atom atom2 = new Atom()
		mol.addToAtoms(atom1)
		mol.addToAtoms(atom2)
		mol.save(flush:true)
		
		then:
		mol.atoms.size() == 2
    }
	
	void "test that Molecule's atoms are instances of Atom"(){
		when:
		Atom atom1 = new Atom()
		mol.addToAtoms(atom1)
		mol.save(flush:true)
		
		then:
		mol.atoms[0] instanceof Atom
	}
	
	void "test createElementMap"(){
		setup:
		Molecule molec = new Molecule(name:"fake")
		Atom atom1 = new Atom(element:"C", xCoord: 1.2345, yCoord: 2.3456, zCoord: 3.4567, idInMolecule: "a0")
		Atom atom2 = new Atom(element:"H", xCoord: 1.2345, yCoord: 2.3456, zCoord: 3.4567, idInMolecule: "a1")
		Atom atom3 = new Atom(element:"O", xCoord: 1.2345, yCoord: 2.3456, zCoord: 3.4567, idInMolecule: "a2")
		molec.addToAtoms(atom1)
		molec.addToAtoms(atom2)
		molec.addToAtoms(atom3)
		
		when:
		molec.createElementMap()
		molec.save()
		
		then:
		molec.elementMap == ["C":"1", "H":"1", "O":"1"] || molec.elementMap == ["C":"1", "O":"1", "H":"1"] || molec.elementMap == ["H":"1", "C":"1", "O":"1"] || molec.elementMap == ["H":"1", "O":"1", "C":"1"] || molec.elementMap == ["O":"1", "C":"1", "H":"1"] || molec.elementMap == ["O":"1", "H":"1", "C":"1"]
	}
	
	void "test createMoleculeName"(){
		setup:
		Molecule molec = new Molecule()
		Atom atom1 = new Atom(element:"C", xCoord: 1.2345, yCoord: 2.3456, zCoord: 3.4567, idInMolecule: "a0")
		Atom atom2 = new Atom(element:"H", xCoord: 1.2345, yCoord: 2.3456, zCoord: 3.4567, idInMolecule: "a1")
		molec.addToAtoms(atom1)
		molec.addToAtoms(atom2)
		molec.createElementMap()
		
		when:
		molec.createMoleculeName()
		
		then:
		molec.name == "CH" || "HC"
	}
}
