package reaktor.populator



import reaktor.*
import spock.lang.*
import reaktor.wrapper.OBabelWrapper


class XyzDatabasePopulatorIntegrationSpec extends Specification {
	XyzDatabasePopulator populator = new XyzDatabasePopulator()

    def setup() {
    }

    def cleanup() {
    }

    void "test something"() {
    }
	
	void "test createAtomFromXyz creates atom from xyz data"(){
		when:
		Collection lineList = ["C","1.2345678901","2.3456789012","3.4567890123"]
		Atom atom = new Atom(element: "C", xCoord : 1.2345678901, yCoord : 2.3456789012, zCoord : 3.4567890123)
		def atomCreated = populator.createAtomFromXyz(lineList, Mock(Molecule))
		
		then:
		atomCreated.element == atom.element
		atomCreated.xCoord == atom.xCoord
		atomCreated.yCoord == atom.yCoord
		atomCreated.zCoord == atom.zCoord
	}
	
	void "test createMoleculeFromXyz creates molecule from xyz data"(){
		setup:
		File folder = new File("test/Test_Folder")
		File file = new File(folder, "test_files/benzene.xyz")
		File xyzFile = new File(folder, "benzene.xyz")
		xyzFile.write(file.text)
		
		when:
		def moleculeCreated = populator.createMoleculeFromXyz(xyzFile)
		
		then:
		moleculeCreated[0].name == "benzene"
		
		cleanup:
		xyzFile.renameTo(file)
	}
	
	void "test createMoleculeFromXyz renames file to molecule name"(){
		setup:
		File folder = new File("test/Test_Folder/")
		File file = new File(folder, "test_files/molecule1.xyz")
		File xyzFile = new File(folder, "molecule1.xyz")
		xyzFile.write(file.text)
		
		when:
		def moleculeCreated = populator.createMoleculeFromXyz(xyzFile)
		
		then:
		new File(folder, "C6H6.xyz").exists() || new File(folder, "H6C6.xyz").exists()
		
		cleanup:
		File fileToClean = new File(folder, "C6H6.xyz")
		if(!fileToClean.exists()){
			fileToClean = new File(folder, "H6C6.xyz")
		}
		fileToClean.renameTo(file)
	}
	
	void "test createMoleculeFromXyz removes original file"(){
		setup:
		File folder = new File("test/Test_Folder/")
		File file = new File(folder, "test_files/molecule1.xyz")
		File xyzFile = new File(folder, "molecule1.xyz")
		xyzFile.write(file.text)
		
		when:
		def moleculeCreated = populator.createMoleculeFromXyz(xyzFile)
		
		then:
		!xyzFile.exists()
		
		cleanup:
		File fileToClean = new File(folder, "C6H6.xyz")
		if(!fileToClean.exists()){
			fileToClean = new File(folder, "H6C6.xyz")
		}
		fileToClean.renameTo(file)
	}

    void "test populate gives ArrayList of molecules from files from xyz file"() {
		setup:
		File folder = new File("test/Test_Folder")
		"cp test_files/molecule3.xyz ./".execute(null, folder)
		"cp test_files/molecule4.xyz ./".execute(null, folder)
		File file1 = new File(folder, "molecule3.xyz")
		File file2 = new File(folder, "molecule4.xyz")
		
		when:
		ArrayList molecule = populator.populate(new ArrayList([file1, file2]))
		
		then:
		molecule[0] instanceof Molecule
		molecule[1] instanceof Molecule
		
		cleanup:
		"rm butadiene.xyz ethene.xyz".execute(null, folder)
    }

    void "test populate gives ArrayList of molecules with correct info from files from xyz file"() {
		setup:
		File folder = new File("test/Test_Folder")
		"cp test_files/molecule3.xyz ./".execute(null, folder)
		"cp test_files/molecule4.xyz ./".execute(null, folder)
		File file1 = new File(folder, "molecule3.xyz")
		File file2 = new File(folder, "molecule4.xyz")
		
		when:
		def molecule = populator.populate(new ArrayList([file1, file2]))
		
		then:
		molecule[0].name == "butadiene"
		molecule[1].name == "ethene"
		
		cleanup:
		"rm butadiene.xyz ethene.xyz".execute(null, folder)
    }
}
