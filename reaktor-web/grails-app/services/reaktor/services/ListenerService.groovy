package reaktor.services

import grails.transaction.Transactional
import grails.plugin.jms.Queue

import reaktor.*
import reaktor.populator.XyzDatabasePopulator
import reaktor.security.User

/**
 * Listener service for all queues
 * 
 * @author suzanne
 *
 */
@Transactional
class ListenerService {
	static exposes = ["jms"]

	//beans
	def obabel
	def defaultFolder
	def productCalculatorService
	def xyzDatabasePopulator
	def messageParser

	/**
	 * Listens at mentorQueue. Runs whenever a reaction is sent through the queueing 
	 * system. Sets up input files and user for productCalculatorService.
	 * 
	 * @param msg
	 */
	@Queue(name = "wallerlab.mentorQueue")
	def runMentorReactants(msg){
		
		String[] smilesStringsList = msg.split(",")
		int numSmilesStrings = smilesStringsList.length
		List xyzFiles = []

		//creates new xyz file for each smiles string in default folder
		for (int i = 0; i < numSmilesStrings; i++){
			//converts smiles string to xyz format and writes to file
			obabel.run(smilesStringsList[i], "molecule${i}.xyz")
			//add for productCalculatorService
			xyzFiles.add(new File(defaultFolder, "molecule${i}.xyz"))
		}

		//runs productCalculatorService
		User mentorUser = User.findByUsername("mentor")
		productCalculatorService.calculateProduct(xyzFiles, mentorUser,
			 new XyzDatabasePopulator())
		
	}

	/**
	 * Listens at productQueue. Runs whenever reaktor-cluster sends files back
	 * from a finished reaction. Adds information about reaction to database.
	 * 
	 * @param msg
	 */
	@Queue(adapter="noMsgConvert", name = "wallerlab.productQueue")
	def updateReactionWithProducts(msg){
		
		Long reactionID = Long.parseLong(msg.getJMSCorrelationID())
		
		//creates and populates product xyz files
		ArrayList productData = messageParser.parse(msg)
		
		//populates database
		ArrayList productMolecules = xyzDatabasePopulator.populate(productData)
		
		//updates reaction
		Reaction reaction = Reaction.get(reactionID)
		for (Molecule molecule: productMolecules){
			molecule.addToReactions(reaction)
			reaction.addToProducts(molecule)
		}
		reaction.hasProducts()
		
	}

}
