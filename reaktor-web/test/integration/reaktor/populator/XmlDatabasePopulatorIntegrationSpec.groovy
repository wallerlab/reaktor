package reaktor.populator



import reaktor.Atom
import reaktor.Molecule
import spock.lang.*
import reaktor.wrapper.OBabelWrapper
import reaktor.fileCreator.XyzFileCreator

/**
 * Integration tests for XmlDatabasePopulator
 */
class XmlDatabasePopulatorIntegrationSpec extends Specification {
	XmlDatabasePopulator xdp = new XmlDatabasePopulator()

    def setup() {
    }

    def cleanup() {
    }

    void "test findMatchingMolecule finds a match if there is one in database"() {
		when:
		Molecule molecule = Mock()
		molecule.name >> "dummy"
		Atom atom = Mock()
		atom.element >> "C"
		atom.xCoord >> 1.2345
		atom.yCoord >> 1.2345
		atom.zCoord >> 1.2345
		atom.idInMolecule >> "a1"
		molecule.atoms >> [atom]
		def hasAMatch = xdp.findMatchingMolecule(molecule)
		
		then:
		hasAMatch[0].name == "dummy"
    }

    void "test findMatchingMolecule finds a match if there is one in database with num"() {
		when:
		Molecule molecule = Mock()
		molecule.name >> "dummy_1"
		Atom atom = Mock()
		atom.element >> "C"
		atom.xCoord >> 1.2345
		atom.yCoord >> 1.2345
		atom.zCoord >> 1.2345
		atom.idInMolecule >> "a1"
		molecule.atoms >> [atom]
		def hasAMatch = xdp.findMatchingMolecule(molecule)
		
		then:
		hasAMatch[0].name == "dummy"
    }

    void "test findMatchingMolecule doesn't find a match if there isn't one in database"() {
		when:
		Molecule molecule = Mock()
		molecule.name >> "newMolecule"
		Atom atom = Mock()
		atom.element >> "C"
		atom.xCoord >> 1.2345
		atom.yCoord >> 1.2345
		atom.zCoord >> 1.2345
		atom.idInMolecule >> "a1"
		molecule.atoms >> [atom]
		def hasAMatch = xdp.findMatchingMolecule(molecule)
		
		then:
		hasAMatch.isEmpty()
    }

    void "test findMatchingMolecule doesn't find a match if there is one in database with similar name"() {
		when:
		Molecule molecule = Mock()
		molecule.name >> "dummy"
		Atom atom = Mock()
		atom.element >> "H"
		atom.xCoord >> 1.2345
		atom.yCoord >> 1.2345
		atom.zCoord >> 1.2345
		atom.idInMolecule >> "a1"
		molecule.atoms >> [atom]
		def hasAMatch = xdp.findMatchingMolecule(molecule)
		
		then:
		hasAMatch.isEmpty()
    }

    void "test findMatchingMolecule doesn't find a match if there isn't one in database with num"() {
		when:
		Molecule molecule = Mock()
		molecule.name >> "dummy_1"
		Atom atom = Mock()
		atom.element >> "H"
		atom.xCoord >> 1.2345
		atom.yCoord >> 1.2345
		atom.zCoord >> 1.2345
		atom.idInMolecule >> "a1"
		molecule.atoms >> [atom]
		def hasAMatch = xdp.findMatchingMolecule(molecule)
		
		then:
		hasAMatch.isEmpty()
    }
	
	void "test createAtomFromXml creates an atom from XML file"(){
		
		when:
		def xmlAtom = new XmlParser().parseText('<atom id="a1" elementType="C" x3="-9.983908476235094" y3="-0.717282371291755" z3="-1.4572471065496932"></atom>')
		Atom atom = xdp.createAtomFromXml(xmlAtom)
		
		then:
		atom.element == "C"
	}
	
	void "test that saveMolecule saves molecule if it's not already in database with similar name"(){
		setup:
		Molecule molecule = Mock()
		molecule.name >> "Methan"
		Atom atom = Mock()
		atom.element >> "C"
		atom.xCoord >> 1.2345
		atom.yCoord >> 1.2345
		atom.zCoord >> 1.2345
		molecule.atoms >> [atom]
		
		when:
		xdp.saveMolecule(molecule)
		
		then:
		1*molecule.save(_)
		0*molecule.delete()
	}
	
	void "test that saveMolecule saves molecule if it's not already in database with different name"(){
		setup:
		Molecule molecule = Mock()
		molecule.name >> "newMolecule"
		Atom atom = Mock()
		atom.element >> "C"
		atom.xCoord >> 1.2345
		atom.yCoord >> 1.2345
		atom.zCoord >> 1.2345
		molecule.atoms >> [atom]
		
		when:
		xdp.saveMolecule(molecule)
		
		then:
		1*molecule.save(_)
		0*molecule.delete()
	}
	
