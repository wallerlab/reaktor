package reaktor.services

import reaktor.*
import reaktor.security.User
import javax.jms.Message


/**
 * Readies files for calculation, then sends them to server.
 * 
 * @author suzanne
 *
 */
class CalculateService {
	
	private File filePath

	//beans
	def xyzDatabasePopulator
	def obabel
	def xyzFileCreator
	def defineFileCreator
	def mainFolder
	def incomingFolder
	def jmsService

	/**
	 * Displays lowest energy product from given input
	 * 
	 * @param smilesString, user, calculation type
	 */
	public void calculate(String smilesString, User user, String calcType){

		//converts molecules from smiles to xyz
		println "smilesString in calculateService is: " + smilesString
		ArrayList<File> reactantMolecules = convertToXYZ(smilesString)
		calculate(reactantMolecules, user, calcType)

	}

	/**
	 * Displays lowest energy product from given input
	 *
	 * @param reactantMoleculeFiles (as List<File>), user, calculation type
	 */
	public void calculate(ArrayList<File> reactantMoleculeFiles, User user,
						  String calcType){

		Map<String,File> reactantMap = [:]
		for(File xyzFile: reactantMoleculeFiles){
			String smilesString = obabel.run(xyzFile)
			if(!reactantMap.get(smilesString)){
				reactantMap.put(smilesString, xyzFile)
			} else{
				reactantMap.put(smilesString+"--", xyzFile)
			}
		}
		//populates molecule database
		ArrayList<Molecule> reactantMolecules = xyzDatabasePopulator.populate(reactantMap)

		//creates reaction in database
		Reaction reaction = createReaction(reactantMolecules, user, calcType)

		//creates and populates files and folders
		createProjectFilePath(reaction)
		createInputFiles(reaction)

		//creates and sends message to queue
		String message = createMessageString(calcType)
		String reactionID = reaction.id.toString()
		jmsService.send(queue:"wallerlab.reactantQueue", message) {Message msg ->
			msg.setCorrelationId(reactionID)
			msg
		}
		reaction.status = "enqueued"
		reaction.save(flush: true)

	}

	/*
	 * Converts SMILES string to xyz
	 *
	 */
	private ArrayList<File> convertToXYZ(String input) {

		String[] molecules = input.tokenize(".")
		println "molecules.size in convertToXYZ is " + molecules.size()
		List<File> reactantMolecules = []
		for (int i=0; i < molecules.size(); i++) {
			reactantMolecules << obabel.run(molecules[i], i + ".xyz")
		}
		println "reactantMolecules is " + reactantMolecules
		for(String reactantMolecule: reactantMolecules){
			println reactantMolecule
			String fileContents = new File(reactantMolecule).text
			println fileContents
		}
		return reactantMolecules

	}

	/*
	 * Saves reaction to database with reactants, user, status and calculation type
	 *
	 */
	private Reaction createReaction(ArrayList reactantMolecules, User
			reactionUser, String calcType){

		Reaction reaction = new Reaction(user: reactionUser, reactionType:
				calcType, status: "waiting for parameters")
		for(Molecule molecule : reactantMolecules){
			reaction.addToReactants(molecule)
			molecule.setReactantReaction(reaction)
		}
		reaction.save()
		reaction.createProjectFolderName()
		reaction.save(flush: true)
		return reaction

	}
	
	/*
	 * Creates a unique file path for the project to run in.
	 * 
	 */
	private void createProjectFilePath(Reaction reaction) {

		filePath = new File(mainFolder, "${reaction.reactionFolderName}/input_files")
		if (!filePath.exists()){
			filePath.mkdirs()
		}

	}

	/*
	 * Creates files that are needed to run if they're not already there.
	 * 
	 */
	private void createInputFiles(Reaction reaction) {

		Set<Molecule> reactantMolecules = reaction.reactants
		for(int i = 0; i < reactantMolecules.size(); i++){
			File reactantIFile = new File(incomingFolder,
					"${reactantMolecules[i]}.xyz")
			if(reactantIFile.exists()){
				reactantIFile.renameTo(new File(filePath,
						"${reactantMolecules[i]}.xyz"))
			}
			else{
				xyzFileCreator.createFile(reactantMolecules[i], filePath)
			}
		}
		if(reaction.reactionType == "Reaction"){
			defineFileCreator.createFile("define.inp", filePath)
		}

	}
	
	/*
	 * Creates a String consisting of files and folders needed to run calculation on
	 * reaktor-cluster
	 *
	 */
	private String createMessageString(String calcType){

		StringBuilder messageString = new StringBuilder()
		String prefix = ","
		messageString << calcType
		filePath.eachFile {file ->
			if(!file.isHidden()){
				messageString << prefix
				messageString << "${file.name}:${file.text}"
			}
		}
		return messageString

	}
}
