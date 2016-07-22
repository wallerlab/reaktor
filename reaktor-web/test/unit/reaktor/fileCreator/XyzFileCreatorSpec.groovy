package reaktor.fileCreator

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import reaktor.Atom
import reaktor.Molecule
import reaktor.Reaction
import spock.lang.*
import reaktor.services.*

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
class XyzFileCreatorSpec extends Specification {
	XyzFileCreator xfc = new XyzFileCreator()

    def setup() {
    }

    def cleanup() {
    }
	
	@Unroll
	void "test findMaxOrMin finds the max or min"(){
		setup:
		File file = new File("test/Test_Folder/test_files/bz.xyz")
		
		expect:
		a == xfc.findMaxOrMin(b, file, c)
		
		where:
		a			|	b		|	c
		2.15666		|	true	|	1
		2.49029		|	true	|	2
		0.00000		|	true	|	3
		-2.15666	|	false	|	1
		-2.49029	|	false	|	2
		0.00000		|	false	|	3
		
	}
	
	@Unroll
	void "test createCombinedFileArray creates a combined file array"(){
		setup:
		File folder = new File("test/Test_Folder/test_files")
		Molecule molec = Mock()
		molec.name >> "benzene"
		Molecule molec1 = Mock()
		molec1.name >> "ethene"
		Molecule molec2 = Mock()
		molec2.name >> "bz"
		Molecule molec3 = Mock()
		molec3.name >> "molecule3"
		
		expect:
		List list = xfc.createCombinedFileArray(a, folder, c)
		list.size() == b
		list[-1] == d
		
		where:
		a																	|	b	|	c		|	d
		[new Molecule(name: "benzene"), new Molecule(name: "ethene")]		|	20	|	[0, 2]	|	"C    3.22614    0.00000    0.00000"
		[new Molecule(name: "benzene"), new Molecule(name: "ethene"),
			 new Molecule(name: "bz")]										|	32	|	[0, 3]	|	"H    9.93182    1.24515    0.00000"
		[new Molecule(name: "benzene"), new Molecule(name: "ethene"), 
			new Molecule(name: "bz")]										|	20	|	[0,2]	|	"C    3.22614    0.00000    0.00000"
		[new Molecule(name: "benzene"), new Molecule(name: "ethene"), 
			new Molecule(name: "bz"), new Molecule(name: "molecule3")]		|	22	|	[2,4]	|	"C    5.97924       -0.39648    0.00000"
		
			
	}
	
	@Unroll
	void "startAndEndNums given is #d when numFilesToMake is #a, j is #b and numXyzFiles is #c"(){
		setup:
		xfc.numFilesToMake = a
		
		expect:
		xfc.assignStartAndEndNums(b, c) == new ArrayList(d)
		
		where:
		a	|	b	|	c	|	d
		2	|	0	|	4	|	[0,2]
		2	|	0	|	5	|	[0,2]
		2	|	1	|	5	|	[2,5]
		4	|	2	|	10	|	[4,7]
		5	|	3	|	10	|	[6,8]
		5	|	4	|	24	|	[19,24]
		
	}
	
	void "test createXyzDisplayFile creates an xyz display file when one doesn't already exist"(){
		setup:
		xfc.mainFolder = new File("test/Test_Folder/")
		Reaction reaction = Mock()
		Molecule mol1 = Mock()
		mol1.name >> "unkH11C5N1"
		Molecule mol2 = Mock()
		mol2.name >> "unkH6C6"
		reaction.reactants >> [mol1, mol2]
		reaction.id >> 2
		
		when:
		xfc.createXyzDisplayFile(reaction)
		
		then:
		File displayFile = new File(xfc.mainFolder, "ProductData_2/displayFile.xyz")
		displayFile.exists()
		displayFile.size() == 1170
		
		cleanup:
		displayFile.delete()
		
	}
	
	void "test createXyzDisplayFile doesn't create an xyz display file when one already exists"(){
		setup:
		xfc.mainFolder = new File("test/Test_Folder/")
		Reaction reaction = Mock()
		reaction.id >> 1
		
		when:
		xfc.createXyzDisplayFile(reaction)
		
		then:
		File displayFile = new File(xfc.mainFolder, "ProductData_1/displayFile.xyz")
		displayFile.exists()
	}
	
