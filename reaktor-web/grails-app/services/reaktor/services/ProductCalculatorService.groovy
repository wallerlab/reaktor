package reaktor.services

import reaktor.*
import reaktor.security.User
import reaktor.populator.*
import javax.jms.Message


/**
 * Readies files for calculation, then sends them to server.
 * 
 * @author suzanne
 *
 */
class ProductCalculatorService {
	
	private File filePath
	private Reaction reaction

	//beans
	def xyzFileCreator
	def defineFileCreator
	def mainFolder
	def defaultFolder
	def jmsService

	/**
	 * Displays lowest energy product from given input
	 * 
	 * @param input
	 * @return String file path for product
	 */
	public void calculateProduct(input, User user, Populator populator){
		
		//populates molecule database
		List moleculesForCalculation = populator.populate(input)
		
		//creates reaction in database
		createReaction(moleculesForCalculation, user)
		
		//creates and populates files for pyreactor
		createProjectFilePath()
		moleculesForCalculation = createTwoXyzFiles()
		createInputFiles(moleculesForCalculation)
		
		//creates and sends message to queue
		String message = createMessageString()
		String reactionID = reaction.id.toString()
		jmsService.send(queue:"wallerlab.reactantQueue", message) {Message msg ->
				msg.setCorrelationId(reactionID)
				msg
			}
		reaction.status = "enqueued"
		reaction.save(flush: true)
		
	}
	
	/*
	 * Creates reaction with reactants, user, and status and saves to database
	 *
	 */
	private void createReaction(ArrayList moleculesForPyreactor, User reactionUser){
		
		reaction = new Reaction(user: reactionUser, status: "waiting for parameters")
		for(Molecule molecule : moleculesForPyreactor){
			MolecRxn molecRxn = new MolecRxn(molecule: molecule, reaction: reaction, role: "reactant")
			reaction.addToMolecules(molecRxn)
			molecule.addToMolecRxn(molecRxn)
		}
		reaction.save(flush: true)
		
	}
	
	/*
	 * Creates a unique file path for the project to run in.
	 * 
	 */
	private void createProjectFilePath() {
		
		filePath = new File(mainFolder, "ProductData_${reaction.id}/input_files")
		if (!filePath.exists()){
			filePath.mkdirs()
		}
		
	}
	
	/*
	 * If there are more than 2 xyz files, condenses them into 2
	 *
	 */
	private List createTwoXyzFiles(){
		
		if(reaction.reactants.size() > 2){
			xyzFileCreator.createFilesFromMultipleFiles(reaction)
			return new ArrayList(["startMols0", 'startMols1'])
		}
		return new ArrayList(reaction.reactants)
		
	}

	/*
	 * Creates files that are needed to run if they're not already there.
	 * 
	 */
	private void createInputFiles(moleculesForPyreactor) {
		
		for(int i = 0; i < moleculesForPyreactor.size(); i++){
			File reactantIFile = new File(defaultFolder, "${moleculesForPyreactor[i]}.xyz")
			if(reactantIFile.exists()){
				reactantIFile.renameTo(new File(filePath,"${moleculesForPyreactor[i]}.xyz"))
			}
			else{
				xyzFileCreator.createFile(moleculesForPyreactor[i], filePath)
			}
		}
		defineFileCreator.createFile("define.inp", filePath)
		
	}
	
	/*
	 * Creates a String consisting of files and folders needed to run calculation on
	 * reactor-cluster
	 *
	 */
	private String createMessageString(){
		
		StringBuilder messageString = new StringBuilder()
		String prefix = ""
		filePath.eachFile {file ->
			if(!file.isHidden()){
				messageString << prefix
				prefix = ","
				messageString << "${file.name}:${file.text}"
			}
		}
		return messageString
		
	}
}
