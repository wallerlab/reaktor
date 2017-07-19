package cluster.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Extends the java SimpleFileVisitor class specifically for Reaktor use in
 * creating messages from folders
 *
 * Created by suzanne on 04/01/2017.
 */
public class ReaktorFileVisitor extends SimpleFileVisitor<Path> {

	String fileExtension = ".xyz";
	Path directoryPath;
	StringBuilder fileStringBuilder;
	String folderPrefix = "";
	String filePrefix;
	String reactionType;
	int aggFileNum;

	public ReaktorFileVisitor(Path directoryPath, String reactionType){
		this.directoryPath = directoryPath;
		this.fileStringBuilder = new StringBuilder();
		this.reactionType = reactionType;
	}

	@Override
	public FileVisitResult preVisitDirectory(Path filePath, BasicFileAttributes
			attrs) throws IOException {

		if(new File(filePath.toString()).getName().startsWith("aggregate")){
			String[] fileSplit = filePath.toString().split("_");
			System.out.println(fileSplit[fileSplit.length-1]);
			aggFileNum = Integer.parseInt(fileSplit[fileSplit.length - 1]);
		}
		filePrefix = "";
		if(filePath != directoryPath){
			fileStringBuilder.append(folderPrefix + filePath.getFileName() + ";");
			folderPrefix = "#";
		}
		return FileVisitResult.CONTINUE;

	}

	@Override
	public FileVisitResult visitFile(Path filePath, BasicFileAttributes attrs)
			throws IOException{

		File file = new File(filePath.toString());
		if(!filePath.getParent().endsWith(this.directoryPath.getFileName())
				|| filePath.endsWith(fileExtension)){
			if(!file.isHidden()){
				if(reactionType == "reaction" || (reactionType ==
						"aggregation" && file.getName().startsWith(
								"start_"+ (aggFileNum-1)))){
					String newString = new String(Files.readAllBytes(filePath));
					fileStringBuilder.append(filePrefix + file.getName() + ":" + newString);
				}
			}
			filePrefix = ",";
		}
		return FileVisitResult.CONTINUE;

	}

	public String getFileString(){
		return fileStringBuilder.toString();
	}
}