	@Unroll
	void "test createFilesFromMultipleFiles creates #a files from molecules in reaction"(){
		setup:
		xfc.defaultFolder = new File("test/Test_Folder")
		new File("test/Test_Folder/test_files").eachFile{file ->
			if(file.name.contains("molecule")){
				new File(xfc.defaultFolder, file.name).write(file.text)
			}
		}
		xfc.numFilesToMake = a
		
		expect:
		xfc.createFilesFromMultipleFiles(b)
		for(int i = 0; i < a; i++){
			def file = new File(xfc.defaultFolder, "startMols${i}.xyz")
			file.exists()
			file.readLines()[0] == c[i]
			file.readLines().size() == d[i]
		}
		
		cleanup:
		"rm molecule0.xyz molecule1.xyz molecule2.xyz molecule3.xyz molecule4.xyz molecule5.xyz molecule6.xyz".execute(null, xfc.defaultFolder)
		"rm startMols0.xyz startMols1.xyz startMols2.xyz startMols3.xyz".execute(null, xfc.defaultFolder)

		where:
		a	|	b								|	c							|	d
		2	|	new Reaction(reactants: [new Molecule(name: "molecule0"), new Molecule(name: "molecule1"), new Molecule(name: "molecule2"),
			 new Molecule(name: "molecule3"), new Molecule(name: "molecule4"), 
			 new Molecule(name: "molecule5")])	|	["24","28"]					|	[26,27]
		2	|	new Reaction(reactants: [new Molecule(name: "molecule0"), new Molecule(name: "molecule1"), new Molecule(name: "molecule2")
			, new Molecule(name: "molecule3"), new Molecule(name: "molecule4"), new Molecule(name: "molecule5")
			, new Molecule(name: "molecule6")])	|	["36","28"]					|	[37,28]
		3	|	new Reaction(reactants: [new Molecule(name: "molecule0"), new Molecule(name: "molecule1"), new Molecule(name: "molecule2")
			, new Molecule(name: "molecule3"), new Molecule(name: "molecule4"), 
			new Molecule(name: "molecule5")])	|	["12","24","16"]			|	[14,25,16]
		4	| 	new Reaction(reactants: [new Molecule(name: "molecule0"), new Molecule(name: "molecule1"), new Molecule(name: "molecule2")
			, new Molecule(name: "molecule3"), new Molecule(name: "molecule4"), new Molecule(name: "molecule5"), 
			new Molecule(name: "molecule6")])	|	["12", "12", "22", "18"]	|	[14,14,19,20]
	}
	
    void "test createFile creates xyz file"() {
		when:
		Molecule molecule = Mock(Molecule)
		molecule.toString() >> "createFile"
		molecule.name >> "createFile"
		Atom atom = Mock(Atom)
		atom.element >> "C"
		atom.xCoord >> 1.2345
		atom.yCoord >> 1.2345678901
		atom.zCoord >> 2.3456789
		molecule.atoms >> [atom, atom, atom]
		xfc.createFile(molecule, "test/Test_Folder")
		
		then:
		new File("test/Test_Folder", "${molecule}.xyz").exists()
		
		cleanup:
		new File("test/Test_Folder/createFile.xyz").delete()
    }
	
	void "test createFile creates xyz file with the right format"(){
		when:
		Molecule molecule = Mock(Molecule)
		molecule.toString() >> "createFile"
		molecule.name >> "createFile"
		Atom atom = Mock(Atom)
		atom.element >> "C"
		atom.xCoord >> 1.2345
		atom.yCoord >> 1.2345678901
		atom.zCoord >> 2.3456789
		Atom atom2 = Mock(Atom)
		atom2.element >> "C"
		atom2.xCoord >> 1.2345
		atom2.yCoord >> 1.2345678901
		atom2.zCoord >> 2.3456789
		molecule.atoms >> [atom, atom2]
		xfc.createFile(molecule, "test/Test_Folder")
		
		then:
		def fileLines = new File("test/Test_Folder", "${molecule}.xyz").readLines()
		fileLines[0] == "2"
		fileLines[1] == "createFile"
		fileLines[2] == "C      1.23450      1.23457      2.34568"
		
		cleanup:
		new File("test/Test_Folder/createFile.xyz").delete()
	}

}