	void "test that saveMolecule doesn't save molecule if it's already in database"(){
		setup:
		Molecule molecule = Mock()
		molecule.name >> "dumm"
		Atom atom = Mock()
		atom.element >> "C"
		atom.xCoord >> 1.2345
		atom.yCoord >> 1.2345
		atom.zCoord >> 1.2345
		atom.idInMolecule >> "a1"
		molecule.atoms >> [atom]
		
		when:
		xdp.saveMolecule(molecule)
		
		then:
		0*molecule.save(_)
		1*molecule.delete()
	}
	
	void "test that completeMoleculeProperties calls createElementMap"(){
		setup:
		Molecule molecule = Mock()
		molecule.name >> "dumm"
		Atom atom = Mock()
		atom.element >> "C"
		atom.xCoord >> 1.2345
		atom.yCoord >> 1.2345
		atom.zCoord >> 1.2345
		atom.idInMolecule >> "a1"
		molecule.atoms >> [atom]
		
		when:
		xdp.completeMoleculeProperties(molecule)
		
		then:
		1*molecule.createElementMap()
	}
	
	void "test that completeMoleculeProperties calls createMoleculeName if molecule has no name"(){
		setup:
		Molecule molecule = Mock()
		molecule.name >> null
		Atom atom = Mock()
		atom.element >> "C"
		atom.xCoord >> 1.2345
		atom.yCoord >> 1.2345
		atom.zCoord >> 1.2345
		atom.idInMolecule >> "a1"
		molecule.atoms >> [atom]
		
		when:
		xdp.completeMoleculeProperties(molecule)
		
		then:
		1*molecule.createMoleculeName()
	}
	
	void "test that completeMoleculeProperties doesn't call createMoleculeName if molecule has a name"(){
		setup:
		Molecule molecule = Mock()
		molecule.name >> "dumm"
		Atom atom = Mock()
		atom.element >> "C"
		atom.xCoord >> 1.2345
		atom.yCoord >> 1.2345
		atom.zCoord >> 1.2345
		atom.idInMolecule >> "a1"
		molecule.atoms >> [atom]
		
		when:
		xdp.completeMoleculeProperties(molecule)
		
		then:
		0*molecule.createMoleculeName()
	}
	
	void "test that createMoleculeFromXml creates a molecule with a bondArray"(){
		setup:
		File file = new File("test/Test_Folder/test_files/reactants_bondArray.xml")
		xdp.moleculeXMLNode = new XmlParser().parse(file).molecule[1]
		
		when:
		xdp.createMoleculeFromXml()
		
		then:
		xdp.molecules[0].name == "H6C6" || xdp.molecules[0].name == "C6H6"
	}
	
	void "test that createMoleculeFromXml creates a molecule with no bondArray"(){
		setup:
		File file = new File("test/Test_Folder/test_files/good_reactants.xml")
		xdp.moleculeXMLNode = new XmlParser().parse(file).molecule[0]
		
		when:
		xdp.createMoleculeFromXml()
		
		then:
		xdp.molecules[0].name == "li+"
	}
	
	void "test that createMoleculeFromXml creates a molecule with a bondArray and more than one molecule"(){
		setup:
		File file = new File("test/Test_Folder/test_files/HighlyBranched.xml")
		xdp.moleculeXMLNode = new XmlParser().parse(file).molecule[0]
		
		when:
		xdp.createMoleculeFromXml()
		
		then:
		xdp.molecules[0].name == "H80C42" || xdp.molecules[0].name == "C42H80"
		xdp.molecules[1].name == "H10O1C8" || xdp.molecules[1].name == "H10C8O1" || xdp.molecules[1].name == "C8H10O1" || xdp.molecules[1].name == "C8O1H10" || xdp.molecules[1].name == "O1C8H10" || xdp.molecules[1].name == "O1H10C8"
	}
	
	void "test that populate returns an ArrayList of molecules when given a file"(){
		setup:
		File file = new File("test/Test_Folder/test_files/HighlyBranched.xml")
		
		when:
		def molecules = xdp.populate(file)
		
		then:
		molecules[0].name == "H80C42" || molecules[0].name == "C42H80"
		molecules[1].name == "H10O1C8" || molecules[1].name == "H10C8O1" || molecules[1].name == "C8H10O1" || molecules[1].name == "C8O1H10" || molecules[1].name == "O1C8H10" || molecules[1].name == "O1H10C8"
	}
}
