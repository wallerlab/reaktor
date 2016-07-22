package cluster.services.workspace;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.TextMessage;

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

	@Value("${cluster.wsc.runSimulation}")
	private String runSimulation;

	@Value("${cluster.wsc.simulator}")
	private String simulator;

	@Value("${cluster.wsc.suffix}")
	private String suffix;

	public static File filePath;
	
	private ArrayList<Path> startFiles;

	/**
	 * Creates working folder and files
	 */
	@Override
	public void createWorkspace(TextMessage msg) {
		
		createWorkingFolder(msg);
		writeFilesToFolder(msg);
		copySubmitScriptToFolder();
		
	}

	/*
	 * Creates the working folder
	 * 
	 */
	private void createWorkingFolder(TextMessage msg) {
		
		try {
			String folderName = msg.getJMSCorrelationID();
			filePath = new File(directory, folderName);
			filePath.mkdir();
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
	}

	/*
	 * Writes all files and subfolders in text message to folder
	 * 
	 */
	private void writeFilesToFolder(TextMessage msg) {
		
		try {
			startFiles = new ArrayList<Path>();
			String[] textForFiles = msg.getText().split(",");
			for (String fileNameAndText : textForFiles) {
				String[] fileInfo = fileNameAndText.split(":");
				byte fileText[] = fileInfo[1].getBytes();
				Path path = Paths.get(filePath.getPath() + "/" + fileInfo[0]);
				Files.write(path,fileText);
				if(path.toString().endsWith(suffix)){
					startFiles.add(path);
				}
			}
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/*
	 * Copies the submitscript to working folder, changing necessary variables
	 * 
	 */
	private void copySubmitScriptToFolder() {
		
		try {
			String line, newLine;
			String jobName = filePath.getName();
			String runSimulator = runSimulation+startFiles.get(0).getFileName().toString()+" "+startFiles.get(1).getFileName().toString();
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