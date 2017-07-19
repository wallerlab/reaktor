package cluster.services.workspace;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Creates the workspace needed on the cluster
 * 
 * @author suzanne
 *
 */
@Service
public class SimpleWorkspaceCreator implements WorkspaceCreator {
	
	@Resource
	public File directory;
	
	@Value("${cluster.wsc.email}")
	private String email;

	@Value("${cluster.wsc.submitScript}")
	private String submitScript;

	@Value("${cluster.wsc.runReaction}")
	private String runReaction;

	@Value("${cluster.wsc.runAggregation}")
	private String runAggregation;

	@Value("${cluster.wsc.simulator}")
	private String simulator;

	@Value("${cluster.wsc.suffix}")
	private String suffix;

	Logger logger = Logger.getLogger("reaktor-cluster-SWC");

	/**
	 * Creates working folder and files
	 */
	@Override
	public File createWorkspace(TextMessage msg) {
		
		File filePath = createWorkingFolder(msg);
		try{
			ArrayList<String> textForFiles = new ArrayList<>(Arrays.asList(msg.getText().split(",")));
			String simulationType = getSimulationType(textForFiles);
			List<Path> startFiles = writeFilesToFolder(textForFiles, filePath);
			copySubmitScriptToFolder(simulationType, startFiles, filePath);
		} catch(JMSException e){
			e.printStackTrace();
		}
		return filePath;
		
	}

	/*
	 * Creates the working folder
	 * 
	 */
	private File createWorkingFolder(TextMessage msg) {

		try {
			String folderName = msg.getJMSCorrelationID();
			logger.info("JMS correlation ID: " + folderName);
			File filePath = new File(directory, folderName);
			filePath.mkdir();
			return filePath;
		} catch (JMSException e) {
			e.printStackTrace();
		}
		return null;
		
	}

	/*
	 * Returns simulation type from separated message
	 *
	 */
	private String getSimulationType(ArrayList<String> textForFiles){

		String simulationType = textForFiles.remove(0);
		return simulationType;
	}

	/*
	 * Writes all files and subfolders in text message to folder
	 * 
	 */
	private List<Path> writeFilesToFolder(List<String> textForFiles, File
			filePath) {

		ArrayList<Path> startFiles = new ArrayList<>();
		try {
			for (String fileNameAndText : textForFiles) {
				String[] fileInfo = fileNameAndText.split(":");
				byte fileText[] = fileInfo[1].getBytes();
				Path path = Paths.get(filePath.getPath() + "/" + fileInfo[0]);
				Files.write(path,fileText);
				if(path.toString().endsWith(suffix)){
					startFiles.add(path);
				}
			}
			return startFiles;
		}catch (IOException e) {
			e.printStackTrace();
		}
		return startFiles;
	}

	/*
	 * Copies the submitscript to working folder, changing necessary variables
	 * 
	 */
	private void copySubmitScriptToFolder(String simulationType, List<Path>
			startFiles, File filePath) {

		try {
			String line, newLine;
			String jobName = filePath.getName();
			String runSimulator = "";
			if(simulationType == "Reaction"){
				runSimulator = runReaction + startFiles.get(0).getFileName().toString()+" "+startFiles.get(1).getFileName().toString();
			} else if(simulationType == "Aggregation"){
				runSimulator = runAggregation + startFiles.get(0).getFileName().toString();
				if(startFiles.size() == 2){
					runSimulator = runSimulator + " " + startFiles.get(1).getFileName().toString();
				}
			}
			File ssFile = new File(directory, submitScript);
			File newSSFile = new File(filePath, submitScript);
			newSSFile.createNewFile();
			BufferedReader reader = new BufferedReader(new FileReader(ssFile));
			BufferedWriter writer = new BufferedWriter(new FileWriter(newSSFile));
			while((line = reader.readLine()) != null){
				if(line.contains("job name")){
					newLine = line.replace("job name", jobName);
				}
				else if (line.contains("email")){
					newLine = line.replace("email", email);
				}
				else if (line.contains(simulator)){
					newLine = line.replace(simulator, runSimulator);
				}
				else{
					newLine = line;
				}
				writer.write(newLine+"\n");
			}
			reader.close();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		};
		
	}

}