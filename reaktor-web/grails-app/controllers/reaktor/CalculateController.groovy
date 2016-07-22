package reaktor

import reaktor.fileCreator.*
import reaktor.populator.*
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_ADMIN','ROLE_USER'])
class CalculateController {

	def productCalculatorService
	def defaultFolder
	def springSecurityService
	def xyzFileCreator
	
	def index() {
		redirect uri: "/" //in plugin, is redirect controller: "reaktor"
	}

	def calculate(){
		//for molecule sketcher
		def user = springSecurityService.currentUser // in plugin, currentUser.email
		if (!params.MolTxt1.contains("z3")) {
			flash.message = message(code: 'default.2Datoms.message')
		} else if(params.MolTxt1 != ""){
			productCalculatorService.calculateProduct(params.MolTxt1, 
				user,new XmlDatabasePopulator())
		} else {
			flash.message = message(code: 'default.empty.params.message')
		}
		redirect uri: "/" //in plugin, is redirect controller: "reaktor"
	}
	
	def getReactants(){
		//for jQuery table
		Reaction reactionInstance = Reaction.get(Long.parseLong(params.id))
		File file = xyzFileCreator.createXyzDisplayFile(reactionInstance)
		def xyzFileString = file.text
		render (text: xyzFileString, contentType: "text")
		
	}
	
	def error(message){
		redirect action: "error"
		flash.message = message
	}
	
	def sendFiles(){
		//for file uploader
		def user = springSecurityService.currentUser // in plugin, currentUser.email
		ArrayList moleculeFiles = []
		if(defaultFolder.listFiles().size() != 0){
			for(File file: defaultFolder.listFiles()){
				moleculeFiles << file
			}
			productCalculatorService.calculateProduct(moleculeFiles, user,
				new XyzDatabasePopulator())
		}
		else{
			flash.message = message(code: 'default.empty.params.message')
		}
		redirect uri: "/" //in plugin, is redirect controller: "reaktor"
	}
	
}