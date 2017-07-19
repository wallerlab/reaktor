package reaktor

import grails.plugin.springsecurity.annotation.Secured
import org.apache.log4j.Logger

@Secured(['ROLE_ADMIN','ROLE_USER'])
class CalculateController {

	def calculateService
	def incomingFolder
	def springSecurityService
	def xyzFileCreator
	def obabel
	Logger logger = Logger.getLogger("reaktor-web-calculateController")

	static allowedMethods = [startCalculation: ["POST", "GET"]]
	
	def index() {
		redirect uri: "/"
	}

	/**
	 * Sends params from web molecule sketcher to calculateService
	 *
	 * @return
	 */
	def startCalculation(){
		def user = springSecurityService.currentUser
		File newFile = new File(incomingFolder, "new.mrv")
		newFile.text = params.MolTxt
		String smilesString = obabel.run(newFile)
		if(smilesString == null){
			logger.info(message(code: 'default.empty.params.message'))
			render message(code: 'default.empty.params.message')
		} else if (params.query_type == "Reaction" && smilesString.count(".") < 1) {
			logger.info(message(code: 'default.tooFewMols.message'))
			render message(code: 'default.tooFewMols.message')
		} else if(smilesString.count(".") > 1){
			logger.info(message(code: 'default.tooManyMols.message'))
			render message(code: 'default.tooManyMols.message')
		} else{
			calculateService.calculate(smilesString, user, params.query_type)
			logger.info("${params.query_type} has been added to the queue")
			render message(code: 'default.calculationAdded.message')
		}
	}
	/**
	 * Gets all reactions/aggregations for a particular user to display in web
	 * table
	 *
	 * @return
	 */
	def getReactants(){
		Reaction reactionInstance = Reaction.get(Long.parseLong(params.id))
		File file = xyzFileCreator.createXyzDisplayFile(reactionInstance)
		def xyzFileString = file.text
		render (text: xyzFileString, contentType: "text")
		
	}
	
	def error(message){
		redirect action: "error"
		flash.message = message
	}
	/**
	 * Uploads files from uploadr and starts calculation
	 *
	 * @return
	 */
	def sendFiles(){
		def user = springSecurityService.currentUser
		ArrayList moleculeFiles = []
		if(incomingFolder.listFiles().size() != 0){
			for(File file: incomingFolder.listFiles()){
				moleculeFiles << file
			}
			calculateService.calculate(moleculeFiles, user, params.query_type)
		}
		else{
			logger.info(message(code: 'default.empty.params.message'))
			flash.message = message(code: 'default.empty.params.message')
		}
		redirect uri: "/"
	}
	
}