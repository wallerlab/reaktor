package reaktor.parser

import groovy.util.Node
import javax.jms.TextMessage
import reaktor.Reaction
import javax.annotation.Resource
import org.springframework.beans.factory.annotation.Value;

/**
 * Parses JMS messages
 * 
 * @author suzanne
 *
 */
class MessageParser implements Parser {

	@Resource
	def mailService
	
	@Resource
	private def mainFolder
	
	@Value('${mail.error.subject}')
	String errorSubject
	
	@Value('${mail.error.message}')
	String errorMessage
	
	@Value('${mail.finished.subject}')
	String finishedSubject
	
	@Value('${mail.finished.message}')
	String finishedMessage

	/**
	 * Parses message into file name and file text, then creates and
	 * writes to files
	 * 
	 */
	@Override
	public Object parse(Object msg) {
		//TODO: populate the right folders for the right calc type
		
		(TextMessage) msg
		Reaction reaction = Reaction.get(Long.parseLong(msg.getJMSCorrelationID()))
		File reactionFolder = new File(mainFolder, reaction.reactionFolderName)
		String[] foldersWithFiles = msg.getText().split("#")
		ArrayList moleculeData = []
		if(foldersWithFiles[0] == "Error"){
			reaction.status = "error while calculating"
		}
		else{
			moleculeData = populateReactionFolder(foldersWithFiles, reactionFolder);
			reaction.status = "finished"
		}
		reaction.save(flush: true)
		sendMailToUser(reaction)
		return moleculeData
		
	}

	/*
	 * Sends mail to user updating them on the status of their finished calculation
	 * 
	 */
	private void sendMailToUser(reaction) {
		String status = reaction.status.tokenize()[0]
		String mailSubject = this["${status}Subject"]
		String mailMessage = this["${status}Message"]
		mailService.sendMail{
			to reaction.user.email
			from 'do.not.reply@localhost'
			subject mailSubject
			body mailMessage
		}
	}

	/*
	 * Populates reaction folder with files and folders from message
	 * 
	 */
	private List populateReactionFolder(String[] foldersWithFiles, File reactionFolder) {
		
		ArrayList moleculeData = []
		for (String folderAndFiles : foldersWithFiles) {
			String[] folderAndFilesSeparated = folderAndFiles.split(";")
			File folder = new File(reactionFolder,
					folderAndFilesSeparated[0])
			folder.mkdir()
			//make sure folder isn't empty
			if(folderAndFilesSeparated.size() > 1){
				String[] filesWithText = folderAndFilesSeparated[1].split(",")
				for(String fileAndText : filesWithText){
					String[] fileAndTextSeparated = fileAndText.split(":")
					File file = new File(folder, fileAndTextSeparated[0])
					String fileText = fileAndTextSeparated[1]
					file.createNewFile()
					file << fileText
					if(folder.name == "product_geom"){
						moleculeData << file
					}
				}
			}
		}
		return moleculeData
		
	}
	
}
